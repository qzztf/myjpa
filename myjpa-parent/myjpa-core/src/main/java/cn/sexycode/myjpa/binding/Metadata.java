package cn.sexycode.myjpa.binding;

import cn.sexycode.myjpa.mapping.MappedSuperclass;
import cn.sexycode.myjpa.mapping.PersistentClass;

import java.util.Collection;
import java.util.Set;

/**
 * Represents the ORM model as determined from all provided mapping sources.
 * <p>
 * NOTE : for the time being this is essentially a copy of the legacy Mappings contract, split between
 * reading the mapping information exposed here and collecting it via InFlightMetadataCollector
 *
 *
 */
public interface Metadata {

    /**
     * Retrieves the PersistentClass entity metadata representation for known all entities.
     * <p>
     * Returned collection is immutable
     *
     * @return All PersistentClass representations.
     */
    Collection<PersistentClass> getEntityBindings();

    /**
     * Retrieves the PersistentClass entity mapping metadata representation for
     * the given entity name.
     *
     * @param entityName The entity name for which to retrieve the metadata.
     * @return The entity mapping metadata, or {@code null} if no matching entity found.
     */
    PersistentClass getEntityBinding(String entityName);

    Set<MappedSuperclass> getMappedSuperclassMappingsCopy();

}
