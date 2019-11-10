/*
 * Hibernate, Relational Persistence for Idiomatic Java
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later.
 * See the lgpl.txt file in the root directory or <http://www.gnu.org/licenses/lgpl-2.1.html>.
 */
package cn.sexycode.myjpa.query.criteria.internal.expression.function;

import cn.sexycode.myjpa.query.criteria.internal.CriteriaBuilderImpl;

import java.io.Serializable;
import java.sql.Date;

/**
 * Models the ANSI SQL <tt>CURRENT_DATE</tt> function.
 *
 * @author Steve Ebersole
 */
public class CurrentDateFunction extends BasicFunctionExpression<Date> implements Serializable {
    public static final String NAME = "current_date";

    public CurrentDateFunction(CriteriaBuilderImpl criteriaBuilder) {
        super(criteriaBuilder, Date.class, NAME);
    }
}
