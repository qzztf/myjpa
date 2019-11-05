package cn.sexycode.myjpa.boot;

import cn.sexycode.myjpa.binding.MetadataBuildingOptions;
import cn.sexycode.myjpa.binding.StandardScanOptions;
import cn.sexycode.sql.dialect.function.SQLFunction;
import cn.sexycode.util.core.cls.classloading.ClassLoaderService;
import cn.sexycode.util.core.file.ArchiveDescriptorFactory;
import cn.sexycode.util.core.file.scan.ScanEnvironment;
import cn.sexycode.util.core.file.scan.ScanOptions;
import cn.sexycode.util.core.service.StandardServiceRegistry;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Andrea Boriero
 */
public class BootstrapContextImpl implements BootstrapContext {

    private final StandardServiceRegistry serviceRegistry;

    private boolean isJpaBootstrap;

    private ScanOptions scanOptions;

    private ScanEnvironment scanEnvironment;

    private Object scannerSetting;

    private ArchiveDescriptorFactory archiveDescriptorFactory;

    private MetadataBuildingOptions metadataBuildingOptions;

    private HashMap<String, SQLFunction> sqlFunctionMap;

    public BootstrapContextImpl(StandardServiceRegistry serviceRegistry,
            MetadataBuildingOptions metadataBuildingOptions) {
        this.serviceRegistry = serviceRegistry;
        this.metadataBuildingOptions = metadataBuildingOptions;

        final ClassLoaderService classLoaderService = serviceRegistry.getService(ClassLoaderService.class);
        this.scanOptions = new StandardScanOptions(null, false);

        // ScanEnvironment must be set explicitly
		/*this.scannerSetting = configService.getSettings().get( AvailableSettings.SCANNER );
		if ( this.scannerSetting == null ) {
			this.scannerSetting = configService.getSettings().get( AvailableSettings.SCANNER_DEPRECATED );
			if ( this.scannerSetting != null ) {
				DEPRECATION_LOGGER.logDeprecatedScannerSetting();
			}
		}
		this.archiveDescriptorFactory = strategySelector.resolveStrategy(
				ArchiveDescriptorFactory.class,
				configService.getSettings().get( AvailableSettings.SCANNER_ARCHIVE_INTERPRETER )
		);*/
    }

    @Override
    public StandardServiceRegistry getServiceRegistry() {
        return serviceRegistry;
    }

    @Override
    public MetadataBuildingOptions getMetadataBuildingOptions() {
        return metadataBuildingOptions;
    }

    @Override
    public boolean isJpaBootstrap() {
        return isJpaBootstrap;
    }

    @Override
    public void markAsJpaBootstrap() {
        isJpaBootstrap = true;
    }

    @Override
    public ClassLoader getJpaTempClassLoader() {
        //		return classLoaderAccess.getJpaTempClassLoader();
        return null;
    }

    @Override
    public ArchiveDescriptorFactory getArchiveDescriptorFactory() {
        return archiveDescriptorFactory;
    }

    @Override
    public ScanOptions getScanOptions() {
        return scanOptions;
    }

    @Override
    public ScanEnvironment getScanEnvironment() {
        return scanEnvironment;
    }

    @Override
    public Object getScanner() {
        return scannerSetting;
    }

    @Override
    public Map<String, SQLFunction> getSqlFunctions() {
        return sqlFunctionMap == null ? Collections.emptyMap() : sqlFunctionMap;
    }

    @Override
    public void release() {
        scanOptions = null;
        scanEnvironment = null;
        scannerSetting = null;
        archiveDescriptorFactory = null;

        if (sqlFunctionMap != null) {
            sqlFunctionMap.clear();
        }
    }

}
