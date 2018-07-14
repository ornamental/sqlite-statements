package dev.ornamental.sqlite.statement;

/**
 * Represents a table reference with either a forced index use (<code>INDEXED BY</code> clause)
 * or a forced absence of such (<code>NOT INDEXED</code> clause).
 */
public final class TableWithIndex implements TableExpression {

	private final TableExpression table;

	private final CharSequence indexName; // null is admissible

	TableWithIndex(UnaliasedTable table, CharSequence indexName) {
		this((TableExpression)table, indexName);
	}

	TableWithIndex(AliasedTable table, CharSequence indexName) {
		this((TableExpression)table, indexName);
	}

	private TableWithIndex(TableExpression table, CharSequence indexName) {
		this.table = table;
		this.indexName = indexName;
	}

	@Override
	public boolean isJoin() {
		return false;
	}

	@Override
	public void appendTo(StringBuilder receptacle) {
		table.appendTo(receptacle);
		appendIndexDirective(receptacle, indexName);
	}

	@Override
	public TableWithIndex copy() {
		TableExpression tableCopy = table.copy();
		CharSequence indexNameCopy = indexName == null ? null : indexName.toString();

		return tableCopy == table && indexNameCopy == indexName
			? this : new TableWithIndex(tableCopy, indexNameCopy);
	}

	static void appendIndexDirective(StringBuilder receptacle, CharSequence indexName) {
		if (indexName == null) {
			receptacle.append(" NOT INDEXED");
		} else {
			receptacle.append(" INDEXED BY ");
			SqliteUtilities.appendQuotedName(receptacle, indexName);
		}
	}
}
