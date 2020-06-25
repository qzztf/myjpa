package cn.sexycode.myjpa.metamodel.internal;

/**
 * Hibernate extension to the JPA {@link BasicType} descriptor
 *
 * @author Steve Ebersole
 */
public interface BasicTypeDescriptor<J> extends BasicDomainType<J>, SimpleTypeDescriptor<J> {
}