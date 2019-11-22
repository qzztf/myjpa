package cn.sexycode.myjpa.orm.vendor;

import cn.sexycode.myjpa.AvailableSettings;
import cn.sexycode.myjpa.Configuration;
import cn.sexycode.myjpa.MyjpaPersistenceProvider;
import cn.sexycode.myjpa.session.Session;
import cn.sexycode.myjpa.session.SessionFactory;
import cn.sexycode.myjpa.sql.dialect.MySQLDialect;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.orm.jpa.vendor.AbstractJpaVendorAdapter;
import org.springframework.orm.jpa.vendor.Database;
import org.springframework.orm.jpa.vendor.HibernateJpaDialect;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.spi.PersistenceProvider;
import javax.persistence.spi.PersistenceUnitInfo;
import java.util.HashMap;
import java.util.Map;

/**
 * {@link org.springframework.orm.jpa.JpaVendorAdapter} implementation for Hibernate
 * EntityManager. Developed and tested against Hibernate 5.0, 5.1 and 5.2;
 * backwards-compatible with Hibernate 4.3 at runtime on a best-effort basis.
 *
 * <p>Exposes Hibernate's persistence provider and EntityManager extension interface,
 * and adapts {@link AbstractJpaVendorAdapter}'s common configuration settings.
 * Also supports the detection of annotated packages (through
 * {@link org.springframework.orm.jpa.persistenceunit.SmartPersistenceUnitInfo#getManagedPackages()}),
 * e.g. containing Hibernate {@link org.hibernate.annotations.FilterDef} annotations,
 * along with Spring-driven entity scanning which requires no {@code persistence.xml}
 * ({@link org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean#setPackagesToScan}).
 *
 * <p><b>A note about {@code HibernateJpaVendorAdapter} vs native Hibernate settings:</b>
 * Some settings on this adapter may conflict with native Hibernate configuration rules
 * or custom Hibernate properties. For example, specify either {@link #setDatabase} or
 * Hibernate's "hibernate.dialect_resolvers" property, not both. Also, be careful about
 * Hibernate's connection release mode: This adapter prefers {@code ON_CLOSE} behavior,
 * aligned with {@link HibernateJpaDialect#setPrepareConnection}, at least for non-JTA
 * scenarios; you may override this through corresponding native Hibernate properties.
 *
 * @author Juergen Hoeller
 * @author Rod Johnson
 * @see HibernateJpaDialect
 * @since 2.0
 */
public class MyjpaVendorAdapter extends AbstractJpaVendorAdapter {

    private final MyjpaDialect jpaDialect = new MyjpaDialect();

    private SqlSessionFactory sessionFactory;

    private final PersistenceProvider persistenceProvider;

    public MyjpaVendorAdapter() {
        persistenceProvider = null;
    }

    public MyjpaVendorAdapter(SqlSessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
        persistenceProvider = new MyjpaPersistenceProvider(sessionFactory);
    }

    @Override
    public PersistenceProvider getPersistenceProvider() {
        return this.persistenceProvider;
    }

    @Override
    public String getPersistenceProviderRootPackage() {
        return "cn.sexycode.myjpa";
    }

    @Override
    public Map<String, Object> getJpaPropertyMap(PersistenceUnitInfo pui) {
        return getJpaPropertyMap();
    }

    @Override
    public Map<String, Object> getJpaPropertyMap() {
        Map<String, Object> jpaProperties = new HashMap<>(5);
        if (getDatabasePlatform() != null) {
            jpaProperties.put(Configuration.DIALECT, getDatabasePlatform());
        } else {
            Class<?> databaseDialectClass = determineDatabaseDialectClass(getDatabase());
            if (databaseDialectClass != null) {
                jpaProperties.put(AvailableSettings.DIALECT, databaseDialectClass.getName());
            }
        }
        if (isShowSql()) {
            jpaProperties.put(AvailableSettings.SHOW_SQL, "true");
        }
        return jpaProperties;
    }

    /**
     * Determine the Hibernate database dialect class for the given target database.
     *
     * @param database the target database
     * @return the Hibernate database dialect class, or {@code null} if none found
     */

    protected Class<?> determineDatabaseDialectClass(Database database) {
        switch (database) {
            //            case DB2: return DB2Dialect.class;
            //            case DERBY: return DerbyTenSevenDialect.class;
            //            case H2: return H2Dialect.class;
            //            case HANA: return HANAColumnStoreDialect.class;
            //            case HSQL: return HSQLDialect.class;
            //            case INFORMIX: return InformixDialect.class;
            case MYSQL:
                return MySQLDialect.class;
            //            case ORACLE: return Oracle12cDialect.class;
            //            case POSTGRESQL: return PostgreSQL95Dialect.class;
            //            case SQL_SERVER: return SQLServer2012Dialect.class;
            //            case SYBASE: return SybaseDialect.class;
            default:
                return null;
        }
    }

    @Override
    public MyjpaDialect getJpaDialect() {
        return this.jpaDialect;
    }

    @Override
    public Class<? extends EntityManagerFactory> getEntityManagerFactoryInterface() {
        return SessionFactory.class;
    }

    @Override
    public Class<? extends EntityManager> getEntityManagerInterface() {
        return Session.class;
    }

}
