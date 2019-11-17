package cn.sexycode.myjpa.query.criteria.internal.compile;

import cn.sexycode.myjpa.query.criteria.LiteralHandlingMode;
import cn.sexycode.myjpa.query.criteria.internal.expression.function.FunctionExpression;
import cn.sexycode.sql.dialect.Dialect;
import cn.sexycode.sql.mapping.ast.Clause;
import cn.sexycode.util.core.collection.Stack;

import javax.persistence.criteria.ParameterExpression;

/**
 * Used to provide a context and services to the rendering.
 *
 * @author Steve Ebersole
 */
public interface RenderingContext {
    /**
     * Generate a correlation name.
     *
     * @return The generated correlation name
     */
    String generateAlias();

    /**
     * Register parameters explicitly encountered in the criteria query.
     *
     * @param criteriaQueryParameter The parameter expression
     * @return The JPA-QL parameter name
     */
    ExplicitParameterInfo registerExplicitParameter(ParameterExpression<?> criteriaQueryParameter);

    /**
     * Register a parameter that was not part of the criteria query (at least not as a parameter).
     *
     * @param literal  The literal value
     * @param javaType The java type as which to handle the literal value.
     * @return The JPA-QL parameter name
     */
    String registerLiteralParameterBinding(Object literal, Class javaType);

    /**
     * Given a java type, determine the proper cast type name.
     *
     * @param javaType The java type.
     * @return The cast type name.
     */
    String getCastType(Class javaType);

    /**
     * Current Dialect.
     *
     * @return Dialect
     */
    Dialect getDialect();

    /**
     * How literals are going to be handled.
     *
     * @return literal handling strategy
     */
    default LiteralHandlingMode getCriteriaLiteralHandlingMode() {
        return LiteralHandlingMode.AUTO;
    }

    Stack<Clause> getClauseStack();

    Stack<FunctionExpression> getFunctionStack();
}
