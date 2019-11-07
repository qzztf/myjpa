package cn.sexycode.myjpa.query;

import cn.sexycode.myjpa.session.SessionFactory;

import javax.persistence.Query;

/**
 * @author Steve Ebersole
 */
public class QueryImpl<R> implements Query<R> {
	private final String queryString;

	private final QueryParameterBindingsImpl queryParameterBindings;

	public QueryImpl(
			SessionFactory producer,
			ParameterMetadata parameterMetadata,
			String queryString) {
		super( producer, parameterMetadata );
		this.queryString = queryString;
		this.queryParameterBindings = QueryParameterBindingsImpl.from(
				parameterMetadata,
				producer.getFactory(),
				producer.isQueryParametersValidationEnabled()
		);
	}

	@Override
	protected QueryParameterBindings getQueryParameterBindings() {
		return queryParameterBindings;
	}

	@Override
	public String getQueryString() {
		return queryString;
	}

	@Override
	protected boolean isNativeQuery() {
		return false;
	}

	@Override
	public Type[] getReturnTypes() {
		return getProducer().getFactory().getReturnTypes( queryString );
	}

	@Override
	public String[] getReturnAliases() {
		return getProducer().getFactory().getReturnAliases( queryString );
	}

	@Override
	public Query setEntity(int position, Object val) {
		return setParameter( position, val, getProducer().getFactory().getTypeHelper().entity( resolveEntityName( val ) ) );
	}

	@Override
	public Query setEntity(String name, Object val) {
		return setParameter( name, val, getProducer().getFactory().getTypeHelper().entity( resolveEntityName( val ) ) );
	}
}
