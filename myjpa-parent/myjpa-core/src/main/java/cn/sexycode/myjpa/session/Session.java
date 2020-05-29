package cn.sexycode.myjpa.session;

import cn.sexycode.myjpa.AvailableSettings;
import cn.sexycode.myjpa.binding.ModelProxy;
import cn.sexycode.util.core.str.StringUtils;
import org.apache.ibatis.cursor.Cursor;
import org.apache.ibatis.executor.BatchResult;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.apache.ibatis.session.SqlSession;

import javax.persistence.EntityManager;
import javax.persistence.FlushModeType;
import java.io.Closeable;
import java.sql.Connection;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

/**
 * @author qzz
 */
public interface Session extends EntityManager,SqlSession, Closeable {
    Logger LOGGER = Logger.getLogger(Session.class.getCanonicalName());

    /**
     * get mybatis sqlSession
     *
     * @return SqlSession
     * @see SqlSession
     */
    SqlSession getSession();

    @Override
    default void flush() {
        LOGGER.finer("flush method not support.");
    }

    @Override
    default void setFlushMode(FlushModeType flushMode) {
    }


    @Override
    default FlushModeType getFlushMode() {
        return null;
    }

    @Override
    default void refresh(Object entity) {
        LOGGER.finer("refresh method not support.");
    }

    @Override
    default void clear() {
        getSession().clearCache();
    }

    @Override
    default void close() {
        getSession().close();
    }

    @Override
    default boolean isOpen() {
        return false;
    }


    @Override
    default void persist(Object entity) {
        new SessionAdaptor(this).execute("persist", entity);
    }

    @Override
    default <T> T merge(T entity) {
        new SessionAdaptor(this).execute("merge", entity);
        return entity;
    }

    /**
     * @param entity
     */
    @Override
    default void remove(Object entity) {
        new SessionAdaptor(this).execute("remove", entity);
    }


    /**
     * @param entityClass
     * @param primaryKey
     * @param <T>
     * @return
     */
    @Override
    default <T> T find(Class<T> entityClass, Object primaryKey) {
        ModelProxy findModelProxy;
        if (primaryKey instanceof ModelProxy){
            findModelProxy = (ModelProxy) primaryKey;
        }else {
            findModelProxy = new ModelProxy(primaryKey, StringUtils.join(".", new String[] { entityClass.getCanonicalName(), getSessionFactory().getProperties()
                    .getOrDefault(AvailableSettings.MybatisMapperMethodMapping.FIND,
                            AvailableSettings.MybatisMapperMethodMapping.Mapping.FIND).toString() }));
        }
        return (T) new SessionAdaptor(this).execute("find", findModelProxy);
    }

    /**
     * 返回 SessionFactory
     * @return SessionFactory
     */
    SessionFactory getSessionFactory();

    /**
     * 返回mybatis Configuration
     * @return Configuration mybatis
     */
    @Override
    Configuration getConfiguration();

    @Override
    default <T> T selectOne(String statement) {
        return getSession().selectOne(statement);
    }

    @Override
    default <T> T selectOne(String statement, Object parameter) {
        return getSession().selectOne(statement, parameter);
    }

    @Override
    default <E> List<E> selectList(String statement) {
        return getSession().selectList(statement);
    }

    @Override
    default <E> List<E> selectList(String statement, Object parameter) {
        return getSession().selectList(statement, parameter);
    }

    @Override
    default <E> List<E> selectList(String statement, Object parameter, RowBounds rowBounds) {
        return getSession().selectList(statement,parameter,rowBounds);
    }

    @Override
    default <K, V> Map<K, V> selectMap(String statement, String mapKey) {
        return getSession().selectMap(statement, mapKey);
    }

    @Override
    default <K, V> Map<K, V> selectMap(String statement, Object parameter, String mapKey) {
        return getSession().selectMap(statement, parameter,mapKey);
    }

    @Override
    default <K, V> Map<K, V> selectMap(String statement, Object parameter, String mapKey, RowBounds rowBounds) {
        return getSession().selectMap(statement, parameter, mapKey, rowBounds);
    }

    @Override
    default <T> Cursor<T> selectCursor(String statement) {
        return getSession().selectCursor(statement);
    }

    @Override
    default <T> Cursor<T> selectCursor(String statement, Object parameter) {
        return getSession().selectCursor(statement);
    }

    @Override
    default <T> Cursor<T> selectCursor(String statement, Object parameter, RowBounds rowBounds) {
        return getSession().selectCursor(statement, parameter, rowBounds);
    }

    @Override
    default void select(String statement, Object parameter, ResultHandler handler) {
        getSession().select(statement, parameter, handler);
    }

    @Override
    default void select(String statement, ResultHandler handler) {
        getSession().select(statement, handler);
    }

    @Override
    default void select(String statement, Object parameter, RowBounds rowBounds, ResultHandler handler) {
        getSession().select(statement, parameter, rowBounds, handler);
    }

    @Override
    default int insert(String statement) {
        return getSession().insert(statement);
    }

    @Override
    default int insert(String statement, Object parameter) {
        return getSession().insert(statement, parameter);
    }

    @Override
    default int update(String statement) {
        return getSession().update(statement);
    }

    @Override
    default int update(String statement, Object parameter) {
        return getSession().update(statement, parameter);
    }

    @Override
    default int delete(String statement) {
        return getSession().delete(statement);
    }

    @Override
    default int delete(String statement, Object parameter) {
        return getSession().delete(statement, parameter);
    }

    @Override
    default void commit() {
        getSession().commit();
    }

    @Override
    default void commit(boolean force) {
        getSession().commit(force);
    }

    @Override
    default void rollback() {
        getSession().rollback();
    }

    @Override
    default void rollback(boolean force) {
        getSession().rollback(force);
    }

    @Override
    default List<BatchResult> flushStatements() {
        return getSession().flushStatements();
    }

    @Override
    default void clearCache() {
        getSession().clearCache();
    }

    @Override
    default <T> T getMapper(Class<T> type) {
        return getSession().getMapper(type);
    }

    /**
     * @return Connection
     */
    @Override
    default Connection getConnection() {
        return getSession().getConnection();
    }
}
