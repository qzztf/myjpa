package cn.sexycode.myjpa.boot.autoconfigure;

import cn.sexycode.myjpa.spring.factory.BeanFactoryAdapter;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;

import javax.persistence.EntityManager;
import javax.sql.DataSource;

import static cn.sexycode.myjpa.boot.autoconfigure.MyjpaAutoConfiguration.MyjpaDataConfiguration;

/**
 * @author qinzaizhen
 */
@Configuration
@ConditionalOnClass({LocalContainerEntityManagerFactoryBean.class, EntityManager.class})
@EnableConfigurationProperties(JpaProperties.class)
@AutoConfigureAfter({ DataSourceAutoConfiguration.class })
@Import({ MyjpaConfiguration.class, MyjpaDataConfiguration.class})
@ConditionalOnBean(DataSource.class)
public class MyjpaAutoConfiguration {

    @Configuration
    @Import(MyjpaRepositoriesAutoConfigureRegistrar.class)
    @ConditionalOnClass(JpaRepository.class)
    @ConditionalOnProperty(prefix = "spring.data.jpa.repositories", name = "enabled", havingValue = "true", matchIfMissing = true)
    public static class MyjpaDataConfiguration{

    }


    @Bean("beanFactoryAdapter")
    @ConditionalOnMissingBean
    public static BeanFactoryAdapter beanFactoryAdapter(BeanFactory beanFactory) {
        return new BeanFactoryAdapter(beanFactory);
    }
}
