package cn.sexycode.myjpa.sql.mapping.ordering.antlr;

/**
 * General contract for AST nodes.
 *
 */
public interface Node {
	/**
	 * Get the intrinsic text of this node.
	 *
	 * @return The node's text.
	 */
	public String getText();

	/**
	 * Get a string representation of this node usable for debug logging or similar.
	 *
	 * @return The node's debugging text.
	 */
	public String getDebugText();

	/**
	 * Build the node's representation for use in the resulting rendering.
	 *
	 * @return The text for use in the translated output.
	 */
	public String getRenderableText();
}
