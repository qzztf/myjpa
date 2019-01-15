/*
 * Copyright 2012-2018 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package cn.sexycode.mybatis.jpa.boot.autoconfigure;

import cn.sexycode.mybatis.jpa.data.repository.support.MyJpaRepositoryFactoryBean;
import cn.sexycode.mybatis.jpa.orm.vendor.MyJpaVendorAdapter;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.orm.jpa.HibernatePropertiesCustomizer;
import org.springframework.boot.autoconfigure.orm.jpa.JpaBaseConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties;
import org.springframework.boot.autoconfigure.transaction.TransactionManagerCustomizers;
import org.springframework.boot.jdbc.SchemaManagementProvider;
import org.springframework.boot.jdbc.metadata.CompositeDataSourcePoolMetadataProvider;
import org.springframework.boot.jdbc.metadata.DataSourcePoolMetadata;
import org.springframework.boot.jdbc.metadata.DataSourcePoolMetadataProvider;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jndi.JndiLocatorDelegate;
import org.springframework.orm.jpa.vendor.AbstractJpaVendorAdapter;
import org.springframework.transaction.jta.JtaTransactionManager;
import org.springframework.util.ClassUtils;

import javax.sql.DataSource;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * {@link JpaBaseConfiguration} implementation for MyJpa
 */
@Configuration
//@ConditionalOnSingleCandidate(DataSource.class)
@EnableJpaRepositories(repositoryFactoryBeanClass = MyJpaRepositoryFactoryBean.class)
class MyJpaConfiguration extends JpaBaseConfiguration {

//	private static final Log logger = LogFactory.getLog(HibernateJpaConfiguration.class);

    private static final String JTA_PLATFORM = "hibernate.transaction.jta.platform";

    private static final String PROVIDER_DISABLES_AUTOCOMMIT = "hibernate.connection.provider_disables_autocommit";

    /**
     * {@code NoJtaPlatform} implementations for various Hibernate versions.
     */
    private static final String[] NO_JTA_PLATFORM_CLASSES = {
            "org.hibernate.engine.transaction.jta.platform.internal.NoJtaPlatform",
            "org.hibernate.service.jta.platform.internal.NoJtaPlatform"};

    /**
     * {@code WebSphereExtendedJtaPlatform} implementations for various Hibernate
     * versions.
     */
    private static final String[] WEBSPHERE_JTA_PLATFORM_CLASSES = {
            "org.hibernate.engine.transaction.jta.platform.internal.WebSphereExtendedJtaPlatform",
            "org.hibernate.service.jta.platform.internal.WebSphereExtendedJtaPlatform"};


    private DataSourcePoolMetadataProvider poolMetadataProvider;

//	private final List<HibernatePropertiesCustomizer> hibernatePropertiesCustomizers;

    MyJpaConfiguration(DataSource dataSource, JpaProperties jpaProperties,
                       ObjectProvider<JtaTransactionManager> jtaTransactionManager,
                       ObjectProvider<TransactionManagerCustomizers> transactionManagerCustomizers,
                       ObjectProvider<Collection<DataSourcePoolMetadataProvider>> metadataProviders,
                       ObjectProvider<List<SchemaManagementProvider>> providers,
//                       ObjectProvider<PhysicalNamingStrategy> physicalNamingStrategy,
//                       ObjectProvider<ImplicitNamingStrategy> implicitNamingStrategy,
                       ObjectProvider<List<HibernatePropertiesCustomizer>> hibernatePropertiesCustomizers) {
        super(dataSource, jpaProperties, jtaTransactionManager,
                transactionManagerCustomizers);
		/*this.defaultDdlAutoProvider = new HibernateDefaultDdlAutoProvider(
				providers.getIfAvailable(Collections::emptyList));*/
        this.poolMetadataProvider = new CompositeDataSourcePoolMetadataProvider(
                metadataProviders.getIfAvailable());
	/*	this.hibernatePropertiesCustomizers = determineHibernatePropertiesCustomizers(
				physicalNamingStrategy.getIfAvailable(),
				implicitNamingStrategy.getIfAvailable(),
				hibernatePropertiesCustomizers.getIfAvailable(Collections::emptyList));*/
    }

/*
	private List<HibernatePropertiesCustomizer> determineHibernatePropertiesCustomizers(
			PhysicalNamingStrategy physicalNamingStrategy,
			ImplicitNamingStrategy implicitNamingStrategy,
			List<HibernatePropertiesCustomizer> hibernatePropertiesCustomizers) {
		if (physicalNamingStrategy != null || implicitNamingStrategy != null) {
			LinkedList<HibernatePropertiesCustomizer> customizers = new LinkedList<>(
					hibernatePropertiesCustomizers);
			customizers.addFirst(new NamingStrategiesHibernatePropertiesCustomizer(
					physicalNamingStrategy, implicitNamingStrategy));
			return customizers;
		}
		return hibernatePropertiesCustomizers;
	}
*/

    @Override
    protected AbstractJpaVendorAdapter createJpaVendorAdapter() {
        return new MyJpaVendorAdapter();
    }

    @Override
    protected Map<String, Object> getVendorProperties() {
        return new LinkedHashMap<>();
		/*Supplier<String> defaultDdlMode = () -> this.defaultDdlAutoProvider
				.getDefaultDdlAuto(getDataSource());
		return new LinkedHashMap<>(
				getProperties().getHibernateProperties(new HibernateSettings()
						.ddlAuto(defaultDdlMode).hibernatePropertiesCustomizers(
								this.hibernatePropertiesCustomizers)));*/
    }

    @Override
    protected void customizeVendorProperties(Map<String, Object> vendorProperties) {
        super.customizeVendorProperties(vendorProperties);
        /*if (!vendorProperties.containsKey(JTA_PLATFORM)) {
            configureJtaPlatform(vendorProperties);
        }
        if (!vendorProperties.containsKey(PROVIDER_DISABLES_AUTOCOMMIT)) {
            configureProviderDisablesAutocommit(vendorProperties);
        }*/
    }

    private void configureJtaPlatform(Map<String, Object> vendorProperties)
            throws LinkageError {
        JtaTransactionManager jtaTransactionManager = getJtaTransactionManager();
        if (jtaTransactionManager != null) {
            if (runningOnWebSphere()) {
                // We can never use SpringJtaPlatform on WebSphere as
                // WebSphereUowTransactionManager has a null TransactionManager
                // which will cause Hibernate to NPE
                configureWebSphereTransactionPlatform(vendorProperties);
            } else {
                configureSpringJtaPlatform(vendorProperties, jtaTransactionManager);
            }
        } else {
            vendorProperties.put(JTA_PLATFORM, getNoJtaPlatformManager());
        }
    }

    private void configureProviderDisablesAutocommit(
            Map<String, Object> vendorProperties) {
        if (isDataSourceAutoCommitDisabled() && !isJta()) {
            vendorProperties.put(PROVIDER_DISABLES_AUTOCOMMIT, "true");
        }
    }

    private boolean isDataSourceAutoCommitDisabled() {
        DataSourcePoolMetadata poolMetadata = this.poolMetadataProvider
                .getDataSourcePoolMetadata(getDataSource());
        return poolMetadata != null
                && Boolean.FALSE.equals(poolMetadata.getDefaultAutoCommit());
    }

    private boolean runningOnWebSphere() {
        return ClassUtils.isPresent(
                "com.ibm.websphere.jtaextensions.ExtendedJTATransaction",
                getClass().getClassLoader());
    }

    private void configureWebSphereTransactionPlatform(
            Map<String, Object> vendorProperties) {
        vendorProperties.put(JTA_PLATFORM, getWebSphereJtaPlatformManager());
    }

    private Object getWebSphereJtaPlatformManager() {
        return getJtaPlatformManager(WEBSPHERE_JTA_PLATFORM_CLASSES);
    }

    private void configureSpringJtaPlatform(Map<String, Object> vendorProperties,
                                            JtaTransactionManager jtaTransactionManager) {
        try {
          /*  vendorProperties.put(JTA_PLATFORM,
                    new SpringJtaPlatform(jtaTransactionManager));*/
        } catch (LinkageError ex) {
            // NoClassDefFoundError can happen if Hibernate 4.2 is used and some
            // containers (e.g. JBoss EAP 6) wrap it in the superclass LinkageError
            if (!isUsingJndi()) {
                throw new IllegalStateException("Unable to set Hibernate JTA "
                        + "platform, are you using the correct "
                        + "version of Hibernate?", ex);
            }
            // Assume that Hibernate will use JNDI
           /* if (logger.isDebugEnabled()) {
                logger.debug("Unable to set Hibernate JTA platform : " + ex.getMessage());
            }*/
        }
    }

    private boolean isUsingJndi() {
        try {
            return JndiLocatorDelegate.isDefaultJndiEnvironmentAvailable();
        } catch (Error ex) {
            return false;
        }
    }

    private Object getNoJtaPlatformManager() {
        return getJtaPlatformManager(NO_JTA_PLATFORM_CLASSES);
    }

    private Object getJtaPlatformManager(String[] candidates) {
        for (String candidate : candidates) {
            try {
                return Class.forName(candidate).newInstance();
            } catch (Exception ex) {
                // Continue searching
            }
        }
        throw new IllegalStateException("Could not configure JTA platform");
    }

}
