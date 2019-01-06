/*
 * Hibernate, Relational Persistence for Idiomatic Java
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later.
 * See the lgpl.txt file in the root directory or <http://www.gnu.org/licenses/lgpl-2.1.html>.
 */
package cn.sexycode.mybatis.jpa;

import cn.sexycode.mybatis.jpa.session.SessionFactoryImpl;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceException;
import javax.persistence.spi.PersistenceProvider;
import javax.persistence.spi.PersistenceUnitInfo;
import javax.persistence.spi.ProviderUtil;
import java.util.Collections;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * The Hibernate {@link PersistenceProvider} implementation
 *
 * @author qzz
 */
public class MybatisPersistenceProvider implements PersistenceProvider {
    private static final Logger log = Logger.getLogger(MybatisPersistenceProvider.class.getName());

    /**
     * {@inheritDoc}
     * <p/>
     * Note: per-spec, the values passed as {@code properties} override values found in {@code persistence.xml}
     */
    @Override
    public EntityManagerFactory createEntityManagerFactory(String persistenceUnitName, Map properties) {
        if (log.isLoggable(Level.FINEST)) {
            log.finest(String.format("Starting createEntityManagerFactory for persistenceUnitName %s", persistenceUnitName));
        }

        try {
            return new SessionFactoryImpl(new SqlSessionFactoryBuilder().build(Resources.getResourceAsStream(Consts.DEFAULT_CFG_FILE)));
        } catch (PersistenceException pe) {
            throw pe;
        } catch (Exception e) {
            log.log(Level.WARNING, "Unable to build entity manager factory", e);
            throw new PersistenceException("Unable to build entity manager factory", e);
        }
    }

    private Configuration getConfig(Map properties) {

        return new Configuration();
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
