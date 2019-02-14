/*
 * Hibernate, Relational Persistence for Idiomatic Java
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later.
 * See the lgpl.txt file in the root directory or <http://www.gnu.org/licenses/lgpl-2.1.html>.
 */
package cn.sexycode.sql.type;

/**
 * A type that represents some kind of association between entities.
 *
 * @author Gavin King
 * @see org.hibernate.engine.internal.Cascade
 */
public interface AssociationType extends Type {

    /**
     * Get the foreign key directionality of this association
     */
    public ForeignKeyDirection getForeignKeyDirection();

    //TODO: move these to a new JoinableType abstract class,
    //extended by EntityType and PersistentCollectionType:

    /**
     * Is the primary key of the owning entity table
     * to be used in the join?
     */
    public boolean useLHSPrimaryKey();

    /**
     * Get the name of a property in the owning entity
     * that provides the join key (null if the identifier)
     */
    public String getLHSPropertyName();

    /**
     * The name of a unique property of the associated entity
     * that provides the join key (null if the identifier of
     * an entity, or key of a collection)
     */
    public String getRHSUniqueKeyPropertyName();


}
