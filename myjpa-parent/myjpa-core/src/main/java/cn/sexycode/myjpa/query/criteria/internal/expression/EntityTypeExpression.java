package cn.sexycode.myjpa.query.criteria.internal.expression;

import cn.sexycode.myjpa.query.criteria.internal.CriteriaBuilderImpl;
import cn.sexycode.myjpa.query.criteria.internal.ParameterRegistry;
import cn.sexycode.myjpa.query.criteria.internal.compile.RenderingContext;

import java.io.Serializable;

/**
 * TODO : javadoc
 *
 * @author Steve Ebersole
 */
public class EntityTypeExpression<T> extends ExpressionImpl<T> implements Serializable {
    public EntityTypeExpression(CriteriaBuilderImpl criteriaBuilder, Class<T> javaType) {
        super(criteriaBuilder, javaType);
    }

    public void registerParameters(ParameterRegistry registry) {
        // nothign to do
    }

    public String render(RenderingContext renderingContext) {
        // todo : is it valid for this to get rendered into the query itself?
        throw new IllegalArgumentException("Unexpected call on EntityTypeExpression#render");
    }
}
