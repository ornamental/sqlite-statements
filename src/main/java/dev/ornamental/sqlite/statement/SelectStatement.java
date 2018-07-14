package dev.ornamental.sqlite.statement;

/**
 * The common interface for the flavours of <code>SELECT</code> and <code>VALUE</code> statements.
 * Each such statement also represents an SQL expression as long as it returns a single value
 * (single column is also acceptable in SQLite, but only the first value will be used in this case).
 * When used as an expression, the statement is enclosed in parentheses.<br>
 * All the implementations are complete SQL statements and complete SQL expressions.
 */
public interface SelectStatement extends ExplicableStatement, SqlExpression, TriggerStatement {

	@Override
	SelectStatement copy() throws IllegalStateException;

	@Override
	default void appendTo(StringBuilder receptacle) {
		// when used as an expression, SELECT and VALUE statements are surrounded by parentheses
		receptacle.append('(');
		build(receptacle);
		receptacle.append(')');
	}

	@Override
	default int getPrecedence() {
		// as SELECT and VALUE statements are always surrounded by parentheses when used as expressions,
		// they become unbreakable and never need extra parentheses
		return Integer.MAX_VALUE;
	}

	/**
	 * Adds a table alias to the statement; aliased <code>SELECT</code> and <code>VALUE</code> statements
	 * may be used as parts of table expression in <code>FROM</code> clauses.
	 * @param alias the alias assigned to the row set to which this statement would evaluate
	 * @return the aliased statement
	 */
	default TableExpression alias(CharSequence alias) {
		return new AliasedSelect(this, alias);
	}
}
