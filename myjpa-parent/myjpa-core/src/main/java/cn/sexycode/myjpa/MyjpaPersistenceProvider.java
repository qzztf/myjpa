package cn.sexycode.myjpa;

import cn.sexycode.myjpa.session.SessionFactoryBuilderImpl;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSessionFactory;

import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceException;
import javax.persistence.spi.PersistenceProvider;
import javax.persistence.spi.PersistenceUnitInfo;
import javax.persistence.spi.ProviderUtil;
import java.io.InputStream;
import java.util.Collections;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * The Hibernate {@link PersistenceProvider} implementation
 *
 * @author qzz
 */
public class MyjpaPersistenceProvider implements PersistenceProvider {
    private static final Logger log = Logger.getLogger(MyjpaPersistenceProvider.class.getName());
    private SqlSessionFactory sessionFactory;

    private PersistenceUnitInfo persistenceUnitInfo;

    public MyjpaPersistenceProvider(SqlSessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    /**
     * {@inheritDoc}
     * <p/>
     * Note: per-spec, the values passed as {@code properties} override values found in {@code persistence.xml}
     * <p>
     * 先从mybatis-config.xml， 再尝试从 Configuration 对象创建 SqlSessionFactory
     */
    @Override
    public EntityManagerFactory createEntityManagerFactory(String persistenceUnitName, Map properties) {
        if (log.isLoggable(Level.FINEST)) {
            log.finest(String.format("Starting createEntityManagerFactory for persistenceUnitName %s", persistenceUnitName));
        }

        try {
            if (sessionFactory != null) {
                return new SessionFactoryBuilderImpl(this.persistenceUnitInfo, properties)
                        .sqlSessionFactory(sessionFactory).build((MyjpaConfiguration) getConfig(properties));
            }
            Properties prop = new Properties();
            prop.putAll(wrap(properties));
            InputStream configStream = Resources.getResourceAsStream(ClassLoader.getSystemClassLoader(), Consts.DEFAULT_CFG_FILE);
            if (configStream != null) {
                return new SessionFactoryBuilderImpl(this.persistenceUnitInfo, properties).build(configStream, prop);
            }
            return new SessionFactoryBuilderImpl(this.persistenceUnitInfo, properties)
                    .build(((MyjpaConfiguration) getConfig(properties)));
        } catch (PersistenceException pe) {
            throw pe;
        } catch (Exception e) {
            log.log(Level.WARNING, "Unable to build entity manager factory", e);
            throw new PersistenceException("Unable to build entity manager factory", e);
        }
    }

    private Configuration getConfig(Map properties) {

        return new MyjpaConfiguration();
    }


    protected static Map wrap(Map properties) {
        return properties == null ? Collections.emptyMap() : Collections.unmodifiableMap(properties);
    }

    /**
     * {@inheritDoc}
     * <p/>
     * Note: per-spec, the values passed as {@code properties} override values found in {@link PersistenceUnitInfo}
     */
    @Override
    public EntityManagerFactory createContainerEntityManagerFactory(PersistenceUnitInfo info, Map properties) {
        if (log.isLoggable(Level.FINEST)) {
            log.finest(String.format("Starting createContainerEntityManagerFactory : %s", info.getPersistenceUnitName()));
        }
        this.persistenceUnitInfo = info;
        return createEntityManagerFactory(info.getPersistenceUnitName(), properties);
    }

    @Override
    public void generateSchema(PersistenceUnitInfo persistenceUnitInfo, Map map) {

    }

    @Override
    public boolean generateSchema(String s, Map map) {
        return false;
    }

    @Override
    public ProviderUtil getProviderUtil() {
        return null;
    }


}
