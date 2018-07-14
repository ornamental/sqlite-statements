package dev.ornamental.sqlite.statement;

/**
 * Represents a table expression referencing a virtual table (table-valued function call)
 * given an alias. It has the form<br>
 * <code><strong>[<em>schemaName</em>.]<em>virtualTableName</em>([<em>arg<sub>0</sub></em>{,
 * <em>arg<sub>i</sub></em>}]) AS <em>alias</em></strong></code>.
 */
public final class AliasedVirtualTable implements TableExpression {

	private final UnaliasedVirtualTable previous;

	private final CharSequence alias;

	AliasedVirtualTable(UnaliasedVirtualTable previous, CharSequence alias) {
		this.previous = previous;
		this.alias = alias;
	}

	@Override
	public boolean isJoin() {
		return false;
	}

	@Override
	public void appendTo(StringBuilder receptacle) {
		previous.appendTo(receptacle);
		receptacle.append(" AS ");
		SqliteUtilities.appendQuotedName(receptacle, alias);
	}

	@Override
	public AliasedVirtualTable copy() {
		UnaliasedVirtualTable previousCopy = previous.copy();
		CharSequence aliasCopy = alias.toString();

		return previousCopy == previous && aliasCopy == alias
			? this : new AliasedVirtualTable(previousCopy, aliasCopy);
	}
}
