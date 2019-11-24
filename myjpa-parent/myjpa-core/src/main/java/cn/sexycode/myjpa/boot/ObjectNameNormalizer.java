package cn.sexycode.myjpa.boot;

import cn.sexycode.myjpa.binding.MetadataBuildingContext;
import cn.sexycode.myjpa.sql.model.Database;
import cn.sexycode.myjpa.sql.model.Identifier;
import cn.sexycode.util.core.str.StringUtils;

/**
 * Provides centralized normalization of how database object names are handled.
 *
 * @author Steve Ebersole
 */
public abstract class ObjectNameNormalizer {
    private Database database;

    /**
     * Normalizes the quoting of identifiers.
     * <p/>
     * This implements the rules set forth in JPA 2 (section "2.13 Naming of Database Objects") which
     * states that the double-quote (") is the character which should be used to denote a <tt>quoted
     * identifier</tt>.  Here, we handle recognizing that and converting it to the more elegant
     * bactick (`) approach used in Hibernate..  Additionally we account for applying what JPA2 terms
     * "globally quoted identifiers".
     *
     * @param identifierText The identifier to be quoting-normalized.
     * @return The identifier accounting for any quoting that need be applied.
     */
    public Identifier normalizeIdentifierQuoting(String identifierText) {
        return database().toIdentifier(identifierText);
    }

    protected Database database() {
        if (database == null) {
            database = getBuildingContext().getMetadataCollector().getDatabase();
        }
        return database;
    }

    public Identifier normalizeIdentifierQuoting(Identifier identifier) {
		return getBuildingContext().getMetadataCollector()
				.getDatabase()
				.getEnvironment()
				.getIdentifierHelper()
				.normalizeQuoting( identifier );
    }

    /**
     * Normalizes the quoting of identifiers.  This form returns a String rather than an Identifier
     * to better work with the legacy code in {@link org.hibernate.mapping}
     *
     * @param identifierText The identifier to be quoting-normalized.
     * @return The identifier accounting for any quoting that need be applied.
     */
    public String normalizeIdentifierQuotingAsString(String identifierText) {
        final Identifier identifier = normalizeIdentifierQuoting(identifierText);
        if (identifier == null) {
            return null;
        }
        return identifier.render(database().getDialect());
    }

    public String toDatabaseIdentifierText(String identifierText) {
        return database().getDialect().quote(normalizeIdentifierQuotingAsString(identifierText));
    }

    /**
     * Determine the logical name give a (potentially {@code null}/empty) explicit name.
     *
     * @param explicitName         The explicit, user-supplied name
     * @param namingStrategyHelper The naming strategy helper.
     * @return The logical name
     */
    public Identifier determineLogicalName(String explicitName, NamingStrategyHelper namingStrategyHelper) {
        Identifier logicalName;
        if (StringUtils.isEmpty(explicitName)) {
            logicalName = namingStrategyHelper.determineImplicitName(getBuildingContext());
        } else {
            logicalName = namingStrategyHelper.handleExplicitName(explicitName, getBuildingContext());
        }
        logicalName = getBuildingContext().getMetadataCollector().getDatabase().getEnvironment().getIdentifierHelper()
                .normalizeQuoting(logicalName);

        return logicalName;
    }

    /**
     * Intended only for use in handling quoting requirements for {@code column-definition}
     * as defined by {@link javax.persistence.Column#columnDefinition()},
     * {@link javax.persistence.JoinColumn#columnDefinition}, etc.  This method should not
     * be called in any other scenario.
     *
     * @param text The specified column definition
     * @return The name with global quoting applied
     */
    public String applyGlobalQuoting(String text) {
        return database().getEnvironment().getIdentifierHelper().applyGlobalQuoting(text)
                .render(database().getDialect());
    }

    /**
     * Access the contextual information related to the current process of building metadata.  Here,
     * that typically might be needed for accessing:<ul>
     * <li>{@link org.hibernate.boot.model.naming.ImplicitNamingStrategy}</li>
     * <li>{@link org.hibernate.boot.model.naming.PhysicalNamingStrategy}</li>
     * <li>{@link org.hibernate.boot.model.relational.Database}</li>
     * </ul>
     *
     * @return The current building context
     */
    protected abstract MetadataBuildingContext getBuildingContext();
}
