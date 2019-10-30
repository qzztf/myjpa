package cn.sexycode.myjpa.session;

import cn.sexycode.myjpa.MyjpaConfiguration;
import cn.sexycode.mybatis.jpa.binding.*;
import cn.sexycode.myjpa.binding.*;
import cn.sexycode.myjpa.service.ServiceRegistry;
import cn.sexycode.myjpa.service.ServiceRegistryImpl;
import cn.sexycode.sql.dialect.function.SQLFunction;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import javax.persistence.spi.PersistenceUnitInfo;
import java.io.InputStream;
import java.util.*;

/**
 * @author qzz
 */
public class SessionFactoryBuilderImpl implements SessionFactoryBuilder {
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
            persistenceUnitInfo.getManagedClassNames()
                    .forEach(managedClassName -> metadataSources.addAnnotatedClassName(managedClassName));
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
                public Map<String, SQLFunction> getSqlFunctions() {
                    return sqlFunctionMap;
                }

                @Override
                public MappingDefaults getMappingDefaults() {
                    return new MappingDefaultsImpl(null);
                }

                @Override
                public ScanEnvironment getScanEnvironment() {
                    return new StandardScanEnvironmentImpl(persistenceUnitInfo);
                }

                @Override
                public ServiceRegistry getServiceRegistry() {
                    return new ServiceRegistryImpl();
                }
            };
            ManagedResources managedResources = MetadataBuildingProcess.prepare(metadataSources, options);
            this.metadata = MetadataBuildingProcess.complete(managedResources, options);
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
        return new SessionFactoryImpl(metadata, sqlSessionFactory);
    }

    public SessionFactory build(InputStream inputStream, Properties properties) {
        return new SessionFactoryImpl(metadata, sqlSessionFactoryBuilder.build(inputStream, properties));
    }
}
