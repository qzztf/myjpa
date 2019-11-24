package cn.sexycode.myjpa.session;

import cn.sexycode.myjpa.binding.*;
import cn.sexycode.myjpa.boot.BootstrapContextImpl;
import cn.sexycode.myjpa.boot.ImplicitNamingStrategy;
import cn.sexycode.myjpa.boot.ImplicitNamingStrategyJpaCompliantImpl;
import cn.sexycode.myjpa.boot.MetadataBuildingOptions;
import cn.sexycode.myjpa.mybatis.MyjpaConfiguration;
import cn.sexycode.myjpa.mybatis.SqlSessionFactoryBuilder;
import cn.sexycode.myjpa.sql.dialect.function.SQLFunction;
import cn.sexycode.myjpa.sql.model.PhysicalNamingStrategy;
import cn.sexycode.util.core.factory.BeanFactoryUtil;
import cn.sexycode.util.core.factory.selector.StrategySelector;
import cn.sexycode.util.core.service.Service;
import cn.sexycode.util.core.service.ServiceRegistry;
import cn.sexycode.util.core.service.StandardServiceRegistry;
import org.apache.ibatis.session.SqlSessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.spi.PersistenceUnitInfo;
import java.io.InputStream;
import java.util.*;
import java.util.concurrent.Callable;

/**
 * @author qzz
 */
public class SessionFactoryBuilderImpl implements SessionFactoryBuilder {
    private static final Logger LOGGER = LoggerFactory.getLogger(SessionFactoryBuilderImpl.class);

    private final PersistenceUnitInfo persistenceUnitInfo;

    private Map properties;

    private Metadata metadata;

    private SqlSessionFactoryBuilder sqlSessionFactoryBuilder = new SqlSessionFactoryBuilder();

    public SessionFactoryBuilderImpl(PersistenceUnitInfo info, Map properties) {
        this.persistenceUnitInfo = info;
        this.properties = properties;

        this.metadata = metadata();
    }

    private Metadata metadata() {
        if (this.metadata == null) {
            final MetadataSources metadataSources = new MetadataSources();
            persistenceUnitInfo.getManagedClassNames().forEach(metadataSources::addAnnotatedClassName);
            MetadataBuildingOptions options = new MetadataBuildingOptions() {
                private ArrayList<BasicTypeRegistration> basicTypeRegistrations = new ArrayList<>();

                private HashMap<String, SQLFunction> sqlFunctionMap = new HashMap<>(1);

                @Override
                public List<BasicTypeRegistration> getBasicTypeRegistrations() {
                    return basicTypeRegistrations;
                }

                @Override
                public Object getScanner() {
                    return null;
                }

                @Override
                public ImplicitNamingStrategy getImplicitNamingStrategy() {
                    final StrategySelector strategySelector = getServiceRegistry().getService(StrategySelector.class);
                    return strategySelector.resolveDefaultableStrategy(ImplicitNamingStrategy.class,
                            //                            configService.getSettings().get( AvailableSettings.IMPLICIT_NAMING_STRATEGY ),
                            "cn.sexycode.myjpa.boot.ImplicitNamingStrategyJpaCompliantImpl",
                            new Callable<ImplicitNamingStrategy>() {
                                @Override
                                public ImplicitNamingStrategy call() {
                                    return strategySelector
                                            .resolveDefaultableStrategy(ImplicitNamingStrategy.class, "default",
                                                    ImplicitNamingStrategyJpaCompliantImpl.INSTANCE);
                                }
                            });
                }

                @Override
                public PhysicalNamingStrategy getPhysicalNamingStrategy() {
                    return null;
                }

                /*@Override
                public Map<String, SQLFunction> getSqlFunctions() {
                    return sqlFunctionMap;
                }*/

                @Override
                public MappingDefaults getMappingDefaults() {
                    return new MappingDefaultsImpl(null);
                }
 /*
                @Override
                public ScanEnvironment getScanEnvironment() {
                    return new StandardScanEnvironmentImpl(persistenceUnitInfo);
                }
                */

                @Override
                public StandardServiceRegistry getServiceRegistry() {
                    return BeanFactoryUtil.getBeanFactory()
                            .getBean(StandardServiceRegistry.class);
                }
/*
                @Override
                public JdbcEnvironment getJdbcEnvironment() {
                    final DialectFactory dialectFactory = getServiceRegistry().getService(DialectFactory.class);

                    // if we get here, either we were asked to not use JDBC metadata or accessing the JDBC metadata failed.
                    return new JdbcEnvironmentImpl(getServiceRegistry(), dialectFactory
                            .buildDialect(PropertiesUtil.getString(AvailableSettings.DIALECT, properties, "cn.sexycode.myjpa.sql.dialect.H2Dialect"),
                                    null));
                }*/
            };
            BootstrapContextImpl bootstrapContext = new BootstrapContextImpl(new StandardServiceRegistry() {
                @Override
                public ServiceRegistry getParentServiceRegistry() {
                    return options.getServiceRegistry();
                }

                @Override
                public <R extends Service> R getService(Class<R> aClass) {
                    return options.getServiceRegistry().getService(aClass);
                }

                @Override
                public void close() {

                }
            }, options);
            bootstrapContext.injectScanEnvironment(new StandardScanEnvironmentImpl(persistenceUnitInfo));
            ManagedResources managedResources = MetadataBuildingProcess.prepare(metadataSources, bootstrapContext);
            this.metadata = MetadataBuildingProcess.complete(managedResources, bootstrapContext, options);
        }
        return metadata;
    }

    private SqlSessionFactory sqlSessionFactory;

    public SessionFactoryBuilder sqlSessionFactory(SqlSessionFactory sqlSessionFactory) {
        this.sqlSessionFactory = sqlSessionFactory;
        return this;
    }

    @Override
    public SessionFactory build(MyjpaConfiguration config) {
        if (Objects.isNull(sqlSessionFactory)) {
            this.sqlSessionFactory = sqlSessionFactoryBuilder.build(config);
        }
    return new SessionFactoryImpl(metadata, sqlSessionFactory, config);
    }

    public SessionFactory build(InputStream inputStream, Properties properties) {
        SqlSessionFactory sqlSessionFactory = sqlSessionFactoryBuilder.build(inputStream, properties);
        return new SessionFactoryImpl(metadata, sqlSessionFactory);
    }
}
