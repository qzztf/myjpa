/*
 * Hibernate, Relational Persistence for Idiomatic Java
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later.
 * See the lgpl.txt file in the root directory or <http://www.gnu.org/licenses/lgpl-2.1.html>.
 */
package cn.sexycode.sql.dialect.function;

import cn.sexycode.sql.QueryException;
import cn.sexycode.sql.type.Mapping;
import cn.sexycode.sql.type.Type;

import java.util.List;

/**
 * A function which takes no arguments
 *
 * @author Michi
 */
public class NoArgSQLFunction implements SQLFunction {
    private Type returnType;
    private boolean hasParenthesesIfNoArguments;
    private String name;

    /**
     * Constructs a NoArgSQLFunction
     *
     * @param name       The function name
     * @param returnType The function return type
     */
    public NoArgSQLFunction(String name, Type returnType) {
        this(name, returnType, true);
    }

    /**
     * Constructs a NoArgSQLFunction
     *
     * @param name                        The function name
     * @param returnType                  The function return type
     * @param hasParenthesesIfNoArguments Does the function call need parenthesis if there are no arguments?
     */
    public NoArgSQLFunction(String name, Type returnType, boolean hasParenthesesIfNoArguments) {
        this.returnType = returnType;
        this.hasParenthesesIfNoArguments = hasParenthesesIfNoArguments;
        this.name = name;
    }

    @Override
    public boolean hasArguments() {
        return false;
    }

    @Override
    public boolean hasParenthesesIfNoArguments() {
        return hasParenthesesIfNoArguments;
    }

    @Override
    public Type getReturnType(Type argumentType, Mapping mapping) throws QueryException {
        return returnType;
    }

    @Override
    public String render(Type argumentType, List args) throws QueryException {
        if (args.size() > 0) {
            throw new QueryException("function takes no arguments: " + name);
        }
        return hasParenthesesIfNoArguments ? name + "()" : name;
    }

    protected String getName() {
        return name;
    }
}
