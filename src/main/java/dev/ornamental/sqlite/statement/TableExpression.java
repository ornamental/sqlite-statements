package dev.ornamental.sqlite.statement;

/**
 * This interface represents the table-valued expressions which may be used
 * as <code>SELECT</code> statement sources in <code>FROM</code> clauses.
 */
public interface TableExpression {

	/**
	 * Appends the table expression representation to the specified {@link StringBuilder} instance.
	 * @param receptacle the {@link StringBuilder} instance receiving the output
	 */
	void appendTo(StringBuilder receptacle);

	/**
	 * Returns the snapshot of the table expression with all the variable elements replaced
	 * with readonly elements reflecting the current state.
	 * @return the snapshot of the table expression at the moment of invocation
	 */
	TableExpression copy();

	/**
	 * Joins this table expression with another one using
	 * the <code>NATURAL INNER JOIN</code> operator.
	 * @param other the other table expression
	 * @return the compound table expression
	 */
	default TableExpression naturalInnerJoin(TableExpression other) {
		return new NaturalJoin(this, other, JoinType.INNER);
	}

	/**
	 * Joins this table expression with another one using
	 * the <code>NATURAL LEFT JOIN</code> operator.
	 * @param other the other table expression
	 * @return the compound table expression
	 */
	default TableExpression naturalLeftJoin(TableExpression other) {
		return new NaturalJoin(this, other, JoinType.LEFT);
	}

	/**
	 * Joins this table expression with another one using
	 * the <code>NATURAL CROSS JOIN</code> operator.
	 * @param other the other table expression
	 * @return the compound table expression
	 */
	default TableExpression naturalCrossJoin(TableExpression other) {
		return new NaturalJoin(this, other, JoinType.CROSS);
	}

	/**
	 * Joins this table expression with another one using the <code>INNER JOIN</code>
	 * operator. The join condition may be optionally specified later.
	 * @param other the other table expression
	 * @return the compound table expression
	 */
	default Join innerJoin(TableExpression other) {
		return new Join(this, other, JoinType.INNER);
	}

	/**
	 * Joins this table expression with another one using the <code>LEFT JOIN</code>
	 * operator. The join condition may be optionally specified later.
	 * @param other the other table expression
	 * @return the compound table expression
	 */
	default Join leftJoin(TableExpression other) {
		return new Join(this, other, JoinType.LEFT);
	}

	/**
	 * Joins this table expression with another one using the <code>CROSS JOIN</code>
	 * operator. The join condition may be optionally specified later.
	 * @param other the other table expression
	 * @return the compound table expression
	 */
	default Join crossJoin(TableExpression other) {
		return new Join(this, other, JoinType.CROSS);
	}
}
