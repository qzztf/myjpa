/*
 * Hibernate, Relational Persistence for Idiomatic Java
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later.
 * See the lgpl.txt file in the root directory or <http://www.gnu.org/licenses/lgpl-2.1.html>.
 */
package cn.sexycode.mybatis.jpa.mapping;

import cn.sexycode.mybatis.jpa.binding.MappingException;
import cn.sexycode.sql.dialect.Dialect;
import cn.sexycode.sql.type.Mapping;

/**
 * A relational object which may be created using DDL
 *
 * @author Gavin King
 */
public interface RelationalModel {
    public String sqlCreateString(Dialect dialect, Mapping p, String defaultCatalog, String defaultSchema) throws MappingException;

    public String sqlDropString(Dialect dialect, String defaultCatalog, String defaultSchema);
}
