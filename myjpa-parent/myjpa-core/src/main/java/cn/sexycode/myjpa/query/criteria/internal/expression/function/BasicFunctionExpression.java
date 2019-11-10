/*
 * Hibernate, Relational Persistence for Idiomatic Java
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later.
 * See the lgpl.txt file in the root directory or <http://www.gnu.org/licenses/lgpl-2.1.html>.
 */
package cn.sexycode.myjpa.query.criteria.internal.expression.function;

import cn.sexycode.myjpa.query.criteria.internal.CriteriaBuilderImpl;
import cn.sexycode.myjpa.query.criteria.internal.ParameterRegistry;
import cn.sexycode.myjpa.query.criteria.internal.compile.RenderingContext;
import cn.sexycode.myjpa.query.criteria.internal.expression.ExpressionImpl;

import java.io.Serializable;

/**
 * Models the basic concept of a SQL function.
 *
 * @author Steve Ebersole
 */
public class BasicFunctionExpression<X> extends ExpressionImpl<X> implements FunctionExpression<X>, Serializable {

    private final String functionName;

    public BasicFunctionExpression(CriteriaBuilderImpl criteriaBuilder, Class<X> javaType, String functionName) {
        super(criteriaBuilder, javaType);
        this.functionName = functionName;
    }

    protected static int properSize(int number) {
        return number + (int) (number * .75) + 1;
    }

    public String getFunctionName() {
        return functionName;
    }

    public boolean isAggregation() {
        return false;
    }

    public void registerParameters(ParameterRegistry registry) {
        // nothing to do here...
    }

    public String render(RenderingContext renderingContext) {
        return getFunctionName() + "()";
    }
}
