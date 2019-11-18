package cn.sexycode.myjpa.metamodel.internal;


import cn.sexycode.myjpa.session.SessionFactory;

import javax.persistence.metamodel.EmbeddableType;
import java.io.Serializable;

/**
 *
 */
public class EmbeddableTypeImpl<X>
		extends AbstractManagedType<X>
		implements EmbeddableType<X>, Serializable {

	private final AbstractManagedType parent;
//	private final ComponentType hibernateType;

	public EmbeddableTypeImpl(Class<X> javaType, AbstractManagedType parent, SessionFactory sessionFactory) {
		super( javaType, null, null , sessionFactory);
		this.parent = parent;
//		this.hibernateType = hibernateType;
	}

	@Override
	public PersistenceType getPersistenceType() {
		return PersistenceType.EMBEDDABLE;
	}

	public AbstractManagedType getParent() {
		return parent;
	}

	/*public ComponentType getHibernateType() {
		return hibernateType;
	}*/
}
