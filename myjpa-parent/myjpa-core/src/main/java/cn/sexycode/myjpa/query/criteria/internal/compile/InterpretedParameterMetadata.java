package cn.sexycode.myjpa.query.criteria.internal.compile;

import javax.persistence.criteria.ParameterExpression;
import java.util.List;
import java.util.Map;

/**
 * Represents information about parameters from a compiled criteria query.
 *
 * @author Steve Ebersole
 */
public interface InterpretedParameterMetadata {
    public Map<ParameterExpression<?>, ExplicitParameterInfo<?>> explicitParameterInfoMap();

    //	public Map<ParameterExpression<?>,String> explicitParameterMapping();
    //	public Map<String,ParameterExpression<?>> explicitParameterNameMapping();
    public List<ImplicitParameterBinding> implicitParameterBindings();
    //	public Map<String,Class> implicitParameterTypes();
}
