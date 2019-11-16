package cn.sexycode.myjpa.mybatis;

import org.apache.ibatis.session.Configuration;

/**
 * @author qinzaizhen
 */
public interface ConfigurationFactory {
    /**
     * @param mybatisConfiguration
     * @return
     */
    default MyjpaConfiguration getConfiguration(Configuration mybatisConfiguration) {
        return new MyjpaConfiguration(mybatisConfiguration);
    }
}
