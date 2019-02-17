/*
 * Hibernate, Relational Persistence for Idiomatic Java
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later.
 * See the lgpl.txt file in the root directory or <http://www.gnu.org/licenses/lgpl-2.1.html>.
 */
package cn.sexycode.mybatis.jpa.mapping;


import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import java.lang.reflect.Array;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author max
 */
public interface ValueVisitor {


    /**
     * @param bag
     */

    /**
     * @param list
     */
    Object accept(List list);

    Object accept(Array list);

    /**
     * @param map
     */
    Object accept(Map map);

    /**
     * @param many
     */
    Object accept(OneToMany many);

    /**
     * @param set
     */
    Object accept(Set set);


    /**
     * @param value
     */
    Object accept(SimpleValue value);

}
