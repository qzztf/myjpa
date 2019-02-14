/*
 * Hibernate, Relational Persistence for Idiomatic Java
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later.
 * See the lgpl.txt file in the root directory or <http://www.gnu.org/licenses/lgpl-2.1.html>.
 */
package cn.sexycode.mybatis.jpa.binding;

import cn.sexycode.mybatis.jpa.SessionFactoryBuilder;
import cn.sexycode.mybatis.jpa.mapping.PersistentClass;
import cn.sexycode.mybatis.jpa.session.SessionFactory;

import javax.persistence.MappedSuperclass;
import java.io.Serializable;
import java.util.*;

/**
 * Container for configuration data collected during binding the metamodel.
 *
 * @author Steve Ebersole
 * @author Hardy Ferentschik
 * @author Gail Badner
 */
public class MetadataImpl implements Metadata, Serializable {
    private final UUID uuid;


    private final Map<String, PersistentClass> entityBindingMap;
    private final Map<Class, MappedSuperclass> mappedSuperclassMap;
    private final Map<String, Collection> collectionBindingMap;

    public MetadataImpl(
            UUID uuid,
            Map<String, PersistentClass> entityBindingMap,
            Map<Class, MappedSuperclass> mappedSuperclassMap,
            Map<String, Collection> collectionBindingMap
    ) {
        this.uuid = uuid;
        this.entityBindingMap = entityBindingMap;
        this.mappedSuperclassMap = mappedSuperclassMap;
        this.collectionBindingMap = collectionBindingMap;
    }

    @Override
    public java.util.Collection<PersistentClass> getEntityBindings() {
        return entityBindingMap.values();
    }

    @Override
    public PersistentClass getEntityBinding(String entityName) {
        return entityBindingMap.get(entityName);
    }


}
