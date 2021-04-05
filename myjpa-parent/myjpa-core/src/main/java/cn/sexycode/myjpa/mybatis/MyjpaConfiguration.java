package cn.sexycode.myjpa.mybatis;

import cn.sexycode.util.core.object.ObjectUtils;
import cn.sexycode.util.core.object.ReflectionUtils;
import org.apache.ibatis.session.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.Properties;

/**
 * @author qzz
 */
public class MyjpaConfiguration extends Configuration {
    private static final Logger LOGGER = LoggerFactory.getLogger(MyjpaConfiguration.class);

    public MyjpaConfiguration() {
        this(null);
    }

    public MyjpaConfiguration(Configuration configuration) {
        if (ObjectUtils.isEmpty(configuration)) {
            configuration = new Configuration();
        }
        copyConfiguration(configuration);
    }

    private void copyConfiguration(Configuration config) {
        ReflectionUtils.doWithFields(MyjpaConfiguration.class, field -> {
            try {
                Object value = ReflectionUtils.getField(field, config);
                if (value != null) {
                    ReflectionUtils.setField(field, MyjpaConfiguration.this, value);
                }
            } catch (Exception ignored) {
            }

        });
        ReflectionUtils.setField("mapperRegistry", this, new MybatisMapperRegistry(this, config.getMapperRegistry()));
    }



    public MyjpaConfiguration(Configuration configuration, Map properties) {
        this(configuration);
        if (!ObjectUtils.isEmpty(properties)) {
            Properties prop = new Properties();
            prop.putAll(properties);
            if (ObjectUtils.isEmpty(getVariables())) {
                setVariables(prop);
            } else {
                getVariables().putAll(prop);
            }
        }
    }

}
