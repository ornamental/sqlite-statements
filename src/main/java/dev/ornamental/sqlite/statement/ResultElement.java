package dev.ornamental.sqlite.statement;

/**
 * This interface corresponds to the elements of enumerations immediately
 * following the <code>SELECT</code> keyword and defining the values
 * in the rows emitted by the <code>SELECT</code> statement execution.<br/>
 * Each SQL expression ({@link SqlExpression}) is an implementation of this interface;
 * the other implementations include
 * <ul>
 *     <li>the aliased SQL expressions represented by {@link NamedResultColumn}</li>
 *     <li>the instance returned by this class's static method {@link ResultElements#all()}
 *     standing for the single asterisk symbol (<code>*</code>) designating all the columns
 *     of the source table expression (following the <code>FROM</code> keyword) at once</li>
 *     <li>the instances returned by this class's static method {@link ResultElements#allOf(CharSequence)}
 *     designating all the columns of one of the tables named in the source table expression
 *     (<code><em>tableNameOrAlias</em>.*</code>).</li>
 * </ul>
 */
public interface ResultElement {

	/**
	 * Appends the result columns definition to the specified {@link  StringBuilder} instance.
	 * @param receptacle the {@link  StringBuilder} instance to receive the output
	 */
	void appendTo(StringBuilder receptacle);

	/**
	 * Creates a deep immutable copy of the current state of this instance.
	 * @return the deep immutable copy of the current state of this instance
	 */
	ResultElement copy();
}
