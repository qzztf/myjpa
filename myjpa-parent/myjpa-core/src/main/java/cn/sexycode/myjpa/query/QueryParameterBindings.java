package cn.sexycode.myjpa.query;

import java.util.Map;

import cn.sexycode.myjpa.session.Session;

import javax.persistence.Parameter;

/**
 * @author Steve Ebersole
 */
public interface QueryParameterBindings {
	boolean isBound(Parameter parameter);

	<T> QueryParameterBinding<T> getBinding(Parameter<T> parameter);
	<T> QueryParameterBinding<T> getBinding(String name);
	<T> QueryParameterBinding<T> getBinding(int position);

	void verifyParametersBound(boolean callable);
	String expandListValuedParameters(String queryString, Session producer);

	<T> QueryParameterListBinding<T> getQueryParameterListBinding(Parameter<T> parameter);
	<T> QueryParameterListBinding<T> getQueryParameterListBinding(String name);
	<T> QueryParameterListBinding<T> getQueryParameterListBinding(int position);

	Type[] collectPositionalBindTypes();
	Object[] collectPositionalBindValues();
	Map<String,TypedValue> collectNamedParameterBindings();
}
