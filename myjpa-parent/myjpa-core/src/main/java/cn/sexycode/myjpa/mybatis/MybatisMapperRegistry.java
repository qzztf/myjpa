package cn.sexycode.myjpa.mybatis;

import org.apache.ibatis.binding.BindingException;
import org.apache.ibatis.binding.MapperProxyFactory;
import org.apache.ibatis.binding.MapperRegistry;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSession;

import java.util.*;

/**
 * @author Clinton Begin
 * @author Eduardo Macarron
 * @author Lasse Voss
 */
public class MybatisMapperRegistry extends MapperRegistry {

    private final Map<Class<?>, MapperProxyFactory<?>> knownMappers = new HashMap<>();
    private final Configuration config;

    private final MapperRegistry innerMapperRegistry;

    public MybatisMapperRegistry(Configuration config, MapperRegistry mapperRegistry) {
        super(config);
        this.config = config;
        this.innerMapperRegistry = Optional.ofNullable(mapperRegistry).orElse(new MapperRegistry(config));
        initEntityMapper();
    }

    public MybatisMapperRegistry(Configuration config) {
        this(config, null);
    }

    @Override
    public <T> T getMapper(Class<T> type, SqlSession sqlSession) {

        final MapperProxyFactory<T> mapperProxyFactory = (MapperProxyFactory<T>) knownMappers.get(type);
        if (mapperProxyFactory == null) {
            return innerMapperRegistry.getMapper(type, sqlSession);
        }
        try {
            return mapperProxyFactory.newInstance(sqlSession);
        } catch (Exception e) {
            throw new BindingException("Error getting mapper instance.", e);
        }

    }

    @Override
    public <T> boolean hasMapper(Class<T> type) {
        return innerMapperRegistry.hasMapper(type) && knownMappers.containsKey(type);
    }

    @Override
    public <T> void addMapper(Class<T> type) {
        if (!innerMapperRegistry.hasMapper(type)) {
            innerMapperRegistry.addMapper(type);
        }
        if (type.isInterface()) {
            if (knownMappers.containsKey(type)) {
                return;
            }
            boolean loadCompleted = false;
            try {
                knownMappers.put(type, new MyjpaMapperProxyFactory<T>(type));
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
        Set<Class<?>> set = new HashSet<>(knownMappers.keySet());
        set.addAll(super.getMappers());
        return Collections.unmodifiableCollection(set);
    }

    private void initEntityMapper() {
        innerMapperRegistry.getMappers().forEach(this::addMapper);
    }
}
