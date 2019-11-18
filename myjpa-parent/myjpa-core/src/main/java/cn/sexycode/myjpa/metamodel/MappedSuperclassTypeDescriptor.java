package cn.sexycode.myjpa.metamodel;


/**
 * Hibernate extension to the JPA {@link MappedSuperclassType} descriptor
 *
 * @author Steve Ebersole
 */
public interface MappedSuperclassTypeDescriptor<J> extends MappedSuperclassDomainType<J>, IdentifiableTypeDescriptor<J> {
}
