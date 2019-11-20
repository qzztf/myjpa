package cn.sexycode.myjpa.binding;

import cn.sexycode.myjpa.boot.BootstrapContext;
import cn.sexycode.myjpa.boot.MetadataBuildingOptions;
import cn.sexycode.util.core.cls.ReflectionManager;
import cn.sexycode.util.core.cls.internal.JavaReflectionManager;

/**
 *
 */
public class MetadataBuildingContextRootImpl implements MetadataBuildingContext {
    private final MetadataBuildingOptions options;

    private final BootstrapContext bootstrapContext;
    private final MappingDefaults mappingDefaults;

    private final InFlightMetadataCollector metadataCollector;

    private final JavaReflectionManager reflectionManager;

    public MetadataBuildingContextRootImpl(BootstrapContext bootstrapContext, MetadataBuildingOptions options,
            InFlightMetadataCollector metadataCollector) {
        this.bootstrapContext = bootstrapContext;
        this.options = options;
        this.mappingDefaults = options.getMappingDefaults();
        this.metadataCollector = metadataCollector;
        this.reflectionManager = new JavaReflectionManager();
    }

    @Override
    public MetadataBuildingOptions getBuildingOptions() {
        return options;
    }

    @Override
    public MappingDefaults getMappingDefaults() {
        return mappingDefaults;
    }

    @Override
    public InFlightMetadataCollector getMetadataCollector() {
        return metadataCollector;
    }

    @Override
    public ReflectionManager getReflectionManager() {
        return reflectionManager;
    }

    @Override
    public BootstrapContext getBootstrapContext() {
        return this.bootstrapContext;
    }

}
