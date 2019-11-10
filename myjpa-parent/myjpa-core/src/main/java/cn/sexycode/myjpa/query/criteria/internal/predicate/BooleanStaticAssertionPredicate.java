/*
 * Hibernate, Relational Persistence for Idiomatic Java
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later.
 * See the lgpl.txt file in the root directory or <http://www.gnu.org/licenses/lgpl-2.1.html>.
 */
package cn.sexycode.myjpa.query.criteria.internal.predicate;

import cn.sexycode.myjpa.query.criteria.internal.CriteriaBuilderImpl;
import cn.sexycode.myjpa.query.criteria.internal.ParameterRegistry;
import cn.sexycode.myjpa.query.criteria.internal.compile.RenderingContext;

import java.io.Serializable;

/**
 * Predicate used to assert a static boolean condition.
 *
 * @author Steve Ebersole
 */
public class BooleanStaticAssertionPredicate extends AbstractSimplePredicate implements Serializable {
    private final Boolean assertedValue;

    public BooleanStaticAssertionPredicate(CriteriaBuilderImpl criteriaBuilder, Boolean assertedValue) {
        super(criteriaBuilder);
        this.assertedValue = assertedValue;
    }

    public Boolean getAssertedValue() {
        return assertedValue;
    }

    @Override
    public void registerParameters(ParameterRegistry registry) {
        // nada
    }

    @Override
    public String render(boolean isNegated, RenderingContext renderingContext) {
        boolean isTrue = getAssertedValue();
        if (isNegated) {
            isTrue = !isTrue;
        }
        return isTrue ? "1=1" : "0=1";
    }

}
