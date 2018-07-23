package dev.ornamental.sqlite.statement;

/**
 * This class contains static factory methods starting the construction of various
 * statements (excluding the <code>SELECT</code> and <code>VALUE</code> statements
 * having no <code>WITH</code> clause).
 */
public final class SqlStatements {

	private SqlStatements() { }

	/**
	 * Constructs an <code>EXPLAIN</code> statement from the statement which is to be explained.<br>
	 * The result is a complete SQL statement.
	 * @param statement the statement to be explained
	 * @return the statement having the form<br>
	 * <code><strong>EXPLAIN <em>statement</em></strong></code>
	 */
	public static Explain explain(ExplicableStatement statement) {
		return new Explain(statement, false);
	}

	/**
	 * Constructs an <code>EXPLAIN QUERY PLAN</code> statement from the statement\
	 * whose execution plan is to be explained.<br>
	 * The result is a complete SQL statement.
	 * @param statement the statement whose execution plan is to be explained
	 * @return the statement having the form<br>
	 * <code><strong>EXPLAIN QUERY PLAN <em>statement</em></strong></code>
	 */
	public static Explain explainQueryPlan(ExplicableStatement statement) {
		return new Explain(statement, true);
	}

	/**
	 * Creates an <code>ALTER TABLE</code> statement stub for the specified target table.
	 * @param tableName the name of the table to alter
	 * @return the initial part of the <code>ALTER TABLE</code> statement:<br>
	 * <code><strong>ALTER TABLE <em>tableName</em></strong></code>
	 */
	public static AlterTable.Stub alterTable(CharSequence tableName) {
		return new AlterTable.Stub(tableName);
	}

	/**
	 * Creates an <code>ALTER TABLE</code> statement stub for the specified target table.
	 * @param schemaName the name of the schema containing the target table
	 * @param tableName the name of the table to alter
	 * @return the initial part of the <code>ALTER TABLE</code> statement:<br>
	 * <code><strong>ALTER TABLE <em>schemaName</em>.<em>tableName</em></strong></code>
	 */
	public static AlterTable.Stub alterTable(CharSequence schemaName, CharSequence tableName) {
		return new AlterTable.Stub(schemaName, tableName);
	}

	/**
	 * Creates an <code>ALTER TABLE</code> statement stub for the specified target table.
	 * @param table the table to alter
	 * @return the initial part of the <code>ALTER TABLE</code> statement:<br>
	 * <code><strong>ALTER TABLE [<em>schemaName</em>.]<em>tableName</em></strong></code>
	 */
	public static AlterTable.Stub alterTable(Table table) {
		return alterTable(table.schemaName(), table.tableName());
	}

	/**
	 * Creates an <code>ANALYZE</code> statement for the specified schema, table, or index.<br>
	 * If there is an ambiguity, use the qualified object name
	 * ({@link #analyze(CharSequence, CharSequence)}).<br>
	 * The result is a complete SQL statement.
	 * @param schemaOrTableOrIndex the name of the schema, table, or index to analyze
	 * @return the statement having the form<br>
	 * <code><strong>ANALYZE <em>schemaOrTableOrIndex</em></strong></code>
	 */
	public static Analyze analyze(CharSequence schemaOrTableOrIndex) {
		return new Analyze(schemaOrTableOrIndex, null);
	}

	/**
	 * Creates an <code>ANALYZE</code> statement for the specified schema, table, or index.<br>
	 * The result is a complete SQL statement.
	 * @param schema the name of the schema containing the object to analyze
	 * @param tableOrIndex the name of the table or index to analyze
	 * @return the statement having the form<br>
	 * <code><strong>ANALYZE <em>schema</em>.<em>tableOrIndex</em></strong></code>
	 */
	public static Analyze analyze(CharSequence schema, CharSequence tableOrIndex) {
		return new Analyze(schema, tableOrIndex);
	}

	/**
	 * Creates an <code>ATTACH</code> statement stub for the specified target database.
	 * @param databaseFileName the name of the database file to attach
	 * @return the initial part of the <code>ATTACH</code> statement:<br>
	 * <code><strong>ATTACH <em>databaseFileName</em></strong></code>
	 */
	public static Attach.Stub attach(String databaseFileName) {
		return attach(Literal.value(databaseFileName));
	}

	/**
	 * Creates an <code>ATTACH</code> statement stub for the specified target database.
	 * @param databaseFileNameExpression the epression determining the name
	 * of the database file to attach
	 * @return the initial part of the <code>ATTACH</code> statement:<br>
	 * <code><strong>ATTACH <em>databaseFileNameExpression</em></strong></code>
	 */
	public static Attach.Stub attach(SqlExpression databaseFileNameExpression) {
		return new Attach.Stub(databaseFileNameExpression);
	}

	/**
	 * Returns the <code><strong>BEGIN TRANSACTION</strong></code> statement.<br>
	 * This is a complete SQL statement.
	 * @return the <code><strong>BEGIN TRANSACTION</strong></code> statement
	 */
	public static BeginTransaction beginTransaction() {
		return BeginTransaction.DEFAULT;
	}

	/**
	 * Returns the <code><strong>BEGIN DEFERRED TRANSACTION</strong></code> statement.<br>
	 * This is a complete SQL statement.
	 * @return the <code><strong>BEGIN DEFERRED TRANSACTION</strong></code> statement
	 */
	public static BeginTransaction beginDeferredTransaction() {
		return TransactionType.DEFERRED.beginStatement();
	}

	/**
	 * Returns the <code><strong>BEGIN IMMEDIATE TRANSACTION</strong></code> statement.<br>
	 * This is a complete SQL statement.
	 * @return the <code><strong>BEGIN IMMEDIATE TRANSACTION</strong></code> statement
	 */
	public static BeginTransaction beginImmediateTransaction() {
		return TransactionType.IMMEDIATE.beginStatement();
	}

	/**
	 * Returns the <code><strong>BEGIN EXCLUSIVE TRANSACTION</strong></code> statement.<br>
	 * This is a complete SQL statement.
	 * @return the <code><strong>BEGIN EXCLUSIVE TRANSACTION</strong></code> statement
	 */
	public static BeginTransaction beginExclusiveTransaction() {
		return TransactionType.EXCLUSIVE.beginStatement();
	}

	/**
	 * Returns the <code><strong>COMMIT TRANSACTION</strong></code> statement.<br>
	 * This is a complete SQL statement.
	 * @return the <code><strong>COMMIT TRANSACTION</strong></code> statement
	 */
	public static ExplicableStatement commitTransaction() {
		return CommitTransaction.INSTANCE;
	}

	/**
	 * Returns the initial part of a <code>CREATE INDEX</code> statement.
	 * @return the <code>CREATE INDEX</code> statement beginning:
	 * <code><strong>CREATE INDEX</strong></code>
	 */
	public static CreateIndex.Stub createIndex() {
		return new CreateIndex.Stub(false, false);
	}

	/**
	 * Returns the initial part of a <code>CREATE UNIQUE INDEX</code> statement.
	 * @return the <code>CREATE INDEX</code> statement beginning:
	 * <code><strong>CREATE UNIQUE INDEX</strong></code>
	 */
	public static CreateIndex.Stub createUniqueIndex() {
		return new CreateIndex.Stub(true, false);
	}

	/**
	 * Returns the initial part of a <code>CREATE INDEX</code> statement.
	 * @return the <code>CREATE INDEX</code> statement beginning:
	 * <code><strong>CREATE INDEX IF NOT EXISTS</strong></code>
	 */
	public static CreateIndex.Stub createIndexIfNotExists() {
		return new CreateIndex.Stub(false, true);
	}

	/**
	 * Returns the initial part of a <code>CREATE UNIQUE INDEX</code> statement.
	 * @return the <code>CREATE INDEX</code> statement beginning:
	 * <code><strong>CREATE UNIQUE INDEX IF NOT EXISTS</strong></code>
	 */
	public static CreateIndex.Stub createUniqueIndexIfNotExists() {
		return new CreateIndex.Stub(true, true);
	}

	/**
	 * Returns the initial part of a <code>CREATE TABLE</code> statement.
	 * @param tableName the name of the table to create
	 * @return the initial part of the <code>CREATE TABLE</code> statement:<br>
	 * <code><strong>CREATE TABLE <em>tableName</em></strong></code>
	 */
	public static CreateTable.Stub createTable(CharSequence tableName) {
		return createTable(null, tableName);
	}

	/**
	 * Returns the initial part of a <code>CREATE TABLE</code> statement.
	 * @param schemaName the name of the schema to contain the new table
	 * @param tableName the name of the table to create
	 * @return the initial part of the <code>CREATE TABLE</code> statement:<br>
	 * <code><strong>CREATE TABLE <em>schemaName</em>.<em>tableName</em></strong></code>
	 */
	public static CreateTable.Stub createTable(CharSequence schemaName, CharSequence tableName) {
		return new CreateTable.Stub(false, false, schemaName, tableName);
	}

	/**
	 * Returns the initial part of a <code>CREATE TABLE</code> statement.
	 * @param table the table to create
	 * @return the initial part of the <code>CREATE TABLE</code> statement:<br>
	 * <code><strong>CREATE TABLE [<em>schemaName</em>.]<em>tableName</em></strong></code>
	 */
	public static CreateTable.Stub createTable(Table table) {
		return createTable(table.schemaName(), table.tableName());
	}

	/**
	 * Returns the initial part of a <code>CREATE TABLE</code> statement.
	 * @param tableName the name of the table to create
	 * @return the initial part of the <code>CREATE TABLE</code> statement:<br>
	 * <code><strong>CREATE TABLE IF NOT EXISTS <em>tableName</em></strong></code>
	 */
	public static CreateTable.Stub createTableIfNotExists(CharSequence tableName) {
		return createTableIfNotExists(null, tableName);
	}

	/**
	 * Returns the initial part of a <code>CREATE TABLE</code> statement.
	 * @param schemaName the name of the schema to contain the new table
	 * @param tableName the name of the table to create
	 * @return the initial part of the <code>CREATE TABLE</code> statement:<br>
	 * <code><strong>CREATE TABLE IF NOT EXISTS <em>schemaName</em>.<em>tableName</em></strong></code>
	 */
	public static CreateTable.Stub createTableIfNotExists(CharSequence schemaName, CharSequence tableName) {
		return new CreateTable.Stub(false, true, schemaName, tableName);
	}

	/**
	 * Returns the initial part of a <code>CREATE TABLE</code> statement.
	 * @param table the table to create
	 * @return the initial part of the <code>CREATE TABLE</code> statement:<br>
	 * <code><strong>CREATE TABLE IF NOT EXISTS [<em>schemaName</em>.]<em>tableName</em></strong></code>
	 */
	public static CreateTable.Stub createTableIfNotExists(Table table) {
		return createTableIfNotExists(table.schemaName(), table.tableName());
	}

	/**
	 * Returns the initial part of a <code>CREATE TEMPORARY TABLE</code> statement.
	 * @param tableName the name of the temporary table to create
	 * @return the initial part of the <code>CREATE TEMPORARY TABLE</code> statement:<br>
	 * <code><strong>CREATE TEMPORARY TABLE <em>tableName</em></strong></code>
	 */
	public static CreateTable.Stub createTemporaryTable(CharSequence tableName) {
		return new CreateTable.Stub(true, false, null, tableName);
	}

	/**
	 * Returns the initial part of a <code>CREATE TEMPORARY TABLE</code> statement.
	 * @param tableName the name of the temporary table to create
	 * @return the initial part of the <code>CREATE TEMPORARY TABLE</code> statement:<br>
	 * <code><strong>CREATE TEMPORARY TABLE IF NOT EXISTS <em>tableName</em></strong></code>
	 */
	public static CreateTable.Stub createTemporaryTableIfNotExists(CharSequence tableName) {
		return new CreateTable.Stub(true, true, null, tableName);
	}

	/**
	 * Returns the initial part of a <code>CREATE TRIGGER</code> statement.
	 * @param triggerName the name of the trigger to create
	 * @return the initial part of the <code>CREATE TRIGGER</code> statement:<br>
	 * <code><strong>CREATE TRIGGER <em>triggerName</em></strong></code>
	 */
	public static CreateTrigger.Stub createTrigger(CharSequence triggerName) {
		return new CreateTrigger.Stub(false, false, null, triggerName);
	}

	/**
	 * Returns the initial part of a <code>CREATE TRIGGER</code> statement.
	 * @param triggerName the name of the trigger to create
	 * @return the initial part of the <code>CREATE TRIGGER</code> statement:<br>
	 * <code><strong>CREATE TRIGGER IF NOT EXISTS <em>triggerName</em></strong></code>
	 */
	public static CreateTrigger.Stub createTriggerIfNotExists(CharSequence triggerName) {
		return new CreateTrigger.Stub(false, true, null, triggerName);
	}

	/**
	 * Returns the initial part of a <code>CREATE TRIGGER</code> statement.
	 * @param schemaName the name of the schema to contain the new trigger
	 * @param triggerName the name of the trigger to create
	 * @return the initial part of the <code>CREATE TRIGGER</code> statement:<br>
	 * <code><strong>CREATE TRIGGER <em>schemaName</em>.<em>triggerName</em></strong></code>
	 */
	public static CreateTrigger.Stub createTrigger(CharSequence schemaName, CharSequence triggerName) {
		return new CreateTrigger.Stub(false, false, schemaName, triggerName);
	}

	/**
	 * Returns the initial part of a <code>CREATE TRIGGER</code> statement.
	 * @param schemaName the name of the schema to contain the new trigger
	 * @param triggerName the name of the trigger to create
	 * @return the initial part of the <code>CREATE TRIGGER</code> statement:<br>
	 * <code><strong>CREATE TRIGGER IF NOT EXISTS <em>schemaName</em>.<em>triggerName</em></strong></code>
	 */
	public static CreateTrigger.Stub createTriggerIfNotExists(CharSequence schemaName, CharSequence triggerName) {
		return new CreateTrigger.Stub(false, true, schemaName, triggerName);
	}

	/**
	 * Returns the initial part of a <code>CREATE TEMPORARY TRIGGER</code> statement.
	 * @param triggerName the name of the trigger to create
	 * @return the initial part of the <code>CREATE TEMPORARY TRIGGER</code> statement:<br>
	 * <code><strong>CREATE TEMPORARY TRIGGER <em>triggerName</em></strong></code>
	 */
	public static CreateTrigger.Stub createTemporaryTrigger(CharSequence triggerName) {
		return new CreateTrigger.Stub(true, false, null, triggerName);
	}

	/**
	 * Returns the initial part of a <code>CREATE TEMPORARY TRIGGER</code> statement.
	 * @param triggerName the name of the trigger to create
	 * @return the initial part of the <code>CREATE TEMPORARY TRIGGER</code> statement:<br>
	 * <code><strong>CREATE TEMPORARY TRIGGER IF NOT EXISTS <em>triggerName</em></strong></code>
	 */
	public static CreateTrigger.Stub createTemporaryTriggerIfNotExists(CharSequence triggerName) {
		return new CreateTrigger.Stub(true, true, null, triggerName);
	}

	/**
	 * Returns the initial part of a <code>CREATE VIEW</code> statement.
	 * @param viewName the name of the view to create
	 * @return the initial part of the <code>CREATE VIEW</code> statement:<br>
	 * <code><strong>CREATE VIEW <em>viewName</em></strong></code>
	 */
	public static CreateView.Stub createView(CharSequence viewName) {
		return new CreateView.Stub(false, false, null, viewName);
	}

	/**
	 * Returns the initial part of a <code>CREATE VIEW</code> statement.
	 * @param viewName the name of the view to create
	 * @return the initial part of the <code>CREATE VIEW</code> statement:<br>
	 * <code><strong>CREATE VIEW IF NOT EXISTS <em>viewName</em></strong></code>
	 */
	public static CreateView.Stub createViewIfNotExists(CharSequence viewName) {
		return new CreateView.Stub(false, true, null, viewName);
	}

	/**
	 * Returns the initial part of a <code>CREATE VIEW</code> statement.
	 * @param schemaName the name of the schema to contain the new view
	 * @param viewName the name of the view to create
	 * @return the initial part of the <code>CREATE VIEW</code> statement:<br>
	 * <code><strong>CREATE VIEW <em>schemaName</em>.<em>viewName</em></strong></code>
	 */
	public static CreateView.Stub createView(CharSequence schemaName, CharSequence viewName) {
		return new CreateView.Stub(false, false, schemaName, viewName);
	}

	/**
	 * Returns the initial part of a <code>CREATE VIEW</code> statement.
	 * @param schemaName the name of the schema to contain the new view
	 * @param viewName the name of the view to create
	 * @return the initial part of the <code>CREATE VIEW</code> statement:<br>
	 * <code><strong>CREATE VIEW IF NOT EXISTS <em>schemaName</em>.<em>viewName</em></strong></code>
	 */
	public static CreateView.Stub createViewIfNotExists(CharSequence schemaName, CharSequence viewName) {
		return new CreateView.Stub(false, true, schemaName, viewName);
	}

	/**
	 * Returns the initial part of a <code>CREATE TEMPORARY VIEW</code> statement.
	 * @param viewName the name of the view to create
	 * @return the initial part of the <code>CREATE TEMPORARY VIEW</code> statement:<br>
	 * <code><strong>CREATE TEMPORARY VIEW <em>viewName</em></strong></code>
	 */
	public static CreateView.Stub createTemporaryView(CharSequence viewName) {
		return new CreateView.Stub(true, false, null, viewName);
	}

	/**
	 * Returns the initial part of a <code>CREATE TEMPORARY VIEW</code> statement.
	 * @param viewName the name of the view to create
	 * @return the initial part of the <code>CREATE TEMPORARY VIEW</code> statement:<br>
	 * <code><strong>CREATE TEMPORARY VIEW IF NOT EXISTS <em>viewName</em></strong></code>
	 */
	public static CreateView.Stub createTemporaryViewIfNotExists(CharSequence viewName) {
		return new CreateView.Stub(true, true, null, viewName);
	}

	/**
	 * Returns the initial part of a <code>CREATE VIRTUAL TABLE</code> statement.
	 * @param tableName the name of the virtual table to create
	 * @return the initial part of the <code>CREATE VIRTUAL TABLE</code> statement:<br>
	 * <code><strong>CREATE VIRTUAL TABLE <em>tableName</em></strong></code>
	 */
	public static CreateVirtualTable.Stub createVirtualTable(CharSequence tableName) {
		return new CreateVirtualTable.Stub(false, null, tableName);
	}

	/**
	 * Returns the initial part of a <code>CREATE VIRTUAL TABLE</code> statement.
	 * @param schemaName the name of the schema to contain the new virtual table
	 * @param tableName the name of the virtual table to create
	 * @return the initial part of the <code>CREATE VIRTUAL TABLE</code> statement:<br>
	 * <code><strong>CREATE VIRTUAL TABLE <em>schemaName</em>.<em>tableName</em></strong></code>
	 */
	public static CreateVirtualTable.Stub createVirtualTable(CharSequence schemaName, CharSequence tableName) {
		return new CreateVirtualTable.Stub(false, schemaName, tableName);
	}

	/**
	 * Returns the initial part of a <code>CREATE VIRTUAL TABLE</code> statement.
	 * @param virtualTable the virtual table to create
	 * @return the initial part of the <code>CREATE VIRTUAL TABLE</code> statement:<br>
	 * <code><strong>CREATE VIRTUAL TABLE [<em>schemaName</em>.]<em>tableName</em></strong></code>
	 */
	public static CreateVirtualTable.Stub createVirtualTable(Table virtualTable) {
		return createVirtualTable(virtualTable.schemaName(), virtualTable.tableName());
	}

	/**
	 * Returns the initial part of a <code>CREATE VIRTUAL TABLE</code> statement.
	 * @param tableName the name of the virtual table to create
	 * @return the initial part of the <code>CREATE VIRTUAL TABLE</code> statement:<br>
	 * <code><strong>CREATE VIRTUAL TABLE IF NOT EXISTS <em>tableName</em></strong></code>
	 */
	public static CreateVirtualTable.Stub createVirtualTableIfNotExists(CharSequence tableName) {
		return new CreateVirtualTable.Stub(true, null, tableName);
	}

	/**
	 * Returns the initial part of a <code>CREATE VIRTUAL TABLE</code> statement.
	 * @param schemaName the name of the schema to contain the new virtual table
	 * @param tableName the name of the virtual table to create
	 * @return the initial part of the <code>CREATE VIRTUAL TABLE</code> statement:<br>
	 * <code><strong>CREATE VIRTUAL TABLE IF NOT EXISTS <em>schemaName</em>.<em>tableName</em></strong></code>
	 */
	public static CreateVirtualTable.Stub createVirtualTableIfNotExists(
		CharSequence schemaName, CharSequence tableName) {

		return new CreateVirtualTable.Stub(true, schemaName, tableName);
	}

	/**
	 * Returns the initial part of a <code>CREATE VIRTUAL TABLE</code> statement.
	 * @param virtualTable the virtual table to create
	 * @return the initial part of the <code>CREATE VIRTUAL TABLE</code> statement:<br>
	 * <code><strong>CREATE VIRTUAL TABLE IF NOT EXISTS [<em>schemaName</em>.]<em>tableName</em></strong></code>
	 */
	public static CreateVirtualTable.Stub createVirtualTableIfNotExists(Table virtualTable) {

		return createVirtualTable(virtualTable.schemaName(), virtualTable.tableName());
	}

	/**
	 * Returns the basic form of <code>DELETE</code> statement clearing the table contents.<br>
	 * The result is a complete SQL statement.
	 * @param tableName the name of the table to perform deletion from
	 * @return the minimal form of <code>DELETE</code> statement:<br>
	 * <code><strong>DELETE FROM <em>tableName</em></strong></code>
	 */
	public static Delete.All deleteFrom(CharSequence tableName) {
		return new Delete.All(null, null, tableName);
	}

	/**
	 * Returns the basic form of <code>DELETE</code> statement clearing the table contents.<br>
	 * The result is a complete SQL statement.
	 * @param schemaName the name of the schema containing the target table
	 * @param tableName the name of the table to perform deletion from
	 * @return the minimal form of <code>DELETE</code> statement:<br>
	 * <code><strong>DELETE FROM <em>schemaName</em>.<em>tableName</em></strong></code>
	 */
	public static Delete.All deleteFrom(CharSequence schemaName, CharSequence tableName) {
		return new Delete.All(null, schemaName, tableName);
	}

	/**
	 * Returns the basic form of <code>DELETE</code> statement clearing the table contents.<br>
	 * The result is a complete SQL statement.
	 * @param table the table to perform deletion from
	 * @return the minimal form of <code>DELETE</code> statement:<br>
	 * <code><strong>DELETE FROM [<em>schemaName</em>.]<em>tableName</em></strong></code>
	 */
	public static Delete.All deleteFrom(Table table) {
		return deleteFrom(table.schemaName(), table.tableName());
	}

	/**
	 * Returns the initial part of an <code>INSERT</code> statement.
	 * @param tableName the name of the table to perform insertion into
	 * @return the initial part of the <code>INSERT</code> statement:<br>
	 * <code><strong>INSERT INTO <em>tableName</em></strong></code>
	 */
	public static Insert.Into insertInto(CharSequence tableName) {
		return new Insert.Into(null, InsertVerb.INSERT, null, tableName);
	}

	/**
	 * Returns the initial part of an <code>INSERT</code> statement.
	 * @param schemaName the name of the schema containing the table
	 * @param tableName the name of the table to perform insertion into
	 * @return the initial part of the <code>INSERT</code> statement:<br>
	 * <code><strong>INSERT INTO <em>schemaName</em>.<em>tableName</em></strong></code>
	 */
	public static Insert.Into insertInto(CharSequence schemaName, CharSequence tableName) {
		return new Insert.Into(null, InsertVerb.INSERT, schemaName, tableName);
	}

	/**
	 * Returns the initial part of an <code>INSERT</code> statement.
	 * @param table the table to perform insertion into
	 * @return the initial part of the <code>INSERT</code> statement:<br>
	 * <code><strong>INSERT INTO [<em>schemaName</em>.]<em>tableName</em></strong></code>
	 */
	public static Insert.Into insertInto(Table table) {
		return insertInto(table.schemaName(), table.tableName());
	}

	/**
	 * Returns the initial part of a <code>REPLACE</code> statement.
	 * @param tableName the name of the table to perform insertion into
	 * @return the initial part of the <code>REPLACE</code> statement:<br>
	 * <code><strong>REPLACE INTO <em>tableName</em></strong></code>
	 */
	public static Insert.Into replaceInto(CharSequence tableName) {
		return new Insert.Into(null, InsertVerb.REPLACE, null, tableName);
	}

	/**
	 * Returns the initial part of a <code>REPLACE</code> statement.
	 * @param schemaName the name of the schema containing the table
	 * @param tableName the name of the table to perform insertion into
	 * @return the initial part of the <code>REPLACE</code> statement:<br>
	 * <code><strong>REPLACE INTO <em>schemaName</em>.<em>tableName</em></strong></code>
	 */
	public static Insert.Into replaceInto(CharSequence schemaName, CharSequence tableName) {
		return new Insert.Into(null, InsertVerb.REPLACE, schemaName, tableName);
	}

	/**
	 * Returns the initial part of a <code>REPLACE</code> statement.
	 * @param table the table to perform insertion into
	 * @return the initial part of the <code>REPLACE</code> statement:<br>
	 * <code><strong>REPLACE INTO [<em>schemaName</em>.]<em>tableName</em></strong></code>
	 */
	public static Insert.Into replaceInto(Table table) {
		return replaceInto(table.schemaName(), table.tableName());
	}

	/**
	 * Returns the initial part of an <code>INSERT</code> statement.
	 * @param tableName the name of the table to perform insertion into
	 * @return the initial part of the <code>INSERT</code> statement:<br>
	 * <code><strong>INSERT OR REPLACE INTO <em>tableName</em></strong></code>
	 */
	public static Insert.Into insertOrReplaceInto(CharSequence tableName) {
		return new Insert.Into(null, InsertVerb.INSERT_OR_REPLACE, null, tableName);
	}

	/**
	 * Returns the initial part of an <code>INSERT</code> statement.
	 * @param schemaName the name of the schema containing the table
	 * @param tableName the name of the table to perform insertion into
	 * @return the initial part of the <code>INSERT</code> statement:<br>
	 * <code><strong>INSERT OR REPLACE INTO <em>schemaName</em>.<em>tableName</em></strong></code>
	 */
	public static Insert.Into insertOrReplaceInto(CharSequence schemaName, CharSequence tableName) {
		return new Insert.Into(null, InsertVerb.INSERT_OR_REPLACE, schemaName, tableName);
	}

	/**
	 * Returns the initial part of an <code>INSERT</code> statement.
	 * @param table the table to perform insertion into
	 * @return the initial part of the <code>INSERT</code> statement:<br>
	 * <code><strong>INSERT OR REPLACE INTO [<em>schemaName</em>.]<em>tableName</em></strong></code>
	 */
	public static Insert.Into insertOrReplaceInto(Table table) {
		return insertOrReplaceInto(table.schemaName(), table.tableName());
	}

	/**
	 * Returns the initial part of an <code>INSERT</code> statement.
	 * @param tableName the name of the table to perform insertion into
	 * @return the initial part of the <code>INSERT</code> statement:<br>
	 * <code><strong>INSERT OR ABORT INTO <em>tableName</em></strong></code>
	 */
	public static Insert.Into insertOrAbortInto(CharSequence tableName) {
		return new Insert.Into(null, InsertVerb.INSERT_OR_ABORT, null, tableName);
	}

	/**
	 * Returns the initial part of an <code>INSERT</code> statement.
	 * @param schemaName the name of the schema containing the table
	 * @param tableName the name of the table to perform insertion into
	 * @return the initial part of the <code>INSERT</code> statement:<br>
	 * <code><strong>INSERT OR ABORT INTO <em>schemaName</em>.<em>tableName</em></strong></code>
	 */
	public static Insert.Into insertOrAbortInto(CharSequence schemaName, CharSequence tableName) {
		return new Insert.Into(null, InsertVerb.INSERT_OR_ABORT, schemaName, tableName);
	}

	/**
	 * Returns the initial part of an <code>INSERT</code> statement.
	 * @param table the table to perform insertion into
	 * @return the initial part of the <code>INSERT</code> statement:<br>
	 * <code><strong>INSERT OR ABORT INTO [<em>schemaName</em>.]<em>tableName</em></strong></code>
	 */
	public static Insert.Into insertOrAbortInto(Table table) {
		return insertOrAbortInto(table.schemaName(), table.tableName());
	}

	/**
	 * Returns the initial part of an <code>INSERT</code> statement.
	 * @param tableName the name of the table to perform insertion into
	 * @return the initial part of the <code>INSERT</code> statement:<br>
	 * <code><strong>INSERT OR ROLLBACK INTO <em>tableName</em></strong></code>
	 */
	public static Insert.Into insertOrRollbackInto(CharSequence tableName) {
		return new Insert.Into(null, InsertVerb.INSERT_OR_ROLLBACK, null, tableName);
	}

	/**
	 * Returns the initial part of an <code>INSERT</code> statement.
	 * @param schemaName the name of the schema containing the table
	 * @param tableName the name of the table to perform insertion into
	 * @return the initial part of the <code>INSERT</code> statement:<br>
	 * <code><strong>INSERT OR ROLLBACK INTO <em>schemaName</em>.<em>tableName</em></strong></code>
	 */
	public static Insert.Into insertOrRollbackInto(CharSequence schemaName, CharSequence tableName) {
		return new Insert.Into(null, InsertVerb.INSERT_OR_ROLLBACK, schemaName, tableName);
	}

	/**
	 * Returns the initial part of an <code>INSERT</code> statement.
	 * @param table the table to perform insertion into
	 * @return the initial part of the <code>INSERT</code> statement:<br>
	 * <code><strong>INSERT OR ROLLBACK INTO [<em>schemaName</em>.]<em>tableName</em></strong></code>
	 */
	public static Insert.Into insertOrRollbackInto(Table table) {
		return insertOrRollbackInto(table.schemaName(), table.tableName());
	}

	/**
	 * Returns the initial part of an <code>INSERT</code> statement.
	 * @param tableName the name of the table to perform insertion into
	 * @return the initial part of the <code>INSERT</code> statement:<br>
	 * <code><strong>INSERT OR FAIL INTO <em>tableName</em></strong></code>
	 */
	public static Insert.Into insertOrFailInto(CharSequence tableName) {
		return new Insert.Into(null, InsertVerb.INSERT_OR_FAIL, null, tableName);
	}

	/**
	 * Returns the initial part of an <code>INSERT</code> statement.
	 * @param schemaName the name of the schema containing the table
	 * @param tableName the name of the table to perform insertion into
	 * @return the initial part of the <code>INSERT</code> statement:<br>
	 * <code><strong>INSERT OR FAIL INTO <em>schemaName</em>.<em>tableName</em></strong></code>
	 */
	public static Insert.Into insertOrFailInto(CharSequence schemaName, CharSequence tableName) {
		return new Insert.Into(null, InsertVerb.INSERT_OR_FAIL, schemaName, tableName);
	}

	/**
	 * Returns the initial part of an <code>INSERT</code> statement.
	 * @param table the table to perform insertion into
	 * @return the initial part of the <code>INSERT</code> statement:<br>
	 * <code><strong>INSERT OR FAIL INTO [<em>schemaName</em>.]<em>tableName</em></strong></code>
	 */
	public static Insert.Into insertOrFailInto(Table table) {
		return insertOrFailInto(table.schemaName(), table.tableName());
	}

	/**
	 * Returns the initial part of an <code>INSERT</code> statement.
	 * @param tableName the name of the table to perform insertion into
	 * @return the initial part of the <code>INSERT</code> statement:<br>
	 * <code><strong>INSERT OR IGNORE INTO <em>tableName</em></strong></code>
	 */
	public static Insert.Into insertOrIgnoreInto(CharSequence tableName) {
		return new Insert.Into(null, InsertVerb.INSERT_OR_IGNORE, null, tableName);
	}

	/**
	 * Returns the initial part of an <code>INSERT</code> statement.
	 * @param schemaName the name of the schema containing the table
	 * @param tableName the name of the table to perform insertion into
	 * @return the initial part of the <code>INSERT</code> statement:<br>
	 * <code><strong>INSERT OR IGNORE INTO <em>schemaName</em>.<em>tableName</em></strong></code>
	 */
	public static Insert.Into insertOrIgnoreInto(CharSequence schemaName, CharSequence tableName) {
		return new Insert.Into(null, InsertVerb.INSERT_OR_IGNORE, schemaName, tableName);
	}

	/**
	 * Returns the initial part of an <code>INSERT</code> statement.
	 * @param table the table to perform insertion into
	 * @return the initial part of the <code>INSERT</code> statement:<br>
	 * <code><strong>INSERT OR IGNORE INTO [<em>schemaName</em>.]<em>tableName</em></strong></code>
	 */
	public static Insert.Into insertOrIgnoreInto(Table table) {
		return insertOrIgnoreInto(table.schemaName(), table.tableName());
	}

	/**
	 * Returns the initial part of an <code>UPDATE</code> statement.
	 * @param tableName the name of the table to perform the update of
	 * @return the initial part of the <code>UPDATE</code> statement:<br>
	 * <code><strong>UPDATE <em>tableName</em></strong></code>
	 */
	public static Update.Stub update(CharSequence tableName) {
		return new Update.Stub(null, UpdateVerb.UPDATE, null, tableName);
	}

	/**
	 * Returns the initial part of an <code>UPDATE</code> statement.
	 * @param schemaName  the name of the schema containing the target database
	 * @param tableName the name of the table to perform the update of
	 * @return the initial part of the <code>UPDATE</code> statement:<br>
	 * <code><strong>UPDATE <em>schemaName</em>.<em>tableName</em></strong></code>
	 */
	public static Update.Stub update(CharSequence schemaName, CharSequence tableName) {
		return new Update.Stub(null, UpdateVerb.UPDATE, schemaName, tableName);
	}

	/**
	 * Returns the initial part of an <code>UPDATE</code> statement.
	 * @param table the table to perform the update of
	 * @return the initial part of the <code>UPDATE</code> statement:<br>
	 * <code><strong>UPDATE [<em>schemaName</em>.]<em>tableName</em></strong></code>
	 */
	public static Update.Stub update(Table table) {
		return update(table.schemaName(), table.tableName());
	}

	/**
	 * Returns the initial part of an <code>UPDATE</code> statement.
	 * @param tableName the name of the table to perform the update of
	 * @return the initial part of the <code>UPDATE</code> statement:<br>
	 * <code><strong>UPDATE OR ABORT <em>tableName</em></strong></code>
	 */
	public static Update.Stub updateOrAbort(CharSequence tableName) {
		return new Update.Stub(null, UpdateVerb.UPDATE_OR_ABORT, null, tableName);
	}

	/**
	 * Returns the initial part of an <code>UPDATE</code> statement.
	 * @param schemaName  the name of the schema containing the target database
	 * @param tableName the name of the table to perform the update of
	 * @return the initial part of the <code>UPDATE</code> statement:<br>
	 * <code><strong>UPDATE OR ABORT <em>schemaName</em>.<em>tableName</em></strong></code>
	 */
	public static Update.Stub updateOrAbort(CharSequence schemaName, CharSequence tableName) {
		return new Update.Stub(null, UpdateVerb.UPDATE_OR_ABORT, schemaName, tableName);
	}


	/**
	 * Returns the initial part of an <code>UPDATE</code> statement.
	 * @param table the table to perform the update of
	 * @return the initial part of the <code>UPDATE</code> statement:<br>
	 * <code><strong>UPDATE OR ABORT [<em>schemaName</em>.]<em>tableName</em></strong></code>
	 */
	public static Update.Stub updateOrAbort(Table table) {
		return updateOrAbort(table.schemaName(), table.tableName());
	}


	/**
	 * Returns the initial part of an <code>UPDATE</code> statement.
	 * @param tableName the name of the table to perform the update of
	 * @return the initial part of the <code>UPDATE</code> statement:<br>
	 * <code><strong>UPDATE OR FAIL <em>tableName</em></strong></code>
	 */
	public static Update.Stub updateOrFail(CharSequence tableName) {
		return new Update.Stub(null, UpdateVerb.UPDATE_OR_FAIL, null, tableName);
	}

	/**
	 * Returns the initial part of an <code>UPDATE</code> statement.
	 * @param schemaName  the name of the schema containing the target database
	 * @param tableName the name of the table to perform the update of
	 * @return the initial part of the <code>UPDATE</code> statement:<br>
	 * <code><strong>UPDATE OR FAIL <em>schemaName</em>.<em>tableName</em></strong></code>
	 */
	public static Update.Stub updateOrFail(CharSequence schemaName, CharSequence tableName) {
		return new Update.Stub(null, UpdateVerb.UPDATE_OR_FAIL, schemaName, tableName);
	}

	/**
	 * Returns the initial part of an <code>UPDATE</code> statement.
	 * @param table the table to perform the update of
	 * @return the initial part of the <code>UPDATE</code> statement:<br>
	 * <code><strong>UPDATE OR FAIL [<em>schemaName</em>.]<em>tableName</em></strong></code>
	 */
	public static Update.Stub updateOrFail(Table table) {
		return updateOrFail(table.schemaName(), table.tableName());
	}

	/**
	 * Returns the initial part of an <code>UPDATE</code> statement.
	 * @param tableName the name of the table to perform the update of
	 * @return the initial part of the <code>UPDATE</code> statement:<br>
	 * <code><strong>UPDATE OR IGNORE <em>tableName</em></strong></code>
	 */
	public static Update.Stub updateOrIgnore(CharSequence tableName) {
		return new Update.Stub(null, UpdateVerb.UPDATE_OR_IGNORE, null, tableName);
	}

	/**
	 * Returns the initial part of an <code>UPDATE</code> statement.
	 * @param schemaName  the name of the schema containing the target database
	 * @param tableName the name of the table to perform the update of
	 * @return the initial part of the <code>UPDATE</code> statement:<br>
	 * <code><strong>UPDATE OR IGNORE <em>schemaName</em>.<em>tableName</em></strong></code>
	 */
	public static Update.Stub updateOrIgnore(CharSequence schemaName, CharSequence tableName) {
		return new Update.Stub(null, UpdateVerb.UPDATE_OR_IGNORE, schemaName, tableName);
	}


	/**
	 * Returns the initial part of an <code>UPDATE</code> statement.
	 * @param table the table to perform the update of
	 * @return the initial part of the <code>UPDATE</code> statement:<br>
	 * <code><strong>UPDATE OR IGNORE [<em>schemaName</em>.]<em>tableName</em></strong></code>
	 */
	public static Update.Stub updateOrIgnore(Table table) {
		return updateOrIgnore(table.schemaName(), table.tableName());
	}

	/**
	 * Returns the initial part of an <code>UPDATE</code> statement.
	 * @param tableName the name of the table to perform the update of
	 * @return the initial part of the <code>UPDATE</code> statement:<br>
	 * <code><strong>UPDATE OR REPLACE <em>tableName</em></strong></code>
	 */
	public static Update.Stub updateOrReplace(CharSequence tableName) {
		return new Update.Stub(null, UpdateVerb.UPDATE_OR_REPLACE, null, tableName);
	}

	/**
	 * Returns the initial part of an <code>UPDATE</code> statement.
	 * @param schemaName  the name of the schema containing the target database
	 * @param tableName the name of the table to perform the update of
	 * @return the initial part of the <code>UPDATE</code> statement:<br>
	 * <code><strong>UPDATE OR REPLACE <em>schemaName</em>.<em>tableName</em></strong></code>
	 */
	public static Update.Stub updateOrReplace(CharSequence schemaName, CharSequence tableName) {
		return new Update.Stub(null, UpdateVerb.UPDATE_OR_REPLACE, schemaName, tableName);
	}

	/**
	 * Returns the initial part of an <code>UPDATE</code> statement.
	 * @param table the table to perform the update of
	 * @return the initial part of the <code>UPDATE</code> statement:<br>
	 * <code><strong>UPDATE OR REPLACE [<em>schemaName</em>.]<em>tableName</em></strong></code>
	 */
	public static Update.Stub updateOrReplace(Table table) {
		return updateOrReplace(table.schemaName(), table.tableName());
	}

	/**
	 * Returns the initial part of an <code>UPDATE</code> statement.
	 * @param tableName the name of the table to perform the update of
	 * @return the initial part of the <code>UPDATE</code> statement:<br>
	 * <code><strong>UPDATE OR ROLLBACK <em>tableName</em></strong></code>
	 */
	public static Update.Stub updateOrRollback(CharSequence tableName) {
		return new Update.Stub(null, UpdateVerb.UPDATE_OR_ROLLBACK, null, tableName);
	}

	/**
	 * Returns the initial part of an <code>UPDATE</code> statement.
	 * @param schemaName  the name of the schema containing the target database
	 * @param tableName the name of the table to perform the update of
	 * @return the initial part of the <code>UPDATE</code> statement:<br>
	 * <code><strong>UPDATE OR ROLLBACK <em>schemaName</em>.<em>tableName</em></strong></code>
	 */
	public static Update.Stub updateOrRollback(CharSequence schemaName, CharSequence tableName) {
		return new Update.Stub(null, UpdateVerb.UPDATE_OR_ROLLBACK, schemaName, tableName);
	}

	/**
	 * Returns the initial part of an <code>UPDATE</code> statement.
	 * @param table the table to perform the update of
	 * @return the initial part of the <code>UPDATE</code> statement:<br>
	 * <code><strong>UPDATE OR ROLLBACK [<em>schemaName</em>.]<em>tableName</em></strong></code>
	 */
	public static Update.Stub updateOrRollback(Table table) {
		return updateOrRollback(table.schemaName(), table.tableName());
	}

	/**
	 * Returns the <code>DETACH</code> statement for the specified database.<br>
	 * The result is a complete SQL statement.
	 * @param schemaName the name of the schema (database) to detach
	 * @return the statement having the form<br>
	 * <code><strong>DETACH <em>schemaName</em></strong></code>
	 */
	public static Detach detach(CharSequence schemaName) {
		return new Detach(schemaName);
	}

	/**
	 * Returns the <code>DROP INDEX</code> statement for the specified index.<br>
	 * The result is a complete SQL statement.
	 * @param indexName the name of the index to drop
	 * @return the statement having the form<br>
	 * <code><strong>DROP INDEX <em>indexName</em></strong></code>
	 */
	public static Drop.Index dropIndex(CharSequence indexName) {
		return new Drop.Index(false, null, indexName);
	}

	/**
	 * Returns the <code>DROP INDEX</code> statement for the specified index.<br>
	 * The result is a complete SQL statement.
	 * @param schemaName the name of the schema containing the index
	 * @param indexName the name of the index to drop
	 * @return the statement having the form<br>
	 * <code><strong>DROP INDEX <em>schemaName</em>.<em>indexName</em></strong></code>
	 */
	public static Drop.Index dropIndex(CharSequence schemaName, CharSequence indexName) {
		return new Drop.Index(false, schemaName, indexName);
	}

	/**
	 * Returns the <code>DROP INDEX</code> statement for the specified index.<br>
	 * The result is a complete SQL statement.
	 * @param indexName the name of the index to drop
	 * @return the statement having the form<br>
	 * <code><strong>DROP INDEX IF EXISTS <em>indexName</em></strong></code>
	 */
	public static Drop.Index dropIndexIfExists(CharSequence indexName) {
		return new Drop.Index(true, null, indexName);
	}

	/**
	 * Returns the <code>DROP INDEX</code> statement for the specified index.<br>
	 * The result is a complete SQL statement.
	 * @param schemaName the name of the schema containing the index
	 * @param indexName the name of the index to drop
	 * @return the statement having the form<br>
	 * <code><strong>DROP INDEX IF EXISTS <em>schemaName</em>.<em>indexName</em></strong></code>
	 */
	public static Drop.Index dropIndexIfExists(CharSequence schemaName, CharSequence indexName) {
		return new Drop.Index(true, schemaName, indexName);
	}

	/**
	 * Returns the <code>DROP TABLE</code> statement for the specified table.<br>
	 * The result is a complete SQL statement.
	 * @param tableName the name of the table to drop
	 * @return the statement having the form<br>
	 * <code><strong>DROP TABLE <em>tableName</em></strong></code>
	 */
	public static Drop.Table dropTable(CharSequence tableName) {
		return new Drop.Table(false, null, tableName);
	}

	/**
	 * Returns the <code>DROP TABLE</code> statement for the specified table.<br>
	 * The result is a complete SQL statement.
	 * @param schemaName the name of the schema containing the table
	 * @param tableName the name of the table to drop
	 * @return the statement having the form<br>
	 * <code><strong>DROP TABLE <em>schemaName</em>.<em>tableName</em></strong></code>
	 */
	public static Drop.Table dropTable(CharSequence schemaName, CharSequence tableName) {
		return new Drop.Table(false, schemaName, tableName);
	}

	/**
	 * Returns the <code>DROP TABLE</code> statement for the specified table.<br>
	 * The result is a complete SQL statement.
	 * @param table the table to drop
	 * @return the statement having the form<br>
	 * <code><strong>DROP TABLE [<em>schemaName</em>.]<em>tableName</em></strong></code>
	 */
	public static Drop.Table dropTable(Table table) {
		return dropTable(table.schemaName(), table.tableName());
	}

	/**
	 * Returns the <code>DROP TABLE</code> statement for the specified table.<br>
	 * The result is a complete SQL statement.
	 * @param tableName the name of the table to drop
	 * @return the statement having the form<br>
	 * <code><strong>DROP TABLE IF EXISTS <em>tableName</em></strong></code>
	 */
	public static Drop.Table dropTableIfExists(CharSequence tableName) {
		return new Drop.Table(true, null, tableName);
	}

	/**
	 * Returns the <code>DROP TABLE</code> statement for the specified table.<br>
	 * The result is a complete SQL statement.
	 * @param schemaName the name of the schema containing the table
	 * @param tableName the name of the table to drop
	 * @return the statement having the form<br>
	 * <code><strong>DROP TABLE IF EXISTS <em>schemaName</em>.<em>tableName</em></strong></code>
	 */
	public static Drop.Table dropTableIfExists(CharSequence schemaName, CharSequence tableName) {
		return new Drop.Table(true, schemaName, tableName);
	}

	/**
	 * Returns the <code>DROP TABLE</code> statement for the specified table.<br>
	 * The result is a complete SQL statement.
	 * @param table the table to drop
	 * @return the statement having the form<br>
	 * <code><strong>DROP TABLE IF EXISTS [<em>schemaName</em>.]<em>tableName</em></strong></code>
	 */
	public static Drop.Table dropTableIfExists(Table table) {
		return dropTableIfExists(table.schemaName(), table.tableName());
	}

	/**
	 * Returns the <code>DROP TRIGGER</code> statement for the specified trigger.<br>
	 * The result is a complete SQL statement.
	 * @param triggerName the name of the trigger to drop
	 * @return the statement having the form<br>
	 * <code><strong>DROP TRIGGER <em>triggerName</em></strong></code>
	 */
	public static Drop.Trigger dropTrigger(CharSequence triggerName) {
		return new Drop.Trigger(false, null, triggerName);
	}

	/**
	 * Returns the <code>DROP TRIGGER</code> statement for the specified trigger.<br>
	 * The result is a complete SQL statement.
	 * @param schemaName the name of the schema containing the trigger
	 * @param triggerName the name of the trigger to drop
	 * @return the statement having the form<br>
	 * <code><strong>DROP TRIGGER <em>schemaName</em>.<em>triggerName</em></strong></code>
	 */
	public static Drop.Trigger dropTrigger(CharSequence schemaName, CharSequence triggerName) {
		return new Drop.Trigger(false, schemaName, triggerName);
	}

	/**
	 * Returns the <code>DROP TRIGGER</code> statement for the specified trigger.<br>
	 * The result is a complete SQL statement.
	 * @param triggerName the name of the trigger to drop
	 * @return the statement having the form<br>
	 * <code><strong>DROP TRIGGER IF EXISTS <em>triggerName</em></strong></code>
	 */
	public static Drop.Trigger dropTriggerIfExists(CharSequence triggerName) {
		return new Drop.Trigger(true, null, triggerName);
	}

	/**
	 * Returns the <code>DROP TRIGGER</code> statement for the specified trigger.<br>
	 * The result is a complete SQL statement.
	 * @param schemaName the name of the schema containing the trigger
	 * @param triggerName the name of the trigger to drop
	 * @return the statement having the form<br>
	 * <code><strong>DROP TRIGGER IF EXISTS <em>schemaName</em>.<em>triggerName</em></strong></code>
	 */
	public static Drop.Trigger dropTriggerIfExists(CharSequence schemaName, CharSequence triggerName) {
		return new Drop.Trigger(true, schemaName, triggerName);
	}

	/**
	 * Returns the <code>DROP VIEW</code> statement for the specified view.<br>
	 * The result is a complete SQL statement.
	 * @param viewName the name of the view to drop
	 * @return the statement having the form<br>
	 * <code><strong>DROP VIEW <em>viewName</em></strong></code>
	 */
	public static Drop.View dropView(CharSequence viewName) {
		return new Drop.View(false, null, viewName);
	}

	/**
	 * Returns the <code>DROP VIEW</code> statement for the specified view.<br>
	 * The result is a complete SQL statement.
	 * @param schemaName the name of the schema containing the view
	 * @param viewName the name of the view to drop
	 * @return the statement having the form<br>
	 * <code><strong>DROP VIEW <em>schemaName</em>.<em>viewName</em></strong></code>
	 */
	public static Drop.View dropView(CharSequence schemaName, CharSequence viewName) {
		return new Drop.View(false, schemaName, viewName);
	}

	/**
	 * Returns the <code>DROP VIEW</code> statement for the specified view.<br>
	 * The result is a complete SQL statement.
	 * @param viewName the name of the view to drop
	 * @return the statement having the form<br>
	 * <code><strong>DROP VIEW IF EXISTS <em>viewName</em></strong></code>
	 */
	public static Drop.View dropViewIfExists(CharSequence viewName) {
		return new Drop.View(true, null, viewName);
	}

	/**
	 * Returns the <code>DROP VIEW</code> statement for the specified view.<br>
	 * The result is a complete SQL statement.
	 * @param schemaName the name of the schema containing the view
	 * @param viewName the name of the view to drop
	 * @return the statement having the form<br>
	 * <code><strong>DROP VIEW IF EXISTS <em>schemaName</em>.<em>viewName</em></strong></code>
	 */
	public static Drop.View dropViewIfExists(CharSequence schemaName, CharSequence viewName) {
		return new Drop.View(true, schemaName, viewName);
	}

	/**
	 * Returns the <code><strong>REINDEX</strong></code> statement to reindex all the attached databases.<br>
	 * The result is a complete statement.
	 * @return the <code><strong>REINDEX</strong></code> statement
	 *
	 */
	public static Reindex reindex() {
		return Reindex.ALL;
	}

	/**
	 * Returns the <code>REINDEX</code> statement for the specific collation sequence.<br>
	 * The result is a complete statement.
	 * @param collation the collation sequence used to determine which indices need to be updated
	 * @return the statement having the form<br>
	 * <code><strong>REINDEX <em>collation</em></strong></code>
	 */
	public static Reindex reindex(Collation collation) {
		return new Reindex(collation);
	}

	/**
	 * Returns the <code>REINDEX</code> statement for the specific table or index.<br>
	 * The result is a complete statement.
	 * @param tableOrIndexName the name of the target table or index
	 * @return the statement having the form<br>
	 * <code><strong>REINDEX <em>tableOrIndexName</em></strong></code>
	 */
	public static Reindex reindex(CharSequence tableOrIndexName) {
		return new Reindex(null, tableOrIndexName);
	}

	/**
	 * Returns the <code>REINDEX</code> statement for the specific table or index.<br>
	 * The result is a complete statement.
	 * @param schemaName the name of the database containing the table or the index
	 * @param tableOrIndexName the name of the target table or index
	 * @return the statement having the form<br>
	 * <code><strong>REINDEX <em>schemaName</em>.<em>tableOrIndexName</em></strong></code>
	 */
	public static Reindex reindex(CharSequence schemaName, CharSequence tableOrIndexName) {
		return new Reindex(schemaName, tableOrIndexName);
	}

	/**
	 * Returns the <code>RELEASE</code> statement releasing the specified savepoint.<br>
	 * The result is a complete statement.
	 * @param savepointName the name of the savepoint to release
	 * @return the statement having the form<br>
	 * <code><strong>RELEASE <em>savepointName</em></strong></code>
	 */
	public static ReleaseSavepoint releaseSavepoint(CharSequence savepointName) {
		return new ReleaseSavepoint(savepointName);
	}

	/**
	 * Returns the <code><strong>ROLLBACK TRANSACTION</strong></code> statement.<br>
	 * The result is a complete statement.
	 * @return the <code><strong>ROLLBACK TRANSACTION</strong></code> statement
	 */
	public static RollbackTransaction rollbackTransaction() {
		return RollbackTransaction.OUTER;
	}

	/**
	 * Returns the <code>ROLLBACK TRANSACTION</code> statement for the specified savepoint.<br>
	 * The result is a complete statement.
	 * @param savepointName the name of the savepoint to rollback to
	 * @return the statement having the form<br>
	 * <code><strong>ROLLBACK TRANSACTION TO SAVEPOINT <em>savepointName</em></strong></code>
	 */
	public static RollbackTransaction rollbackTransactionToSavepoint(CharSequence savepointName) {
		return new RollbackTransaction(savepointName);
	}

	/**
	 * Returns the <code>SAVEPOINT</code> statement creating a savepoint with the specified name.<br>
	 * The result is a complete statement.
	 * @param savepointName the name of the new savepoint
	 * @return the statement having the form<br>
	 * <code><strong>SAVEPOINT <em>savepointName</em></strong></code>
	 */
	public static Savepoint savepoint(CharSequence savepointName) {
		return new Savepoint(savepointName);
	}

	/**
	 * Returns the <code><strong>VACUUM</strong></code> statement executing the vacuuming operation on
	 * all the attached databases.<br>
	 * The result is a complete statement.
	 * @return the <code><strong>VACUUM</strong></code> statement
	 */
	public static Vacuum vacuum() {
		return Vacuum.ALL;
	}

	/**
	 * Returns the <code>VACUUM</code> statement executing the vacuuming operation on the specified
	 * database.<br>
	 * The result is a complete statement.
	 * @param schemaName the name of the database (schema) to perform vacuuming of
	 * @return the statement having the form<br>
	 * <code><strong>VACUUM <em>schemaName</em></strong></code>
	 */
	public static Vacuum vacuum(CharSequence schemaName) {
		return new Vacuum(schemaName);
	}

	/**
	 * Returns the <code>PRAGMA</code> statement with specified command name and no argument.
	 * @param pragmaName the name of the <code>PRAGMA</code> command
	 * @return the statement having the form<br>
	 * <code><strong>PRAGMA <em>pragmaName</em></strong></code>
	 */
	public static Pragma pragma(String pragmaName) {
		return new Pragma(null, pragmaName);
	}

	/**
	 * Returns the <code>PRAGMA</code> statement with specified command name and target database,
	 * without an argument.
	 * @param schemaName the name of the target database (schema)
	 * @param pragmaName the name of the <code>PRAGMA</code> command
	 * @return the statement having the form<br>
	 * <code><strong>PRAGMA <em>schemaName</em>.<em>pragmaName</em></strong></code>
	 */
	public static Pragma pragma(CharSequence schemaName, String pragmaName) {
		return new Pragma(schemaName, pragmaName);
	}

	/**
	 * Initializes a common table (<code>WITH</code> clause) construction.
	 * @param cteName the name of the first common table in the common table expression
	 * @return the initial stage of common table expression construction
	 */
	public static CteBuilderNoColumns with(String cteName) {
		return new CteBuilderNoColumns(null, cteName);
	}
}
