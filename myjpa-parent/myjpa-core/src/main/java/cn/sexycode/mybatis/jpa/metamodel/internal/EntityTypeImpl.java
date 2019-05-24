package cn.sexycode.mybatis.jpa.metamodel.internal;

import cn.sexycode.mybatis.jpa.mapping.PersistentClass;

import javax.persistence.metamodel.EntityType;
import java.io.Serializable;

/**
 * Defines the Hibernate implementation of the JPA {@link EntityType} contract.
 *
 */
public class EntityTypeImpl<X>  extends AbstractIdentifiableType<X> implements EntityType<X>, Serializable {
	private final String jpaEntityName;

	@SuppressWarnings("unchecked")
    public EntityTypeImpl(Class javaType, AbstractIdentifiableType<? super X> superType,
            PersistentClass persistentClass) {
		super(
				javaType,
				javaType.getCanonicalName(),
				superType,
				false,
				true,
				false
		);/*super(
				javaType,
				persistentClass.getEntityName(),
				superType,
				persistentClass.getDeclaredIdentifierMapper() != null || ( superType != null && superType.hasIdClass() ),
				persistentClass.hasIdentifierProperty(),
				persistentClass.isVersioned()
		);*/
		this.jpaEntityName = "";
//		this.jpaEntityName = persistentClass.getJpaEntityName();
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
	public Class<X> getBindableJavaType() {
		return getJavaType();
	}

	@Override
	public PersistenceType getPersistenceType() {
		return PersistenceType.ENTITY;
	}
}
