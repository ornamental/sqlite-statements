package dev.ornamental.sqlite.statement;

/**
 * Represents a table expression being a join of tables or subquery results constrained
 * by an <code>ON</code> clause:<br>
 * <code><strong>(<em>tableOrSubquery<sub>L</sub></em> INNER|LEFT|CROSS JOIN
 * <em>tableOrSubquery<sub>R</sub></em> ON <em>conditionExpression</em>)</strong></code>.
 */
public final class JoinOn implements TableExpression {

	private final Join previous;

	private final SqlExpression condition;

	JoinOn(Join previous, SqlExpression condition) {
		this.previous = previous;
		this.condition = condition;
	}

	@Override
	public void appendTo(StringBuilder receptacle) {
		previous.appendTo(receptacle);
		receptacle.append(" ON ");
		condition.appendTo(receptacle);
	}

	@Override
	public JoinOn copy() {
		Join previousCopy = previous.copy();
		SqlExpression conditionCopy = condition.copy();

		return previousCopy == previous && conditionCopy == condition
			? this : new JoinOn(previousCopy, conditionCopy);
	}
}
