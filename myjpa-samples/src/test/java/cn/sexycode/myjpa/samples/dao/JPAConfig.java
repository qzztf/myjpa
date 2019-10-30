package cn.sexycode.myjpa.samples.dao;

import cn.sexycode.myjpa.data.repository.support.MyjpaRepositoryFactoryBean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 *
 */
@Configuration
@EnableJpaRepositories(repositoryFactoryBeanClass = MyjpaRepositoryFactoryBean.class)
public class JPAConfig {

}
