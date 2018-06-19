package dev.ornamental.sqlite.statement;

/**
 * Represents a <code>SELECT</code> subquery whose result is given an alias.
 */
public final class AliasedSelect implements TableExpression {

	private final SelectStatement select;

	private final CharSequence alias;

	AliasedSelect(SelectStatement select, CharSequence alias) {
		this.select = select;
		this.alias = alias;
	}

	@Override
	public void appendTo(StringBuilder receptacle) {
		select.appendTo(receptacle);
		receptacle.append(" AS ");
		SqliteUtilities.appendQuotedName(receptacle, alias);
	}

	@Override
	public AliasedSelect copy() {
		SelectStatement selectCopy = select.copy();
		CharSequence aliasCopy = alias.toString();

		return selectCopy == select && aliasCopy == alias
			? this : new AliasedSelect(selectCopy, aliasCopy);
	}
}
