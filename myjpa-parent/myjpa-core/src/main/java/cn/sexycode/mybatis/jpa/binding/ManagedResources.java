package cn.sexycode.mybatis.jpa.binding;

import java.util.Collection;

/**
 * Represents the result of the first step of the process of building {@link org.hibernate.boot.MetadataSources}
 * reference into a {@link org.hibernate.boot.Metadata} reference.
 * <p/>
 * Essentially it represents thecombination of:<ol>
 * <li>domain classes, packages and mapping files defined via MetadataSources</li>
 * <li>attribute converters defined via MetadataBuildingOptions</li>
 * <li>classes, converters, packages and mapping files auto-discovered as part of scanning</li>
 * </ol>
 *
 *
 */
public interface ManagedResources {


    /**
     * Informational access to any entity and component classes in the user domain model known by Class
     * reference .  Changes to made to the returned list have no effect.
     *
     * @return The list of entity/component classes known by Class reference.
     */
    Collection<Class> getAnnotatedClassReferences();

    /**
     * Informational access to any entity and component classes in the user domain model known by name.
     * Changes to made to the returned list have no effect.
     *
     * @return The list of entity/component classes known by name.
     */
    Collection<String> getAnnotatedClassNames();

    Collection<String> getAnnotatedPackageNames();
}
