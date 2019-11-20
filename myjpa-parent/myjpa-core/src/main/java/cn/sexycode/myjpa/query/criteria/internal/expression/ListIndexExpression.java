/*
 * Hibernate, Relational Persistence for Idiomatic Java
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later.
 * See the lgpl.txt file in the root directory or <http://www.gnu.org/licenses/lgpl-2.1.html>.
 */
package cn.sexycode.myjpa.query.criteria.internal.expression;

import cn.sexycode.myjpa.query.criteria.internal.CriteriaBuilderImpl;
import cn.sexycode.myjpa.query.criteria.internal.ParameterRegistry;
import cn.sexycode.myjpa.query.criteria.internal.PathImplementor;
import cn.sexycode.myjpa.query.criteria.internal.compile.RenderingContext;

import javax.persistence.metamodel.ListAttribute;
import java.io.Serializable;

/**
 * An expression for referring to the index of a list.
 *
 * @author Steve Ebersole
 */
public class ListIndexExpression extends ExpressionImpl<Integer> implements Serializable {
    private final PathImplementor origin;

    private final ListAttribute listAttribute;

    public ListIndexExpression(CriteriaBuilderImpl criteriaBuilder, PathImplementor origin,
            ListAttribute listAttribute) {
        super(criteriaBuilder, Integer.class);
        this.origin = origin;
        this.listAttribute = listAttribute;
    }

    public ListAttribute getListAttribute() {
        return listAttribute;
    }

    public void registerParameters(ParameterRegistry registry) {
        // nothing to do
    }

    public String render(RenderingContext renderingContext) {
        return "index(" + origin.getPathIdentifier() + ")";
    }
}