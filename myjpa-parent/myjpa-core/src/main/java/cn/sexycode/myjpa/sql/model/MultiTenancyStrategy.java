package cn.sexycode.myjpa.sql.model;

import cn.sexycode.myjpa.sql.Environment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Locale;
import java.util.Map;

/**
 * Describes the methods for multi-tenancy understood by Hibernate.
 *
 */
public enum MultiTenancyStrategy {
	/**
	 * Multi-tenancy implemented by use of discriminator columns.
	 */
	DISCRIMINATOR,
	/**
	 * Multi-tenancy implemented as separate schemas.
	 */
	SCHEMA,
	/**
	 * Multi-tenancy implemented as separate databases.
	 */
	DATABASE,
	/**
	 * No multi-tenancy.
	 */
	NONE;

	private static final Logger LOGGER = LoggerFactory.getLogger(MultiTenancyStrategy.class);

	/**
	 * Does this strategy indicate a requirement for the specialized
	 * {@link org.hibernate.engine.jdbc.connections.spi.MultiTenantConnectionProvider}, rather than the
	 * traditional {@link org.hibernate.engine.jdbc.connections.spi.ConnectionProvider}?
	 *
	 * @return {@code true} indicates a MultiTenantConnectionProvider is required; {@code false} indicates it is not.
	 */
	public boolean requiresMultiTenantConnectionProvider() {
		return this == DATABASE || this == SCHEMA;
	}

	/**
	 * Extract the MultiTenancyStrategy from the setting map.
	 *
	 * @param properties The map of settings.
	 *
	 * @return The selected strategy.  {@link #NONE} is always the default.
	 */
	public static MultiTenancyStrategy determineMultiTenancyStrategy(Map properties) {
		final Object strategy = properties.get( Environment.MULTI_TENANT );
		if ( strategy == null ) {
			return MultiTenancyStrategy.NONE;
		}

		if ( MultiTenancyStrategy.class.isInstance( strategy ) ) {
			return (MultiTenancyStrategy) strategy;
		}

		final String strategyName = strategy.toString();
		try {
			return MultiTenancyStrategy.valueOf( strategyName.toUpperCase(Locale.ROOT) );
		}
		catch ( RuntimeException e ) {
			LOGGER.warn( "Unknown multi tenancy strategy [ " +strategyName +" ], using MultiTenancyStrategy.NONE." );
			return MultiTenancyStrategy.NONE;
		}
	}
}
