package dev.ornamental.sqlite.statement;

import java.util.Arrays;

/**
 * Represents a table expression being an unconstrained join of tables or subquery results.
 * It has the form<br>
 * <code><strong>(<em>tableOrSubquery<sub>L</sub></em> INNER|LEFT|CROSS JOIN
 * <em>tableOrSubquery<sub>R</sub></em>)</strong></code><br>
 * thus not being suitable for <code>NATURAL JOIN</code>.
 */
public final class Join implements TableExpression {

	private final JoinType type;

	private final TableExpression left;

	private final TableExpression right;

	Join(TableExpression left, TableExpression right, JoinType type) {
		this.type = type;
		this.left = left;
		this.right = right;
	}

	@Override
	public boolean isJoin() {
		return true;
	}

	@Override
	public void appendTo(StringBuilder receptacle) {
		SqliteUtilities.parentheses(receptacle, left.isJoin(), left::appendTo);
		receptacle.append(' ').append(type.toString()).append(' ');
		SqliteUtilities.parentheses(receptacle, right.isJoin(), right::appendTo);
	}

	@Override
	public Join copy() {
		TableExpression leftCopy = left.copy();
		TableExpression rightCopy = right.copy();

		return leftCopy == left && rightCopy == right
			? this : new Join(leftCopy, rightCopy, type);
	}

	/**
	 * Adds a join constraint in form of an <code>ON <em>conditionExpression</em></code> clause.
	 * @param conditionExpression the join condition
	 * @return the <code>INNER|LEFT|CROSS JOIN</code> table expression having the specified
	 * <code>ON</code> constraining clause
	 */
	public JoinOn on(SqlExpression conditionExpression) {
		return new JoinOn(this, conditionExpression);
	}

	/**
	 * Adds a join constraint in form of the following clause:<br>
	 * <code>USING (<em>column<sub>0</sub></em>{, <em>column<sub>i</sub></em>})</code>.
	 * @param columns the names of the columns to use for equality comparison when joining
	 * @return the <code>INNER|LEFT|CROSS JOIN</code> table expression having the specified
	 * <code>USING</code> constraining clause
	 */
	public JoinUsing using(CharSequence... columns) {
		return new JoinUsing(this, Arrays.asList(columns));
	}

	/**
	 * Adds a join constraint in form of the following clause:<br>
	 * <code>USING (<em>column<sub>0</sub></em>{, <em>column<sub>i</sub></em>})</code>.
	 * @param columns the names of the columns to use for equality comparison when joining
	 * @return the <code>INNER|LEFT|CROSS JOIN</code> table expression having the specified
	 * <code>USING</code> constraining clause
	 */
	public JoinUsing using(Iterable<? extends CharSequence> columns) {
		return new JoinUsing(this, columns);
	}
}
