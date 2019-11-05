package cn.sexycode.myjpa.binding;

import cn.sexycode.myjpa.boot.BootstrapContext;
import cn.sexycode.util.core.cls.classloading.ClassLoaderService;
import cn.sexycode.util.core.file.ArchiveDescriptorFactory;
import cn.sexycode.util.core.file.StandardArchiveDescriptorFactory;
import cn.sexycode.util.core.file.scan.*;
import cn.sexycode.util.core.service.ServiceRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.AttributeConverter;
import java.lang.reflect.Constructor;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Coordinates the process of executing {@link Scanner} (if enabled)
 * and applying the resources (classes, packages and mappings) discovered.
 *
 * @author Steve Ebersole
 */
public class ScanningCoordinator {
    private static final Logger log = LoggerFactory.getLogger(ScanningCoordinator.class);

    /**
     * Singleton access
     */
    public static final ScanningCoordinator INSTANCE = new ScanningCoordinator();

    private ScanningCoordinator() {
    }

    public void coordinateScan(ManagedResourcesImpl managedResources, BootstrapContext bootstrapContext) {
        if (bootstrapContext.getScanEnvironment() == null) {
            return;
        }

        final ClassLoaderService classLoaderService = bootstrapContext.getServiceRegistry()
                .getService(ClassLoaderService.class);

        // NOTE : the idea with JandexInitializer/JandexInitManager was to allow adding classes
        // to the index as we discovered them via scanning and .  Currently
        final Scanner scanner = buildScanner(bootstrapContext, classLoaderService);
        final ScanResult scanResult = scanner
                .scan(bootstrapContext.getScanEnvironment(), bootstrapContext.getScanOptions(),
                        StandardScanParameters.INSTANCE);

        applyScanResultsToManagedResources(managedResources, scanResult, bootstrapContext);
    }

    private static final Class[] SINGLE_ARG = new Class[] { ArchiveDescriptorFactory.class };

    @SuppressWarnings("unchecked")
    private static Scanner buildScanner(BootstrapContext bootstrapContext, ClassLoaderService classLoaderAccess) {
        final Object scannerSetting = bootstrapContext.getScanner();
        final ArchiveDescriptorFactory archiveDescriptorFactory = bootstrapContext.getArchiveDescriptorFactory();

        if (scannerSetting == null) {
            // No custom Scanner specified, use the StandardScanner
            if (archiveDescriptorFactory == null) {
                return new StandardScanner();
            } else {
                return new StandardScanner(archiveDescriptorFactory);
            }
        } else {
            if (scannerSetting instanceof Scanner) {
                if (archiveDescriptorFactory != null) {
                    throw new IllegalStateException(
                            "A Scanner instance and an ArchiveDescriptorFactory were both specified; please "
                                    + "specify one or the other, or if you need to supply both, Scanner class to use "
                                    + "(assuming it has a constructor accepting a ArchiveDescriptorFactory).  "
                                    + "Alternatively, just pass the ArchiveDescriptorFactory during your own "
                                    + "Scanner constructor assuming it is statically known.");
                }
                return (Scanner) scannerSetting;
            }

            final Class<? extends Scanner> scannerImplClass;
            if (scannerSetting instanceof Class) {
                scannerImplClass = (Class<? extends Scanner>) scannerSetting;
            } else {
                scannerImplClass = classLoaderAccess.classForName(scannerSetting.toString());
            }

            if (archiveDescriptorFactory != null) {
                // find the single-arg constructor - its an error if none exists
                try {
                    final Constructor<? extends Scanner> constructor = scannerImplClass.getConstructor(SINGLE_ARG);
                    try {
                        return constructor.newInstance(archiveDescriptorFactory);
                    } catch (Exception e) {
                        throw new IllegalStateException(
                                "Error trying to instantiate custom specified Scanner [" + scannerImplClass.getName()
                                        + "]", e);
                    }
                } catch (NoSuchMethodException e) {
                    throw new IllegalArgumentException(
                            "Configuration named a custom Scanner and a custom ArchiveDescriptorFactory, but "
                                    + "Scanner impl did not define a constructor accepting ArchiveDescriptorFactory");
                }
            } else {
                // could be either ctor form...
                // find the single-arg constructor - its an error if none exists
                try {
                    final Constructor<? extends Scanner> constructor = scannerImplClass.getConstructor(SINGLE_ARG);
                    try {
                        return constructor.newInstance(StandardArchiveDescriptorFactory.INSTANCE);
                    } catch (Exception e) {
                        throw new IllegalStateException(
                                "Error trying to instantiate custom specified Scanner [" + scannerImplClass.getName()
                                        + "]", e);
                    }
                } catch (NoSuchMethodException e) {
                    try {
                        final Constructor<? extends Scanner> constructor = scannerImplClass.getConstructor();
                        try {
                            return constructor.newInstance();
                        } catch (Exception e2) {
                            throw new IllegalStateException(
                                    "Error trying to instantiate custom specified Scanner [" + scannerImplClass
                                            .getName() + "]", e2);
                        }
                    } catch (NoSuchMethodException ignore) {
                        throw new IllegalArgumentException(
                                "Configuration named a custom Scanner, but we were unable to locate "
                                        + "an appropriate constructor");
                    }
                }
            }
        }
    }

    public void applyScanResultsToManagedResources(ManagedResourcesImpl managedResources, ScanResult scanResult,
            BootstrapContext bootstrapContext) {

        final ScanEnvironment scanEnvironment = bootstrapContext.getScanEnvironment();
        final ServiceRegistry serviceRegistry = bootstrapContext.getServiceRegistry();
        final ClassLoaderService classLoaderService = serviceRegistry.getService(ClassLoaderService.class);



        // classes and packages ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

        final List<String> unresolvedListedClassNames = scanEnvironment.getExplicitlyListedClassNames() == null
                ? new ArrayList<String>()
                : new ArrayList<String>(scanEnvironment.getExplicitlyListedClassNames());

        for (ClassDescriptor classDescriptor : scanResult.getLocatedClasses()) {
            if (classDescriptor.getCategorization() == ClassDescriptor.CategorizationEnum.CONVERTER) {
                // converter classes are safe to load because we never enhance them,
                // and notice we use the ClassLoaderService specifically, not the temp ClassLoader (if any)
                /*managedResources.addAttributeConverterDefinition(AttributeConverterDefinition
                        .from(classLoaderService.<AttributeConverter>classForName(classDescriptor.getName())));*/
            } else if (classDescriptor.getCategorization() == ClassDescriptor.CategorizationEnum.MODEL) {
                managedResources.addAnnotatedClassName(classDescriptor.getName());
            }
            unresolvedListedClassNames.remove(classDescriptor.getName());
        }

        // IMPL NOTE : "explicitlyListedClassNames" can contain class or package names...
        for (PackageDescriptor packageDescriptor : scanResult.getLocatedPackages()) {
            managedResources.addAnnotatedPackageName(packageDescriptor.getName());
            unresolvedListedClassNames.remove(packageDescriptor.getName());
        }

        for (String unresolvedListedClassName : unresolvedListedClassNames) {
            // because the explicit list can contain either class names or package names
            // we need to check for both here...

            // First, try it as a class name
            final String classFileName = unresolvedListedClassName.replace('.', '/') + ".class";
            final URL classFileUrl = classLoaderService.locateResource(classFileName);
            if (classFileUrl != null) {
                managedResources.addAnnotatedClassName(unresolvedListedClassName);
                continue;
            }

            // Then, try it as a package name
            final String packageInfoFileName = unresolvedListedClassName.replace('.', '/') + "/package-info.class";
            final URL packageInfoFileUrl = classLoaderService.locateResource(packageInfoFileName);
            if (packageInfoFileUrl != null) {
                managedResources.addAnnotatedPackageName(unresolvedListedClassName);
                continue;
            }

            log.debug("Unable to resolve class [%s] named in persistence unit [%s]", unresolvedListedClassName,
                    scanEnvironment.getRootUrl());
        }
    }
}
