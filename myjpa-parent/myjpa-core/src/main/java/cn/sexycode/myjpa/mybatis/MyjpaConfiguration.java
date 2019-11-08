package cn.sexycode.myjpa.mybatis;

import cn.sexycode.myjpa.binding.Metadata;
import cn.sexycode.util.core.object.ObjectUtils;
import org.apache.ibatis.session.Configuration;

import java.util.Map;
import java.util.Properties;

public class MyjpaConfiguration extends Configuration {
    private Configuration configuration;

    private Metadata metadata;

    public MyjpaConfiguration() {
        this((Configuration) null);
    }

    public MyjpaConfiguration(Configuration configuration) {
        if (ObjectUtils.isEmpty(configuration)) {
            this.configuration = new Configuration();
        }
        this.configuration = configuration;
    }

    public MyjpaConfiguration(Map properties) {
        this();
        if (!ObjectUtils.isEmpty(properties)) {
            Properties prop = new Properties();
            prop.putAll(properties);
            this.configuration.setVariables(prop);
        }
    }

    public Configuration getConfiguration() {
        return configuration;
    }

    public void setConfiguration(Configuration configuration) {
        this.configuration = configuration;
    }
}
