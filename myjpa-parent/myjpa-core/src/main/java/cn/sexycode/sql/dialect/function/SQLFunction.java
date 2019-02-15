/*
 * Hibernate, Relational Persistence for Idiomatic Java
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later.
 * See the lgpl.txt file in the root directory or <http://www.gnu.org/licenses/lgpl-2.1.html>.
 */
package cn.sexycode.sql.dialect.function;


import cn.sexycode.sql.type.Mapping;
import cn.sexycode.sql.type.Type;

import java.util.List;

/**
 * Provides support routines for the HQL functions as used
 * in the various SQL Dialects
 * <p>
 * Provides an interface for supporting various HQL functions that are
 * translated to SQL. The Dialect and its sub-classes use this interface to
 * provide details required for processing of the function.
 *
 * @author David Channon
 * @author Steve Ebersole
 */
public interface SQLFunction {
    /**
     * Does this function have any arguments?
     *
     * @return True if the function expects to have parameters; false otherwise.
     */
    boolean hasArguments();

    /**
     * If there are no arguments, are parentheses required?
     *
     * @return True if a no-arg call of this function requires parentheses.
     */
    boolean hasParenthesesIfNoArguments();

    /**
     * The return type of the function.  May be either a concrete type which is preset, or variable depending upon
     * the type of the first function argument.
     * <p/>
     * Note, the 'firstArgumentType' parameter should match the one passed into {@link #render}
     *
     * @param firstArgumentType The type of the first argument
     * @param mapping           The mapping source.
     * @return The type to be expected as a return.
     * @throws org.hibernate.QueryException Indicates an issue resolving the return type.
     */
    Type getReturnType(Type firstArgumentType, Mapping mapping);

    /**
     * Render the function call as SQL fragment.
     * <p/>
     * Note, the 'firstArgumentType' parameter should match the one passed into {@link #getReturnType}
     *
     * @param firstArgumentType The type of the first argument
     * @param arguments         The function arguments
     * @param factory           The SessionFactory
     * @return The rendered function call
     * @throws org.hibernate.QueryException Indicates a problem rendering the
     *                                      function call.
     */
    String render(Type firstArgumentType, List arguments);
}
