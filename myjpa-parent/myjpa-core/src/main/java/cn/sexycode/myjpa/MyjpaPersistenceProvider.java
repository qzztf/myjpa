package cn.sexycode.myjpa;

import cn.sexycode.myjpa.boot.ParsedPersistenceXmlDescriptor;
import cn.sexycode.myjpa.boot.PersistenceXmlParser;
import cn.sexycode.myjpa.boot.ProviderChecker;
import cn.sexycode.myjpa.mybatis.MyjpaConfiguration;
import cn.sexycode.myjpa.session.SessionFactoryBuilderImpl;
import cn.sexycode.util.core.factory.BeanFactoryUtil;
import cn.sexycode.util.core.object.ObjectUtils;
import cn.sexycode.util.core.str.StringUtils;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceException;
import javax.persistence.spi.PersistenceProvider;
import javax.persistence.spi.PersistenceUnitInfo;
import javax.persistence.spi.ProviderUtil;
import java.io.InputStream;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * The myjpa {@link PersistenceProvider} implementation
 *
 * @author qzz
 */
public class MyjpaPersistenceProvider implements PersistenceProvider {
    private static final Logger LOGGER = LoggerFactory.getLogger(MyjpaPersistenceProvider.class);

    private SqlSessionFactory sessionFactory;

    private PersistenceUnitInfo persistenceUnitInfo;

    public MyjpaPersistenceProvider() {
    }

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
        LOGGER.debug(
                String.format("Starting createEntityManagerFactory for persistenceUnitName %s", persistenceUnitName));
        Properties prop = new Properties();
        prop.putAll(wrap(properties));
        if (StringUtils.startsWithIgnoreCase(BeanFactoryUtil.getBeanFactory().getClass().getName(),BeanFactoryUtil.class.getName())){
            BeanFactoryUtil.setBeanFactory(new DefaultBeanFactory());
        }
        try {
            if (ObjectUtils.isEmpty(persistenceUnitInfo)) {
                final List<ParsedPersistenceXmlDescriptor> units;
                try {
                    units = PersistenceXmlParser.locatePersistenceUnits(prop);
                } catch (Exception e) {
                    LOGGER.warn("Unable to locate persistence units", e);
                    throw new PersistenceException("Unable to locate persistence units", e);
                }

                LOGGER.debug("Located and parsed {} persistence units; checking each", units.size());

                if (persistenceUnitName == null && units.size() > 1) {
                    // no persistence-unit name to look for was given and we found multiple persistence-units
                    throw new PersistenceException("No name provided and multiple persistence units found");
                }

                for (ParsedPersistenceXmlDescriptor persistenceUnit : units) {
                    LOGGER.debug(
                            "Checking persistence-unit [name={}, explicit-provider={}] against incoming persistence unit name [{}]",
                            persistenceUnit.getName(), persistenceUnit.getProviderClassName(), persistenceUnitName);

                    final boolean matches =
                            persistenceUnitName == null || persistenceUnit.getName().equals(persistenceUnitName);
                    if (!matches) {
                        LOGGER.debug("Excluding from consideration due to name mis-match");
                        continue;
                    }

                    // See if we (Hibernate) are the persistence provider
                    if (!ProviderChecker.isProvider(persistenceUnit, properties)) {
                        LOGGER.debug("Excluding from consideration due to provider mis-match");
                        continue;
                    }

                    persistenceUnitInfo = persistenceUnit.toPersistenceUnitInfo();
                    prop.putAll(persistenceUnitInfo.getProperties());
                }
            }
            if (sessionFactory != null) {
                return new SessionFactoryBuilderImpl(this.persistenceUnitInfo, prop)
                        .sqlSessionFactory(sessionFactory).build((MyjpaConfiguration) getConfig(sessionFactory.getConfiguration(), prop));
            }

            InputStream configStream = Resources
                    .getResourceAsStream(ClassLoader.getSystemClassLoader(), Consts.DEFAULT_CFG_FILE);
            if (configStream != null) {
                return new SessionFactoryBuilderImpl(this.persistenceUnitInfo, prop).build(configStream, prop);
            }
            return new SessionFactoryBuilderImpl(this.persistenceUnitInfo, prop)
                    .build(((MyjpaConfiguration) getConfig(null, prop)));
        } catch (PersistenceException pe) {
            throw pe;
        } catch (Exception e) {
            LOGGER.warn("Unable to build entity manager factory", e);
            throw new PersistenceException("Unable to build entity manager factory", e);
        }
    }

    private Configuration getConfig(Configuration configuration, Map properties) {
        return new MyjpaConfiguration(configuration, properties);
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
        LOGGER.info(String.format("Starting createContainerEntityManagerFactory : %s", info.getPersistenceUnitName()));
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
