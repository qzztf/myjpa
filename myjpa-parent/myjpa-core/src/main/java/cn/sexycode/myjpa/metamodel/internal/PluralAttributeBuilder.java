package cn.sexycode.myjpa.metamodel.internal;

import cn.sexycode.myjpa.mapping.Property;
import cn.sexycode.myjpa.metamodel.ManagedTypeDescriptor;
import cn.sexycode.myjpa.metamodel.SimpleTypeDescriptor;

import javax.persistence.metamodel.Attribute;
import java.lang.reflect.Member;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * A "parameter object" for creating a plural attribute
 */
public class PluralAttributeBuilder<D, C, E, K> {
	private final ManagedTypeDescriptor<D> declaringType;
	private final SimpleTypeDescriptor<E> valueType;

	private Attribute.PersistentAttributeType attributeNature;

	private Property property;
	private Member member;
	private Class<C> collectionClass;

	private SimpleTypeDescriptor<K> keyType;


	public PluralAttributeBuilder(
			ManagedTypeDescriptor<D> ownerType,
			SimpleTypeDescriptor<E> elementType,
			Class<C> collectionClass,
			SimpleTypeDescriptor<K> keyType) {
		this.declaringType = ownerType;
		this.valueType = elementType;
		this.collectionClass = collectionClass;
		this.keyType = keyType;
	}

	public ManagedTypeDescriptor<D> getDeclaringType() {
		return declaringType;
	}

	public Attribute.PersistentAttributeType getAttributeNature() {
		return attributeNature;
	}

	public SimpleTypeDescriptor<K> getKeyType() {
		return keyType;
	}

	public Class<C> getCollectionClass() {
		return collectionClass;
	}

	public SimpleTypeDescriptor<E> getValueType() {
		return valueType;
	}

	public Property getProperty() {
		return property;
	}

	public Member getMember() {
		return member;
	}

	public PluralAttributeBuilder<D,C,E,K> member(Member member) {
		this.member = member;
		return this;
	}

	public PluralAttributeBuilder<D,C,E,K> property(Property property) {
		this.property = property;
		return this;
	}

	public PluralAttributeBuilder<D,C,E,K> persistentAttributeType(Attribute.PersistentAttributeType attrType) {
		this.attributeNature = attrType;
		return this;
	}

	@SuppressWarnings( "unchecked" )
	public AbstractPluralAttribute<D,C,E> build() {
		//apply strict spec rules first
		if ( Map.class.equals( collectionClass ) ) {
			final PluralAttributeBuilder<D,Map<K,E>,E,K> builder = (PluralAttributeBuilder<D,Map<K,E>,E,K>) this;
			return (AbstractPluralAttribute<D, C, E>) new MapAttributeImpl<>(
					builder
			);
		}
		else if ( Set.class.equals( collectionClass ) ) {
			final PluralAttributeBuilder<D, Set<E>, E,?> builder = (PluralAttributeBuilder<D, Set<E>, E,?>) this;
			return (AbstractPluralAttribute<D, C, E>) new SetAttributeImpl<>(
					builder
			);
		}
		else if ( List.class.equals( collectionClass ) ) {
			final PluralAttributeBuilder<D, List<E>, E,?> builder = (PluralAttributeBuilder<D, List<E>, E,?>) this;
			return (AbstractPluralAttribute<D, C, E>) new ListAttributeImpl<>(
					builder
			);
		}
		else if ( Collection.class.equals( collectionClass ) ) {
			final PluralAttributeBuilder<D, Collection<E>,E,?> builder = (PluralAttributeBuilder<D, Collection<E>, E,?>) this;
			return (AbstractPluralAttribute<D, C, E>) new BagAttributeImpl<>(
					builder
			);
		}

		//apply loose rules
		if ( collectionClass.isArray() ) {
			final PluralAttributeBuilder<D, List<E>, E,?> builder = (PluralAttributeBuilder<D, List<E>, E,?>) this;
			return (AbstractPluralAttribute<D, C, E>) new ListAttributeImpl<>(
					builder
			);
		}

		if ( Map.class.isAssignableFrom( collectionClass ) ) {
			final PluralAttributeBuilder<D,Map<K,E>,E,K> builder = (PluralAttributeBuilder<D,Map<K,E>,E,K>) this;
			return (AbstractPluralAttribute<D, C, E>) new MapAttributeImpl<>(
					builder
			);
		}
		else if ( Set.class.isAssignableFrom( collectionClass ) ) {
			final PluralAttributeBuilder<D,Set<E>, E,?> builder = (PluralAttributeBuilder<D, Set<E>, E,?>) this;
			return (AbstractPluralAttribute<D, C, E>) new SetAttributeImpl<>(
					builder
			);
		}
		else if ( List.class.isAssignableFrom( collectionClass ) ) {
			final PluralAttributeBuilder<D, List<E>, E,?> builder = (PluralAttributeBuilder<D, List<E>, E,?>) this;
			return (AbstractPluralAttribute<D, C, E>) new ListAttributeImpl<>(
					builder
			);
		}
		else if ( Collection.class.isAssignableFrom( collectionClass ) ) {
			final PluralAttributeBuilder<D, Collection<E>,E,?> builder = (PluralAttributeBuilder<D, Collection<E>, E,?>) this;
			return (AbstractPluralAttribute<D, C, E>) new BagAttributeImpl<>(
					builder
			);
		}
		throw new UnsupportedOperationException( "Unkown collection: " + collectionClass );
	}
}
