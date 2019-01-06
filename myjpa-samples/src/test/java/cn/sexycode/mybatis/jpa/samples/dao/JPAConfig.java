package cn.sexycode.mybatis.jpa.samples.dao;

import cn.sexycode.mybatis.jpa.orm.vendor.MyJpaVendorAdapter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.vendor.AbstractJpaVendorAdapter;

/**
 *
 */
@Configuration
@EnableJpaRepositories
public class JPAConfig {
    @Bean
    public AbstractJpaVendorAdapter createJpaVendorAdapter() {
        return new MyJpaVendorAdapter();
    }
}
