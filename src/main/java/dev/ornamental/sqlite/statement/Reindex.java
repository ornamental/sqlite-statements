package dev.ornamental.sqlite.statement;

/**
 * Represents a <code>REINDEX</code> statement. It comes in three forms:<br>
 * <ol>
 *     <li><code><strong>REINDEX</strong></code></li>
 *     <li><code><strong>REINDEX <em>collationName</em></strong></code></li>
 *     <li><code><strong>REINDEX [<em>schemaName</em>.]<em>tableOrIndexName</em></strong></code></li>
 * </ol>
 * causing the reindexing of, correspondingly,
 * <ol>
 *     <li>all the indices of the attached databases</li>
 *     <li>all the indices using the specified collation sequence in the attached databases</li>
 *     <li>all the indices on the specified table or the specific index.</li>
 * </ol>
 * This is a complete SQL statement.
 */
public final class Reindex implements ExplicableStatement {

	static final Reindex ALL = new Reindex();

	// all the fields are nullable

	private final Collation collation;

	private final CharSequence schemaName;

	private final CharSequence tableOrIndexName;

	Reindex(Collation collation) {
		this(collation, null, null);
	}

	Reindex(CharSequence schemaName, CharSequence tableOrIndexName) {
		this(null, schemaName, tableOrIndexName);
	}

	private Reindex() {
		this(null, null, null);
	}

	private Reindex(Collation collation, CharSequence schemaName, CharSequence tableOrIndexName) {
		this.collation = collation;
		this.schemaName = schemaName;
		this.tableOrIndexName = tableOrIndexName;
	}

	@Override
	public Reindex copy() throws IllegalStateException {
		CharSequence schemaNameCopy = schemaName == null ? null : schemaName.toString();
		CharSequence tableOrIndexNameCopy = tableOrIndexName == null ? null : tableOrIndexName.toString();

		return schemaNameCopy == schemaName && tableOrIndexNameCopy == tableOrIndexName
			? this : new Reindex(collation, schemaNameCopy, tableOrIndexNameCopy);
	}

	@Override
	public void build(StringBuilder receptacle) {
		receptacle.append("REINDEX");
		if (collation != null) {
			receptacle.append(' ');
			collation.appendTo(receptacle);
		} else if (tableOrIndexName != null) {
			receptacle.append(' ');
			SqliteUtilities.appendQuotedName(receptacle, schemaName, tableOrIndexName);
		}
	}
}
