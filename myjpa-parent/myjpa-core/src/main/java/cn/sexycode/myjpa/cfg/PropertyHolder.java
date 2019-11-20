package cn.sexycode.myjpa.cfg;

import cn.sexycode.myjpa.mapping.PersistentClass;
import cn.sexycode.myjpa.mapping.Property;
import cn.sexycode.sql.mapping.KeyValue;
import cn.sexycode.sql.mapping.Table;
import cn.sexycode.util.core.cls.XClass;
import cn.sexycode.util.core.cls.XProperty;

import javax.persistence.Column;
import javax.persistence.ForeignKey;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.criteria.Join;

/**
 * Property holder abstract property containers from their direct implementation
 *
 * @author Emmanuel Bernard
 */
public interface PropertyHolder {
    String getClassName();

    String getEntityOwnerClassName();

    Table getTable();

    void addProperty(Property prop, XClass declaringClass);

    //	void addProperty(Property prop, Ejb3Column[] columns, XClass declaringClass);

    KeyValue getIdentifier();

    /**
     * Return true if this component is or is embedded in a @EmbeddedId
     */
    boolean isOrWithinEmbeddedId();

    /**
     * Return true if this component is withing an @ElementCollection.
     */
    boolean isWithinElementCollection();

    PersistentClass getPersistentClass();

    boolean isComponent();

    boolean isEntity();

    void setParentProperty(String parentProperty);

    String getPath();

    /**
     * return null if the column is not overridden, or an array of column if true
     */
    Column[] getOverriddenColumn(String propertyName);

    /**
     * return null if the column is not overridden, or an array of column if true
     */
    JoinColumn[] getOverriddenJoinColumn(String propertyName);

    /**
     * return null if hte foreign key is not overridden, or the foreign key if true
     */
    default ForeignKey getOverriddenForeignKey(String propertyName) {
        // todo: does this necessarily need to be a default method?
        return null;
    }

    /**
     * return
     * - null if no join table is present,
     * - the join table if not overridden,
     * - the overridden join table otherwise
     */
    JoinTable getJoinTable(XProperty property);

    String getEntityName();

    Join addJoin(JoinTable joinTableAnn, boolean noDelayInPkColumnCreation);

    boolean isInIdClass();

    void setInIdClass(Boolean isInIdClass);

    /**
     * Called during binding to allow the PropertyHolder to inspect its discovered properties.  Mainly
     * this is used in collecting attribute conversion declarations (via @Convert/@Converts).
     *
     * @param property The property
     */
    void startingProperty(XProperty property);

    /**
     * Determine the AttributeConverter to use for the given property.
     *
     * @param property
     * @return
     */
    //	ConverterDescriptor resolveAttributeConverterDescriptor(XProperty property);
}
