package cn.sexycode.myjpa.query;

import cn.sexycode.myjpa.binding.MyjpaParameterImpl;
import cn.sexycode.myjpa.mybatis.NoSuchMapperMethodException;
import cn.sexycode.myjpa.mybatis.PagePlugin;
import cn.sexycode.myjpa.session.Session;
import cn.sexycode.util.core.object.ReflectionUtils;
import cn.sexycode.util.core.str.StringUtils;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.Interceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.*;
import javax.persistence.metamodel.Metamodel;
import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Stream;

/**
 * @author Steve Ebersole
 */
public class MybatisNamedQueryImpl<R> extends AbstractMybatisQuery<R> {

    private static final Logger LOGGER = LoggerFactory.getLogger(MybatisNamedQueryImpl.class);

    protected final String name;

    protected Session session;

    protected Class<R> resultClass;

    protected MappedStatement mappedStatement;

    protected List<Parameter<?>> parameters = new LinkedList<>();

    protected String methodName;

    protected Object[] parameterValues;

    protected Class mapperInterface;

    public MybatisNamedQueryImpl(Session session, String name) {
        this(session, name, null);
    }

    public MybatisNamedQueryImpl(Session session, String name, Class<R> resultClass) {
        this.name = name;
        this.resultClass = resultClass;
        this.session = session;
        this.mappedStatement = session.getConfiguration().getMappedStatement(name);
        mappedStatement.getParameterMap().getParameterMappings().forEach(mapping -> {
            parameters.add(new MyjpaParameterImpl(mapping));
        });
        Metamodel metamodel = session.getEntityManagerFactory().getMetamodel();
        String[] split = StringUtils.split(".", name, false);
        methodName = split[split.length - 1];
        split = Arrays.copyOf(split, split.length - 1);
        try {
            mapperInterface = Class.forName(StringUtils.join(".", split));
        } catch (ClassNotFoundException e) {
            LOGGER.info("未能加载接口类", e);
        }
    }

    @Override
    public void setParameterValues(Object[] values) {
        this.parameterValues = values;
    }

    protected Object invokeMapper() throws NoSuchMethodException {
        Class[] classes = Stream.of(parameterValues).map(Object::getClass).toArray(Class[]::new);

        Method method = ReflectionUtils.findMethod(mapperInterface, methodName, classes);
        Optional.ofNullable(method).orElseThrow(() -> new NoSuchMapperMethodException("未找到mapper方法" + methodName));
        return ReflectionUtils.invokeMethod(method,
                session.getMapper(mapperInterface), parameterValues);

    }

    @Override
    public List<R> getResultList() {
        try {
            Object rs =  invokeMapper();
            Optional<Interceptor> interceptor = session.getConfiguration().getInterceptors().stream()
                    .filter((i) -> PagePlugin.class.isAssignableFrom(i.getClass())).findFirst();
            boolean page = interceptor.isPresent();
            if (page) {
                PagePlugin  pagePlugin = (PagePlugin) interceptor.get();
                return pagePlugin.unWarpPage(parameterValues, null, rs);
            }
            return (List<R>) rs;
        }catch (NoSuchMethodException e){
            return (List<R>) invokeSessionMethod();
        }
    }

    protected Object invokeSessionMethod() {
        return null;
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

    public String getName() {
        return name;
    }
}
