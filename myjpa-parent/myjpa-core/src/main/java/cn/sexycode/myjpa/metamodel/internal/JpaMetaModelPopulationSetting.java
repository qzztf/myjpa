package cn.sexycode.myjpa.metamodel.internal;

import cn.sexycode.myjpa.AvailableSettings;
import cn.sexycode.util.core.properties.PropertiesUtil;

import java.util.Map;

/**
 * @author Steve Ebersole
 */
public enum JpaMetaModelPopulationSetting {
    ENABLED, DISABLED, IGNORE_UNSUPPORTED;

    public static JpaMetaModelPopulationSetting parse(String setting) {
        if ("enabled".equalsIgnoreCase(setting)) {
            return ENABLED;
        } else if ("disabled".equalsIgnoreCase(setting)) {
            return DISABLED;
        } else {
            return IGNORE_UNSUPPORTED;
        }
    }

    public static JpaMetaModelPopulationSetting determineJpaMetaModelPopulationSetting(Map configurationValues) {
        String setting = PropertiesUtil
                .getString(AvailableSettings.JPA_METAMODEL_POPULATION, configurationValues, null);
        if (setting == null) {
            setting = PropertiesUtil.getString(AvailableSettings.JPA_METAMODEL_GENERATION, configurationValues, null);
            if (setting != null) {

            }
        }
        return JpaMetaModelPopulationSetting.parse(setting);
    }
}
