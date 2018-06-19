package dev.ornamental.sqlite.statement;

/**
 * This class contains static factory methods for the standard non-deprecated <code>PRAGMA</code>s.<br/>
 */
public final class Pragmas {

	private Pragmas() { }

	/**
	 * Produces the statement<br/>
	 * <code>PRAGMA application_id</code><br/>
	 * retrieving the application ID of the main database.
	 */
	public static Pragma applicationId() {
		return SqlStatements.pragma(Pragma.Name.APPLICATION_ID);
	}

	/**
	 * Produces the statement<br/>
	 * <code>PRAGMA schemaName.application_id</code><br/>
	 * retrieving the application ID of the specified database.
	 */
	public static Pragma applicationId(CharSequence schemaName) {
		return SqlStatements.pragma(schemaName, Pragma.Name.APPLICATION_ID);
	}

	/**
	 * Produces the statement<br/>
	 * <code>PRAGMA application_id(value)</code><br/>
	 * setting the application ID of the main database.
	 */
	public static Pragma.SetNumber applicationId(int value) {
		return SqlStatements.pragma(Pragma.Name.APPLICATION_ID).withValue(value);
	}

	/**
	 * Produces the statement<br/>
	 * <code>PRAGMA schemaName.application_id(value)</code><br/>
	 * setting the application ID of the specified database.
	 */
	public static Pragma.SetNumber applicationId(CharSequence schemaName, int value) {
		return SqlStatements.pragma(schemaName, Pragma.Name.APPLICATION_ID).withValue(value);
	}

	/**
	 * Produces the statement<br/>
	 * <code>PRAGMA auto_vacuum</code><br/>
	 * retrieving the auto-vacuum status of the main database.
	 */
	public static Pragma autoVacuum() {
		return SqlStatements.pragma(Pragma.Name.AUTO_VACUUM);
	}

	/**
	 * Produces the statement<br/>
	 * <code>PRAGMA schemaName.auto_vacuum</code><br/>
	 * retrieving the auto-vacuum status of the specified database.
	 */
	public static Pragma autoVacuum(CharSequence schemaName) {
		return SqlStatements.pragma(schemaName, Pragma.Name.AUTO_VACUUM);
	}

	/**
	 * Produces the statement<br/>
	 * <code>PRAGMA auto_vacuum(NONE|FULL|INCREMENTAL)</code><br/>
	 * setting the auto-vacuum status of the main database.
	 */
	public static Pragma.ForName autoVacuum(AutoVacuumMode mode) {
		return SqlStatements.pragma(Pragma.Name.AUTO_VACUUM).forName(mode.toString());
	}

	/**
	 * Produces the statement<br/>
	 * <code>PRAGMA schemaName.auto_vacuum(NONE|FULL|INCREMENTAL)</code><br/>
	 * setting the auto-vacuum status of the specified database.
	 */
	public static Pragma.ForName autoVacuum(CharSequence schemaName, AutoVacuumMode mode) {
		return SqlStatements.pragma(schemaName, Pragma.Name.AUTO_VACUUM).forName(mode.toString());
	}

	/**
	 * Produces the statement<br/>
	 * <code>PRAGMA automatic_index</code><br/>
	 * retrieving the status of automatic indexing.
	 */
	public static Pragma automaticIndex() {
		return SqlStatements.pragma(Pragma.Name.AUTOMATIC_INDEX);
	}

	/**
	 * Produces the statement<br/>
	 * <code>PRAGMA automatic_index(TRUE|FALSE)</code><br/>
	 * turning the automatic indices on or off.
	 */
	public static Pragma.SetBoolean automaticIndex(boolean on) {
		return SqlStatements.pragma(Pragma.Name.AUTOMATIC_INDEX).withValue(on);
	}

	/**
	 * Produces the statement<br/>
	 * <code>PRAGMA busy_timeout</code><br/>
	 * retrieving the value of busy timeout.
	 */
	public static Pragma busyTimeout() {
		return SqlStatements.pragma(Pragma.Name.BUSY_TIMEOUT);
	}

	/**
	 * Produces the statement<br/>
	 * <code>PRAGMA busy_timeout(milliseconds)</code><br/>
	 * setting the value of busy timeout.
	 * @param milliseconds a positive value for the busy timeout, in milliseconds,
	 * or {@literal null} to turn the busy timeout handlers off
	 */
	public static Pragma.SetNumber busyTimeout(Integer milliseconds) {
		if (milliseconds != null && milliseconds <= 0) {
			throw new IllegalArgumentException(
				"The busy timeout must be positive or null to turn the busy handlers off.");
		}
		return SqlStatements.pragma(Pragma.Name.BUSY_TIMEOUT).withValue(milliseconds == null ? 0 : milliseconds);
	}

	/**
	 * Produces the statement<br/>
	 * <code>PRAGMA cache_size</code><br/>
	 * retrieving the cache size of the main database, in pages.
	 */
	public static Pragma cacheSize() {
		return SqlStatements.pragma(Pragma.Name.CACHE_SIZE);
	}

	/**
	 * Produces the statement<br/>
	 * <code>PRAGMA schemaName.cache_size</code><br/>
	 * retrieving the cache size of the specified database, in pages.
	 */
	public static Pragma cacheSize(CharSequence schemaName) {
		return SqlStatements.pragma(schemaName, Pragma.Name.CACHE_SIZE);
	}

	/**
	 * Produces the statement<br/>
	 * <code>PRAGMA cache_size(±size)</code><br/>
	 * setting the cache size of the main database, either in pages or in kibibytes.
	 */
	public static Pragma.SetNumber cacheSize(int size, CacheSizeUnit unit) {
		return SqlStatements.pragma(Pragma.Name.CACHE_SIZE).withValue(unit == CacheSizeUnit.PAGE ? size : -size);
	}

	/**
	 * Produces the statement<br/>
	 * <code>PRAGMA schemaName.cache_size(±size)</code><br/>
	 * setting the cache size of the specified database, either in pages or in kibibytes.
	 */
	public static Pragma.SetNumber cacheSize(CharSequence schemaName, int size, CacheSizeUnit unit) {
		return SqlStatements.pragma(schemaName, Pragma.Name.CACHE_SIZE)
			.withValue(unit == CacheSizeUnit.PAGE ? size : -size);
	}

	/**
	 * Produces the statement<br/>
	 * <code>PRAGMA cache_spill</code><br/>
	 * retrieving the minimum number of pages used by the cache of the main database
	 * in order for cache spilling to occur.
	 */
	public static Pragma cacheSpill() {
		return SqlStatements.pragma(Pragma.Name.CACHE_SPILL);
	}

	/**
	 * Produces the statement<br/>
	 * <code>PRAGMA schemaName.cache_spill</code><br/>
	 * retrieving the minimum number of pages used by the cache of the specified database
	 * in order for cache spilling to occur.
	 */
	public static Pragma cacheSpill(CharSequence schemaName) {
		return SqlStatements.pragma(schemaName, Pragma.Name.CACHE_SPILL);
	}

	/**
	 * Produces the statement<br/>
	 * <code>PRAGMA cache_spill(TRUE|FALSE)</code><br/>
	 * setting or clearing the cache spill flag on all the databases.
	 */
	public static Pragma.SetBoolean cacheSpill(boolean on) {
		return SqlStatements.pragma(Pragma.Name.CACHE_SPILL).withValue(on);
	}

	/**
	 * Produces the statement<br/>
	 * <code>PRAGMA cache_spill(pagesThreshold)</code><br/>
	 * setting the minimum number of pages used by the cache of the main database
	 * in order for cache spilling to occur.
	 */
	public static Pragma.SetNumber cacheSpill(int pagesThreshold) {
		return SqlStatements.pragma(Pragma.Name.CACHE_SPILL).withValue(pagesThreshold);
	}

	/**
	 * Produces the statement<br/>
	 * <code>PRAGMA schemaName.cache_spill(pagesThreshold)</code><br/>
	 * setting the minimum number of pages used by the cache of the specified database
	 * in order for cache spilling to occur.
	 */
	public static Pragma.SetNumber cacheSpill(CharSequence schemaName, int pagesThreshold) {
		return SqlStatements.pragma(schemaName, Pragma.Name.CACHE_SPILL).withValue(pagesThreshold);
	}

	/**
	 * Produces the statement<br/>
	 * <code>PRAGMA case_sensitive_like(TRUE|FALSE)</code><br/>
	 * setting or clearing the <code>LIKE</code> expression case sensitivity flag.
	 */
	public static Pragma.SetBoolean caseSensitiveLike(boolean on) {
		return SqlStatements.pragma(Pragma.Name.CASE_SENSITIVE_LIKE).withValue(on);
	}

	/**
	 * Produces the statement<br/>
	 * <code>PRAGMA cell_size_check</code><br/>
	 * retrieving the cell size check flag.
	 */
	public static Pragma cellSizeCheck() {
		return SqlStatements.pragma(Pragma.Name.CELL_SIZE_CHECK);
	}

	/**
	 * Produces the statement<br/>
	 * <code>PRAGMA cell_size_check(TRUE|FALSE)</code><br/>
	 * setting or clearing the cell size check flag.
	 */
	public static Pragma.SetBoolean cellSizeCheck(boolean on) {
		return SqlStatements.pragma(Pragma.Name.CELL_SIZE_CHECK).withValue(on);
	}

	/**
	 * Produces the statement<br/>
	 * <code>PRAGMA checkpoint_fullfsync</code><br/>
	 * retrieving the <code>fullfsync</code> flag for checkpoint operations.
	 */
	public static Pragma checkpointFullFsync() {
		return SqlStatements.pragma(Pragma.Name.CHECKPOINT_FULLFSYNC);
	}

	/**
	 * Produces the statement<br/>
	 * <code>PRAGMA checkpoint_fullfsync(TRUE|FALSE)</code><br/>
	 * setting or clearing the <code>fullfsync</code> flag for checkpoint operations.
	 */
	public static Pragma.SetBoolean checkpointFullFsync(boolean on) {
		return SqlStatements.pragma(Pragma.Name.CHECKPOINT_FULLFSYNC).withValue(on);
	}

	/**
	 * Produces the statement<br/>
	 * <code>PRAGMA collation_list</code><br/>
	 * retrieving the list of the defined collating sequences.
	 */
	public static Pragma collationList() {
		return SqlStatements.pragma(Pragma.Name.COLLATION_LIST);
	}

	/**
	 * Produces the statement<br/>
	 * <code>PRAGMA compile_options</code><br/>
	 * retrieving the list of options used on SQLite compilation.
	 */
	public static Pragma compileOptions() {
		return SqlStatements.pragma(Pragma.Name.COMPILE_OPTIONS);
	}

	/**
	 * Produces the statement<br/>
	 * <code>PRAGMA data_version</code><br/>
	 * retrieving the connection-bound identifier of the last commit of the main database.
	 */
	public static Pragma dataVersion() {
		return SqlStatements.pragma(Pragma.Name.DATA_VERSION);
	}

	/**
	 * Produces the statement<br/>
	 * <code>PRAGMA schemaName.data_version</code><br/>
	 * retrieving the connection-bound identifier of the last commit of the specified database.
	 */
	public static Pragma dataVersion(CharSequence schemaName) {
		return SqlStatements.pragma(schemaName, Pragma.Name.DATA_VERSION);
	}

	/**
	 * Produces the statement<br/>
	 * <code>PRAGMA database_list</code><br/>
	 * returning the databases attached to the current connection.
	 */
	public static Pragma databaseList() {
		return SqlStatements.pragma(Pragma.Name.DATABASE_LIST);
	}

	/**
	 * Produces the statement<br/>
	 * <code>PRAGMA defer_foreign_keys</code><br/>
	 * indicating if deferred foreign key constraint enforcement is enabled for the current transaction.
	 */
	public static Pragma deferForeignKeys() {
		return SqlStatements.pragma(Pragma.Name.DEFER_FOREIGN_KEYS);
	}

	/**
	 * Produces the statement<br/>
	 * <code>PRAGMA defer_foreign_keys(TRUE|FALSE)</code><br/>
	 * setting or clearing the flag of deferred foreign key constraint enforcement.
	 */
	public static Pragma.SetBoolean deferForeignKeys(boolean on) {
		return SqlStatements.pragma(Pragma.Name.DEFER_FOREIGN_KEYS).withValue(on);
	}

	/**
	 * Produces the statement<br/>
	 * <code>PRAGMA encoding</code><br/>
	 * returning the encoding used by the main database (or the one that will be used upon
	 * the creation of this database).
	 */
	public static Pragma encoding() {
		return SqlStatements.pragma(Pragma.Name.ENCODING);
	}

	/**
	 * Produces the statement<br/>
	 * <code>PRAGMA encoding(databaseEncoding)</code><br/>
	 * setting the encoding that will be used upon the creation of the main database.<br/>
	 * Note that once an encoding has been set for a database, it cannot be changed.
	 * Databases created by the <code>ATTACH</code> command always use the same encoding as the main database.
	 * An attempt to <code>ATTACH</code> a database with a different text encoding from the main database will fail.
	 */
	public static Pragma.ForName encoding(Encoding databaseEncoding) {
		return SqlStatements.pragma(Pragma.Name.ENCODING).forName(databaseEncoding.toString());
	}

	/**
	 * Produces the statement<br/>
	 * <code>PRAGMA foreign_key_check</code><br/>
	 * checking all the tables in the main database.
	 */
	public static Pragma foreignKeyCheck() {
		return SqlStatements.pragma(Pragma.Name.FOREIGN_KEY_CHECK);
	}

	/**
	 * Produces the statement<br/>
	 * <code>PRAGMA schemaName.foreign_key_check</code><br/>
	 * checking all the tables in the specified database.
	 */
	public static Pragma foreignKeyCheckSchema(CharSequence schemaName) {
		return SqlStatements.pragma(schemaName, Pragma.Name.FOREIGN_KEY_CHECK);
	}

	/**
	 * Produces the statement<br/>
	 * <code>PRAGMA foreign_key_check(tableName)</code><br/>
	 * checking the specified table in the main database.
	 */
	public static Pragma.ForName foreignKeyCheckTable(CharSequence tableName) {
		return SqlStatements.pragma(Pragma.Name.FOREIGN_KEY_CHECK).forName(tableName);
	}

	/**
	 * Produces the statement<br/>
	 * <code>PRAGMA schemaName.foreign_key_check(tableName)</code><br/>
	 * checking the specified table in the specified database.
	 */
	public static Pragma.ForName foreignKeyCheckTable(CharSequence schemaName, CharSequence tableName) {
		return SqlStatements.pragma(schemaName, Pragma.Name.FOREIGN_KEY_CHECK).forName(tableName);
	}

	/**
	 * Produces the statement<br/>
	 * <code>PRAGMA foreign_key_list(tableName)</code><br/>
	 * enumerating all the foreign keys of the specified table of the main database.
	 */
	public static Pragma.ForName foreignKeyList(CharSequence tableName) {
		return SqlStatements.pragma(Pragma.Name.FOREIGN_KEY_LIST).forName(tableName);
	}

	/**
	 * Produces the statement<br/>
	 * <code>PRAGMA schemaName.foreign_key_list(tableName)</code><br/>
	 * enumerating all the foreign keys of the specified table of the specified database.
	 */
	public static Pragma.ForName foreignKeyList(CharSequence schemaName, CharSequence tableName) {
		return SqlStatements.pragma(schemaName, Pragma.Name.FOREIGN_KEY_LIST).forName(tableName);
	}

	/**
	 * Produces the statement<br/>
	 * <code>PRAGMA foreign_keys</code><br/>
	 * indicating if foreign key constraints are enforced.
	 */
	public static Pragma foreignKeys() {
		return SqlStatements.pragma(Pragma.Name.FOREIGN_KEYS);
	}

	/**
	 * Produces the statement<br/>
	 * <code>PRAGMA foreign_keys(TRUE|FALSE)</code><br/>
	 * setting or clearing the foreign key constraints enforcement flag.
	 */
	public static Pragma.SetBoolean foreignKeys(boolean on) {
		return SqlStatements.pragma(Pragma.Name.FOREIGN_KEYS).withValue(on);
	}

	/**
	 * Produces the statement<br/>
	 * <code>PRAGMA freelist_count</code><br/>
	 * returning the number of unused pages in the main database file.
	 */
	public static Pragma freeListCount() {
		return SqlStatements.pragma(Pragma.Name.FREELIST_COUNT);
	}

	/**
	 * Produces the statement<br/>
	 * <code>PRAGMA schemaName.freelist_count</code><br/>
	 * returning the number of unused pages in the specified database file.
	 */
	public static Pragma freeListCount(CharSequence schemaName) {
		return SqlStatements.pragma(schemaName, Pragma.Name.FREELIST_COUNT);
	}

	/**
	 * Produces the statement<br/>
	 * <code>PRAGMA fullfsync</code><br/>
	 * returning the fullfsync flag value.
	 */
	public static Pragma fullFsync() {
		return SqlStatements.pragma(Pragma.Name.FULLFSYNC);
	}

	/**
	 * Produces the statement<br/>
	 * <code>PRAGMA fullfsync(TRUE|FALSE)</code><br/>
	 * setting or clearing the fullfsync flag value.
	 */
	public static Pragma.SetBoolean fullFsync(boolean on) {
		return SqlStatements.pragma(Pragma.Name.FULLFSYNC).withValue(on);
	}

	/**
	 * Produces the statement<br/>
	 * <code>PRAGMA function_list</code><br/>
	 * returning the list of SQL functions known to the database connection.
	 */
	public static Pragma functionList() {
		return SqlStatements.pragma(Pragma.Name.FUNCTION_LIST);
	}

	/**
	 * Produces the statement<br/>
	 * <code>PRAGMA ignore_check_constraints</code><br/>
	 * indicating if the CHECK constraints are ignored.
	 */
	public static Pragma ignoreCheckConstraints() {
		return SqlStatements.pragma(Pragma.Name.IGNORE_CHECK_CONSTRAINTS);
	}

	/**
	 * Produces the statement<br/>
	 * <code>PRAGMA ignore_check_constraints(TRUE|FALSE)</code><br/>
	 * making the database engine ignore or enforce the CHECK constraints.
	 */
	public static Pragma.SetBoolean ignoreCheckConstraints(boolean on) {
		return SqlStatements.pragma(Pragma.Name.IGNORE_CHECK_CONSTRAINTS).withValue(on);
	}

	/**
	 * Produces the statement<br/>
	 * <code>PRAGMA incremental_vacuum</code><br/>
	 * performing the full vacuum operation on the main database.
	 */
	public static Pragma incrementalVacuum() {
		return SqlStatements.pragma(Pragma.Name.INCREMENTAL_VACUUM);
	}

	/**
	 * Produces the statement<br/>
	 * <code>PRAGMA schemaName.incremental_vacuum</code><br/>
	 * performing the full vacuum operation on the specified database.
	 */
	public static Pragma incrementalVacuum(CharSequence schemaName) {
		return SqlStatements.pragma(schemaName, Pragma.Name.INCREMENTAL_VACUUM);
	}

	/**
	 * Produces the statement<br/>
	 * <code>PRAGMA incremental_vacuum(maxPages)</code><br/>
	 * performing a partial vacuum operation on the main database.
	 */
	public static Pragma.SetNumber incrementalVacuum(int maxPages) {
		if (maxPages <= 0) {
			throw new IllegalArgumentException("The page number threshold must be positive.");
		}
		return SqlStatements.pragma(Pragma.Name.INCREMENTAL_VACUUM).withValue(maxPages);
	}

	/**
	 * Produces the statement<br/>
	 * <code>PRAGMA schemaName.incremental_vacuum(maxPages)</code><br/>
	 * performing a partial vacuum operation on the specified database.
	 */
	public static Pragma.SetNumber incrementalVacuum(CharSequence schemaName, int maxPages) {
		return SqlStatements.pragma(schemaName, Pragma.Name.INCREMENTAL_VACUUM).withValue(maxPages);
	}

	/**
	 * Produces the statement<br/>
	 * <code>PRAGMA index_info(indexName)</code><br/>
	 * returning index data for the specified index of the main database.
	 */
	public static Pragma.ForName indexInfo(CharSequence indexName) {
		return SqlStatements.pragma(Pragma.Name.INDEX_INFO).forName(indexName);
	}

	/**
	 * Produces the statement<br/>
	 * <code>PRAGMA schemaName.index_info(indexName)</code><br/>
	 * returning index data for the specified index of the specified database.
	 */
	public static Pragma.ForName indexInfo(CharSequence schemaName, CharSequence indexName) {
		return SqlStatements.pragma(schemaName, Pragma.Name.INDEX_INFO).forName(indexName);
	}

	/**
	 * Produces the statement<br/>
	 * <code>PRAGMA index_list(tableName)</code><br/>
	 * returning the indices for the specified table of the main database.
	 */
	public static Pragma.ForName indexList(CharSequence tableName) {
		return SqlStatements.pragma(Pragma.Name.INDEX_LIST).forName(tableName);
	}

	/**
	 * Produces the statement<br/>
	 * <code>PRAGMA schemaName.index_list(tableName)</code><br/>
	 * returning the indices for the specified table of the specified database.
	 */
	public static Pragma.ForName indexList(CharSequence schemaName, CharSequence tableName) {
		return SqlStatements.pragma(schemaName, Pragma.Name.INDEX_LIST).forName(tableName);
	}

	/**
	 * Produces the statement<br/>
	 * <code>PRAGMA index_xinfo(indexName)</code><br/>
	 * returning index data for the specified index of the main database.
	 */
	public static Pragma.ForName indexXInfo(CharSequence indexName) {
		return SqlStatements.pragma(Pragma.Name.INDEX_XINFO).forName(indexName);
	}

	/**
	 * Produces the statement<br/>
	 * <code>PRAGMA schemaName.index_xinfo(indexName)</code><br/>
	 * returning index data for the specified index of the specified database.
	 */
	public static Pragma.ForName indexXInfo(CharSequence schemaName, CharSequence indexName) {
		return SqlStatements.pragma(schemaName, Pragma.Name.INDEX_XINFO).forName(indexName);
	}

	/**
	 * Produces the statement<br/>
	 * <code>PRAGMA integrity_check</code><br/>
	 * initiating the main database integrity check.
	 */
	public static Pragma integrityCheck() {
		return SqlStatements.pragma(Pragma.Name.INTEGRITY_CHECK);
	}

	/**
	 * Produces the statement<br/>
	 * <code>PRAGMA schemaName.integrity_check</code><br/>
	 * initiating the integrity check of the specified database.
	 */
	public static Pragma integrityCheck(CharSequence schemaName) {
		return SqlStatements.pragma(schemaName, Pragma.Name.INTEGRITY_CHECK);
	}

	/**
	 * Produces the statement<br/>
	 * <code>PRAGMA integrity_check(maxErrors)</code><br/>
	 * initiating the main database integrity check with non-default error count limit.
	 */
	public static Pragma.SetNumber integrityCheck(int maxErrors) {
		return SqlStatements.pragma(Pragma.Name.INTEGRITY_CHECK).withValue(maxErrors);
	}

	/**
	 * Produces the statement<br/>
	 * <code>PRAGMA schemaName.integrity_check(maxErrors)</code><br/>
	 * initiating the integrity check of the specified database with non-default error count limit.
	 */
	public static Pragma.SetNumber integrityCheck(CharSequence schemaName, int maxErrors) {
		return SqlStatements.pragma(schemaName, Pragma.Name.INTEGRITY_CHECK).withValue(maxErrors);
	}

	/**
	 * Produces the statement<br/>
	 * <code>PRAGMA journal_mode</code><br/>
	 * returning the journal mode for the main database.
	 */
	public static Pragma journalMode() {
		return SqlStatements.pragma(Pragma.Name.JOURNAL_MODE);
	}

	/**
	 * Produces the statement<br/>
	 * <code>PRAGMA schemaName.journal_mode</code><br/>
	 * returning the journal mode for the specified database.
	 */
	public static Pragma journalMode(CharSequence schemaName) {
		return SqlStatements.pragma(schemaName, Pragma.Name.JOURNAL_MODE);
	}

	/**
	 * Produces the statement<br/>
	 * <code>PRAGMA journal_mode(DELETE|TRUNCATE|PERSIST| MEMORY|WAL|OFF)</code><br/>
	 * setting the journal mode for the main database.
	 */
	public static Pragma.ForName journalMode(JournalMode mode) {
		return SqlStatements.pragma(Pragma.Name.JOURNAL_MODE).forName(mode.toString());
	}

	/**
	 * Produces the statement<br/>
	 * <code>PRAGMA schemaName.journal_mode(DELETE|TRUNCATE|PERSIST| MEMORY|WAL|OFF)</code><br/>
	 * setting the journal mode for the specified database.
	 */
	public static Pragma.ForName journalMode(CharSequence schemaName, JournalMode mode) {
		return SqlStatements.pragma(schemaName, Pragma.Name.JOURNAL_MODE).forName(mode.toString());
	}

	/**
	 * Produces the statement<br/>
	 * <code>PRAGMA journal_size_limit</code><br/>
	 * returning the journal size limit of the main database, in bytes, or -1 if there is no limit.
	 */
	public static Pragma journalSizeLimit() {
		return SqlStatements.pragma(Pragma.Name.JOURNAL_SIZE_LIMIT);
	}

	/**
	 * Produces the statement<br/>
	 * <code>PRAGMA schemaName.journal_size_limit</code><br/>
	 * returning the journal size limit of the specified database, in bytes, or -1 if there is no limit.
	 */
	public static Pragma journalSizeLimit(CharSequence schemaName) {
		return SqlStatements.pragma(schemaName, Pragma.Name.JOURNAL_SIZE_LIMIT);
	}

	/**
	 * Produces the statement<br/>
	 * <code>PRAGMA journal_size_limit(limit)</code><br/>
	 * setting the journal size limit of the main database, in bytes, or removing such limit.
	 * @param limit the maximum journal size, in bytes, or {@literal null} to remove the limit
	 */
	public static Pragma.SetNumber journalSizeLimit(Long limit) {
		return SqlStatements.pragma(Pragma.Name.JOURNAL_SIZE_LIMIT).withValue(limit == null ? -1 : limit);
	}

	/**
	 * Produces the statement<br/>
	 * <code>PRAGMA schemaName.journal_size_limit(limit)</code><br/>
	 * setting the journal size limit of the specified database, in bytes, or removing such limit.
	 * @param limit the maximum journal size, in bytes, or {@literal null} to remove the limit
	 */
	public static Pragma.SetNumber journalSizeLimit(CharSequence schemaName, Long limit) {
		return SqlStatements.pragma(schemaName, Pragma.Name.JOURNAL_SIZE_LIMIT).withValue(limit == null ? -1 : limit);
	}

	/**
	 * Produces the statement<br/>
	 * <code>PRAGMA legacy_file_format</code><br/>
	 * returning the flag showing if new databases will be created in legacy format.
	 */
	public static Pragma legacyFileFormat() {
		return SqlStatements.pragma(Pragma.Name.LEGACY_FILE_FORMAT);
	}

	/**
	 * Produces the statement<br/>
	 * <code>PRAGMA legacy_file_format(TRUE|FALSE)</code><br/>
	 * setting or clearing the flag showing if new databases will be created in legacy format.
	 */
	public static Pragma.SetBoolean legacyFileFormat(boolean on) {
		return SqlStatements.pragma(Pragma.Name.LEGACY_FILE_FORMAT).withValue(on);
	}

	/**
	 * Produces the statement<br/>
	 * <code>PRAGMA locking_mode</code><br/>
	 * returning the locking mode (NORMAL or EXCLUSIVE) of the connection to the main database.
	 */
	public static Pragma lockingMode() {
		return SqlStatements.pragma(Pragma.Name.LOCKING_MODE);
	}

	/**
	 * Produces the statement<br/>
	 * <code>PRAGMA locking_mode(NORMAL|EXCLUSIVE)</code><br/>
	 * setting the locking mode of the connection to all the databases, already opened and newly attached.
	 */
	public static Pragma.ForName lockingMode(LockingMode mode) {
		return SqlStatements.pragma(Pragma.Name.LOCKING_MODE).forName(mode.toString());
	}

	/**
	 * Produces the statement<br/>
	 * <code>PRAGMA schemaName.locking_mode</code><br/>
	 * returning the locking mode (NORMAL or EXCLUSIVE) of the connection to the specified database.
	 */
	public static Pragma lockingMode(CharSequence schemaName) {
		return SqlStatements.pragma(schemaName, Pragma.Name.LOCKING_MODE);
	}

	/**
	 * Produces the statement<br/>
	 * <code>PRAGMA schemaName.locking_mode(NORMAL|EXCLUSIVE)</code><br/>
	 * setting the locking mode of the connection to the specified database.
	 */
	public static Pragma.ForName lockingMode(CharSequence schemaName, LockingMode mode) {
		return SqlStatements.pragma(schemaName, Pragma.Name.LOCKING_MODE).forName(mode.toString());
	}

	/**
	 * Produces the statement<br/>
	 * <code>PRAGMA max_page_count</code><br/>
	 * retrieving the maximum page count in the main database.
	 */
	public static Pragma maxPageCount() {
		return SqlStatements.pragma(Pragma.Name.MAX_PAGE_COUNT);
	}

	/**
	 * Produces the statement<br/>
	 * <code>PRAGMA max_page_count(pageCount)</code><br/>
	 * sets the maximum page count in the main database (but no less than the current page count).
	 */
	public static Pragma.SetNumber maxPageCount(int pageCount) {
		if (pageCount <= 0) {
			throw new IllegalArgumentException("The maximum number of pages must be positive.");
		}
		return SqlStatements.pragma(Pragma.Name.MAX_PAGE_COUNT).withValue(pageCount);
	}

	/**
	 * Produces the statement<br/>
	 * <code>PRAGMA schemaName.max_page_count</code><br/>
	 * retrieving the maximum page count in the specified database.
	 */
	public static Pragma maxPageCount(CharSequence schemaName) {
		return SqlStatements.pragma(schemaName, Pragma.Name.MAX_PAGE_COUNT);
	}

	/**
	 * Produces the statement<br/>
	 * <code>PRAGMA schemaName.max_page_count(pageCount)</code><br/>
	 * sets the maximum page count in the specified database (but no less than the current page count).
	 */
	public static Pragma.SetNumber maxPageCount(CharSequence schemaName, int pageCount) {
		if (pageCount <= 0) {
			throw new IllegalArgumentException("The maximum number of pages must be positive.");
		}
		return SqlStatements.pragma(schemaName, Pragma.Name.MAX_PAGE_COUNT).withValue(pageCount);
	}

	/**
	 * Produces the statement<br/>
	 * <code>PRAGMA mmap_size</code><br/>
	 * retrieving the maximum number of bytes used for memory-mapped I/O on the main database.
	 */
	public static Pragma mmapSize() {
		return SqlStatements.pragma(Pragma.Name.MMAP_SIZE);
	}

	/**
	 * Produces the statement<br/>
	 * <code>PRAGMA mmap_size(bytes)</code><br/>
	 * setting the maximum number of bytes used for memory-mapped I/O on the main database.
	 * @param bytes the desired size limit for memory-mapped I/O buffers, (zero to disable I/O mapping)
	 */
	public static Pragma.SetNumber mmapSize(int bytes) {
		if (bytes < 0) {
			throw new IllegalArgumentException(
				"The amount of memory for memory-mapped I/O must not be negative.");
		}
		return SqlStatements.pragma(Pragma.Name.MMAP_SIZE).withValue(bytes);
	}

	/**
	 * Produces the statement<br/>
	 * <code>PRAGMA schemaName.mmap_size</code><br/>
	 * retrieving the maximum number of bytes used for memory-mapped I/O on the specified database.
	 */
	public static Pragma mmapSize(CharSequence schemaName) {
		return SqlStatements.pragma(schemaName, Pragma.Name.MMAP_SIZE);
	}

	/**
	 * Produces the statement<br/>
	 * <code>PRAGMA schemaName.mmap_size(bytes)</code><br/>
	 * setting the maximum number of bytes used for memory-mapped I/O on the specified database.
	 * @param bytes the desired size limit for memory-mapped I/O buffers, (zero to disable I/O mapping)
	 */
	public static Pragma.SetNumber mmapSize(CharSequence schemaName, int bytes) {
		if (bytes < 0) {
			throw new IllegalArgumentException(
				"The amount of memory for memory-mapped I/O must not be negative.");
		}
		return SqlStatements.pragma(schemaName, Pragma.Name.MMAP_SIZE).withValue(bytes);
	}

	/**
	 * Produces the statement<br/>
	 * <code>PRAGMA module_list</code><br/>
	 * retrieving the list of the registered virtual table modules.
	 */
	public static Pragma moduleList() {
		return SqlStatements.pragma(Pragma.Name.MODULE_LIST);
	}

	/**
	 * Produces the statement<br/>
	 * <code>PRAGMA optimize</code><br/>
	 * performing the default optimization(s) on all the attached databases.
	 */
	public static Pragma optimize() {
		return SqlStatements.pragma(Pragma.Name.OPTIMIZE);
	}

	/**
	 * Produces the statement<br/>
	 * <code>PRAGMA schemaName.optimize</code><br/>
	 * performing the default optimization(s) on the specified database.
	 */
	public static Pragma optimize(CharSequence schemaName) {
		return SqlStatements.pragma(schemaName, Pragma.Name.OPTIMIZE);
	}

	/**
	 * Produces the statement<br/>
	 * <code>PRAGMA optimize(mask)</code><br/>
	 * performing the selected optimization(s) on all the attached databases.
	 * One may use {@link OptimizeMode} constants to form the mask.
	 */
	public static Pragma.SetNumber optimize(int mask) {
		return SqlStatements.pragma(Pragma.Name.OPTIMIZE).withValue(mask);
	}

	/**
	 * Produces the statement<br/>
	 * <code>PRAGMA schemaName.optimize(mask)</code><br/>
	 * performing the selected optimization(s) on the specified database.
	 * One may use {@link OptimizeMode} constants to form the mask.
	 */
	public static Pragma.SetNumber optimize(CharSequence schemaName, int mask) {
		return SqlStatements.pragma(schemaName, Pragma.Name.OPTIMIZE).withValue(mask);
	}

	/**
	 * Produces the statement<br/>
	 * <code>PRAGMA page_count</code><br/>
	 * returning the total number of pages in the main database file.
	 */
	public static Pragma pageCount() {
		return SqlStatements.pragma(Pragma.Name.PAGE_COUNT);
	}

	/**
	 * Produces the statement<br/>
	 * <code>PRAGMA schemaName.page_count</code><br/>
	 * returning the total number of pages in the file of the specified database.
	 */
	public static Pragma pageCount(CharSequence schemaName) {
		return SqlStatements.pragma(schemaName, Pragma.Name.PAGE_COUNT);
	}

	/**
	 * Produces the statement<br/>
	 * <code>PRAGMA page_size</code><br/>
	 * returning the page size of the main database file.
	 */
	public static Pragma pageSize() {
		return SqlStatements.pragma(Pragma.Name.PAGE_SIZE);
	}

	/**
	 * Produces the statement<br/>
	 * <code>PRAGMA schemaName.page_size</code><br/>
	 * returning the page size of the file of the specified database.
	 */
	public static Pragma pageSize(CharSequence schemaName) {
		return SqlStatements.pragma(schemaName, Pragma.Name.PAGE_SIZE);
	}

	/**
	 * Produces the statement<br/>
	 * <code>PRAGMA page_size(bytes)</code><br/>
	 * setting the page size of the main database file.
	 * @param bytes the page size, in bytes; must be a power of two between 512 and 65536
	 */
	public static Pragma.SetNumber pageSize(int bytes) {
		checkPageSize(bytes);
		return SqlStatements.pragma(Pragma.Name.PAGE_SIZE).withValue(bytes);
	}

	/**
	 * Produces the statement<br/>
	 * <code>PRAGMA schemaName.page_size(bytes)</code><br/>
	 * setting the page size of the file of the specified database.
	 * @param bytes the page size, in bytes; must be a power of two between 512 and 65536
	 */
	public static Pragma.SetNumber pageSize(CharSequence schemaName, int bytes) {
		checkPageSize(bytes);
		return SqlStatements.pragma(schemaName, Pragma.Name.PAGE_SIZE).withValue(bytes);
	}

	/**
	 * Produces the statement<br/>
	 * <code>PRAGMA parser_trace(TRUE|FALSE)</code><br/>
	 * turning the SQLite internal parser debugging mode on or off.
	 */
	public static Pragma.SetBoolean parserTrace(boolean on) {
		return SqlStatements.pragma(Pragma.Name.PARSER_TRACE).withValue(on);
	}

	/**
	 * Produces the statement<br/>
	 * <code>PRAGMA pragma_list</code><br/>
	 * returning the list of available PRAGMA commands.
	 */
	public static Pragma pragmaList() {
		return SqlStatements.pragma(Pragma.Name.PRAGMA_LIST);
	}

	/**
	 * Produces the statement<br/>
	 * <code>PRAGMA query_only</code><br/>
	 * indicating if the statement dry run mode is enabed (in which case the databases
	 * are not modified by the executed statements).
	 */
	public static Pragma queryOnly() {
		return SqlStatements.pragma(Pragma.Name.QUERY_ONLY);
	}

	/**
	 * Produces the statement<br/>
	 * <code>PRAGMA query_only(TRUE|FALSE)</code><br/>
	 * setting or clearing the statement dry run option (when enabled, the databases
	 * are not modified by the executed statements).
	 */
	public static Pragma.SetBoolean queryOnly(boolean on) {
		return SqlStatements.pragma(Pragma.Name.QUERY_ONLY).withValue(on);
	}

	/**
	 * Produces the statement<br/>
	 * <code>PRAGMA quick_check</code><br/>
	 * performing a subset of integrity checks on the main database.
	 */
	public static Pragma quickCheck() {
		return SqlStatements.pragma(Pragma.Name.QUICK_CHECK);
	}

	/**
	 * Produces the statement<br/>
	 * <code>PRAGMA schemaName.quick_check</code><br/>
	 * performing a subset of integrity checks on the specified database.
	 */
	public static Pragma quickCheck(CharSequence schemaName) {
		return SqlStatements.pragma(schemaName, Pragma.Name.QUICK_CHECK);
	}

	/**
	 * Produces the statement<br/>
	 * <code>PRAGMA quick_check(maxErrors)</code><br/>
	 * performing a subset of integrity checks on the main database with non-default
	 * error count limit.
	 */
	public static Pragma.SetNumber quickCheck(int maxErrors) {
		return SqlStatements.pragma(Pragma.Name.QUICK_CHECK).withValue(maxErrors);
	}

	/**
	 * Produces the statement<br/>
	 * <code>PRAGMA schemaName.quick_check(maxErrors)</code><br/>
	 * performing a subset of integrity checks on the specified database with non-default
	 * error count limit.
	 */
	public static Pragma.SetNumber quickCheck(CharSequence schemaName, int maxErrors) {
		return SqlStatements.pragma(schemaName, Pragma.Name.QUICK_CHECK).withValue(maxErrors);
	}

	/**
	 * Produces the statement<br/>
	 * <code>PRAGMA read_uncommitted</code><br/>
	 * indicating if READ UNCOMMITTED transaction isolation must be used instead of SERIALIZABLE.
	 */
	public static Pragma readUncommitted() {
		return SqlStatements.pragma(Pragma.Name.READ_UNCOMMITTED);
	}

	/**
	 * Produces the statement<br/>
	 * <code>PRAGMA read_uncommitted(TRUE|FALSE)</code><br/>
	 * enabling or disabling the READ UNCOMMITTED transaction isolation level.
	 */
	public static Pragma.SetBoolean readUncommitted(boolean on) {
		return SqlStatements.pragma(Pragma.Name.READ_UNCOMMITTED).withValue(on);
	}

	/**
	 * Produces the statement<br/>
	 * <code>PRAGMA recursive_triggers</code><br/>
	 * indicating if the recursive trigger capability is enabled.
	 */
	public static Pragma recursiveTriggers() {
		return SqlStatements.pragma(Pragma.Name.RECURSIVE_TRIGGERS);
	}

	/**
	 * Produces the statement<br/>
	 * <code>PRAGMA recursive_triggers(TRUE|FALSE)</code><br/>
	 * enabling or disabling the recursive trigger capability.
	 */
	public static Pragma.SetBoolean recursiveTriggers(boolean on) {
		return SqlStatements.pragma(Pragma.Name.RECURSIVE_TRIGGERS).withValue(on);
	}

	/**
	 * Produces the statement<br/>
	 * <code>PRAGMA reverse_unordered_selects</code><br/>
	 * indicating if the unordered SELECT statements tend to return the results in order
	 * reverse to that they would otherwise.
	 */
	public static Pragma reverseUnorderedSelects() {
		return SqlStatements.pragma(Pragma.Name.REVERSE_UNORDERED_SELECTS);
	}

	/**
	 * Produces the statement<br/>
	 * <code>PRAGMA reverse_unordered_selects(TRUE|FALSE)</code><br/>
	 * making many of the unordered SELECT statements return the results in order
	 * reverse to that they would otherwise, or turning this behaviour off.
	 */
	public static Pragma.SetBoolean reverseUnorderedSelects(boolean on) {
		return SqlStatements.pragma(Pragma.Name.REVERSE_UNORDERED_SELECTS).withValue(on);
	}

	/**
	 * Produces the statement<br/>
	 * <code>PRAGMA schema_version</code><br/>
	 * returning the integer automatically incremented each time the main database schema changes.
	 */
	public static Pragma schemaVersion() {
		return SqlStatements.pragma(Pragma.Name.SCHEMA_VERSION);
	}

	/**
	 * Produces the statement<br/>
	 * <code>PRAGMA schemaName.schema_version</code><br/>
	 * returning the integer automatically incremented each time the specified database schema changes.
	 */
	public static Pragma schemaVersion(CharSequence schemaName) {
		return SqlStatements.pragma(schemaName, Pragma.Name.SCHEMA_VERSION);
	}

	/**
	 * Produces the statement<br/>
	 * <code>PRAGMA schema_version(value)</code><br/>
	 * forcibly setting the main database schema version value.
	 */
	public static Pragma.SetNumber schemaVersion(int value) {
		return SqlStatements.pragma(Pragma.Name.SCHEMA_VERSION).withValue(value);
	}

	/**
	 * Produces the statement<br/>
	 * <code>PRAGMA schemaName.schema_version(value)</code><br/>
	 * forcibly setting the schema version value of the specified database.
	 */
	public static Pragma.SetNumber schemaVersion(CharSequence schemaName, int value) {
		return SqlStatements.pragma(schemaName, Pragma.Name.SCHEMA_VERSION).withValue(value);
	}

	/**
	 * Produces the statement<br/>
	 * <code>PRAGMA secure_delete</code><br/>
	 * indicating the mode of clearing the deleted data in the main database.
	 */
	public static Pragma secureDelete() {
		return SqlStatements.pragma(Pragma.Name.SECURE_DELETE);
	}

	/**
	 * Produces the statement<br/>
	 * <code>PRAGMA schemaName.secure_delete</code><br/>
	 * indicating the mode of clearing the deleted data in the specified database.
	 */
	public static Pragma secureDelete(CharSequence schemaName) {
		return SqlStatements.pragma(schemaName, Pragma.Name.SECURE_DELETE);
	}

	/**
	 * Produces the statement<br/>
	 * <code>PRAGMA secure_delete(OFF|FAST|ON)</code><br/>
	 * setting the mode of clearing the deleted data in the main database.
	 */
	public static Pragma.ForName secureDelete(SecureDeleteMode mode) {
		return SqlStatements.pragma(Pragma.Name.SECURE_DELETE).forName(mode.toString());
	}

	/**
	 * Produces the statement<br/>
	 * <code>PRAGMA schemaName.secure_delete(OFF|FAST|ON)</code><br/>
	 * setting the mode of clearing the deleted data in the specified database.
	 */
	public static Pragma.ForName secureDelete(CharSequence schemaName, SecureDeleteMode mode) {
		return SqlStatements.pragma(schemaName, Pragma.Name.SECURE_DELETE).forName(mode.toString());
	}

	/**
	 * Produces the statement<br/>
	 * <code>PRAGMA shrink_memory</code><br/>
	 * freeing as much memory used by the connection as possible.
	 */
	public static Pragma shrinkMemory() {
		return SqlStatements.pragma(Pragma.Name.SHRINK_MEMORY);
	}

	/**
	 * Produces the statement<br/>
	 * <code>PRAGMA soft_heap_limit</code><br/>
	 * returning the advised heap limit for SQLite.
	 */
	public static Pragma softHeapLimit() {
		return SqlStatements.pragma(Pragma.Name.SOFT_HEAP_LIMIT);
	}

	/**
	 * Produces the statement<br/>
	 * <code>PRAGMA soft_heap_limit(bytes)</code><br/>
	 * setting the advised heap limit for SQLite or removing one.
	 * @param bytes the soft heap limit, in bytes; {@literal null} if one whishes to remove the limit
	 */
	public static Pragma.SetNumber softHeapLimit(Long bytes) {
		if (bytes != null && bytes <= 0) {
			throw new IllegalArgumentException("If specified, the soft heap limit must be positive.");
		}
		return SqlStatements.pragma(Pragma.Name.SOFT_HEAP_LIMIT).withValue(bytes == null ? 0 : bytes);
	}

	/**
	 * Produces the statement<br/>
	 * <code>PRAGMA stats</code><br/>
	 * returning some auxiliary information about tables and indices.
	 */
	public static Pragma stats() {
		return SqlStatements.pragma(Pragma.Name.STATS);
	}

	/**
	 * Produces the statement<br/>
	 * <code>PRAGMA synchronous</code><br/>
	 * returning the main database synchronization mode.
	 */
	public static Pragma synchronous() {
		return SqlStatements.pragma(Pragma.Name.SYNCHRONOUS);
	}

	/**
	 * Produces the statement<br/>
	 * <code>PRAGMA schemaName.synchronous</code><br/>
	 * returning the synchronization mode of the specified database.
	 */
	public static Pragma synchronous(CharSequence schemaName) {
		return SqlStatements.pragma(schemaName, Pragma.Name.SYNCHRONOUS);
	}

	/**
	 * Produces the statement<br/>
	 * <code>PRAGMA synchronous(OFF|NORMAL|FULL|EXTRA)</code><br/>
	 * setting the main database synchronization mode.
	 */
	public static Pragma.ForName synchronous(Synchronization mode) {
		return SqlStatements.pragma(Pragma.Name.SYNCHRONOUS).forName(mode.toString());
	}

	/**
	 * Produces the statement<br/>
	 * <code>PRAGMA schemaName.synchronous(OFF|NORMAL|FULL|EXTRA)</code><br/>
	 * setting the synchronization mode of the specified database.
	 */
	public static Pragma.ForName synchronous(CharSequence schemaName, Synchronization mode) {
		return SqlStatements.pragma(schemaName, Pragma.Name.SYNCHRONOUS).forName(mode.toString());
	}

	/**
	 * Produces the statement<br/>
	 * <code>PRAGMA table_info(tableName)</code><br/>
	 * retrieving per-column definition of a table/view from the main database.
	 */
	public static Pragma.ForName tableInfo(CharSequence tableName) {
		return SqlStatements.pragma(Pragma.Name.SYNCHRONOUS).forName(tableName);
	}

	/**
	 * Produces the statement<br/>
	 * <code>PRAGMA schemaName.table_info(tableName)</code><br/>
	 * retrieving per-column definition of a table/view from the specified database.
	 */
	public static Pragma.ForName tableInfo(CharSequence schemaName, CharSequence tableName) {
		return SqlStatements.pragma(schemaName, Pragma.Name.SYNCHRONOUS).forName(tableName);
	}

	/**
	 * Produces the statement<br/>
	 * <code>PRAGMA temp_store</code><br/>
	 * retrieving the temporary storage type.
	 */
	public static Pragma tempStore() {
		return SqlStatements.pragma(Pragma.Name.TEMP_STORE);
	}

	/**
	 * Produces the statement<br/>
	 * <code>PRAGMA temp_store(DEFAULT|FILE|MEMORY)</code><br/>
	 * setting the temporary storage type (all the temporary objects are deleted when this type is changed).
	 */
	public static Pragma.ForName tempStore(TempStore mode) {
		return SqlStatements.pragma(Pragma.Name.TEMP_STORE).forName(mode.toString());
	}

	/**
	 * Produces the statement<br/>
	 * <code>PRAGMA threads</code><br/>
	 * retrieving the maximum auxiliary thread count for the connection.
	 */
	public static Pragma threads() {
		return SqlStatements.pragma(Pragma.Name.THREADS);
	}

	/**
	 * Produces the statement<br/>
	 * <code>PRAGMA threads(auxiliaryThreadCount)</code><br/>
	 * setting the maximum auxiliary thread count for the connection.
	 */
	public static Pragma.SetNumber threads(int auxiliaryThreadCount) {
		if (auxiliaryThreadCount < 0) {
			throw new IllegalArgumentException("The number of auxiliary threads must be non-negative.");
		}
		return SqlStatements.pragma(Pragma.Name.THREADS).withValue(auxiliaryThreadCount);
	}

	/**
	 * Produces the statement<br/>
	 * <code>PRAGMA user_version</code><br/>
	 * returning a user-specified integer associated with the main database.
	 */
	public static Pragma userVersion() {
		return SqlStatements.pragma(Pragma.Name.USER_VERSION);
	}

	/**
	 * Produces the statement<br/>
	 * <code>PRAGMA user_version(version)</code><br/>
	 * setting a user-specified integer associated with the main database.
	 */
	public static Pragma.SetNumber userVersion(int version) {
		return SqlStatements.pragma(Pragma.Name.USER_VERSION).withValue(version);
	}

	/**
	 * Produces the statement<br/>
	 * <code>PRAGMA schemaName.user_version</code><br/>
	 * returning a user-specified integer associated with the specified database.
	 */
	public static Pragma userVersion(CharSequence schemaName) {
		return SqlStatements.pragma(schemaName, Pragma.Name.USER_VERSION);
	}

	/**
	 * Produces the statement<br/>
	 * <code>PRAGMA schemaName.user_version(version)</code><br/>
	 * setting a user-specified integer associated with the specified database.
	 */
	public static Pragma.SetNumber userVersion(CharSequence schemaName, int version) {
		return SqlStatements.pragma(schemaName, Pragma.Name.USER_VERSION).withValue(version);
	}

	/**
	 * Produces the statement<br/>
	 * <code>PRAGMA vdbe_addoptrace(TRUE|FALSE)</code><br/>
	 * specifying if the VDBE opcodes have to be displayed as they are created during code generation
	 * (SQLite debug option).
	 */
	public static Pragma.SetBoolean vdbeAddOpTrace(boolean on) {
		return SqlStatements.pragma(Pragma.Name.VDBE_ADDOPTRACE).withValue(on);
	}

	/**
	 * Produces the statement<br/>
	 * <code>PRAGMA vdbe_debug(TRUE|FALSE)</code><br/>
	 * specifying if the full VDBE debug mode has to be turned on or off.
	 */
	public static Pragma.SetBoolean vdbeDebug(boolean on) {
		return SqlStatements.pragma(Pragma.Name.VDBE_DEBUG).withValue(on);
	}

	/**
	 * Produces the statement<br/>
	 * <code>PRAGMA vdbe_listing(TRUE|FALSE)</code><br/>
	 * specifying if the a complete listing of the virtual machine opcodes has to appear
	 * on the standard output as each statement is evaluated (SQLite debug option).
	 */
	public static Pragma.SetBoolean vdbeListing(boolean on) {
		return SqlStatements.pragma(Pragma.Name.VDBE_LISTING).withValue(on);
	}

	/**
	 * Produces the statement<br/>
	 * <code>PRAGMA vdbe_trace(TRUE|FALSE)</code><br/>
	 * specifying if the virtual machine opcodes have to be printed on the standard output
	 * as they are evaluated (SQLite debug option).
	 */
	public static Pragma.SetBoolean vdbeTrace(boolean on) {
		return SqlStatements.pragma(Pragma.Name.VDBE_TRACE).withValue(on);
	}

	/**
	 * Produces the statement<br/>
	 * <code>PRAGMA wal_autocheckpoint</code><br/>
	 * retrieving the maximum number of WAL pages before an automatic checkpoint is run.
	 */
	public static Pragma walAutoCheckpoint() {
		return SqlStatements.pragma(Pragma.Name.WAL_AUTOCHECKPOINT);
	}

	/**
	 * Produces the statement<br/>
	 * <code>PRAGMA wal_autocheckpoint(maxPages)</code><br/>
	 * setting the maximum number of WAL pages before an automatic checkpoint is run.
	 * @param maxPages the maximum number of used WAL pages after which automatic checkpoint is run;
	 * {@literal null} if automatic checkpoints have to be turned off
	 */
	public static Pragma.SetNumber walAutoCheckpoint(Integer maxPages) {
		if (maxPages != null && maxPages <= 0) {
			throw new IllegalArgumentException(
				"If specified, the limit of used WAL pages must be positive.");
		}
		return SqlStatements.pragma(Pragma.Name.WAL_AUTOCHECKPOINT).withValue(maxPages == null ? 0 : maxPages);
	}

	/**
	 * Produces the statement<br/>
	 * <code>PRAGMA wal_checkpoint</code><br/>
	 * causing a checkpoint operation on all the attached databases.
	 */
	public static Pragma walCheckpoint() {
		return SqlStatements.pragma(Pragma.Name.WAL_CHECKPOINT);
	}

	/**
	 * Produces the statement<br/>
	 * <code>PRAGMA wal_checkpoint(PASSIVE|FULL|RESTART|TRUNCATE)</code><br/>
	 * causing a checkpoint operation in the specified mode on all the attached databases.
	 */
	public static Pragma.ForName walCheckpoint(WalCheckpoint mode) {
		return SqlStatements.pragma(Pragma.Name.WAL_CHECKPOINT).forName(mode.toString());
	}

	/**
	 * Produces the statement<br/>
	 * <code>PRAGMA schemaName.wal_checkpoint</code><br/>
	 * causing a checkpoint operation on the specified database.
	 */
	public static Pragma walCheckpoint(CharSequence schemaName) {
		return SqlStatements.pragma(schemaName, Pragma.Name.WAL_CHECKPOINT);
	}

	/**
	 * Produces the statement<br/>
	 * <code>PRAGMA schemaName.wal_checkpoint(PASSIVE|FULL|RESTART|TRUNCATE)</code><br/>
	 * causing a checkpoint operation in the specified mode on the specified database.
	 */
	public static Pragma.ForName walCheckpoint(CharSequence schemaName, WalCheckpoint type) {
		return SqlStatements.pragma(schemaName, Pragma.Name.WAL_CHECKPOINT).forName(type.toString());
	}

	/**
	 * Produces the statement<br/>
	 * <code>PRAGMA writable_schema(TRUE|FALSE)</code><br/>
	 * allowing or disallowing one to make changes to the database schema by issuing
	 * INSERT, UPDATE, and DELETE statements to the master tables.
	 */
	public static Pragma.SetBoolean writableSchema(boolean on) {
		return SqlStatements.pragma(Pragma.Name.WRITABLE_SCHEMA).withValue(on);
	}

	private static void checkPageSize(int value) {
		if (value < 512 || value > 65536) {
			throw new IllegalArgumentException("Valid page size must be between 512 and 65536 bytes.");
		} else if (!isPowerOfTwo(value)) {
			throw new IllegalArgumentException("Valid page size must be an exact power of two.");
		}
	}

	private static boolean isPowerOfTwo(int value) {
		return value > 0 && (value & (value - 1)) == 0;
	}
}
