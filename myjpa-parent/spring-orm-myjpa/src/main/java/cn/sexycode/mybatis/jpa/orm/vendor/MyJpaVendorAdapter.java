package cn.sexycode.mybatis.jpa.orm.vendor;

import cn.sexycode.mybatis.jpa.MybatisPersistenceProvider;
import cn.sexycode.mybatis.jpa.session.Session;
import cn.sexycode.mybatis.jpa.session.SessionFactory;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.orm.jpa.vendor.AbstractJpaVendorAdapter;
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
public class MyJpaVendorAdapter extends AbstractJpaVendorAdapter {

    private final MyJpaDialect jpaDialect = new MyJpaDialect();

    private SqlSessionFactory sessionFactory;

    private final PersistenceProvider persistenceProvider;

    public MyJpaVendorAdapter() {
        persistenceProvider = null;
    }

    public MyJpaVendorAdapter(SqlSessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
        persistenceProvider = new MybatisPersistenceProvider(sessionFactory);
    }

    @Override
    public PersistenceProvider getPersistenceProvider() {
        return this.persistenceProvider;
    }

    @Override
    public String getPersistenceProviderRootPackage() {
        return "cn.sexycode.mybatis.jpa";
    }

    @Override
    public Map<String, Object> getJpaPropertyMap(PersistenceUnitInfo pui) {
        Map<String, Object> jpaProperties = new HashMap<>();
        return jpaProperties;
    }

    @Override
    public Map<String, Object> getJpaPropertyMap() {
        Map<String, Object> jpaProperties = new HashMap<>();
        return jpaProperties;
    }


    @Override
    public MyJpaDialect getJpaDialect() {
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
