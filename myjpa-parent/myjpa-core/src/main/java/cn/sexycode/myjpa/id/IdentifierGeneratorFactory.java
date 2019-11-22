package cn.sexycode.myjpa.id;
import cn.sexycode.myjpa.sql.dialect.Dialect;
import cn.sexycode.myjpa.sql.type.Type;

import java.util.Properties;


/**
 * Contract for a <tt>factory</tt> of {@link IdentifierGenerator} instances.
 *
 * @author Steve Ebersole
 */
public interface IdentifierGeneratorFactory {
	/**
	 * Get the dialect.
	 *
	 * @return the dialect
	 */
	public Dialect getDialect();

	/**
	 * Allow injection of the dialect to use.
	 *
	 * @param dialect The dialect
	 *
	 * @deprecated The intention is that Dialect should be required to be specified up-front and it would then get
	 * ctor injected.
	 */
	@Deprecated
	public void setDialect(Dialect dialect);

	/**
	 * Given a strategy, retrieve the appropriate identifier generator instance.
	 *
	 * @param strategy The generation strategy.
	 * @param type The mapping type for the identifier values.
	 * @param config Any configuraion properties given in the generator mapping.
	 *
	 * @return The appropriate generator instance.
	 */
	public IdentifierGenerator createIdentifierGenerator(String strategy, Type type, Properties config);

	/**
	 * Retrieve the class that will be used as the {@link IdentifierGenerator} for the given strategy.
	 *
	 * @param strategy The strategy
	 * @return The generator class.
	 */
	public Class getIdentifierGeneratorClass(String strategy);
}
