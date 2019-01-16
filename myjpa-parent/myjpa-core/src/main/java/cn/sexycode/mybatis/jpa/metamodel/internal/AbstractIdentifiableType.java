/*
 * Hibernate, Relational Persistence for Idiomatic Java
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later.
 * See the lgpl.txt file in the root directory or <http://www.gnu.org/licenses/lgpl-2.1.html>.
 */
package cn.sexycode.mybatis.jpa.metamodel.internal;

import javax.persistence.metamodel.Attribute;
import javax.persistence.metamodel.IdentifiableType;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.Type;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * Defines commonality for the JPA {@link IdentifiableType} types.  JPA defines
 * identifiable types as entities or mapped-superclasses.  Basically things to which an
 * identifier can be attached.
 * <p/>
 * NOTE : Currently we only really have support for direct entities in the Hibernate metamodel
 * as the information for them is consumed into the closest actual entity subclass(es) in the
 * internal Hibernate mapping-metamodel.
 *
 * @author Steve Ebersole
 */
public abstract class AbstractIdentifiableType<X>
		extends AbstractManagedType<X>
		implements IdentifiableType<X>, Serializable {
	private SingularAttributeImpl<X, ?> id;

	private final boolean hasIdentifierProperty;
	private final boolean hasIdClass;
	private Set<SingularAttribute<? super X,?>> idClassAttributes;

	private final boolean isVersioned;


	public AbstractIdentifiableType(
			Class<X> javaType,
			String typeName,
			AbstractIdentifiableType<? super X> superType,
			boolean hasIdClass,
			boolean hasIdentifierProperty,
			boolean versioned) {
		super( javaType, typeName, superType );
		this.hasIdClass = hasIdClass;
		this.hasIdentifierProperty = hasIdentifierProperty;
		this.isVersioned = versioned;
		this.id =  new SingularAttributeImpl.Identifier(
				"id",
				getIdType().getJavaType(),
				null,
				null,
				getIdType(),
				Attribute.PersistentAttributeType.BASIC
		);
	}

	public boolean hasIdClass() {
		return hasIdClass;
	}

	@Override
	public boolean hasSingleIdAttribute() {
//		return !hasIdClass() && hasIdentifierProperty;
		return true;
	}

	@Override
	@SuppressWarnings("unchecked")
	public AbstractIdentifiableType<? super X> getSupertype() {
		// overridden simply to perform the cast
		return (AbstractIdentifiableType<? super X>) super.getSupertype();
	}

	@Override
	@SuppressWarnings({ "unchecked" })
	public <Y> SingularAttribute<? super X, Y> getId(Class<Y> javaType) {
		ensureNoIdClass();
		SingularAttributeImpl id = locateIdAttribute();
		if ( id != null ) {
			checkType( id, javaType );
		}
		return ( SingularAttribute<? super X, Y> ) id;
	}

	private void ensureNoIdClass() {
		if ( hasIdClass() ) {
			throw new IllegalArgumentException(
					"Illegal call to IdentifiableType#getId for class [" + getTypeName() + "] defined with @IdClass"
			);
		}
	}

	private SingularAttributeImpl locateIdAttribute() {
		if ( id != null ) {
			return id;
		}
		else {
			if ( getSupertype() != null ) {
				SingularAttributeImpl id = getSupertype().internalGetId();
				if ( id != null ) {
					return id;
				}
			}
		}

		return null;
	}

	protected SingularAttributeImpl internalGetId() {
		if ( id != null ) {
			return id;
		}
		else {
			if ( getSupertype() != null ) {
				return getSupertype().internalGetId();
			}
		}

		return null;
	}

	@SuppressWarnings("unchecked")
	private void checkType(SingularAttributeImpl attribute, Class javaType) {
		if ( ! javaType.isAssignableFrom( attribute.getType().getJavaType() ) ) {
			throw new IllegalArgumentException(
					String.format(
							"Attribute [%s#%s : %s] not castable to requested type [%s]",
							getTypeName(),
							attribute.getName(),
							attribute.getType().getJavaType().getName(),
							javaType.getName()
					)
			);
		}
	}
	@Override
	@SuppressWarnings({ "unchecked" })
	public <Y> SingularAttribute<X, Y> getDeclaredId(Class<Y> javaType) {
		/*ensureNoIdClass();
		if ( id == null ) {
			throw new IllegalArgumentException( "The id attribute is not declared on this type [" + getTypeName() + "]" );
		}
		checkType( id, javaType );
		return (SingularAttribute<X, Y>) id;*/
		return null;
	}

	@Override
	@SuppressWarnings("unchecked")
	public Type<?> getIdType() {
		/*final SingularAttributeImpl id = locateIdAttribute();
		if ( id != null ) {
			return id.getType();
		}*/
		return new BasicTypeImpl<>(String.class, PersistenceType.BASIC);
		/*Set<SingularAttribute<? super X, ?>> idClassAttributes = getIdClassAttributesSafely();
		if ( idClassAttributes != null ) {
			if ( idClassAttributes.size() == 1 ) {
				return idClassAttributes.iterator().next().getType();
			}
		}

		return null;*/
	}

	/**
	 * A form of {@link #getIdClassAttributes} which prefers to return {@code null} rather than throw exceptions
	 *
	 * @return IdClass attributes or {@code null}
	 */
	public Set<SingularAttribute<? super X, ?>> getIdClassAttributesSafely() {
		if ( !hasIdClass() ) {
			return null;
		}
		final Set<SingularAttribute<? super X, ?>> attributes = new HashSet<SingularAttribute<? super X, ?>>();
		internalCollectIdClassAttributes( attributes );

		if ( attributes.isEmpty() ) {
			return null;
		}

		return attributes;
	}

	@Override
	public Set<SingularAttribute<? super X, ?>> getIdClassAttributes() {
		if ( !hasIdClass() ) {
			throw new IllegalArgumentException( "This class [" + getJavaType() + "] does not define an IdClass" );
		}

		final Set<SingularAttribute<? super X, ?>> attributes = new HashSet<SingularAttribute<? super X, ?>>();
		internalCollectIdClassAttributes( attributes );

		if ( attributes.isEmpty() ) {
//			throw new IllegalArgumentException( "Unable to locate IdClass attributes [" + getJavaType() + "]" );
		}

		return attributes;
	}

	@SuppressWarnings("unchecked")
	private void internalCollectIdClassAttributes(Set attributes) {
		if ( idClassAttributes != null ) {
			attributes.addAll( idClassAttributes );
		}
		else if ( getSupertype() != null ) {
			getSupertype().internalCollectIdClassAttributes( attributes );
		}
	}

	@Override
	public boolean hasVersionAttribute() {
		return isVersioned;
	}

	/*public boolean hasDeclaredVersionAttribute() {
		return isVersioned && version != null;
	}*/

	@Override
	@SuppressWarnings({ "unchecked" })
	public <Y> SingularAttribute<? super X, Y> getVersion(Class<Y> javaType) {
		// todo : is return null allowed?
		if ( ! hasVersionAttribute() ) {
			return null;
		}

		/*SingularAttributeImpl version = locateVersionAttribute();
		if ( version != null ) {
			checkType( version, javaType );
		}
		return ( SingularAttribute<? super X, Y> ) version;*/
		return null;
	}

	/*private SingularAttributeImpl locateVersionAttribute() {
		if ( version != null ) {
			return version;
		}
		else {
			if ( getSupertype() != null ) {
				SingularAttributeImpl version = getSupertype().internalGetVersion();
				if ( version != null ) {
					return version;
				}
			}
		}

		return null;
	}

	protected SingularAttributeImpl internalGetVersion() {
		if ( version != null ) {
			return version;
		}
		else {
			if ( getSupertype() != null ) {
				return getSupertype().internalGetVersion();
			}
		}

		return null;
	}
*/
	@Override
	@SuppressWarnings({ "unchecked" })
	public <Y> SingularAttribute<X, Y> getDeclaredVersion(Class<Y> javaType) {
		checkDeclaredVersion();
		/*checkType( version, javaType );
		return ( SingularAttribute<X, Y> ) version;*/
		return null;
	}

	private void checkDeclaredVersion() {
		/*if ( version == null || ( getSupertype() != null && getSupertype().hasVersionAttribute() )) {
			throw new IllegalArgumentException(
					"The version attribute is not declared by this type [" + getJavaType() + "]"
			);
		}*/
	}


	/**
	 * For used to retrieve the declared version when populating the static metamodel.
	 *
	 * @return The declared
	 */
	public SingularAttribute<X, ?> getDeclaredVersion() {
		/*checkDeclaredVersion();
		return version;*/
		return null;
	}

	/*@Override
	public Builder<X> getBuilder() {
		final AbstractManagedType.Builder<X> managedBuilder = super.getBuilder();
		return new Builder<X>() {
			public void applyIdAttribute(SingularAttributeImpl<X, ?> idAttribute) {
				AbstractIdentifiableType.this.id = idAttribute;
				managedBuilder.addAttribute( idAttribute );
			}

			public void applyIdClassAttributes(Set<SingularAttribute<? super X,?>> idClassAttributes) {
				for ( SingularAttribute<? super X,?> idClassAttribute : idClassAttributes ) {
					if ( AbstractIdentifiableType.this == idClassAttribute.getDeclaringType() ) {
						@SuppressWarnings({ "unchecked" })
						SingularAttribute<X,?> declaredAttribute = ( SingularAttribute<X,?> ) idClassAttribute;
						addAttribute( declaredAttribute );
					}
				}
				AbstractIdentifiableType.this.idClassAttributes = idClassAttributes;
			}

			public void applyVersionAttribute(SingularAttributeImpl<X, ?> versionAttribute) {
				AbstractIdentifiableType.this.version = versionAttribute;
				managedBuilder.addAttribute( versionAttribute );
			}

			public void addAttribute(Attribute<X, ?> attribute) {
				managedBuilder.addAttribute( attribute );
			}
		};
	}

	public static interface Builder<X> extends AbstractManagedType.Builder<X> {
		public void applyIdAttribute(SingularAttributeImpl<X, ?> idAttribute);
		public void applyIdClassAttributes(Set<SingularAttribute<? super X, ?>> idClassAttributes);
		public void applyVersionAttribute(SingularAttributeImpl<X, ?> versionAttribute);
	}*/
}
