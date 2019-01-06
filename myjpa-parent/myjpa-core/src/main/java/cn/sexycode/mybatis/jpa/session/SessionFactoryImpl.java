package cn.sexycode.mybatis.jpa.session;

import org.apache.ibatis.session.SqlSessionFactory;

import javax.naming.NamingException;
import javax.naming.Reference;
import javax.persistence.*;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.metamodel.Metamodel;
import java.util.Map;

/**
 *
 * @author qzz
 */
public class SessionFactoryImpl implements SessionFactory {

    private SqlSessionFactory sessionFactory;

    public SessionFactoryImpl(SqlSessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public Reference getReference() throws NamingException {
        return null;
    }

    @Override
    public EntityManager createEntityManager() {
        return new SessionImpl(sessionFactory.openSession());
    }

    @Override
    public EntityManager createEntityManager(Map map) {
        return createEntityManager();
    }

    @Override
    public EntityManager createEntityManager(SynchronizationType synchronizationType) {
        return null;
    }

    @Override
    public EntityManager createEntityManager(SynchronizationType synchronizationType, Map map) {
        return null;
    }

    @Override
    public CriteriaBuilder getCriteriaBuilder() {
        return null;
    }

    @Override
    public Metamodel getMetamodel() {
        return null;
    }

    @Override
    public void close() {
    }

    @Override
    public Map<String, Object> getProperties() {
        return null;
    }

    @Override
    public Cache getCache() {
        return null;
    }

    @Override
    public PersistenceUnitUtil getPersistenceUnitUtil() {
        return null;
    }

    @Override
    public void addNamedQuery(String name, Query query) {

    }

    @Override
    public <T> T unwrap(Class<T> cls) {
        return null;
    }

    @Override
    public <T> void addNamedEntityGraph(String graphName, EntityGraph<T> entityGraph) {

    }

    @Override
    public boolean isOpen() {
        return false;
    }
}
