/*
 * Hibernate, Relational Persistence for Idiomatic Java
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later.
 * See the lgpl.txt file in the root directory or <http://www.gnu.org/licenses/lgpl-2.1.html>.
 */
package cn.sexycode.myjpa.query.criteria.internal.expression;

import java.io.Serializable;

import cn.sexycode.myjpa.query.criteria.internal.CriteriaBuilderImpl;
import cn.sexycode.myjpa.query.criteria.internal.ParameterRegistry;
import cn.sexycode.myjpa.query.criteria.internal.compile.RenderingContext;
import cn.sexycode.myjpa.query.criteria.internal.path.PluralAttributePath;

/**
 * Represents a "size of" expression in regards to a persistent collection; the implication is
 * that of a subquery.
 *
 * @author Steve Ebersole
 */
public class SizeOfPluralAttributeExpression extends ExpressionImpl<Integer> implements Serializable {
    private final PluralAttributePath path;

    public SizeOfPluralAttributeExpression(CriteriaBuilderImpl criteriaBuilder, PluralAttributePath path) {
        super(criteriaBuilder, Integer.class);
        this.path = path;
    }

    /**
     * @deprecated Use {@link #getPluralAttributePath()} instead
     */
    @Deprecated
    public PluralAttributePath getCollectionPath() {
        return path;
    }

    public PluralAttributePath getPluralAttributePath() {
        return path;
    }

    public void registerParameters(ParameterRegistry registry) {
        // nothing to do
    }

    public String render(RenderingContext renderingContext) {
        return "size(" + getPluralAttributePath().render(renderingContext) + ")";
    }
}
