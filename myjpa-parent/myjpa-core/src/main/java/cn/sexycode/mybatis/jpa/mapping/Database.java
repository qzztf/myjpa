/*
 * Hibernate, Relational Persistence for Idiomatic Java
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later.
 * See the lgpl.txt file in the root directory or <http://www.gnu.org/licenses/lgpl-2.1.html>.
 */
package cn.sexycode.mybatis.jpa.mapping;


import cn.sexycode.sql.JdbcEnvironment;
import cn.sexycode.sql.dialect.Dialect;
import cn.sexycode.sql.dialect.H2Dialect;

import java.util.Map;
import java.util.TreeMap;

/**
 * @author Steve Ebersole
 */
public class Database {
    private final Dialect dialect;
    private final MetadataBuildingOptions buildingOptions;
    private final JdbcEnvironment jdbcEnvironment;

    private Namespace implicitNamespace;

    private final Map<Namespace.Name, Namespace> namespaceMap = new TreeMap<Namespace.Name, Namespace>();

//	private List<InitCommand> initCommands;


    public Database(MetadataBuildingOptions buildingOptions, JdbcEnvironment jdbcEnvironment) {
        this.buildingOptions = buildingOptions;

        this.jdbcEnvironment = jdbcEnvironment;

        this.dialect = determineDialect(buildingOptions);

		/*this.implicitNamespace = makeNamespace(
				new Namespace.Name(
						toIdentifier( buildingOptions.getMappingDefaults().getImplicitCatalogName() ),
						toIdentifier( buildingOptions.getMappingDefaults().getImplicitSchemaName() )
				)
		);*/
    }

    private static Dialect determineDialect(MetadataBuildingOptions buildingOptions) {
		/*final Dialect dialect = buildingOptions.getServiceRegistry().getService( JdbcServices.class ).getDialect();
		if ( dialect != null ) {
			return dialect;
		}*/

        // Use H2 dialect as default
        return new H2Dialect();
    }

    private Namespace makeNamespace(Namespace.Name name) {
        Namespace namespace;
        namespace = new Namespace(this, name);
        namespaceMap.put(name, namespace);
        return namespace;
    }


    public Dialect getDialect() {
        return dialect;
    }

    public JdbcEnvironment getJdbcEnvironment() {
        return jdbcEnvironment;
    }


    public Iterable<Namespace> getNamespaces() {
        return namespaceMap.values();
    }

    public Namespace getDefaultNamespace() {
        return implicitNamespace;
    }

    public Namespace locateNamespace(Identifier catalogName, Identifier schemaName) {
        if (catalogName == null && schemaName == null) {
            return getDefaultNamespace();
        }

        final Namespace.Name name = new Namespace.Name(catalogName, schemaName);
        Namespace namespace = namespaceMap.get(name);
        if (namespace == null) {
            namespace = makeNamespace(name);
        }
        return namespace;
    }

    public Namespace adjustDefaultNamespace(Identifier catalogName, Identifier schemaName) {
        final Namespace.Name name = new Namespace.Name(catalogName, schemaName);
        if (implicitNamespace.getName().equals(name)) {
            return implicitNamespace;
        }

        Namespace namespace = namespaceMap.get(name);
        if (namespace == null) {
            namespace = makeNamespace(name);
        }
        implicitNamespace = namespace;
        return implicitNamespace;
    }


}
