package cn.sexycode.myjpa.sql.model;

import cn.sexycode.myjpa.sql.dialect.Dialect;
import cn.sexycode.myjpa.sql.dialect.H2Dialect;
import cn.sexycode.myjpa.sql.jdbc.JdbcEnvironment;
import cn.sexycode.myjpa.sql.model.Identifier;
import cn.sexycode.myjpa.sql.model.Namespace;
import cn.sexycode.myjpa.sql.model.PhysicalNamingStrategy;
import cn.sexycode.myjpa.sql.model.PhysicalNamingStrategyStandardImpl;

import java.util.Map;
import java.util.TreeMap;

/**
 *
 */
public class Database {
    private final Dialect dialect;

    private final JdbcEnvironment environment;

    private Namespace implicitNamespace;

    private final Map<Namespace.Name, Namespace> namespaceMap = new TreeMap<Namespace.Name, Namespace>();

    public Database(JdbcEnvironment jdbcEnvironment) {

        this.environment = jdbcEnvironment;
        this.dialect = determineDialect(jdbcEnvironment);
/*
		this.implicitNamespace = makeNamespace(
				new Namespace.Name(
						toIdentifier( buildingOptions.getMappingDefaults().getImplicitCatalogName() ),
						toIdentifier( buildingOptions.getMappingDefaults().getImplicitSchemaName() )
				)
		);*/
    }

    private static Dialect determineDialect(JdbcEnvironment jdbcEnvironment) {
        if (jdbcEnvironment != null) {
            final Dialect dialect = jdbcEnvironment.getDialect();
            if (dialect != null) {
                return dialect;
            }
        }

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

    public JdbcEnvironment getEnvironment() {
        return environment;
    }

    /**
     * Wrap the raw name of a database object in it's Identifier form accounting for quoting from
     * any of:<ul>
     * <li>explicit quoting in the name itself</li>
     * <li>global request to quote all identifiers</li>
     * </ul>
     * <p/>
     * NOTE : quoting from database keywords happens only when building physical identifiers
     *
     * @param text The raw object name
     * @return The wrapped Identifier form
     */
    public Identifier toIdentifier(String text) {
        return text == null ? null :/* environment.getIdentifierHelper().toIdentifier( text )*/ null;
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

    public Namespace adjustDefaultNamespace(String implicitCatalogName, String implicitSchemaName) {
        return adjustDefaultNamespace(toIdentifier(implicitCatalogName), toIdentifier(implicitSchemaName));
    }

    public PhysicalNamingStrategy getPhysicalNamingStrategy() {
        return new PhysicalNamingStrategyStandardImpl();
    }
}
