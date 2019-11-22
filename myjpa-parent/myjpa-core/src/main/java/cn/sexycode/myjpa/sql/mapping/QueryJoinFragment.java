package cn.sexycode.myjpa.sql.mapping;

import cn.sexycode.myjpa.sql.dialect.Dialect;
import cn.sexycode.myjpa.sql.mapping.JoinFragment;
import cn.sexycode.myjpa.sql.mapping.JoinType;
import cn.sexycode.util.core.str.StringUtils;

/**
 * A join that appears in a translated HQL query
 *
 */
public class QueryJoinFragment extends cn.sexycode.myjpa.sql.mapping.JoinFragment {

	private StringBuilder afterFrom = new StringBuilder();
	private StringBuilder afterWhere = new StringBuilder();
	private Dialect dialect;
	private boolean useThetaStyleInnerJoins;

	public QueryJoinFragment(Dialect dialect, boolean useThetaStyleInnerJoins) {
		this.dialect = dialect;
		this.useThetaStyleInnerJoins = useThetaStyleInnerJoins;
	}

	@Override
    public void addJoin(String tableName, String alias, String[] fkColumns, String[] pkColumns, cn.sexycode.myjpa.sql.mapping.JoinType joinType) {
		addJoin( tableName, alias, alias, fkColumns, pkColumns, joinType, null );
	}

	@Override
	public void addJoin(String tableName, String alias, String[] fkColumns, String[] pkColumns, cn.sexycode.myjpa.sql.mapping.JoinType joinType, String on) {
		addJoin( tableName, alias, alias, fkColumns, pkColumns, joinType, on );
	}

	public void addJoin(String tableName, String alias, String[][] fkColumns, String[] pkColumns, cn.sexycode.myjpa.sql.mapping.JoinType joinType) {
		addJoin( tableName, alias, alias, fkColumns, pkColumns, joinType, null );
	}

	@Override
	public void addJoin(String tableName, String alias, String[][] fkColumns, String[] pkColumns, cn.sexycode.myjpa.sql.mapping.JoinType joinType, String on) {
		addJoin( tableName, alias, alias, fkColumns, pkColumns, joinType, on );
	}

	private void addJoin(String tableName, String alias, String concreteAlias, String[] fkColumns, String[] pkColumns, cn.sexycode.myjpa.sql.mapping.JoinType joinType, String on) {
		if ( !useThetaStyleInnerJoins || joinType != cn.sexycode.myjpa.sql.mapping.JoinType.INNER_JOIN ) {
			cn.sexycode.myjpa.sql.mapping.JoinFragment jf = dialect.createOuterJoinFragment();
			jf.addJoin( tableName, alias, fkColumns, pkColumns, joinType, on );
			addFragment( jf );
		}
		else {
			addCrossJoin( tableName, alias );
			addCondition( concreteAlias, fkColumns, pkColumns );
			addCondition( on );
		}
	}

	private void addJoin(String tableName, String alias, String concreteAlias, String[][] fkColumns, String[] pkColumns, cn.sexycode.myjpa.sql.mapping.JoinType joinType, String on) {
		if ( !useThetaStyleInnerJoins || joinType != JoinType.INNER_JOIN ) {
			cn.sexycode.myjpa.sql.mapping.JoinFragment jf = dialect.createOuterJoinFragment();
			jf.addJoin( tableName, alias, fkColumns, pkColumns, joinType, on );
			addFragment( jf );
		}
		else {
			addCrossJoin( tableName, alias );
			addCondition( concreteAlias, fkColumns, pkColumns );
			addCondition( on );
		}
	}

	@Override
	public String toFromFragmentString() {
		return afterFrom.toString();
	}

	@Override
	public String toWhereFragmentString() {
		return afterWhere.toString();
	}

	@Override
	public void addJoins(String fromFragment, String whereFragment) {
		afterFrom.append( fromFragment );
		afterWhere.append( whereFragment );
	}

	@Override
	public JoinFragment copy() {
		QueryJoinFragment copy = new QueryJoinFragment( dialect, useThetaStyleInnerJoins );
		copy.afterFrom = new StringBuilder( afterFrom.toString() );
		copy.afterWhere = new StringBuilder( afterWhere.toString() );
		return copy;
	}

	public void addCondition(String alias, String[] columns, String condition) {
		for ( int i = 0; i < columns.length; i++ ) {
			afterWhere.append( " and " )
					.append( alias )
					.append( '.' )
					.append( columns[i] )
					.append( condition );
		}
	}


	@Override
	public void addCrossJoin(String tableName, String alias) {
		afterFrom.append( ", " )
				.append( tableName )
				.append( ' ' )
				.append( alias );
	}

	@Override
	public void addCondition(String alias, String[] fkColumns, String[] pkColumns) {
		for ( int j = 0; j < fkColumns.length; j++ ) {
			afterWhere.append( " and " )
					.append( fkColumns[j] )
					.append( '=' )
					.append( alias )
					.append( '.' )
					.append( pkColumns[j] );
		}
	}

	public void addCondition(String alias, String[][] fkColumns, String[] pkColumns) {
		afterWhere.append( " and " );
		if ( fkColumns.length > 1 ) {
			afterWhere.append( "(" );
		}
		for ( int i = 0; i < fkColumns.length; i++ ) {
			for ( int j = 0; j < fkColumns[i].length; j++ ) {
				afterWhere.append( fkColumns[i][j] )
						.append( '=' )
						.append( alias )
						.append( '.' )
						.append( pkColumns[j] );
				if ( j < fkColumns[i].length - 1 ) {
					afterWhere.append( " and " );
				}
			}
			if ( i < fkColumns.length - 1 ) {
				afterWhere.append( " or " );
			}
		}
		if ( fkColumns.length > 1 ) {
			afterWhere.append( ")" );
		}
	}

	/**
	 * Add the condition string to the join fragment.
	 *
	 * @param condition
	 * @return true if the condition was added, false if it was already in the fragment.
	 */
	@Override
	public boolean addCondition(String condition) {
		// if the condition is not already there...
		if (
				!StringUtils.isEmpty( condition ) &&
				afterFrom.toString().indexOf( condition.trim() ) < 0 &&
				afterWhere.toString().indexOf( condition.trim() ) < 0
		) {
			if ( !condition.startsWith( " and " ) ) {
				afterWhere.append( " and " );
			}
			afterWhere.append( condition );
			return true;
		}
		else {
			return false;
		}
	}

	public void addFromFragmentString(String fromFragmentString) {
		afterFrom.append( fromFragmentString );
	}

	public void clearWherePart() {
		afterWhere.setLength( 0 );
	}
}
