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
        this.configuration = configuration;
        if (ObjectUtils.isEmpty(configuration)) {
            this.configuration = new Configuration();
        }
    }

    public MyjpaConfiguration(Map properties) {
        this();
        if (!ObjectUtils.isEmpty(properties)) {
            Properties prop = new Properties();
            prop.putAll(properties);
            if (ObjectUtils.isEmpty(this.configuration.getVariables())) {
                this.configuration.setVariables(prop);
            } else {
                this.configuration.getVariables().putAll(prop);
            }

        }
    }

    public Configuration getConfiguration() {
        return configuration;
    }

    public void setConfiguration(Configuration configuration) {
        this.configuration = configuration;
    }
}
