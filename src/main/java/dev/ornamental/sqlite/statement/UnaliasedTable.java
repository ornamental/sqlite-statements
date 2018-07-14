package dev.ornamental.sqlite.statement;

/**
 * Represents a table expression being a simple reference to a database table. It has the form<br>
 * <code><strong>[<em>schemaName</em>.]<em>tableName</em></strong></code>.
 */
public final class UnaliasedTable implements TableExpression {

	private final CharSequence schemaName;

	private final CharSequence tableName;

	UnaliasedTable(CharSequence schemaName, CharSequence tableName) {
		this.schemaName = schemaName;
		this.tableName = tableName;
	}

	@Override
	public boolean isJoin() {
		return false;
	}

	@Override
	public void appendTo(StringBuilder receptacle) {
		SqliteUtilities.appendQuotedName(receptacle, schemaName, tableName);
	}

	@Override
	public UnaliasedTable copy() {
		CharSequence schemaNameCopy = schemaName == null ? null : schemaName.toString();
		CharSequence tableNameCopy = tableName.toString();

		return schemaNameCopy == schemaName && tableNameCopy == tableName
			? this : new UnaliasedTable(schemaNameCopy, tableNameCopy);
	}

	/**
	 * Adds an alias to the table reference.
	 * @param alias the alias to assign to the database table
	 * @return the aliased table reference having the form<br>
	 * <code>[<em>schemaName</em>.]<em>tableName</em> <strong>AS <em>alias</em></strong></code>
	 */
	@Override
	public AliasedTable alias(CharSequence alias) {
		return new AliasedTable(this, alias);
	}

	/**
	 * Adds an <code>INDEXED BY indexName</code> clause to the table reference.
	 * @param indexName the index name
	 * @return the table expression with forced index use having the form<br>
	 * <code>[<em>schemaName</em>.]<em>tableName</em> <strong>INDEXED BY <em>indexName</em></strong></code>
	 */
	public TableWithIndex indexedBy(CharSequence indexName) {
		return new TableWithIndex(this, indexName);
	}

	/**
	 * Adds a <code>NOT INDEXED</code> clause to the table reference.
	 * @return the table expression with forbidden index use having the form<br>
	 * <code>[<em>schemaName</em>.]<em>tableName</em> <strong>NOT INDEXED</strong></code>
	 */
	public TableWithIndex notIndexed() {
		return new TableWithIndex(this, null);
	}
}
