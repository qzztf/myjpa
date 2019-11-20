package cn.sexycode.myjpa.metamodel.internal;


import cn.sexycode.myjpa.AvailableSettings;
import cn.sexycode.sql.util.ConfigurationHelper;
import cn.sexycode.util.core.properties.PropertiesUtil;

import java.util.Map;

/**
 * Enumerated setting used to control whether Hibernate looks for and populates
 * JPA static metamodel models of application's domain model.
 *
 * @author Andrea Boriero
 */
public enum JpaStaticMetaModelPopulationSetting {
    /**
     * ENABLED indicates that Hibernate will look for the JPA static metamodel description
     * of the application domain model and populate it.
     */
    ENABLED,
    /**
     * DISABLED indicates that Hibernate will not look for the JPA static metamodel description
     * of the application domain model.
     */
    DISABLED,
    /**
     * SKIP_UNSUPPORTED works as ENABLED but ignores any non-JPA features that would otherwise
     * result in the population failing.
     */
    SKIP_UNSUPPORTED;

    public static JpaStaticMetaModelPopulationSetting parse(String setting) {
        if ("enabled".equalsIgnoreCase(setting)) {
            return ENABLED;
        } else if ("disabled".equalsIgnoreCase(setting)) {
            return DISABLED;
        } else {
            return SKIP_UNSUPPORTED;
        }
    }

    public static JpaStaticMetaModelPopulationSetting determineJpaMetaModelPopulationSetting(Map configurationValues) {
        String setting = PropertiesUtil
                .getString(AvailableSettings.STATIC_METAMODEL_POPULATION, configurationValues, null);
        return JpaStaticMetaModelPopulationSetting.parse(setting);
    }
}
