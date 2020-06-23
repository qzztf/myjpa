package cn.sexycode.myjpa.metamodel.internal;

public interface DomainType<J> extends javax.persistence.metamodel.Type<J> {
	/**
	 * The name of the type - this is Hibernate notion of the type name including
	 * non-pojo mappings, etc.
	 */
	String getTypeName();
}