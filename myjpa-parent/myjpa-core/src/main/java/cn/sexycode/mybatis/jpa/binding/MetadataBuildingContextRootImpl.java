package cn.sexycode.mybatis.jpa.binding;

import cn.sexycode.util.core.cls.ReflectionManager;
import cn.sexycode.util.core.cls.internal.JavaReflectionManager;

/**
 *
 */
public class MetadataBuildingContextRootImpl implements MetadataBuildingContext {
    private final MetadataBuildingOptions options;

    private final MappingDefaults mappingDefaults;

    private final InFlightMetadataCollector metadataCollector;

    private final JavaReflectionManager reflectionManager;

    public MetadataBuildingContextRootImpl(MetadataBuildingOptions options,
            InFlightMetadataCollector metadataCollector) {
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

}
