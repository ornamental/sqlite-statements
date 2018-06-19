package dev.ornamental.sqlite.statement;

/**
 * Represents an <code>ANALYZE</code> statement in one of the forms:
 * <ul>
 *     <li><code><strong>ANALYZE <em>tableOrIndexOrSchemaName</em></strong></code></li>
 *     <li><code><strong>ANALYZE <em>schemaName</em>.<em>tableOrIndexName</em></strong></code></li>
 * </ul>
 * The first form updates statistics for a table, an index, or all the tables and indices
 * of the specified database; the second one updates statistics for a table or an index
 * by its schema-qualified name.
 */
public final class Analyze implements SqlStatement {

	// may be a schema, a table or an index
	private final CharSequence firstNamePart;

	// if the first part is a schema name, this is an optional table or index name (may be null)
	private final CharSequence secondNamePart;

	Analyze(CharSequence firstNamePart, CharSequence secondNamePart) {
		this.firstNamePart = firstNamePart;
		this.secondNamePart = secondNamePart;
	}

	@Override
	public Analyze copy() throws IllegalStateException {
		CharSequence firstNamePartCopy = firstNamePart.toString();
		CharSequence secondNamePartCopy = secondNamePart == null ? null : secondNamePart.toString();

		return firstNamePartCopy == firstNamePart && secondNamePartCopy == secondNamePart
			? this : new Analyze(firstNamePartCopy, secondNamePartCopy);
	}

	@Override
	public void build(StringBuilder receptacle) {
		receptacle.append("ANALYZE ");
		if (secondNamePart != null) {
			SqliteUtilities.appendQuotedName(receptacle, firstNamePart, secondNamePart);
		} else {
			SqliteUtilities.appendQuotedName(receptacle, firstNamePart);
		}
	}
}
