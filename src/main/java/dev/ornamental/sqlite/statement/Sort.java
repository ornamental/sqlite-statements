package dev.ornamental.sqlite.statement;

/**
 * Represents a sorting key in an <code>ORDER BY</code> clause.
 */
public final class Sort implements Variable<Sort> {

	private final SqlExpression expression;

	private final SortingOrder order; // may be null

	Sort(SqlExpression expression, SortingOrder order) {
		this.expression = expression;
		this.order = order;
	}

	void appendTo(StringBuilder receptacle) {
		expression.appendTo(receptacle);
		if (order != null) {
			receptacle.append(' ').append(order.toString());
		}
	}

	@Override
	public Sort copy() {
		SqlExpression expressionCopy = expression.copy();

		return expressionCopy == expression ? this : new Sort(expressionCopy, order);
	}
}
