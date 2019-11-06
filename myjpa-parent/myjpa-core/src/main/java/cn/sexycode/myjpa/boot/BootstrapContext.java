package cn.sexycode.myjpa.boot;

import cn.sexycode.myjpa.binding.MetadataBuildingOptions;
import cn.sexycode.sql.dialect.function.SQLFunction;
import cn.sexycode.util.core.cls.classloading.ClassLoaderAccess;
import cn.sexycode.util.core.file.ArchiveDescriptorFactory;
import cn.sexycode.util.core.file.scan.ScanEnvironment;
import cn.sexycode.util.core.file.scan.ScanOptions;
import cn.sexycode.util.core.service.StandardServiceRegistry;

import java.util.Map;

/**
 * Defines a context for things generally available to the process of
 * bootstrapping a SessionFactory that are expected to be released after
 * the SessionFactory is built.
 *
 * @author Steve Ebersole
 */
public interface BootstrapContext {
    StandardServiceRegistry getServiceRegistry();

    MetadataBuildingOptions getMetadataBuildingOptions();

    boolean isJpaBootstrap();

    ClassLoaderAccess getClassLoaderAccess();
    /**
     * Indicates that bootstrap was initiated from JPA bootstrapping.  Internally {@code false} is
     * the assumed value.  We only need to call this to mark that as true.
     */
    void markAsJpaBootstrap();

    /**
     * Access the temporary ClassLoader passed to us as defined by
     * {@link javax.persistence.spi.PersistenceUnitInfo#getNewTempClassLoader()}, if any.
     *
     * @return The tempo ClassLoader
     */
    ClassLoader getJpaTempClassLoader();

    /**
     * Access to the ArchiveDescriptorFactory to be used for scanning
     *
     * @return The ArchiveDescriptorFactory
     */
    ArchiveDescriptorFactory getArchiveDescriptorFactory();

    /**
     * Access to the options to be used for scanning
     *
     * @return The scan options
     */
    ScanOptions getScanOptions();

    /**
     * Access to the environment for scanning.  Consider this temporary; see discussion on
     * {@link ScanEnvironment}
     *
     * @return The scan environment
     */
    ScanEnvironment getScanEnvironment();

    /**
     * Access to the Scanner to be used for scanning.  Can be:<ul>
     * <li>A Scanner instance</li>
     * <li>A Class reference to the Scanner implementor</li>
     * <li>A String naming the Scanner implementor</li>
     * </ul>
     *
     * @return The scanner
     */
    Object getScanner();

    /**
     * Access to any SQL functions explicitly registered with the MetadataBuilder.  This
     * does not include Dialect defined functions, etc.
     * <p/>
     * Should never return {@code null}
     *
     * @return The SQLFunctions registered through MetadataBuilder
     */
    Map<String, SQLFunction> getSqlFunctions();

    /**
     * Releases the "bootstrap only" resources held by this BootstrapContext.
     * <p/>
     * Only one call to this method is supported, after we have completed the process of
     * building the (non-inflight) Metadata impl.  We may want to delay this until we
     * get into SF building.  Not sure yet.
     *
     * @todo verify this ^^
     */
    void release();
}
