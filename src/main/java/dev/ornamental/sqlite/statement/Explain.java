package dev.ornamental.sqlite.statement;

/**
 * Represents an <code>EXPLAIN</code> statement having the form<br/>
 * <code><strong>EXPLAIN [QUERY PLAN] <em>explicableSqlStatement</em></strong></code><br/>
 * where <code><em>explicableSqlStatement</em></code> is any SQL statement except for
 * <code>EXPLAIN</code> statements themselves.<br/>
 * This is a complete SQL statement.
 */
public final class Explain implements SqlStatement {

	private final ExplicableStatement statement;

	private final boolean queryPlan;

	Explain(ExplicableStatement statement, boolean queryPlan) {
		this.statement = statement;
		this.queryPlan = queryPlan;
	}

	@Override
	public SqlStatement copy() throws IllegalStateException {
		ExplicableStatement statementCopy = statement.copy();

		return statementCopy == statement ? this : new Explain(statementCopy, queryPlan);
	}

	@Override
	public void build(StringBuilder receptacle) {
		receptacle.append("EXPLAIN ");
		if (queryPlan) {
			receptacle.append("QUERY PLAN ");
		}
		statement.build(receptacle);
	}
}
