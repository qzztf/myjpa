/*
 * Hibernate, Relational Persistence for Idiomatic Java
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later.
 * See the lgpl.txt file in the root directory or <http://www.gnu.org/licenses/lgpl-2.1.html>.
 */
package cn.sexycode.mybatis.jpa.mapping;


import cn.sexycode.sql.dialect.Dialect;
import cn.sexycode.sql.dialect.function.SQLFunctionRegistry;
import cn.sexycode.sql.sql.Template;

import java.io.Serializable;

/**
 * A formula is a derived column value
 *
 * @author Gavin King
 */
public class Formula implements Selectable, Serializable {
    private static int formulaUniqueInteger;

    private String formula;
    private int uniqueInteger;

    public Formula() {
        uniqueInteger = formulaUniqueInteger++;
    }

    public Formula(String formula) {
        this();
        this.formula = formula;
    }

    @Override
    public String getTemplate(Dialect dialect, SQLFunctionRegistry functionRegistry) {
        return Template.renderWhereStringTemplate(formula, dialect, functionRegistry);
    }

    @Override
    public String getText(Dialect dialect) {
        return getFormula();
    }

    @Override
    public String getText() {
        return getFormula();
    }

    @Override
    public String getAlias(Dialect dialect) {
        return "formula" + Integer.toString(uniqueInteger) + '_';
    }

    @Override
    public String getAlias(Dialect dialect, Table table) {
        return getAlias(dialect);
    }

    public String getFormula() {
        return formula;
    }

    public void setFormula(String string) {
        formula = string;
    }

    @Override
    public boolean isFormula() {
        return true;
    }

    @Override
    public String toString() {
        return this.getClass().getName() + "( " + formula + " )";
    }
}
