package cn.sexycode.myjpa.mapping;

/**
 * Defines an additional contract for PersistentClass/Join in terms of being able to
 * contain attributes (Property).
 * <p/>
 * NOTE : this unifying contract is only used atm from HBM binding and so only defines the
 * needs of that use-case.
 *
 * @author Steve Ebersole
 */
public interface AttributeContainer {
	void addProperty(Property attribute);
}