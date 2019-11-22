package cn.sexycode.myjpa.query;

import cn.sexycode.myjpa.sql.type.Type;

/**
 * Descriptor regarding an ordinal parameter.
 *
 * @author Steve Ebersole
 */
public class OrdinalParameterDescriptor extends AbstractParameterDescriptor {
	private final int label;
	private final int valuePosition;

	/**
	 * Constructs an ordinal parameter descriptor.
	 */
	public OrdinalParameterDescriptor(
			int label,
			int valuePosition,
			Type expectedType,
			int sourceLocation) {
		super( sourceLocation, expectedType );
		this.label = label;
		this.valuePosition = valuePosition;
	}

	@Override
	public Integer getPosition() {
		return label;
	}

	public int getValuePosition() {
		return valuePosition;
	}
}
