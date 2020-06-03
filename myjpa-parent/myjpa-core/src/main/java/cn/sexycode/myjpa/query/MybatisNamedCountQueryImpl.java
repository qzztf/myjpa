package cn.sexycode.myjpa.query;

import cn.sexycode.myjpa.mybatis.NoSuchMapperMethodException;
import cn.sexycode.myjpa.session.Session;
import cn.sexycode.util.core.object.ReflectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.*;
import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Stream;

/**
 * @author Steve Ebersole
 */
public class MybatisNamedCountQueryImpl<R> extends MybatisNamedQueryImpl<R> {

    private static final Logger LOGGER = LoggerFactory.getLogger(MybatisNamedCountQueryImpl.class);



    public MybatisNamedCountQueryImpl(Session session, String name) {
        this(session, name, null);
    }

    public MybatisNamedCountQueryImpl(Session session, String name, Class<R> resultClass) {
        super(session, name, resultClass);
    }


    @Override
    public List<R> getResultList() {
        return (List<R>) invokeSessionMethod();
    }

    @Override
    protected Object invokeSessionMethod() {
        Method method = ReflectionUtils.findMethod(session.getClass(), "selectList", new Class[]{String.class, Object.class});
        return ReflectionUtils.invokeMethod(method,
                session, new Object[]{name, parameterValues});
    }

    @Override
    public R getSingleResult() {
        try {
            return (R) invokeMapper();
        } catch (NoSuchMethodException e) {
            return (R) invokeSessionMethod();
        }
    }

    @Override
    public int executeUpdate() {
        return 0;
    }

    @Override
    public TypedQuery<R> setMaxResults(int maxResult) {
        return null;
    }

    @Override
    public int getMaxResults() {
        return 0;
    }

    @Override
    public TypedQuery<R> setFirstResult(int startPosition) {
        return null;
    }

    @Override
    public int getFirstResult() {
        return 0;
    }

    @Override
    public TypedQuery<R> setHint(String hintName, Object value) {
        return null;
    }

    @Override
    public Map<String, Object> getHints() {
        return null;
    }

    @Override
    public <T> TypedQuery<R> setParameter(Parameter<T> param, T value) {
        return this;
    }

    @Override
    public TypedQuery<R> setParameter(Parameter<Calendar> param, Calendar value, TemporalType temporalType) {
        return this;
    }

    @Override
    public TypedQuery<R> setParameter(Parameter<Date> param, Date value, TemporalType temporalType) {
        return this;
    }

    @Override
    public TypedQuery<R> setParameter(String name, Object value) {
        return this;
    }

    @Override
    public TypedQuery<R> setParameter(String name, Calendar value, TemporalType temporalType) {
        return this;
    }

    @Override
    public TypedQuery<R> setParameter(String name, Date value, TemporalType temporalType) {
        return this;
    }

    @Override
    public TypedQuery<R> setParameter(int position, Object value) {
        return this;
    }

    @Override
    public TypedQuery<R> setParameter(int position, Calendar value, TemporalType temporalType) {
        return null;
    }

    @Override
    public TypedQuery<R> setParameter(int position, Date value, TemporalType temporalType) {
        return null;
    }

    @Override
    public Set<Parameter<?>> getParameters() {
        return new HashSet<>(parameters);
    }

    @Override
    public Parameter<?> getParameter(String name) {
        return null;
    }

    @Override
    public <T> Parameter<T> getParameter(String name, Class<T> type) {
        return null;
    }

    @Override
    public Parameter<?> getParameter(int position) {
        return null;
    }

    @Override
    public <T> Parameter<T> getParameter(int position, Class<T> type) {
        return null;
    }

    @Override
    public boolean isBound(Parameter<?> param) {
        return false;
    }

    @Override
    public <T> T getParameterValue(Parameter<T> param) {
        return null;
    }

    @Override
    public Object getParameterValue(String name) {
        return null;
    }

    @Override
    public Object getParameterValue(int position) {
        return null;
    }

    @Override
    public TypedQuery<R> setFlushMode(FlushModeType flushMode) {
        return null;
    }

    @Override
    public FlushModeType getFlushMode() {
        return null;
    }

    @Override
    public TypedQuery<R> setLockMode(LockModeType lockMode) {
        return null;
    }

    @Override
    public LockModeType getLockMode() {
        return null;
    }

    @Override
    public <T> T unwrap(Class<T> cls) {
        return (T) this;
    }
}
