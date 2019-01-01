package cn.sexycode.mybatis.jpa.session;

import org.apache.ibatis.session.SqlSession;

import javax.persistence.EntityTransaction;
import javax.persistence.LockModeType;
import javax.persistence.Query;

/**
 * @author qzz
 */
public class SessionImpl implements Session {
    private SqlSession sqlSession;

    public SessionImpl(SqlSession sqlSession) {
        this.sqlSession = sqlSession;
    }

    @Override
    public SqlSession getSession() {
        return sqlSession;
    }




    @Override
    public <T> T getReference(Class<T> entityClass, Object primaryKey) {
        return null;
    }


    @Override
    public void lock(Object entity, LockModeType lockMode) {

    }


    @Override
    public boolean contains(Object entity) {
        return false;
    }

    @Override
    public Query createQuery(String qlString) {
        return null;
    }

    @Override
    public Query createNamedQuery(String name) {
        return null;
    }

    @Override
    public Query createNativeQuery(String sqlString) {
        return null;
    }

    @Override
    public Query createNativeQuery(String sqlString, Class resultClass) {
        return null;
    }

    @Override
    public Query createNativeQuery(String sqlString, String resultSetMapping) {
        return null;
    }

    @Override
    public void joinTransaction() {

    }

    @Override
    public Object getDelegate() {
        return null;
    }


    @Override
    public EntityTransaction getTransaction() {
        return null;
    }
}
