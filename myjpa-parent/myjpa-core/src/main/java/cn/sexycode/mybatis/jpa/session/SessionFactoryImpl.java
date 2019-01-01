package cn.sexycode.mybatis.jpa.session;

import org.apache.ibatis.session.SqlSessionFactory;

import javax.naming.NamingException;
import javax.naming.Reference;
import javax.persistence.EntityManager;
import java.util.Map;

/**
 *
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
    public void close() {
    }

    @Override
    public boolean isOpen() {
        return false;
    }
}
