package cn.sexycode.myjpa.boot;

import cn.sexycode.myjpa.binding.MetadataBuildingContext;
import cn.sexycode.myjpa.sql.model.Identifier;

/**
 * Helper contract for dealing with naming strategies.
 */
public interface NamingStrategyHelper {
    /**
     * Called when the user supplied no explicit name/identifier for the given database object.
     * <p/>
     * Typically implementations will access the {@link ImplicitNamingStrategy} via
     * {@link org.hibernate.boot.spi.MetadataBuildingContext#getBuildingOptions()} ->
     * {@link org.hibernate.boot.spi.MetadataBuildingOptions#getImplicitNamingStrategy()}
     * <p/>
     * For proper quoting, {@link org.hibernate.boot.model.relational.Database#toIdentifier(String)}
     * should be used via
     * {@link org.hibernate.boot.spi.MetadataBuildingContext#getMetadataCollector()} ->
     * {@link org.hibernate.boot.spi.InFlightMetadataCollector#getDatabase()}
     *
     * @param buildingContext The building context in which this is called.
     * @return The implicit name
     */
    Identifier determineImplicitName(MetadataBuildingContext buildingContext);

    /**
     * Called when the user has supplied an explicit name for the database object.
     * <p/>
     * Typically implementations will access the {@link ImplicitNamingStrategy} via
     * {@link org.hibernate.boot.spi.MetadataBuildingContext#getBuildingOptions()} ->
     * {@link org.hibernate.boot.spi.MetadataBuildingOptions#getImplicitNamingStrategy()}
     * <p/>
     * For proper quoting, {@link org.hibernate.boot.model.relational.Database#toIdentifier(String)}
     * should be used via
     * {@link org.hibernate.boot.spi.MetadataBuildingContext#getMetadataCollector()} ->
     * {@link org.hibernate.boot.spi.InFlightMetadataCollector#getDatabase()}
     *
     * @param explicitName    The explicit object name.
     * @param buildingContext The building context in which this is called.
     * @return The strategy-handled name.
     */
    Identifier handleExplicitName(String explicitName, MetadataBuildingContext buildingContext);

    /**
     * Handle converting a logical name to a physical name
     * <p/>
     * Typically implementations will access the {@link PhysicalNamingStrategy} via
     * {@link org.hibernate.boot.spi.MetadataBuildingContext#getBuildingOptions()} ->
     * {@link org.hibernate.boot.spi.MetadataBuildingOptions#getPhysicalNamingStrategy()}
     * <p/>
     * For proper quoting, {@link org.hibernate.boot.model.relational.Database#toIdentifier(String)}
     * should be used via
     * {@link org.hibernate.boot.spi.MetadataBuildingContext#getMetadataCollector()} ->
     * {@link org.hibernate.boot.spi.InFlightMetadataCollector#getDatabase()}
     *
     * @param logicalName     The logical name to convert to a physical name
     * @param buildingContext The building context in which this is called.
     * @return The physical name
     */
    Identifier toPhysicalName(Identifier logicalName, MetadataBuildingContext buildingContext);
}