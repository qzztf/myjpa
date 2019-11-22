package cn.sexycode.myjpa.sql.mapping;

import cn.sexycode.myjpa.sql.mapping.ANSIJoinFragment;
import cn.sexycode.myjpa.sql.mapping.JoinType;
import cn.sexycode.util.core.exception.AssertionFailure;

/**
 * A Cach&eacute; dialect join.  Differs from ANSI only in that full outer join
 * is not supported.
 *
 */
public class CacheJoinFragment extends ANSIJoinFragment {

	@Override
    public void addJoin(String rhsTableName, String rhsAlias, String[] lhsColumns, String[] rhsColumns, cn.sexycode.myjpa.sql.mapping.JoinType joinType, String on) {
		if ( joinType == JoinType.FULL_JOIN ) {
			throw new AssertionFailure( "Cache does not support full outer joins" );
		}
		super.addJoin( rhsTableName, rhsAlias, lhsColumns, rhsColumns, joinType, on );
	}

}
