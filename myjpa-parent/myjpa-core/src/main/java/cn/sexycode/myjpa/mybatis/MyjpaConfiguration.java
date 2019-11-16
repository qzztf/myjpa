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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author qzz
 */
public class MyjpaConfiguration extends Configuration {
    private static final Logger LOGGER = LoggerFactory.getLogger(MyjpaConfiguration.class);
    private Configuration configuration;

    private MybatisMapperRegistry mybatisMapperRegistry;

    public MyjpaConfiguration() {
        this(null);
    }

    public MyjpaConfiguration(Configuration configuration) {
        if (ObjectUtils.isEmpty(configuration)) {
            configuration = new Configuration();
        }
        this.configuration = configuration;
        mybatisMapperRegistry = new MybatisMapperRegistry(this, configuration.getMapperRegistry());
        ObjectUtils.copyProperties(configuration, this);
        initMappedStatements();
        mybatisMapperRegistry.initEntityMapper();
    }

    private void initMappedStatements() {
        new HashSet<>(configuration.getMappedStatements()).forEach(ms -> {
            try {
                if (ObjectUtils.isEmpty(getMappedStatement(ms.getId()))){
                    //获取不到时会抛出异常
                }
            } catch (IllegalArgumentException e) {
                //ignore
                addMappedStatement(ms);
                LOGGER.debug("初始化MappedStatements失败", e);
            } catch (Exception e) {
                //ignore
                LOGGER.debug("初始化MappedStatements失败", e);
            }

        });

        new HashSet<>(getMappedStatements()).forEach(ms -> {
            try {
                if (ObjectUtils.isEmpty(configuration.getMappedStatement(ms.getId()))){
                    //获取不到时会抛出异常
                }
            } catch (IllegalArgumentException e) {
                //ignore
                configuration.addMappedStatement(ms);
                LOGGER.debug("初始化MappedStatements失败", e);
            } catch (Exception e) {
                //ignore
                LOGGER.debug("初始化MappedStatements失败", e);
            }
        });
    }

    public MyjpaConfiguration(Configuration configuration, Map properties) {
        this(configuration);
        if (!ObjectUtils.isEmpty(properties)) {
            Properties prop = new Properties();
            prop.putAll(properties);
            if (ObjectUtils.isEmpty(getVariables())) {
                setVariables(prop);
            } else {
                getVariables().putAll(prop);
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
    public void addMappedStatement(MappedStatement ms) {
        mappedStatements.put(ms.getId(), ms);
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
        return Optional.ofNullable(mybatisMapperRegistry.getMapper(type, sqlSession))
                .orElse(configuration.getMapper(type, sqlSession));
    }

    @Override
    public boolean hasMapper(Class<?> type) {
        return mybatisMapperRegistry.hasMapper(type) || configuration.hasMapper(type);
    }
}
