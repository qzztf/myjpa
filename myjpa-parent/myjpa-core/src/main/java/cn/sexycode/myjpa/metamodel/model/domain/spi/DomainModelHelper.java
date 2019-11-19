package cn.sexycode.myjpa.metamodel.model.domain.spi;

import cn.sexycode.myjpa.metamodel.spi.MetamodelImplementor;
import cn.sexycode.myjpa.session.SessionFactory;
import cn.sexycode.util.core.cls.classloading.ClassLoaderService;

/**
 * Helper containing utilities useful for domain model handling
 *
 * @author Steve Ebersole
 */
public class DomainModelHelper {
    public static EntityPersister resolveEntityPersister(EntityTypeDescriptor<?> entityType,
            SessionFactory sessionFactory) {
        // Our EntityTypeImpl#getType impl returns the Hibernate entity-name
        // which is exactly what we want
        final String hibernateEntityName = entityType.getName();
        return sessionFactory.getMetamodel().entityPersister(hibernateEntityName);
    }

    @SuppressWarnings("unchecked")
    public static <T, S extends T> ManagedTypeDescriptor<S> resolveSubType(ManagedTypeDescriptor<T> baseType,
            String subTypeName, SessionFactory sessionFactory) {
        final MetamodelImplementor metamodel = sessionFactory.getMetamodel();

        if (baseType instanceof EmbeddedTypeDescriptor<?>) {
            // todo : at least validate the string is a valid sub-type of the embeddable class?
            return (ManagedTypeDescriptor) baseType;
        }

        final String importedClassName = metamodel.getImportedClassName(subTypeName);
        if (importedClassName != null) {
            // first, try to find it by name directly..
            ManagedTypeDescriptor<S> subManagedType = metamodel.entity(importedClassName);
            if (subManagedType != null) {
                return subManagedType;
            }

            // it could still be a mapped-superclass
            try {
                final Class<S> subTypeClass = sessionFactory.getServiceRegistry().getService(ClassLoaderService.class)
                        .classForName(importedClassName);

                return metamodel.managedType(subTypeClass);
            } catch (Exception ignore) {
            }
        }

        throw new IllegalArgumentException("Unknown sub-type name (" + baseType.getName() + ") : " + subTypeName);
    }

    public static <S> ManagedTypeDescriptor<S> resolveSubType(ManagedTypeDescriptor<? super S> baseType,
            Class<S> subTypeClass, SessionFactory sessionFactory) {
        // todo : validate the hierarchy-ness...
        final MetamodelImplementor metamodel = sessionFactory.getMetamodel();
        return metamodel.managedType(subTypeClass);
    }
}
