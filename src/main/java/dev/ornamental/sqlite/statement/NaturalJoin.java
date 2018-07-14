package dev.ornamental.sqlite.statement;

/**
 * Represents a table expression being a natural join of tables or subquery results
 * (the join columns are automatically inferred from the matching column names):<br>
 * It has the form<br>
 * <code><strong>(<em>tableOrSubquery<sub>L</sub></em> NATURAL INNER|LEFT|CROSS JOIN
 * <em>tableOrSubquery<sub>R</sub></em>)</strong></code><br>.
 */
public final class NaturalJoin implements TableExpression {

	private final JoinType type;

	private final TableExpression left;

	private final TableExpression right;

	NaturalJoin(TableExpression left, TableExpression right, JoinType type) {
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
		receptacle.append(" NATURAL ").append(type.toString()).append(' ');
		SqliteUtilities.parentheses(receptacle, right.isJoin(), right::appendTo);
	}

	@Override
	public NaturalJoin copy() {
		TableExpression leftCopy = left.copy();
		TableExpression rightCopy = right.copy();

		return leftCopy == left && rightCopy == right
			? this : new NaturalJoin(leftCopy, rightCopy, type);
	}
}
