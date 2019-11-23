package cn.sexycode.myjpa.id;

import cn.sexycode.myjpa.MyjpaException;
import cn.sexycode.myjpa.session.Session;

import java.io.Serializable;
import javax.persistence.GeneratedValue;


/**
 * The general contract between a class that generates unique
 * identifiers and the <tt>Session</tt>. It is not intended that
 * this interface ever be exposed to the application. It <b>is</b>
 * intended that users implement this interface to provide
 * custom identifier generation strategies.<br>
 * <br>
 * Implementors should provide a public default constructor.<br>
 * <br>
 * Implementations that accept configuration parameters should
 * also implement <tt>Configurable</tt>.
 * <br>
 * Implementors <em>must</em> be thread-safe
 *
 * @author Gavin King
 *
 * @see PersistentIdentifierGenerator
 * @see Configurable
 */
public interface IdentifierGenerator {
	/**
	 * The configuration parameter holding the entity name
	 */
	String ENTITY_NAME = "entity_name";

	/**
	 * The configuration parameter holding the JPA entity name
	 */
	String JPA_ENTITY_NAME = "jpa_entity_name";

	/**
	 * Used as a key to pass the name used as {@link GeneratedValue#generator()} to  the
	 * {@link IdentifierGenerator} as it is configured.
	 */
	String GENERATOR_NAME = "GENERATOR_NAME";

	/**
	 * Generate a new identifier.
	 *
	 * @param session The session from which the request originates
	 * @param object the entity or collection (idbag) for which the id is being generated
	 *
	 * @return a new identifier
	 *
	 * @throws HibernateException Indicates trouble generating the identifier
	 */
	Serializable generate(Session session, Object object) throws MyjpaException;

	/**
	 * Check if JDBC batch inserts are supported.
	 *
	 * @return JDBC batch inserts are supported.
	 */
	default boolean supportsJdbcBatchInserts() {
		return true;
	}
}