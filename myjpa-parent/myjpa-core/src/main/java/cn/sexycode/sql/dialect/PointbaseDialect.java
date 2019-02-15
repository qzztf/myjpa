/*
 * Hibernate, Relational Persistence for Idiomatic Java
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later.
 * See the lgpl.txt file in the root directory or <http://www.gnu.org/licenses/lgpl-2.1.html>.
 */
package cn.sexycode.sql.dialect;


import java.sql.Types;

/**
 * A Dialect for Pointbase.
 *
 * @author Ed Mackenzie
 */
public class PointbaseDialect extends AbstractDialect {
    /**
     * Creates new PointbaseDialect
     */
    public PointbaseDialect() {
        super();
        //no pointbase BIT
        registerColumnType(Types.BIT, "smallint");
        registerColumnType(Types.BIGINT, "bigint");
        registerColumnType(Types.SMALLINT, "smallint");
        //no pointbase TINYINT
        registerColumnType(Types.TINYINT, "smallint");
        registerColumnType(Types.INTEGER, "integer");
        registerColumnType(Types.CHAR, "char(1)");
        registerColumnType(Types.VARCHAR, "varchar($l)");
        registerColumnType(Types.FLOAT, "float");
        registerColumnType(Types.DOUBLE, "double precision");
        registerColumnType(Types.DATE, "date");
        registerColumnType(Types.TIME, "time");
        registerColumnType(Types.TIMESTAMP, "timestamp");
        //the BLOB type requires a size arguement - this defaults to
        //bytes - no arg defaults to 1 whole byte!
        //other argument mods include K - kilobyte, M - megabyte, G - gigabyte.
        //refer to the PBdevelopers guide for more info.
        registerColumnType(Types.VARBINARY, "blob($l)");
        registerColumnType(Types.NUMERIC, "numeric($p,$s)");
    }

    @Override
    public String getAddColumnString() {
        return "add";
    }

    @Override
    public boolean dropConstraints() {
        return false;
    }

    @Override
    public String getCascadeConstraintsString() {
        return " cascade";
    }

    @Override
    public String getForUpdateString() {
        return "";
    }

}
