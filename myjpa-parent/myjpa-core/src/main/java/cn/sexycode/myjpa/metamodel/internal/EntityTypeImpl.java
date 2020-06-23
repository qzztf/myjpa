package cn.sexycode.myjpa.metamodel.internal;

import cn.sexycode.myjpa.mapping.PersistentClass;
import cn.sexycode.myjpa.session.SessionFactory;

import javax.persistence.metamodel.EntityType;
import java.io.Serializable;

/**
 * Defines the Hibernate implementation of the JPA {@link EntityType} contract.
 *
 * @author Steve Ebersole
 * @author Emmanuel Bernard
 */
public class EntityTypeImpl<J>
		extends AbstractIdentifiableType<J>
		implements EntityTypeDescriptor<J>, Serializable {
	private final String jpaEntityName;

	public EntityTypeImpl(
			Class javaType,
			IdentifiableTypeDescriptor<? super J> superType,
			PersistentClass persistentClass,
			SessionFactory sessionFactory) {
		super(
				javaType,
				persistentClass.getEntityName(),
				superType,
				false,
				persistentClass.hasIdentifierProperty(),
				persistentClass.isVersioned(),
				sessionFactory
		);
		this.jpaEntityName = persistentClass.getJpaEntityName();
	}

	@Override
	public String getName() {
		return jpaEntityName;
	}

	@Override
	public BindableType getBindableType() {
		return BindableType.ENTITY_TYPE;
	}

	@Override
	public Class<J> getBindableJavaType() {
		return getJavaType();
	}

	@Override
	public PersistenceType getPersistenceType() {
		return PersistenceType.ENTITY;
	}

	@Override
	public IdentifiableTypeDescriptor<? super J> getSuperType() {
		return super.getSuperType();
	}

	@Override
	public String toString() {
		return getName();
	}
}
