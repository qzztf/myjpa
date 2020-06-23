package cn.sexycode.myjpa.metamodel.internal;

import javax.persistence.metamodel.*;

public interface ManagedTypeDescriptor<J> extends SimpleTypeDescriptor<J>, ManagedDomainType<J> {
	/**
	 * Get this ManagedType's super type descriptor.  ATM only supported for the
	 * {@link IdentifiableTypeDescriptor} branch of the ManagedType tree
	 */
	ManagedTypeDescriptor<? super J> getSuperType();

	/**
	 * The Hibernate "type name" ("entity name" - for non-POJO representations)
	 */
	String getName();

/*

	<S extends J> ManagedTypeDescriptor<S> findSubType(String subTypeName);

	<S extends J> ManagedTypeDescriptor<S> findSubType(Class<S> type);
*/

	/**
	 * In-flight access to the managed type.  Used to add attributes, etc.
	 * Valid only during boot.
	 */
	InFlightAccess<J> getInFlightAccess();

	/**
	 * Used during creation of the managed type object to add its attributes
	 */
	interface InFlightAccess<J> {
		void addAttribute(PersistentAttributeDescriptor<J, ?> attribute);

		/**
		 * Called when configuration of the managed-type is complete
		 */
		void finishUp();
	}

	PersistentAttributeDescriptor<J, ?> findDeclaredAttribute(String name);

	PersistentAttributeDescriptor<? super J, ?> findAttribute(String name);

	@Override
	PersistentAttributeDescriptor<J, ?> getDeclaredAttribute(String name);

	@Override
	PersistentAttributeDescriptor<? super J, ?> getAttribute(String name);

	@Override
	<Y> SingularPersistentAttribute<? super J, Y> getSingularAttribute(String name, Class<Y> type);

	@Override
	<Y> SingularPersistentAttribute<J,Y> getDeclaredSingularAttribute(String name, Class<Y> type);

	<C,E> PluralPersistentAttribute<J,C,E> getPluralAttribute(String name);

	@Override
	<E> BagPersistentAttribute<? super J, E> getCollection(String name, Class<E> elementType);

	@Override
	default <E> CollectionAttribute<J, E> getDeclaredCollection(
			String name, Class<E> elementType) {
		return null;
	}

	@Override
	default <E> SetAttribute<? super J, E> getSet(String name, Class<E> elementType) {
		return null;
	}

	@Override
	default <E> SetAttribute<J, E> getDeclaredSet(String name, Class<E> elementType) {
		return null;
	}

	@Override
	default <E> ListAttribute<? super J, E> getList(String name, Class<E> elementType) {
		return null;
	}

	@Override
	default <E> ListAttribute<J, E> getDeclaredList(String name, Class<E> elementType) {
		return null;
	}

	@Override
	default <K, V> MapAttribute<? super J, K, V> getMap(
			String name, Class<K> keyType, Class<V> valueType) {
		return null;
	}

	@Override
	default <K, V> MapAttribute<J, K, V> getDeclaredMap(
			String name, Class<K> keyType, Class<V> valueType) {
		return null;
	}

	@Override
	default SingularAttribute<? super J, ?> getSingularAttribute(String name) {
		return null;
	}

	@Override
	default SingularAttribute<J, ?> getDeclaredSingularAttribute(String name) {
		return null;
	}

	@Override
	default CollectionAttribute<? super J, ?> getCollection(String name) {
		return null;
	}

	@Override
	default CollectionAttribute<J, ?> getDeclaredCollection(String name) {
		return null;
	}

	@Override
	default SetPersistentAttribute<? super J, ?> getSet(String name) {
		return null;
	}

	@Override
	default SetPersistentAttribute<J, ?> getDeclaredSet(String name) {
		return null;
	}

	@Override
	default ListPersistentAttribute<? super J, ?> getList(String name) {
		return null;
	}

	@Override
	default ListPersistentAttribute<J, ?> getDeclaredList(String name) {
		return null;
	}

	@Override
	default MapPersistentAttribute<? super J, ?, ?> getMap(String name) {
		return null;
	}

	@Override
	default MapPersistentAttribute<J, ?, ?> getDeclaredMap(String name) {
		return null;
	}
}