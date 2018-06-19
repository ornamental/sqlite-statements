package dev.ornamental.sqlite.statement;

/**
 * Represents an SQL expression referring to a table column. It may take one of the three forms
 * depending on whether schema and table are specified or omitted:<br/>
 * <code><strong>[[<em>schemaName</em>.]<em>tableName</em>.]<em>columnName</em></strong></code>.<br/>
 * This is a complete SQL expression.
 */
public final class ColumnExpression implements SqlExpression {

	private final CharSequence schema; // nullable

	private final CharSequence table; // nullable

	private final CharSequence column;

	ColumnExpression(CharSequence schema, CharSequence table, CharSequence column) {
		this.schema = schema;
		this.table = table;
		this.column = column;
	}

	@Override
	public void appendTo(StringBuilder receptacle) {
		SqliteUtilities.appendQuotedName(receptacle, schema, table, column);
	}

	@Override
	public int getPrecedence() {
		return Integer.MAX_VALUE; // column name is an unbreakable expression never needing parentheses around it
	}

	@Override
	public SqlExpression copy() {
		CharSequence schemaCopy = schema == null ? null : schema.toString();
		CharSequence tableCopy = table == null ? null : table.toString();
		CharSequence columnCopy = column.toString();

		return schemaCopy == schema && tableCopy == table && columnCopy == column
			? this : new ColumnExpression(schemaCopy, tableCopy, columnCopy);
	}
}
