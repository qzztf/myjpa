package cn.sexycode.myjpa.metamodel.internal;

import java.io.Serializable;

/**
 * @author Emmanuel Bernard
 */
public class BasicTypeImpl<J> implements BasicTypeDescriptor<J>, Serializable {
	private final Class<J> clazz;
	private PersistenceType persistenceType;

	@Override
	public PersistenceType getPersistenceType() {
		return persistenceType;
	}

	@Override
	public Class<J> getJavaType() {
		return clazz;
	}

	public BasicTypeImpl(Class<J> clazz, PersistenceType persistenceType) {
		this.clazz = clazz;
		this.persistenceType = persistenceType;
	}

	@Override
	public String getTypeName() {
		return clazz.getName();
	}
}