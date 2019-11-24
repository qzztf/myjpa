package cn.sexycode.myjpa.boot;

/**
 * Context for determining the implicit name for a join table.
 *
 * @author Steve Ebersole
 * @see javax.persistence.JoinTable
 */
public interface ImplicitJoinTableNameSource extends ImplicitNameSource {
    /**
     * Access to the physical name of the owning entity's primary table.
     *
     * @return Owning entity's primary table  name.
     */
    String getOwningPhysicalTableName();

    /**
     * Access to entity naming information for the owning side.
     *
     * @return Owning entity naming information
     */
    EntityNaming getOwningEntityNaming();

    /**
     * Access to the physical name of the non-owning entity's primary table.
     *
     * @return Owning entity's primary table  name.
     */
    String getNonOwningPhysicalTableName();

    /**
     * Access to entity naming information for the owning side.
     *
     * @return Owning entity naming information
     */
    EntityNaming getNonOwningEntityNaming();

    /**
     * Access to the name of the attribute, from the owning side, that defines the association.
     *
     * @return The owning side's attribute name.
     */
    AttributePath getAssociationOwningAttributePath();
}
