/*
 * Hibernate, Relational Persistence for Idiomatic Java
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later.
 * See the lgpl.txt file in the root directory or <http://www.gnu.org/licenses/lgpl-2.1.html>.
 */
package cn.sexycode.mybatis.jpa.mapping;

import cn.sexycode.sql.util.EqualsHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sound.midi.Sequence;
import java.util.Collection;
import java.util.Map;
import java.util.TreeMap;

/**
 * Represents a namespace (named schema/catalog pair) with a Database and manages objects defined within.
 *
 * @author Steve Ebersole
 */
public class Namespace {
    private static final Logger log = LoggerFactory.getLogger(Namespace.class);

    private final Database database;
    private final Name name;
//	private final Name physicalName;

    private Map<Identifier, Table> tables = new TreeMap<>();
    private Map<Identifier, Sequence> sequences = new TreeMap<Identifier, Sequence>();

    public Namespace(Database database, Name name) {
        this.database = database;
        this.name = name;

		/*this.physicalName = new Name(
				database.getPhysicalNamingStrategy()
						.toPhysicalCatalogName( name.getCatalog(), database.getJdbcEnvironment() ),
				database.getPhysicalNamingStrategy()
						.toPhysicalSchemaName( name.getSchema(), database.getJdbcEnvironment() )
		);*/

        log.debug(
                "Created database namespace [logicalName=%s, physicalName=%s]",
                name.toString(),
//				physicalName.toString()
                null
        );
    }

    public Name getName() {
        return name;
    }

    public Name getPhysicalName() {
        return null;
    }

    public Collection<Table> getTables() {
        return tables.values();
    }

    /**
     * Returns the table with the specified logical table name.
     *
     * @param logicalTableName - the logical name of the table
     * @return the table with the specified table name,
     * or null if there is no table with the specified
     * table name.
     */
    public Table locateTable(Identifier logicalTableName) {
        return tables.get(logicalTableName);
    }


    public Sequence locateSequence(Identifier name) {
        return sequences.get(name);
    }

    @Override
    public String toString() {
        return "Schema" + "{name=" + name + '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        final Namespace that = (Namespace) o;
        return EqualsHelper.equals(this.name, that.name);
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }

    public Iterable<Sequence> getSequences() {
        return sequences.values();
    }

    public static class Name implements Comparable<Name> {
        private final Identifier catalog;
        private final Identifier schema;

        public Name(Identifier catalog, Identifier schema) {
            this.schema = schema;
            this.catalog = catalog;
        }

        public Identifier getCatalog() {
            return catalog;
        }

        public Identifier getSchema() {
            return schema;
        }

        @Override
        public String toString() {
            return "Name" + "{catalog=" + catalog + ", schema=" + schema + '}';
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }

            final Name that = (Name) o;

            return EqualsHelper.equals(this.catalog, that.catalog)
                    && EqualsHelper.equals(this.schema, that.schema);
        }

        @Override
        public int hashCode() {
            int result = catalog != null ? catalog.hashCode() : 0;
            result = 31 * result + (schema != null ? schema.hashCode() : 0);
            return result;
        }

        @Override
        public int compareTo(Name that) {
            // per Comparable, the incoming Name cannot be null.  However, its catalog/schema might be
            // so we need to account for that.
            int catalogCheck = ComparableHelper.compare(this.getCatalog(), that.getCatalog());
            if (catalogCheck != 0) {
                return catalogCheck;
            }

            return ComparableHelper.compare(this.getSchema(), that.getSchema());
        }
    }

    public static class ComparableHelper {
        public static <T extends Comparable<T>> int compare(T first, T second) {
            if (first == null) {
                if (second == null) {
                    return 0;
                } else {
                    return 1;
                }
            } else {
                if (second == null) {
                    return -1;
                } else {
                    return first.compareTo(second);
                }
            }
        }
    }
}
