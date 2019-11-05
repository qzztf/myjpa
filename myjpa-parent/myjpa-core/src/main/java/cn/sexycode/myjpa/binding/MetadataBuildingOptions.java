package cn.sexycode.myjpa.binding;

import cn.sexycode.sql.dialect.function.SQLFunction;
import cn.sexycode.util.core.file.scan.ScanEnvironment;
import cn.sexycode.util.core.service.ServiceRegistry;
import cn.sexycode.util.core.service.StandardServiceRegistry;

import java.util.List;
import java.util.Map;

/**
 * Describes the options used while building the Metadata object (during
 * {@link org.hibernate.boot.MetadataBuilder#build()} processing).
 */
public interface MetadataBuildingOptions {
    /**
     * Access the list of BasicType registrations.  These are the BasicTypes explicitly
     * registered via calls to:<ul>
     * <li>{@link org.hibernate.boot.MetadataBuilder#applyBasicType(org.hibernate.type.BasicType)}</li>
     * <li>{@link org.hibernate.boot.MetadataBuilder#applyBasicType(org.hibernate.type.BasicType, String[])}</li>
     * <li>{@link org.hibernate.boot.MetadataBuilder#applyBasicType(org.hibernate.usertype.UserType, String[])}</li>
     * <li>{@link org.hibernate.boot.MetadataBuilder#applyBasicType(org.hibernate.usertype.CompositeUserType, String[])}</li>
     * </ul>
     *
     * @return The BasicType registrations
     */
    List<BasicTypeRegistration> getBasicTypeRegistrations();

    /**
     * Access to the Scanner to be used for scanning.  Can be:<ul>
     * <li>A Scanner instance</li>
     * <li>A Class reference to the Scanner implementor</li>
     * <li>A String naming the Scanner implementor</li>
     * </ul>
     *
     * @return The scanner
     */
    Object getScanner();

    /**
     * Access to any SQL functions explicitly registered with the MetadataBuilder.  This
     * does not include Dialect defined functions, etc.
     *
     * @return The SQLFunctions registered through MetadataBuilder
     */
    Map<String, SQLFunction> getSqlFunctions();

    MappingDefaults getMappingDefaults();

    ScanEnvironment getScanEnvironment();

    ServiceRegistry getServiceRegistry();
}
