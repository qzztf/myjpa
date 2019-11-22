package cn.sexycode.myjpa.sql.mapping;

import cn.sexycode.myjpa.sql.mapping.Alias;
import cn.sexycode.util.core.str.StringUtils;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Abstract SQL case fragment renderer
 *
 */
public abstract class CaseFragment {
	public abstract String toFragmentString();

	protected String returnColumnName;

	protected Map cases = new LinkedHashMap();

	public CaseFragment setReturnColumnName(String returnColumnName) {
		this.returnColumnName = returnColumnName;
		return this;
	}

	public CaseFragment setReturnColumnName(String returnColumnName, String suffix) {
		return setReturnColumnName( new Alias(suffix).toAliasString(returnColumnName) );
	}

	public CaseFragment addWhenColumnNotNull(String alias, String columnName, String value) {
		cases.put( StringUtils.qualify( alias, columnName ), value );
		return this;
	}
}
