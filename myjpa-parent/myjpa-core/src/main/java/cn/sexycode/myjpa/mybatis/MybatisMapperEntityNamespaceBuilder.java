package cn.sexycode.myjpa.mybatis;

import cn.sexycode.util.core.cls.ResolvableType;
import cn.sexycode.util.core.object.ObjectUtils;
import cn.sexycode.util.core.str.StringUtils;
import org.apache.ibatis.builder.MapperBuilderAssistant;
import org.apache.ibatis.builder.annotation.MapperAnnotationBuilder;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.session.Configuration;

import javax.persistence.Entity;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
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
                if (!ObjectUtils.isEmpty(aClass.getAnnotation(Entity.class))) {
                    for (Method method : type.getMethods()) {
                        MappedStatement mappedStatement = configuration
                                .getMappedStatement(type + "." + method.getName());
                        if (!ObjectUtils.isEmpty(mappedStatement)) {
                            MappedStatement.Builder statementBuilder = new MappedStatement.Builder(configuration,
                                    aClass.getName() + "." + method.getName(), mappedStatement.getSqlSource(),
                                    mappedStatement.getSqlCommandType()).resource(mappedStatement.getResource())
                                    .fetchSize(mappedStatement.getFetchSize()).timeout(mappedStatement.getTimeout())
                                    .statementType(mappedStatement.getStatementType())
                                    .keyGenerator(mappedStatement.getKeyGenerator())
                                    .keyProperty(StringUtils.join(",", mappedStatement.getKeyProperties()))
                                    .keyColumn(StringUtils.join(",", mappedStatement.getKeyColumns()))
                                    .databaseId(mappedStatement.getDatabaseId()).lang(mappedStatement.getLang())
                                    .resultOrdered(mappedStatement.isResultOrdered())
                                    .resultSets(StringUtils.join(",", mappedStatement.getResultSets()))
                                    .resultMaps(mappedStatement.getResultMaps())
                                    .resultSetType(mappedStatement.getResultSetType())
                                    .flushCacheRequired(mappedStatement.isFlushCacheRequired())
                                    .useCache(mappedStatement.isUseCache()).cache(mappedStatement.getCache());
                            statementBuilder.parameterMap(mappedStatement.getParameterMap());
                            configuration.addMappedStatement(statementBuilder.build());

                            statementBuilder = new MappedStatement.Builder(configuration,
                                    aClass.getSimpleName() + "." + method.getName(), mappedStatement.getSqlSource(),
                                    mappedStatement.getSqlCommandType()).resource(mappedStatement.getResource())
                                    .fetchSize(mappedStatement.getFetchSize()).timeout(mappedStatement.getTimeout())
                                    .statementType(mappedStatement.getStatementType())
                                    .keyGenerator(mappedStatement.getKeyGenerator())
                                    .keyProperty(StringUtils.join(",", mappedStatement.getKeyProperties()))
                                    .keyColumn(StringUtils.join(",", mappedStatement.getKeyColumns()))
                                    .databaseId(mappedStatement.getDatabaseId()).lang(mappedStatement.getLang())
                                    .resultOrdered(mappedStatement.isResultOrdered())
                                    .resultSets(StringUtils.join(",", mappedStatement.getResultSets()))
                                    .resultMaps(mappedStatement.getResultMaps())
                                    .resultSetType(mappedStatement.getResultSetType())
                                    .flushCacheRequired(mappedStatement.isFlushCacheRequired())
                                    .useCache(mappedStatement.isUseCache()).cache(mappedStatement.getCache());
                            statementBuilder.parameterMap(mappedStatement.getParameterMap());
                            configuration.addMappedStatement(statementBuilder.build());
                        }
                    }
                }
            }
        });
    }

}
