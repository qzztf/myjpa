package cn.sexycode.mybatis.jpa.binding;

import cn.sexycode.util.core.cls.ReflectionManager;

/**
 * Describes the context in which the process of building Metadata out of MetadataSources occurs.
 * <p>
 * BindingContext are generally hierarchical getting more specific as we "go
 * down".  E.g.  global -> PU -> document -> mapping
 *
 */
public interface MetadataBuildingContext {
    MetadataBuildingOptions getBuildingOptions();

    MappingDefaults getMappingDefaults();

    /**
     * Access to the collector of metadata as we build it.
     *
     * @return The metadata collector.
     */
    InFlightMetadataCollector getMetadataCollector();

    ReflectionManager getReflectionManager();
}
