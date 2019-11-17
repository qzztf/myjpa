package cn.sexycode.myjpa.session;

import cn.sexycode.myjpa.query.QueryFactory;
import cn.sexycode.myjpa.query.criteria.internal.CriteriaBuilderImpl;
import cn.sexycode.myjpa.transaction.MyjpaTransaction;
import cn.sexycode.myjpa.transaction.MyjpaTransactionImpl;
import cn.sexycode.util.core.factory.BeanFactoryUtil;
import org.apache.ibatis.mapping.Environment;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.transaction.Transaction;

import javax.persistence.*;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaDelete;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.CriteriaUpdate;
import javax.persistence.metamodel.Metamodel;
import java.sql.Connection;
import java.util.List;
import java.util.Map;

/**
 * @author qzz
 */
public class SessionImpl implements Session {
    private final SessionFactory sessionFactory;

    private final MyjpaTransaction myjpaTransaction;

    private SqlSession sqlSession;

    public SessionImpl(SqlSession sqlSession, SessionFactory sessionFactory, MyjpaTransaction myjpaTransaction) {
        this.sqlSession = sqlSession;
        this.sessionFactory = sessionFactory;
        this.myjpaTransaction = myjpaTransaction;
    }

    @Override
    public SqlSession getSession() {
        return sqlSession;
    }

    @Override
    public SessionFactory getSessionFactory() {
        return sessionFactory;
    }
    @Override
    public Configuration getConfiguration() {
        return sessionFactory.getConfiguration();
    }

    @Override
    public <T> T find(Class<T> entityClass, Object primaryKey, Map<String, Object> properties) {
        return null;
    }

    @Override
    public <T> T find(Class<T> entityClass, Object primaryKey, LockModeType lockMode) {
        return null;
    }

    @Override
    public <T> T find(Class<T> entityClass, Object primaryKey, LockModeType lockMode, Map<String, Object> properties) {
        return null;
    }

    @Override
    public <T> T getReference(Class<T> entityClass, Object primaryKey) {
        return null;
    }


    @Override
    public void lock(Object entity, LockModeType lockMode) {

    }

    @Override
    public void lock(Object entity, LockModeType lockMode, Map<String, Object> properties) {

    }

    @Override
    public void refresh(Object entity, Map<String, Object> properties) {

    }

    @Override
    public void refresh(Object entity, LockModeType lockMode) {

    }

    @Override
    public void refresh(Object entity, LockModeType lockMode, Map<String, Object> properties) {

    }

    @Override
    public void detach(Object entity) {

    }


    @Override
    public boolean contains(Object entity) {
        return false;
    }

    @Override
    public LockModeType getLockMode(Object entity) {
        return null;
    }

    @Override
    public void setProperty(String propertyName, Object value) {

    }

    @Override
    public Map<String, Object> getProperties() {
        return null;
    }

    @Override
    public Query createQuery(String qlString) {
        return BeanFactoryUtil.getBeanFactory().getBean(QueryFactory.class).createQuery(this, qlString);
    }

    @Override
    public <T> TypedQuery<T> createQuery(CriteriaQuery<T> criteriaQuery) {
        return BeanFactoryUtil.getBeanFactory().getBean(QueryFactory.class).createQuery(this, criteriaQuery);
    }

    @Override
    public Query createQuery(CriteriaUpdate updateQuery) {
        return null;
    }

    @Override
    public Query createQuery(CriteriaDelete deleteQuery) {
        return null;
    }

    @Override
    public <T> TypedQuery<T> createQuery(String qlString, Class<T> resultClass) {
        return BeanFactoryUtil.getBeanFactory().getBean(QueryFactory.class).createQuery(this, qlString, resultClass);
    }

    @Override
    public Query createNamedQuery(String name) {
        return BeanFactoryUtil.getBeanFactory().getBean(QueryFactory.class).createNamedQuery(this, name);
    }

    @Override
    public <T> TypedQuery<T> createNamedQuery(String name, Class<T> resultClass) {
        return BeanFactoryUtil.getBeanFactory().getBean(QueryFactory.class).createNamedQuery(this, name, resultClass);
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
    public StoredProcedureQuery createNamedStoredProcedureQuery(String name) {
        return null;
    }

    @Override
    public StoredProcedureQuery createStoredProcedureQuery(String procedureName) {
        return null;
    }

    @Override
    public StoredProcedureQuery createStoredProcedureQuery(String procedureName, Class... resultClasses) {
        return null;
    }

    @Override
    public StoredProcedureQuery createStoredProcedureQuery(String procedureName, String... resultSetMappings) {
        return null;
    }

    @Override
    public void joinTransaction() {

    }

    @Override
    public boolean isJoinedToTransaction() {
        return false;
    }

    @Override
    public <T> T unwrap(Class<T> cls) {
        if (cls.isAssignableFrom(this.getClass())) {
            return ((T) this);
        }
        throw new PersistenceException("cannot unwrap");
    }

    @Override
    public Object getDelegate() {
        return this;
    }


    @Override
    public EntityTransaction getTransaction() {
        return  myjpaTransaction;
    }

    @Override
    public EntityManagerFactory getEntityManagerFactory() {
        return sessionFactory;
    }

    @Override
    public CriteriaBuilder getCriteriaBuilder() {
        return new CriteriaBuilderImpl(this.sessionFactory);
    }

    @Override
    public Metamodel getMetamodel() {
        return sessionFactory.getMetamodel();
    }

    @Override
    public <T> EntityGraph<T> createEntityGraph(Class<T> rootType) {
        return null;
    }

    @Override
    public EntityGraph<?> createEntityGraph(String graphName) {
        return null;
    }

    @Override
    public EntityGraph<?> getEntityGraph(String graphName) {
        return null;
    }

    @Override
    public <T> List<EntityGraph<? super T>> getEntityGraphs(Class<T> entityClass) {
        return null;
    }
}
