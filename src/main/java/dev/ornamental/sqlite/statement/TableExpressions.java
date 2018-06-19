package dev.ornamental.sqlite.statement;

/**
 * This class contains static factory methods serving for construction of {@link TableExpression} instances.
 */
public final class TableExpressions {

	private TableExpressions() { }

	/**
	 * Creates the simplest table expression referring to a database table.
	 * @param tableName the name of the table
	 * @return the table expression having the form <code><strong><em>tableName</em></strong></code>
	 */
	public static UnaliasedTable table(CharSequence tableName) {
		return table(null, tableName);
	}

	/**
	 * Creates the simplest table expression referring to a database table.
	 * @param schemaName the name of the schema containing the table
	 * @param tableName the name of the table
	 * @return the table expression having the form
	 * <code><strong><em>schemaName</em>.<em>tableName</em></strong></code>
	 */
	public static UnaliasedTable table(CharSequence schemaName, CharSequence tableName) {
		return new UnaliasedTable(schemaName, tableName);
	}

	/**
	 * Creates the table expression referring to a database virtual table.
	 * @param virtualTableName the name of the virtual table
	 * @param args the possibly empty argument list (used for filtering
	 * by the hidden columns of the virtual table)
	 * @return the table expression having the form<br/>
	 * <code><strong><em>virtualTableName</em>([<em>arg<sub>0</sub></em>{,
	 * <em>arg<sub>i</sub></em>}])</strong></code>
	 */
	public static UnaliasedVirtualTable virtualTable(CharSequence virtualTableName, SqlExpression... args) {
		return virtualTable(null, virtualTableName, args);
	}

	/**
	 * Creates the table expression referring to a database virtual table.
	 * @param schemaName the name of the schema containing the virtual table
	 * @param virtualTableName the name of the virtual table
	 * @param args the possibly empty argument list (used for filtering
	 * by the hidden columns of the virtual table)
	 * @return the table expression having the form<br/>
	 * <code><strong><em>schemaName</em>.<em>virtualTableName</em>([<em>arg<sub>0</sub></em>{,
	 * <em>arg<sub>i</sub></em>}])</strong></code>
	 */
	public static UnaliasedVirtualTable virtualTable(
		CharSequence schemaName, CharSequence virtualTableName, SqlExpression... args) {

		return new UnaliasedVirtualTable(
			schemaName, virtualTableName, args.length == 0 ? null : SqlExpressions.rowOf(args));
	}
}
