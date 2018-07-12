package dev.ornamental.sqlite.statement;

/**
 * Represents a table expression referencing a virtual table (table-valued function call).
 * It has the form<br>
 * <code><strong>[<em>schemaName</em>.]<em>virtualTableName</em>([<em>arg<sub>0</sub></em>{,
 * <em>arg<sub>i</sub></em>}])</strong></code>.
 */
public class UnaliasedVirtualTable implements TableExpression {

	private final CharSequence schemaName;

	private final CharSequence tableName;

	private final RowExpression args; // may be null

	UnaliasedVirtualTable(CharSequence schemaName, CharSequence tableName, RowExpression args) {
		this.schemaName = schemaName;
		this.tableName = tableName;
		this.args = args;
	}

	@Override
	public void appendTo(StringBuilder receptacle) {
		SqliteUtilities.appendQuotedName(receptacle, schemaName, tableName);
		if (args != null) {
			args.appendTo(receptacle);
		} else {
			receptacle.append("()");
		}
	}

	@Override
	public UnaliasedVirtualTable copy() {
		CharSequence schemaNameCopy = schemaName == null ? null : schemaName.toString();
		CharSequence tableNameCopy = tableName.toString();
		RowExpression argsCopy = args == null ? null : args.copy();

		return schemaNameCopy == schemaName && tableNameCopy == tableName && argsCopy == args
			? this : new UnaliasedVirtualTable(schemaNameCopy, tableNameCopy, argsCopy);
	}

	/**
	 * Adds an alias to the parametrized virtual table reference.
	 * @param alias the alias to assign to the parametrized virtual table
	 * @return the aliased virtual table reference (table-valued function call) having the form<br>
	 * <code>[<em>schemaName</em>.]<em>virtualTableName</em>([<em>arg<sub>0</sub></em>{,
	 * <em>arg<sub>i</sub></em>}]) <strong>AS <em>alias</em></strong></code>.
	 */
	public AliasedVirtualTable alias(CharSequence alias) {
		return new AliasedVirtualTable(this, alias);
	}
}
