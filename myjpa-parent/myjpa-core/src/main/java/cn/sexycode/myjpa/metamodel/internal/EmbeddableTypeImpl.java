package cn.sexycode.myjpa.metamodel.internal;

import cn.sexycode.myjpa.session.SessionFactory;

import java.io.Serializable;

/**
 * Standard Hibernate implementation of JPA's {@link javax.persistence.metamodel.EmbeddableType}
 * contract
 *
 * @author Emmanuel Bernard
 * @author Steve Ebersole`
 */
public class EmbeddableTypeImpl<J>
		extends AbstractManagedType<J>
		implements EmbeddedTypeDescriptor<J>, Serializable {

	private final ManagedTypeDescriptor<?> parent;

	public EmbeddableTypeImpl(
			Class<J> javaType,
			ManagedTypeDescriptor<?> parent,
			SessionFactory sessionFactory) {
		super( javaType, null, null, sessionFactory );
		this.parent = parent;
	}

	@Override
	public PersistenceType getPersistenceType() {
		return PersistenceType.EMBEDDABLE;
	}

	@Override
	public ManagedTypeDescriptor<?> getParent() {
		return parent;
	}

}
