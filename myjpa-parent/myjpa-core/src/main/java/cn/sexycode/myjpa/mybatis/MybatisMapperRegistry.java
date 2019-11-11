package cn.sexycode.myjpa.mybatis;

import org.apache.ibatis.binding.MapperProxyFactory;
import org.apache.ibatis.binding.MapperRegistry;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSession;

import java.util.*;

/**
 * <p>
 * 继承至MapperRegistry
 * </p>
 *
 * @author Caratacus hubin
 * @since 2017-04-19
 */
public class MybatisMapperRegistry extends MapperRegistry {

    private final Map<Class<?>, MapperProxyFactory<?>> knownMappers = new HashMap<>();
    private final Configuration config;

    private final MapperRegistry innerMapperRegistry;

    public MybatisMapperRegistry(Configuration config, MapperRegistry mapperRegistry) {
        super(config);
        this.config = config;
        this.innerMapperRegistry = Optional.ofNullable(mapperRegistry).orElse(new MapperRegistry(config));
    }

    public MybatisMapperRegistry(Configuration config) {
        this(config, null);
    }

    @Override
    public <T> T getMapper(Class<T> type, SqlSession sqlSession) {
        return innerMapperRegistry.getMapper(type, sqlSession);
    }

    @Override
    public <T> boolean hasMapper(Class<T> type) {
        return innerMapperRegistry.hasMapper(type);
    }

    @Override
    public <T> void addMapper(Class<T> type) {
        innerMapperRegistry.addMapper(type);
        if (type.isInterface()) {
            if (knownMappers.containsKey(type)) {
                return;
            }
            boolean loadCompleted = false;
            try {
                knownMappers.put(type, new MapperProxyFactory<>(type));
                MybatisMapperEntityNamespaceBuilder builder = new MybatisMapperEntityNamespaceBuilder(config, type);
                builder.parse();
                loadCompleted = true;
            } finally {
                if (!loadCompleted) {
                    knownMappers.remove(type);
                }
            }
        }
    }

    /**
     * @since 3.2.2
     */
    @Override
    public Collection<Class<?>> getMappers() {
        return Collections.unmodifiableCollection(knownMappers.keySet());
    }
}
