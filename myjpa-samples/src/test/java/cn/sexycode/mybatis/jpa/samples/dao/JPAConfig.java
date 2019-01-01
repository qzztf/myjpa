package cn.sexycode.mybatis.jpa.samples.dao;

import cn.sexycode.mybatis.jpa.orm.vendor.MyJpaVendorAdapter;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureDataJpa;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.jpa.vendor.AbstractJpaVendorAdapter;

/**
 *
 */
@Configuration
@AutoConfigureDataJpa
public class JPAConfig {
    @Bean
    public AbstractJpaVendorAdapter createJpaVendorAdapter() {
        return new MyJpaVendorAdapter();
    }
}
