/*
 * Hibernate, Relational Persistence for Idiomatic Java
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later.
 * See the lgpl.txt file in the root directory or <http://www.gnu.org/licenses/lgpl-2.1.html>.
 */
package cn.sexycode.myjpa.query.criteria.internal.expression;

import cn.sexycode.myjpa.query.criteria.internal.CriteriaBuilderImpl;
import cn.sexycode.myjpa.query.criteria.internal.ParameterRegistry;
import cn.sexycode.myjpa.query.criteria.internal.Renderable;
import cn.sexycode.myjpa.query.criteria.internal.compile.RenderingContext;

import javax.persistence.criteria.CriteriaBuilder.Coalesce;
import javax.persistence.criteria.Expression;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Models an ANSI SQL <tt>COALESCE</tt> expression.  <tt>COALESCE</tt> is a specialized <tt>CASE</tt> statement.
 *
 * @author Steve Ebersole
 */
public class CoalesceExpression<T> extends ExpressionImpl<T> implements Coalesce<T>, Serializable {
    private final List<Expression<? extends T>> expressions;

    private Class<T> javaType;

    public CoalesceExpression(CriteriaBuilderImpl criteriaBuilder) {
        this(criteriaBuilder, null);
    }

    public CoalesceExpression(CriteriaBuilderImpl criteriaBuilder, Class<T> javaType) {
        super(criteriaBuilder, javaType);
        this.javaType = javaType;
        this.expressions = new ArrayList<Expression<? extends T>>();
    }

    @Override
    public Class<T> getJavaType() {
        return javaType;
    }

    public Coalesce<T> value(T value) {
        return value(new LiteralExpression<T>(criteriaBuilder(), value));
    }

    @SuppressWarnings({ "unchecked" })
    public Coalesce<T> value(Expression<? extends T> value) {
        expressions.add(value);
        if (javaType == null) {
            javaType = (Class<T>) value.getJavaType();
        }
        return this;
    }

    public List<Expression<? extends T>> getExpressions() {
        return expressions;
    }

    public void registerParameters(ParameterRegistry registry) {
        for (Expression expression : getExpressions()) {
            Helper.possibleParameter(expression, registry);
        }
    }

    public String render(RenderingContext renderingContext) {
        StringBuilder buffer = new StringBuilder("coalesce(");
        String sep = "";
        for (Expression expression : getExpressions()) {
            buffer.append(sep).append(((Renderable) expression).render(renderingContext));
            sep = ", ";
        }
        return buffer.append(")").toString();
    }
}
