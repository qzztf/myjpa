package cn.sexycode.myjpa.query.criteria;

import javax.persistence.criteria.CriteriaBuilder;
import java.io.Serializable;

/**
 * Used to construct the result of {@link javax.persistence.criteria.Path#type()}
 *
 * @author Steve Ebersole
 */
public class PathTypeExpression<T> extends AbstractExpressionImpl<T> implements Serializable {
	private final AbstractPathImpl<T> pathImpl;

	public PathTypeExpression(CriteriaBuilder criteriaBuilder, Class<T> javaType, AbstractPathImpl<T> pathImpl) {
		super( criteriaBuilder, javaType );
		this.pathImpl = pathImpl;
	}

	/**
	 * 返回条件的sql片段
	 *
	 * @return SQL片段
	 */
	@Override
	public String getSql() {
		return pathImpl.getSql();
	}
}
