package cn.sexycode.myjpa.sql.type;


/**
 * Represents directionality of the foreign key constraint
 *
 */
public enum ForeignKeyDirection {
    /**
     * A foreign key from child to parent
     */
    TO_PARENT,
    /**
     * A foreign key from parent to child
     */
    FROM_PARENT;
}
