package cn.sexycode.myjpa.session;

import cn.sexycode.myjpa.binding.ModelProxy;
import org.apache.ibatis.session.SqlSession;

import javax.persistence.EntityManager;
import javax.persistence.FlushModeType;
import java.io.Closeable;
import java.util.logging.Logger;

/**
 * @author qzz
 */
public interface Session extends EntityManager, Closeable {
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
        return (T) new SessionAdaptor(this).execute("find", primaryKey);
    }
}
