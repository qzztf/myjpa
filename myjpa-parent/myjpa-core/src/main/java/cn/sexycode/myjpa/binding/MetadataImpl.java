package cn.sexycode.myjpa.binding;

import cn.sexycode.myjpa.mapping.PersistentClass;
import cn.sexycode.sql.dialect.function.SQLFunction;
import cn.sexycode.sql.model.Database;
import cn.sexycode.sql.type.TypeResolver;

import javax.persistence.MappedSuperclass;
import java.io.Serializable;
import java.util.Collection;
import java.util.Map;
import java.util.UUID;

/**
 * Container for configuration data collected during binding the metamodel.
 *
 *
 */
public class MetadataImpl implements Metadata, Serializable {
    private final UUID uuid;


    private final Map<String, PersistentClass> entityBindingMap;
    private final Map<Class, MappedSuperclass> mappedSuperclassMap;

    public MetadataImpl(UUID uuid, MetadataBuildingOptions options, TypeResolver typeResolver,
            Map<String, PersistentClass> entityBindingMap, Map<Class, MappedSuperclass> mappedSuperclassMap,
            Map<String, SQLFunction> sqlFunctionMap, Database database
    ) {
        this.uuid = uuid;
        this.entityBindingMap = entityBindingMap;
        this.mappedSuperclassMap = mappedSuperclassMap;
    }

    @Override
    public Collection<PersistentClass> getEntityBindings() {
        return entityBindingMap.values();
    }

    @Override
    public PersistentClass getEntityBinding(String entityName) {
        return entityBindingMap.get(entityName);
    }


}
