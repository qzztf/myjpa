/*
 * Hibernate, Relational Persistence for Idiomatic Java
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later.
 * See the lgpl.txt file in the root directory or <http://www.gnu.org/licenses/lgpl-2.1.html>.
 */
package cn.sexycode.mybatis.jpa.mapping;


import cn.sexycode.mybatis.jpa.binding.MappingException;
import cn.sexycode.sql.type.Mapping;
import cn.sexycode.sql.type.Type;

import javax.imageio.spi.ServiceRegistry;
import java.io.Serializable;
import java.util.Iterator;

/**
 * A value is anything that is persisted by value, instead of
 * by reference. It is essentially a Hibernate Type, together
 * with zero or more columns. Values are wrapped by things with
 * higher level semantics, for example properties, collections,
 * classes.
 *
 * @author Gavin King
 */
public interface Value extends Serializable {
    int getColumnSpan();

    Iterator<Selectable> getColumnIterator();

    Type getType() throws MappingException;


    Table getTable();

    boolean hasFormula();

    boolean isAlternateUniqueKey();

    boolean isNullable();

    boolean[] getColumnUpdateability();

    boolean[] getColumnInsertability();

    void createForeignKey() throws MappingException;

    boolean isSimpleValue();

    boolean isValid(Mapping mapping) throws MappingException;

//    public void setTypeUsingReflection(String className, String propertyName) throws MappingException;

    Object accept(ValueVisitor visitor);

}
