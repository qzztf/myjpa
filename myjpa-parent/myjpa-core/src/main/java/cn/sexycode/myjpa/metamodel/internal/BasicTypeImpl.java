package cn.sexycode.myjpa.metamodel.internal;

import javax.persistence.metamodel.BasicType;
import java.io.Serializable;

public class BasicTypeImpl<X> implements BasicType<X>, Serializable {
	private final Class<X> clazz;
	private PersistenceType persistenceType;

	@Override
	public PersistenceType getPersistenceType() {
		return persistenceType;
	}

	@Override
	public Class<X> getJavaType() {
		return clazz;
	}

	public BasicTypeImpl(Class<X> clazz, PersistenceType persistenceType) {
		this.clazz = clazz;
		this.persistenceType = persistenceType;
	}
}