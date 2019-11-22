package cn.sexycode.myjpa.sql.type;

import cn.sexycode.myjpa.sql.type.ForeignKeyDirection;
import cn.sexycode.myjpa.sql.type.Type;

/**
 * A type that represents some kind of association between entities.
 *
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
