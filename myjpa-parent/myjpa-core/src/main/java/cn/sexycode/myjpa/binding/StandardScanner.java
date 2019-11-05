package cn.sexycode.myjpa.binding;

import cn.sexycode.util.core.file.*;
import cn.sexycode.util.core.file.scan.*;
import org.apache.ibatis.javassist.bytecode.AnnotationsAttribute;
import org.apache.ibatis.javassist.bytecode.ClassFile;

import javax.persistence.Converter;
import javax.persistence.Embeddable;
import javax.persistence.Entity;
import javax.persistence.MappedSuperclass;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

public class StandardScanner extends AbstractScannerImpl {

    public StandardScanner() {
        this(StandardArchiveDescriptorFactory.INSTANCE);
    }

    protected StandardScanner(ArchiveDescriptorFactory archiveDescriptorFactory) {
        super(archiveDescriptorFactory);
    }
    @Override
    public ScanResult scan(ScanEnvironment environment, ScanOptions options, ScanParameters parameters) {
        final ScanResultCollector collector = new ScanResultCollectorImpl( environment, options, parameters );

        if ( environment.getNonRootUrls() != null ) {
            final ArchiveContext context = new ArchiveContextImpl( false, collector );
            for ( URL url : environment.getNonRootUrls() ) {
                final ArchiveDescriptor descriptor = buildArchiveDescriptor( url, environment, false );
                descriptor.visitArchive( context );
            }
        }

        if ( environment.getRootUrl() != null ) {
            final ArchiveContext context = new ArchiveContextImpl( true, collector );
            final ArchiveDescriptor descriptor = buildArchiveDescriptor( environment.getRootUrl(), environment, true );
            descriptor.visitArchive( context );
        }

        return collector.toScanResult();
    }
    private ArchiveDescriptor buildArchiveDescriptor(
            URL url,
            ScanEnvironment environment,
            boolean isRootUrl) {
        final ArchiveDescriptor descriptor;
        final ArchiveDescriptorInfo descriptorInfo = archiveDescriptorCache.get( url );
        if ( descriptorInfo == null ) {
			/*if ( !isRootUrl && archiveDescriptorFactory instanceof JarFileEntryUrlAdjuster ) {
				url = ( (JarFileEntryUrlAdjuster) archiveDescriptorFactory ).adjustJarFileEntryUrl( url, environment.getRootUrl() );
			}*/
            descriptor = archiveDescriptorFactory.buildArchiveDescriptor( url );
            archiveDescriptorCache.put(
                    url,
                    new ArchiveDescriptorInfo( descriptor, isRootUrl )
            );
        }
        else {
            validateReuse( descriptorInfo, isRootUrl );
            descriptor = descriptorInfo.archiveDescriptor;
        }
        return descriptor;
    }

    public class ArchiveContextImpl implements ArchiveContext {
        private final boolean isRootUrl;

        private final ClassFileArchiveEntryHandler classEntryHandler;
        private final PackageInfoArchiveEntryHandler packageEntryHandler;
        private final ArchiveEntryHandler fileEntryHandler;

        public ArchiveContextImpl(boolean isRootUrl, ScanResultCollector scanResultCollector) {
            this.isRootUrl = isRootUrl;

            this.classEntryHandler = new JpaClassFileArchiveEntryHandler(scanResultCollector);
            this.packageEntryHandler = new PackageInfoArchiveEntryHandler( scanResultCollector );
            this.fileEntryHandler = new NonClassFileArchiveEntryHandler( scanResultCollector );
        }

        @Override
        public boolean isRootUrl() {
            return isRootUrl;
        }

        @Override
        public ArchiveEntryHandler obtainArchiveEntryHandler(ArchiveEntry entry) {
            final String nameWithinArchive = entry.getNameWithinArchive();

            if ( nameWithinArchive.endsWith( "package-info.class" ) ) {
                return packageEntryHandler;
            }
            else if ( nameWithinArchive.endsWith( ".class" ) ) {
                return classEntryHandler;
            }
            else {
                return fileEntryHandler;
            }
        }
    }

    static class JpaClassFileArchiveEntryHandler extends ClassFileArchiveEntryHandler {
        public JpaClassFileArchiveEntryHandler(ScanResultCollector resultCollector) {
            super(resultCollector);
        }

        @Override
        public void handleEntry(ArchiveEntry entry, ArchiveContext context) {
            // Ultimately we'd like to leverage Jandex here as long term we want to move to
            // using Jandex for annotation processing.  But even then, Jandex atm does not have
            // any facility for passing a stream and conditionally indexing it into an Index or
            // returning existing ClassInfo objects.
            //
            // So not sure we can ever not do this unconditional input stream read :(
            final ClassFile classFile = toClassFile( entry );
            final ClassDescriptor classDescriptor = toClassDescriptor( classFile, entry );

            if ( classDescriptor.getCategorization() == ClassDescriptor.CategorizationEnum.OTHER ) {
                return;
            }

            resultCollector.handleClass( classDescriptor, context.isRootUrl() );
        }

        private ClassFile toClassFile(ArchiveEntry entry) {
            final InputStream inputStream = entry.getStreamAccess();
            final DataInputStream dataInputStream = new DataInputStream( inputStream );
            try {
                return new ClassFile( dataInputStream );
            }
            catch (IOException e) {
                throw new ArchiveException( "Could not build ClassFile", e );
            }
            finally {
                try {
                    dataInputStream.close();
                }
                catch (Exception ignore) {
                }

                try {
                    inputStream.close();
                }
                catch (IOException ignore) {
                }
            }
        }

        private ClassDescriptor toClassDescriptor(ClassFile classFile, ArchiveEntry entry) {
            ClassDescriptor.Categorization categorization = ClassDescriptor.CategorizationEnum.OTHER;

            final AnnotationsAttribute visibleAnnotations = (AnnotationsAttribute) classFile.getAttribute( AnnotationsAttribute.visibleTag );
            if ( visibleAnnotations != null ) {
                if ( visibleAnnotations.getAnnotation( Entity.class.getName() ) != null
                        || visibleAnnotations.getAnnotation( MappedSuperclass.class.getName() ) != null
                        || visibleAnnotations.getAnnotation( Embeddable.class.getName() ) != null ) {
                    categorization = ClassDescriptor.CategorizationEnum.MODEL;
                }
                else if ( visibleAnnotations.getAnnotation( Converter.class.getName() ) != null ) {
                    categorization = ClassDescriptor.CategorizationEnum.CONVERTER;
                }
            }

            return new ClassDescriptorImpl( classFile.getName(), categorization, entry.getStreamAccess() );
        }
    }
}
