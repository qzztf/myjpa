package cn.sexycode.myjpa.query.criteria.internal.compile;

import cn.sexycode.myjpa.query.criteria.internal.expression.function.FunctionExpression;
import cn.sexycode.myjpa.session.Session;
import cn.sexycode.myjpa.session.SessionFactory;
import cn.sexycode.myjpa.sql.dialect.Dialect;
import cn.sexycode.myjpa.sql.mapping.ast.Clause;
import cn.sexycode.myjpa.sql.type.Type;
import cn.sexycode.util.core.collection.Stack;
import cn.sexycode.util.core.collection.StandardStack;
import cn.sexycode.util.core.service.ServiceRegistry;
import cn.sexycode.util.core.str.StringUtils;

import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.ParameterExpression;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Compiles a JPA criteria query into an executable {@link TypedQuery}.  Its single contract is the {@link #compile}
 * method.
 * <p/>
 * NOTE : This is a temporary implementation which simply translates the criteria query into a JPAQL query string.  A
 * better, long-term solution is being implemented as part of refactoring the JPAQL/HQL translator.
 *
 * @author Steve Ebersole
 */
public class CriteriaCompiler implements Serializable {
    private final Session entityManager;

    public CriteriaCompiler(Session entityManager) {
        this.entityManager = entityManager;
    }

    public Query compile(CompilableCriteria criteria) {
        try {
            criteria.validate();
        } catch (IllegalStateException ise) {
            throw new IllegalArgumentException("Error occurred validating the Criteria", ise);
        }

        final Map<ParameterExpression<?>, ExplicitParameterInfo<?>> explicitParameterInfoMap = new HashMap<>();
        final List<ImplicitParameterBinding> implicitParameterBindings = new ArrayList<>();

        final SessionFactory sessionFactory = entityManager.getSessionFactory();

    /*    final LiteralHandlingMode criteriaLiteralHandlingMode = sessionFactory.getSessionFactoryOptions()
                .getCriteriaLiteralHandlingMode();*/

        ServiceRegistry serviceRegistry = sessionFactory.getServiceRegistry();
        final Dialect dialect = null;
//                serviceRegistry.getService(JdbcEnvironment.class).getDialect();

        RenderingContext renderingContext = new RenderingContext() {
            private int aliasCount;

            private int explicitParameterCount;

            private final Stack<Clause> clauseStack = new StandardStack<>();

            private final Stack<FunctionExpression> functionContextStack = new StandardStack<>();

            @Override
            public String generateAlias() {
                return "generatedAlias" + aliasCount++;
            }

            public String generateParameterName() {
                return "param" + explicitParameterCount++;
            }

            @Override
            public Stack<Clause> getClauseStack() {
                return clauseStack;
            }

            @Override
            public Stack<FunctionExpression> getFunctionStack() {
                return functionContextStack;
            }

            @Override
            @SuppressWarnings("unchecked")
            public ExplicitParameterInfo registerExplicitParameter(ParameterExpression<?> criteriaQueryParameter) {
                ExplicitParameterInfo parameterInfo = explicitParameterInfoMap.get(criteriaQueryParameter);
                if (parameterInfo == null) {
                    if (StringUtils.isNotEmpty(criteriaQueryParameter.getName())) {
                        parameterInfo = new ExplicitParameterInfo(criteriaQueryParameter.getName(), null,
                                criteriaQueryParameter.getJavaType());
                    } else if (criteriaQueryParameter.getPosition() != null) {
                        parameterInfo = new ExplicitParameterInfo(null, criteriaQueryParameter.getPosition(),
                                criteriaQueryParameter.getJavaType());
                    } else {
                        parameterInfo = new ExplicitParameterInfo(generateParameterName(), null,
                                criteriaQueryParameter.getJavaType());
                    }

                    explicitParameterInfoMap.put(criteriaQueryParameter, parameterInfo);
                }

                return parameterInfo;
            }

            @Override
            public String registerLiteralParameterBinding(final Object literal, final Class javaType) {
                final String parameterName = generateParameterName();
                final ImplicitParameterBinding binding = new ImplicitParameterBinding() {
                    @Override
                    public String getParameterName() {
                        return parameterName;
                    }

                    @Override
                    public Class getJavaType() {
                        return javaType;
                    }

                    @Override
                    public void bind(TypedQuery typedQuery) {
                        typedQuery.setParameter(parameterName, literal);
                    }
                };

                implicitParameterBindings.add(binding);
                return parameterName;
            }

            public String getCastType(Class javaType) {
                SessionFactory factory = entityManager.getSessionFactory();
                Type hibernateType = factory.getTypeResolver().heuristicType(javaType.getName());
                if (hibernateType == null) {
                    throw new IllegalArgumentException(
                            "Could not convert java type [" + javaType.getName() + "] to Hibernate type");
                }
                return hibernateType.getName();
            }

            @Override
            public Dialect getDialect() {
                return dialect;
            }

           /* @Override
            public LiteralHandlingMode getCriteriaLiteralHandlingMode() {
                return criteriaLiteralHandlingMode;
            }*/
        };

        return criteria.interpret(renderingContext)
                .buildCompiledQuery(entityManager, new InterpretedParameterMetadata() {
                    @Override
                    public Map<ParameterExpression<?>, ExplicitParameterInfo<?>> explicitParameterInfoMap() {
                        return explicitParameterInfoMap;
                    }

                    @Override
                    public List<ImplicitParameterBinding> implicitParameterBindings() {
                        return implicitParameterBindings;
                    }
                });
    }

}
