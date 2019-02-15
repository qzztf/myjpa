/*
 * Hibernate, Relational Persistence for Idiomatic Java
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later.
 * See the lgpl.txt file in the root directory or <http://www.gnu.org/licenses/lgpl-2.1.html>.
 */
package cn.sexycode.mybatis.jpa.mapping;


import cn.sexycode.sql.dialect.Dialect;
import cn.sexycode.sql.dialect.function.SQLFunctionRegistry;

/**
 * Models the commonality between a column and a formula (computed value).
 */
public interface Selectable {
    public String getAlias(Dialect dialect);

    public String getAlias(Dialect dialect, Table table);

    public boolean isFormula();

    public String getTemplate(Dialect dialect, SQLFunctionRegistry functionRegistry);

    public String getText(Dialect dialect);

    public String getText();
}
