package dev.ornamental.sqlite.statement;

/**
 * Represents a table expression being a reference to a database table with an alias assigned.
 * It has the form<br/>
 * <code><strong>[<em>schemaName</em>.]<em>tableName</em> AS <em>alias</em></strong></code>.
 */
public final class AliasedTable implements TableExpression {

	private final UnaliasedTable previous;

	private final CharSequence alias;

	AliasedTable(UnaliasedTable previous, CharSequence alias) {
		this.previous = previous;
		this.alias = alias;
	}

	@Override
	public void appendTo(StringBuilder receptacle) {
		previous.appendTo(receptacle);
		receptacle.append(" AS \"");
		SqliteUtilities.escapeDoubleQuotes(receptacle, alias);
		receptacle.append('"');
	}

	@Override
	public AliasedTable copy() {
		UnaliasedTable previousCopy = previous.copy();
		CharSequence aliasCopy = alias.toString();

		return previousCopy == previous && aliasCopy == alias
			? this : new AliasedTable(previousCopy, aliasCopy);
	}

	/**
	 * Adds an <code>INDEXED BY <em>indexName</em></code> clause to the aliased table reference.
	 * @param indexName the index name
	 * @return the table expression with forced index use having the form<br/>
	 * <code>[<em>schemaName</em>.]<em>tableName</em> AS <em>alias</em>
	 * <strong>INDEXED BY <em>indexName</em></strong></code>
	 */
	public TableWithIndex indexedBy(CharSequence indexName) {
		return new TableWithIndex(this, indexName);
	}

	/**
	 * Adds a <code>NOT INDEXED</code> clause to the aliased table reference.
	 * @return the table expression with forbidden index use having the form<br/>
	 * <code>[<em>schemaName</em>.]<em>tableName</em> AS <em>alias</em>
	 * <strong>NOT INDEXED</strong></code>
	 */
	public TableWithIndex notIndexed() {
		return new TableWithIndex(this, null);
	}
}
