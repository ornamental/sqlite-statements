package dev.ornamental.sqlite.statement;

/**
 * Represents a result column list having the form <code><strong><em>tableName</em>.*</strong></code>.
 */
final class AllOfTable implements ResultElement {

	static final ResultElement ALL_OF_UNSPECIFIED = new ResultElement() {
		@Override
		public void appendTo(StringBuilder receptacle) {
			receptacle.append('*');
		}

		@Override
		public ResultElement copy() {
			return this;
		}
	};

	private final CharSequence tableName;

	AllOfTable(CharSequence tableName) {
		this.tableName = tableName;
	}

	@Override
	public void appendTo(StringBuilder receptacle) {
		SqliteUtilities.appendQuotedName(receptacle, tableName);
		receptacle.append(".*");
	}

	@Override
	public ResultElement copy() {
		CharSequence tableNameCopy = tableName.toString();

		return tableNameCopy == tableName ? this : new AllOfTable(tableNameCopy);
	}
}
