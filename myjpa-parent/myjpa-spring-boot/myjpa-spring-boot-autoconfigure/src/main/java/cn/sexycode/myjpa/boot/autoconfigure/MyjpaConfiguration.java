package cn.sexycode.myjpa.boot.autoconfigure;

import cn.sexycode.myjpa.orm.vendor.MyjpaVendorAdapter;
import cn.sexycode.myjpa.query.DefaultQueryFactory;
import cn.sexycode.myjpa.query.QueryFactory;
import cn.sexycode.myjpa.sql.dialect.DialectFactory;
import cn.sexycode.myjpa.sql.dialect.DialectFactoryImpl;
import cn.sexycode.myjpa.sql.dialect.StandardDialectResolver;
import cn.sexycode.util.core.cls.classloading.ClassLoaderService;
import cn.sexycode.util.core.cls.classloading.ClassLoaderServiceImpl;
import cn.sexycode.util.core.factory.BeanFactoryUtil;
import cn.sexycode.util.core.factory.selector.StrategySelectorImpl;
import cn.sexycode.util.core.service.Service;
import cn.sexycode.util.core.service.ServiceRegistry;
import cn.sexycode.util.core.service.StandardServiceRegistry;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnSingleCandidate;
import org.springframework.boot.autoconfigure.orm.jpa.JpaBaseConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties;
import org.springframework.boot.autoconfigure.transaction.TransactionManagerCustomizers;
import org.springframework.boot.jdbc.SchemaManagementProvider;
import org.springframework.boot.jdbc.metadata.CompositeDataSourcePoolMetadataProvider;
import org.springframework.boot.jdbc.metadata.DataSourcePoolMetadata;
import org.springframework.boot.jdbc.metadata.DataSourcePoolMetadataProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jndi.JndiLocatorDelegate;
import org.springframework.orm.jpa.vendor.AbstractJpaVendorAdapter;
import org.springframework.transaction.jta.JtaTransactionManager;
import org.springframework.util.ClassUtils;

import javax.sql.DataSource;
import java.util.*;

/**
 * {@link JpaBaseConfiguration} implementation for MyJpa
 * @author qinzaizhen
 */
@Configuration
@ConditionalOnSingleCandidate(DataSource.class)
public class MyjpaConfiguration extends JpaBaseConfiguration implements BeanFactoryAware {

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

    private SqlSessionFactory sessionFactory;
//	private final List<HibernatePropertiesCustomizer> hibernatePropertiesCustomizers;

    private BeanFactory beanFactory;

    MyjpaConfiguration(DataSource dataSource, JpaProperties jpaProperties,
                       ObjectProvider<JtaTransactionManager> jtaTransactionManager,
                       ObjectProvider<TransactionManagerCustomizers> transactionManagerCustomizers,
                       ObjectProvider<Collection<DataSourcePoolMetadataProvider>> metadataProviders,
                       ObjectProvider<List<SchemaManagementProvider>> providers,
//                       ObjectProvider<PhysicalNamingStrategy> physicalNamingStrategy,
//                       ObjectProvider<ImplicitNamingStrategy> implicitNamingStrategy,
                       ObjectProvider<SqlSessionFactory> sessionFactories) {
        super(dataSource, jpaProperties, jtaTransactionManager,
                transactionManagerCustomizers);
		/*this.defaultDdlAutoProvider = new HibernateDefaultDdlAutoProvider(
				providers.getIfAvailable(Collections::emptyList));*/
        this.poolMetadataProvider = new CompositeDataSourcePoolMetadataProvider(
                metadataProviders.getIfAvailable());
        this.sessionFactory = sessionFactories.getIfAvailable();
	/*	this.hibernatePropertiesCustomizers = determineHibernatePropertiesCustomizers(
				physicalNamingStrategy.getIfAvailable(),
				implicitNamingStrategy.getIfAvailable(),
				hibernatePropertiesCustomizers.getIfAvailable(Collections::emptyList));*/
        BeanFactoryUtil.setBeanFactory(new cn.sexycode.util.core.factory.BeanFactory() {
            @Override
            public <T> T getBean(Class<T> clazz) {
                return beanFactory.getBean(clazz);
            }
        });
    }

    @Bean("standardServiceRegistry")
    @ConditionalOnMissingBean
    public StandardServiceRegistry serviceRegistry() {
        return new StandardServiceRegistry() {
            private Map<Class, Service> serviceMap = new HashMap<Class, Service>() {{
                put(ClassLoaderService.class, new ClassLoaderServiceImpl());
                put(DialectFactory.class, new DialectFactoryImpl(new StandardDialectResolver(),
                        new StrategySelectorImpl((ClassLoaderService) get(ClassLoaderService.class))));
            }};

            @Override
            public ServiceRegistry getParentServiceRegistry() {
                return null;
            }

            @Override
            public <R extends Service> R getService(Class<R> aClass) {
                return (R) serviceMap.get(aClass);
            }

            @Override
            public void close() {

            }
        };
    }

    @Bean
    @ConditionalOnMissingBean
    public QueryFactory queryFactory() {
        return new DefaultQueryFactory();
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
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        super.setBeanFactory(beanFactory);
        this.beanFactory = beanFactory;
    }

    @Override
    protected AbstractJpaVendorAdapter createJpaVendorAdapter() {
        return new MyjpaVendorAdapter(sessionFactory);
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
