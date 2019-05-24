package cn.sexycode.mybatis.jpa.samples.dao;

import cn.sexycode.mybatis.jpa.data.repository.support.MyJpaRepositoryFactoryBean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 *
 */
@Configuration
@EnableJpaRepositories(repositoryFactoryBeanClass = MyJpaRepositoryFactoryBean.class)
public class JPAConfig {

}
