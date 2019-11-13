package cn.sexycode.myjpa.mybatis;

import cn.sexycode.util.core.cls.ResolvableType;
import cn.sexycode.util.core.object.ObjectUtils;
import cn.sexycode.util.core.str.StringUtils;
import org.apache.ibatis.builder.MapperBuilderAssistant;
import org.apache.ibatis.builder.annotation.MapperAnnotationBuilder;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.session.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.Entity;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * <p>
 * 继承 MapperAnnotationBuilder 没有XML配置文件注入基础CRUD方法
 * </p>
 *
 * @author Caratacus
 * @since 2017-01-04
 */
public class MybatisMapperEntityNamespaceBuilder extends MapperAnnotationBuilder {
    private static final Logger LOGGER = LoggerFactory.getLogger(MybatisMapperEntityNamespaceBuilder.class);
    private final Configuration configuration;

    private final MapperBuilderAssistant assistant;

    private final Class<?> type;

    public MybatisMapperEntityNamespaceBuilder(Configuration configuration, Class<?> type) {
        // 执行父类
        super(configuration, type);
        String resource = type.getName().replace('.', '/') + ".java (best guess)";
        this.assistant = new MapperBuilderAssistant(configuration, resource);
        this.configuration = configuration;
        this.type = type;
    }

    @Override
    public void parse() {
        ResolvableType resolvableType = ResolvableType.forClass(type);
        ResolvableType[] generics = resolvableType.getGenerics();
        List<ResolvableType> resolvableTypes = new ArrayList<>();
        if (!ObjectUtils.isEmpty(generics)) {
            resolvableTypes.addAll(Arrays.asList(generics));
        }
        if (!ObjectUtils.isEmpty(resolvableType.getInterfaces())) {
            resolvableTypes.addAll(Stream.of(resolvableType.getInterfaces()).flatMap(r -> Stream.of(r.getGenerics()))
                    .collect(Collectors.toList()));
        }
        resolvableTypes.forEach(r -> {
            Class<?> aClass = r.getRawClass();
            if (!ObjectUtils.isEmpty(aClass)) {
                Entity entity = aClass.getAnnotation(Entity.class);
                if (!ObjectUtils.isEmpty(entity)) {
                    for (Method method : type.getMethods()) {
                        try {
                            MappedStatement mappedStatement = configuration
                                    .getMappedStatement(type.getName() + "." + method.getName());
                            if (!ObjectUtils.isEmpty(mappedStatement)) {
                                String id = aClass.getName() + "." + method.getName();
                                addEntityMappedStatement(id, method, mappedStatement);
                                id = Optional.ofNullable(StringUtils.nullIfEmpty(entity.name())).orElse(aClass.getSimpleName()) + "." + method.getName();
                                addEntityMappedStatement(id, method, mappedStatement);
                            }
                        } catch (Exception e) {
                            LOGGER.info("添加实体类命名空间statement失败", e);
                        }
                    }
                }
            }
        });
    }

    private void addEntityMappedStatement(String id , Method method, MappedStatement mappedStatement) {
        MappedStatement.Builder statementBuilder = new MappedStatement.Builder(configuration,
                id, mappedStatement.getSqlSource(),
                mappedStatement.getSqlCommandType()).resource(mappedStatement.getResource())
                .fetchSize(mappedStatement.getFetchSize()).timeout(mappedStatement.getTimeout())
                .statementType(mappedStatement.getStatementType())
                .keyGenerator(mappedStatement.getKeyGenerator())
                .keyProperty(StringUtils.join(",", Optional.ofNullable(mappedStatement.getKeyProperties()).orElse(new String[0])))
                .keyColumn(StringUtils.join(",", Optional.ofNullable(mappedStatement.getKeyColumns()).orElse(new String[0])))
                .databaseId(mappedStatement.getDatabaseId()).lang(mappedStatement.getLang())
                .resultOrdered(mappedStatement.isResultOrdered())
                .resultSets(StringUtils.join(",", Optional.ofNullable(mappedStatement.getResultSets()).orElse(new String[0])))
                .resultMaps(mappedStatement.getResultMaps())
                .resultSetType(mappedStatement.getResultSetType())
                .flushCacheRequired(mappedStatement.isFlushCacheRequired())
                .useCache(mappedStatement.isUseCache()).cache(mappedStatement.getCache());
        statementBuilder.parameterMap(mappedStatement.getParameterMap());
        configuration.addMappedStatement(statementBuilder.build());
    }

}
