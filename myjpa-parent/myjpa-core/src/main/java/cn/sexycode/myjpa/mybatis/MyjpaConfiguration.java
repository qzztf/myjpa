package cn.sexycode.myjpa.mybatis;

import cn.sexycode.myjpa.binding.Metadata;
import cn.sexycode.util.core.object.ObjectUtils;
import org.apache.ibatis.binding.MapperRegistry;
import org.apache.ibatis.builder.CacheRefResolver;
import org.apache.ibatis.builder.ResultMapResolver;
import org.apache.ibatis.builder.annotation.MethodResolver;
import org.apache.ibatis.builder.xml.XMLStatementBuilder;
import org.apache.ibatis.cache.Cache;
import org.apache.ibatis.executor.*;
import org.apache.ibatis.executor.keygen.KeyGenerator;
import org.apache.ibatis.executor.loader.ProxyFactory;
import org.apache.ibatis.executor.loader.javassist.JavassistProxyFactory;
import org.apache.ibatis.executor.parameter.ParameterHandler;
import org.apache.ibatis.executor.resultset.DefaultResultSetHandler;
import org.apache.ibatis.executor.resultset.ResultSetHandler;
import org.apache.ibatis.executor.statement.RoutingStatementHandler;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.io.VFS;
import org.apache.ibatis.logging.Log;
import org.apache.ibatis.mapping.*;
import org.apache.ibatis.parsing.XNode;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.ReflectorFactory;
import org.apache.ibatis.reflection.factory.ObjectFactory;
import org.apache.ibatis.reflection.wrapper.ObjectWrapperFactory;
import org.apache.ibatis.scripting.LanguageDriver;
import org.apache.ibatis.scripting.LanguageDriverRegistry;
import org.apache.ibatis.scripting.xmltags.XMLLanguageDriver;
import org.apache.ibatis.session.*;
import org.apache.ibatis.transaction.Transaction;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.TypeAliasRegistry;
import org.apache.ibatis.type.TypeHandler;
import org.apache.ibatis.type.TypeHandlerRegistry;

import java.util.*;

public class MyjpaConfiguration extends Configuration {
    private Configuration configuration;

    private Metadata metadata;

    private MybatisMapperRegistry mybatisMapperRegistry;

    public MyjpaConfiguration() {
        this((Configuration) null);
    }

    public MyjpaConfiguration(Configuration configuration) {
        this.configuration = configuration;
        if (ObjectUtils.isEmpty(configuration)) {
            this.configuration = new Configuration();
        }
        mybatisMapperRegistry = new MybatisMapperRegistry(this);
    }

    public MyjpaConfiguration(Map properties) {
        this();
        if (!ObjectUtils.isEmpty(properties)) {
            Properties prop = new Properties();
            prop.putAll(properties);
            if (ObjectUtils.isEmpty(this.configuration.getVariables())) {
                this.configuration.setVariables(prop);
            } else {
                this.configuration.getVariables().putAll(prop);
            }
        }
    }

    public Configuration getConfiguration() {
        return configuration;
    }

    public void setConfiguration(Configuration configuration) {
        this.configuration = configuration;
    }

    @Override
    public String getLogPrefix() {
        return configuration.getLogPrefix();
    }

    @Override
    public void setLogPrefix(String logPrefix) {
        configuration.setLogPrefix(logPrefix);
    }

    @Override
    public Class<? extends Log> getLogImpl() {
        return configuration.getLogImpl();
    }

    @Override
    public void setLogImpl(Class<? extends Log> logImpl) {
        configuration.setLogImpl(logImpl);
    }

    @Override
    public Class<? extends VFS> getVfsImpl() {
        return configuration.getVfsImpl();
    }

    @Override
    public void setVfsImpl(Class<? extends VFS> vfsImpl) {
        configuration.setVfsImpl(vfsImpl);
    }

    @Override
    public boolean isCallSettersOnNulls() {
        return configuration.isCallSettersOnNulls();
    }

    @Override
    public void setCallSettersOnNulls(boolean callSettersOnNulls) {
        configuration.setCallSettersOnNulls(callSettersOnNulls);
    }

    @Override
    public boolean isUseActualParamName() {
        return configuration.isUseActualParamName();
    }

    @Override
    public void setUseActualParamName(boolean useActualParamName) {
        this.useActualParamName = useActualParamName;
    }

    public boolean isReturnInstanceForEmptyRow() {
        return returnInstanceForEmptyRow;
    }

    public void setReturnInstanceForEmptyRow(boolean returnEmptyInstance) {
        this.returnInstanceForEmptyRow = returnEmptyInstance;
    }

    public String getDatabaseId() {
        return databaseId;
    }

    public void setDatabaseId(String databaseId) {
        this.databaseId = databaseId;
    }

    public Class<?> getConfigurationFactory() {
        return configurationFactory;
    }

    public void setConfigurationFactory(Class<?> configurationFactory) {
        this.configurationFactory = configurationFactory;
    }

    public boolean isSafeResultHandlerEnabled() {
        return safeResultHandlerEnabled;
    }

    public void setSafeResultHandlerEnabled(boolean safeResultHandlerEnabled) {
        this.safeResultHandlerEnabled = safeResultHandlerEnabled;
    }

    public boolean isSafeRowBoundsEnabled() {
        return safeRowBoundsEnabled;
    }

    public void setSafeRowBoundsEnabled(boolean safeRowBoundsEnabled) {
        this.safeRowBoundsEnabled = safeRowBoundsEnabled;
    }

    public boolean isMapUnderscoreToCamelCase() {
        return mapUnderscoreToCamelCase;
    }

    public void setMapUnderscoreToCamelCase(boolean mapUnderscoreToCamelCase) {
        this.mapUnderscoreToCamelCase = mapUnderscoreToCamelCase;
    }

    public void addLoadedResource(String resource) {
        loadedResources.add(resource);
    }

    public boolean isResourceLoaded(String resource) {
        return loadedResources.contains(resource);
    }

    public Environment getEnvironment() {
        return environment;
    }

    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }

    public AutoMappingBehavior getAutoMappingBehavior() {
        return autoMappingBehavior;
    }

    public void setAutoMappingBehavior(AutoMappingBehavior autoMappingBehavior) {
        this.autoMappingBehavior = autoMappingBehavior;
    }

    /**
     * @since 3.4.0
     */
    public AutoMappingUnknownColumnBehavior getAutoMappingUnknownColumnBehavior() {
        return autoMappingUnknownColumnBehavior;
    }

    /**
     * @since 3.4.0
     */
    public void setAutoMappingUnknownColumnBehavior(AutoMappingUnknownColumnBehavior autoMappingUnknownColumnBehavior) {
        this.autoMappingUnknownColumnBehavior = autoMappingUnknownColumnBehavior;
    }

    public boolean isLazyLoadingEnabled() {
        return lazyLoadingEnabled;
    }

    public void setLazyLoadingEnabled(boolean lazyLoadingEnabled) {
        this.lazyLoadingEnabled = lazyLoadingEnabled;
    }

    public ProxyFactory getProxyFactory() {
        return proxyFactory;
    }

    public void setProxyFactory(ProxyFactory proxyFactory) {
        if (proxyFactory == null) {
            proxyFactory = new JavassistProxyFactory();
        }
        this.proxyFactory = proxyFactory;
    }

    public boolean isAggressiveLazyLoading() {
        return aggressiveLazyLoading;
    }

    public void setAggressiveLazyLoading(boolean aggressiveLazyLoading) {
        this.aggressiveLazyLoading = aggressiveLazyLoading;
    }

    public boolean isMultipleResultSetsEnabled() {
        return multipleResultSetsEnabled;
    }

    public void setMultipleResultSetsEnabled(boolean multipleResultSetsEnabled) {
        this.multipleResultSetsEnabled = multipleResultSetsEnabled;
    }

    public Set<String> getLazyLoadTriggerMethods() {
        return lazyLoadTriggerMethods;
    }

    public void setLazyLoadTriggerMethods(Set<String> lazyLoadTriggerMethods) {
        this.lazyLoadTriggerMethods = lazyLoadTriggerMethods;
    }

    public boolean isUseGeneratedKeys() {
        return useGeneratedKeys;
    }

    public void setUseGeneratedKeys(boolean useGeneratedKeys) {
        this.useGeneratedKeys = useGeneratedKeys;
    }

    public ExecutorType getDefaultExecutorType() {
        return defaultExecutorType;
    }

    public void setDefaultExecutorType(ExecutorType defaultExecutorType) {
        this.defaultExecutorType = defaultExecutorType;
    }

    public boolean isCacheEnabled() {
        return cacheEnabled;
    }

    public void setCacheEnabled(boolean cacheEnabled) {
        this.cacheEnabled = cacheEnabled;
    }

    public Integer getDefaultStatementTimeout() {
        return defaultStatementTimeout;
    }

    public void setDefaultStatementTimeout(Integer defaultStatementTimeout) {
        this.defaultStatementTimeout = defaultStatementTimeout;
    }

    /**
     * @since 3.3.0
     */
    public Integer getDefaultFetchSize() {
        return defaultFetchSize;
    }

    /**
     * @since 3.3.0
     */
    public void setDefaultFetchSize(Integer defaultFetchSize) {
        this.defaultFetchSize = defaultFetchSize;
    }

    public boolean isUseColumnLabel() {
        return useColumnLabel;
    }

    public void setUseColumnLabel(boolean useColumnLabel) {
        this.useColumnLabel = useColumnLabel;
    }

    public LocalCacheScope getLocalCacheScope() {
        return localCacheScope;
    }

    public void setLocalCacheScope(LocalCacheScope localCacheScope) {
        this.localCacheScope = localCacheScope;
    }

    public JdbcType getJdbcTypeForNull() {
        return jdbcTypeForNull;
    }

    public void setJdbcTypeForNull(JdbcType jdbcTypeForNull) {
        this.jdbcTypeForNull = jdbcTypeForNull;
    }

    public Properties getVariables() {
        return variables;
    }

    public void setVariables(Properties variables) {
        this.variables = variables;
    }

    public TypeHandlerRegistry getTypeHandlerRegistry() {
        return typeHandlerRegistry;
    }

    /**
     * Set a default {@link TypeHandler} class for {@link Enum}.
     * A default {@link TypeHandler} is {@link org.apache.ibatis.type.EnumTypeHandler}.
     *
     * @param typeHandler a type handler class for {@link Enum}
     * @since 3.4.5
     */
    @Override
    public void setDefaultEnumTypeHandler(Class<? extends TypeHandler> typeHandler) {
        configuration.setDefaultEnumTypeHandler(typeHandler);
    }

    @Override
    public TypeAliasRegistry getTypeAliasRegistry() {
        return configuration.getTypeAliasRegistry();
    }

    /**
     * @since 3.2.2
     */
    @Override
    public MapperRegistry getMapperRegistry() {
        return mybatisMapperRegistry;
    }

    public ReflectorFactory getReflectorFactory() {
        return reflectorFactory;
    }

    public void setReflectorFactory(ReflectorFactory reflectorFactory) {
        this.reflectorFactory = reflectorFactory;
    }

    public ObjectFactory getObjectFactory() {
        return objectFactory;
    }

    public void setObjectFactory(ObjectFactory objectFactory) {
        this.objectFactory = objectFactory;
    }

    public ObjectWrapperFactory getObjectWrapperFactory() {
        return objectWrapperFactory;
    }

    public void setObjectWrapperFactory(ObjectWrapperFactory objectWrapperFactory) {
        this.objectWrapperFactory = objectWrapperFactory;
    }

    /**
     * @since 3.2.2
     */
    public List<Interceptor> getInterceptors() {
        return interceptorChain.getInterceptors();
    }

    public LanguageDriverRegistry getLanguageRegistry() {
        return languageRegistry;
    }

    public void setDefaultScriptingLanguage(Class<?> driver) {
        if (driver == null) {
            driver = XMLLanguageDriver.class;
        }
        getLanguageRegistry().setDefaultDriverClass(driver);
    }

    public LanguageDriver getDefaultScriptingLanguageInstance() {
        return languageRegistry.getDefaultDriver();
    }

    /**
     * @deprecated Use {@link #getDefaultScriptingLanguageInstance()}
     */
    @Deprecated
    public LanguageDriver getDefaultScriptingLanuageInstance() {
        return getDefaultScriptingLanguageInstance();
    }

    public MetaObject newMetaObject(Object object) {
        return MetaObject.forObject(object, objectFactory, objectWrapperFactory, reflectorFactory);
    }

    public ParameterHandler newParameterHandler(MappedStatement mappedStatement, Object parameterObject,
            BoundSql boundSql) {
        ParameterHandler parameterHandler = mappedStatement.getLang()
                .createParameterHandler(mappedStatement, parameterObject, boundSql);
        parameterHandler = (ParameterHandler) interceptorChain.pluginAll(parameterHandler);
        return parameterHandler;
    }

    public ResultSetHandler newResultSetHandler(Executor executor, MappedStatement mappedStatement, RowBounds rowBounds,
            ParameterHandler parameterHandler, ResultHandler resultHandler, BoundSql boundSql) {
        ResultSetHandler resultSetHandler = new DefaultResultSetHandler(executor, mappedStatement, parameterHandler,
                resultHandler, boundSql, rowBounds);
        resultSetHandler = (ResultSetHandler) interceptorChain.pluginAll(resultSetHandler);
        return resultSetHandler;
    }

    public StatementHandler newStatementHandler(Executor executor, MappedStatement mappedStatement,
            Object parameterObject, RowBounds rowBounds, ResultHandler resultHandler, BoundSql boundSql) {
        StatementHandler statementHandler = new RoutingStatementHandler(executor, mappedStatement, parameterObject,
                rowBounds, resultHandler, boundSql);
        statementHandler = (StatementHandler) interceptorChain.pluginAll(statementHandler);
        return statementHandler;
    }

    public Executor newExecutor(Transaction transaction) {
        return newExecutor(transaction, defaultExecutorType);
    }

    public Executor newExecutor(Transaction transaction, ExecutorType executorType) {
        executorType = executorType == null ? defaultExecutorType : executorType;
        executorType = executorType == null ? ExecutorType.SIMPLE : executorType;
        Executor executor;
        if (ExecutorType.BATCH == executorType) {
            executor = new BatchExecutor(this, transaction);
        } else if (ExecutorType.REUSE == executorType) {
            executor = new ReuseExecutor(this, transaction);
        } else {
            executor = new SimpleExecutor(this, transaction);
        }
        if (cacheEnabled) {
            executor = new CachingExecutor(executor);
        }
        executor = (Executor) interceptorChain.pluginAll(executor);
        return executor;
    }

    public void addKeyGenerator(String id, KeyGenerator keyGenerator) {
        keyGenerators.put(id, keyGenerator);
    }

    public Collection<String> getKeyGeneratorNames() {
        return keyGenerators.keySet();
    }

    public Collection<KeyGenerator> getKeyGenerators() {
        return keyGenerators.values();
    }

    public KeyGenerator getKeyGenerator(String id) {
        return keyGenerators.get(id);
    }

    public boolean hasKeyGenerator(String id) {
        return keyGenerators.containsKey(id);
    }
    @Override
    public void addCache(Cache cache) {
        caches.put(cache.getId(), cache);
    }
    @Override
    public Collection<String> getCacheNames() {
        return caches.keySet();
    }
    @Override
    public Collection<Cache> getCaches() {
        return caches.values();
    }
    @Override
    public Cache getCache(String id) {
        return configuration.getCache(id);
    }
    @Override
    public boolean hasCache(String id) {
        return configuration.hasCache(id);
    }
    @Override
    public void addResultMap(ResultMap rm) {
        configuration.addResultMap(rm);
    }
    @Override
    public Collection<String> getResultMapNames() {
        return configuration.getResultMapNames();
    }
    @Override
    public Collection<ResultMap> getResultMaps() {
        return configuration.getResultMaps();
    }
    @Override
    public ResultMap getResultMap(String id) {
        return configuration.getResultMap(id);
    }
    @Override
    public boolean hasResultMap(String id) {
        return configuration.hasResultMap(id);
    }

    @Override
    public void addParameterMap(ParameterMap pm) {
        configuration.addParameterMap(pm);
    }

    @Override
    public Collection<String> getParameterMapNames() {
        return configuration.getParameterMapNames();
    }

    @Override
    public Collection<ParameterMap> getParameterMaps() {
        return configuration.getParameterMaps();
    }

    @Override
    public ParameterMap getParameterMap(String id) {
        return configuration.getParameterMap(id);
    }

    @Override
    public boolean hasParameterMap(String id) {
        return configuration.hasParameterMap(id);
    }

    @Override
    public void addMappedStatement(MappedStatement ms) {
        configuration.addMappedStatement(ms);
    }

    @Override
    public Collection<String> getMappedStatementNames() {
        return configuration.getMappedStatementNames();
    }

    @Override
    public Collection<MappedStatement> getMappedStatements() {
        return configuration.getMappedStatements();
    }

    @Override
    public Collection<XMLStatementBuilder> getIncompleteStatements() {
        return configuration.getIncompleteStatements();
    }

    @Override
    public void addIncompleteStatement(XMLStatementBuilder incompleteStatement) {
        configuration.addIncompleteStatement(incompleteStatement);
    }

    @Override
    public Collection<CacheRefResolver> getIncompleteCacheRefs() {
        return configuration.getIncompleteCacheRefs();
    }

    @Override
    public void addIncompleteCacheRef(CacheRefResolver incompleteCacheRef) {
        configuration.addIncompleteCacheRef(incompleteCacheRef);
    }

    @Override
    public Collection<ResultMapResolver> getIncompleteResultMaps() {
        return configuration.getIncompleteResultMaps();
    }

    @Override
    public void addIncompleteResultMap(ResultMapResolver resultMapResolver) {
        configuration.addIncompleteResultMap(resultMapResolver);
    }

    @Override
    public void addIncompleteMethod(MethodResolver builder) {
        configuration.addIncompleteMethod(builder);
    }

    @Override
    public Collection<MethodResolver> getIncompleteMethods() {
        return configuration.getIncompleteMethods();
    }

    @Override
    public MappedStatement getMappedStatement(String id) {
        return configuration.getMappedStatement(id);
    }

    @Override
    public MappedStatement getMappedStatement(String id, boolean validateIncompleteStatements) {
        return configuration.getMappedStatement(id, validateIncompleteStatements);
    }

    @Override
    public Map<String, XNode> getSqlFragments() {
        return configuration.getSqlFragments();
    }

    @Override
    public void addInterceptor(Interceptor interceptor) {
        configuration.addInterceptor(interceptor);
    }

    @Override
    public void addMappers(String packageName, Class<?> superType) {
        mybatisMapperRegistry.addMappers(packageName, superType);
    }

    @Override
    public void addMappers(String packageName) {
        mybatisMapperRegistry.addMappers(packageName);
    }

    @Override
    public <T> void addMapper(Class<T> type) {
        mybatisMapperRegistry.addMapper(type);
    }

    @Override
    public <T> T getMapper(Class<T> type, SqlSession sqlSession) {
        return mybatisMapperRegistry.getMapper(type, sqlSession);
    }

    @Override
    public boolean hasMapper(Class<?> type) {
        return mybatisMapperRegistry.hasMapper(type);
    }

    @Override
    public boolean hasStatement(String statementName) {
        return configuration.hasStatement(statementName);
    }

    @Override
    public boolean hasStatement(String statementName, boolean validateIncompleteStatements) {
        return configuration.hasStatement(statementName,validateIncompleteStatements);
    }

    @Override
    public void addCacheRef(String namespace, String referencedNamespace) {
        configuration.addCacheRef(namespace, referencedNamespace);
    }

}
