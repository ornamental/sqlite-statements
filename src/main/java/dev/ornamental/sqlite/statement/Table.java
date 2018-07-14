package dev.ornamental.sqlite.statement;

import static dev.ornamental.sqlite.statement.SqlExpressions.column;
import static dev.ornamental.sqlite.statement.TableExpressions.table;

import java.util.function.Function;

/**
 * This class is meant to be used in the following manner:
 * <ol>
 *     <li>extend the class specifying the table name;</li>
 *     <li>add public fields for the column names and public methods for the column objects
 *     (using the protected {@link #columnFactory} function);</li>
 *     <li>create an instance of the class to be able to use it where a table expression is expected,
 *     and its fields (methods) where an SQL expression is</li>
 * </ol>
 */
public abstract class Table implements TableExpression {

	protected final TableExpression table;

	protected final Function<String, SqlExpression> columnFactory;

	protected Table(String tableName) {
		this(null, tableName);
	}

	protected Table(String schemaName, String tableName) {
		this.table = table(schemaName, tableName);
		this.columnFactory = s -> column(schemaName, tableName, s);
	}

	@Override
	public boolean isJoin() {
		return false;
	}

	@Override
	public void appendTo(StringBuilder receptacle) {
		table.appendTo(receptacle);
	}

	@Override
	public TableExpression copy() {
		return this;
	}
}
