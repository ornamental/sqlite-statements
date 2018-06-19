package dev.ornamental.sqlite.statement;

import static dev.ornamental.sqlite.statement.SqlStatements.pragma;

/**
 * Represents a <code>PRAGMA</code> statement. This base class stands for a statement
 * without arguments while the nested subclasses {@link SetBoolean}, {@link SetNumber},
 * {@link SetString}, and {@link ForName} correspond to <code>PRAGMA</code> statements
 * with a parameter.<br/>
 * This is a complete SQL statement.
 */
public final class Pragma implements ExplicableStatement {

	/**
	 * Represents a <code>PRAGMA</code> statement with a boolean parameter. Boolean
	 * parameters are rendered as <code>ON</code> for {@literal true} and <code>OFF</code>
	 * for {@literal false} by this class.<br/>
	 * This is a complete SQL statement.
	 */
	public static final class SetBoolean implements ExplicableStatement {

		private final Pragma pragma;

		private final boolean value;

		SetBoolean(Pragma pragma, boolean value) {
			this.pragma = pragma;
			this.value = value;
		}

		@Override
		public SetBoolean copy() throws IllegalStateException {
			Pragma pragmaCopy = pragma.copy();

			return pragmaCopy == pragma ? this : new SetBoolean(pragmaCopy, value);
		}

		@Override
		public void build(StringBuilder receptacle) {
			pragma.build(receptacle);
			receptacle.append('(').append(value ? "TRUE" : "FALSE").append(')');
		}
	}

	/**
	 * Represents a <code>PRAGMA</code> statement with an integral parameter.<br/>
	 * This is a complete SQL statement.
	 */
	public static final class SetNumber implements ExplicableStatement {

		private final Pragma pragma;

		private final long value;

		SetNumber(Pragma pragma, long value) {
			this.pragma = pragma;
			this.value = value;
		}

		@Override
		public SetNumber copy() throws IllegalStateException {
			Pragma pragmaCopy = pragma.copy();

			return pragmaCopy == pragma ? this : new SetNumber(pragmaCopy, value);
		}

		@Override
		public void build(StringBuilder receptacle) {
			pragma.build(receptacle);
			receptacle.append('(').append(value).append(')');
		}
	}

	/**
	 * Represents a <code>PRAGMA</code> statement with text parameter.<br/>
	 * This is a complete SQL statement.
	 */
	public static final class SetString implements ExplicableStatement {

		private final Pragma pragma;

		private final CharSequence value;

		SetString(Pragma pragma, CharSequence value) {
			this.pragma = pragma;
			this.value = value;
		}

		@Override
		public SetString copy() throws IllegalStateException {
			Pragma pragmaCopy = pragma.copy();
			CharSequence valueCopy = value.toString();

			return pragmaCopy == pragma && valueCopy == value
				? this : new SetString(pragmaCopy, valueCopy);
		}

		@Override
		public void build(StringBuilder receptacle) {
			pragma.build(receptacle);
			receptacle.append("('");
			SqliteUtilities.escapeSingleQuotes(receptacle, value);
			receptacle.append("')");
		}
	}

	/**
	 * Represents a <code>PRAGMA</code> statement with a name as its parameter. This
	 * is normally used when the parameter takes values from some enumeration.<br/>
	 * This is a complete SQL statement.
	 */
	public static final class ForName implements ExplicableStatement {

		private final Pragma pragma;

		private final CharSequence name;

		ForName(Pragma pragma, CharSequence name) {
			this.pragma = pragma;
			this.name = name;
		}

		@Override
		public ForName copy() throws IllegalStateException {
			Pragma pragmaCopy = pragma.copy();
			CharSequence nameCopy = name.toString();

			return pragmaCopy == pragma && nameCopy == name
				? this : new ForName(pragmaCopy, nameCopy);
		}

		@Override
		public void build(StringBuilder receptacle) {
			pragma.build(receptacle);
			receptacle.append("(");
			SqliteUtilities.quoteNameIfNecessary(receptacle, name);
			receptacle.append(")");
		}
	}

	/**
	 * This class contains the names of standard non-deprecated <code>PRAGMA</code>s.
	 */
	public static final class Name {

		public static final String APPLICATION_ID = "application_id";

		public static final String AUTO_VACUUM = "auto_vacuum";

		public static final String AUTOMATIC_INDEX = "automatic_index";

		public static final String BUSY_TIMEOUT = "busy_timeout";

		public static final String CACHE_SIZE = "cache_size";

		public static final String CACHE_SPILL = "cache_spill";

		public static final String CASE_SENSITIVE_LIKE = "case_sensitive_like";

		public static final String CELL_SIZE_CHECK = "cell_size_check";

		public static final String CHECKPOINT_FULLFSYNC = "checkpoint_fullfsync";

		public static final String COLLATION_LIST = "collation_list";

		public static final String COMPILE_OPTIONS = "compile_options";

		public static final String DATA_VERSION = "data_version";

		public static final String DATABASE_LIST = "database_list";

		public static final String DEFER_FOREIGN_KEYS = "defer_foreign_keys";

		public static final String ENCODING = "encoding";

		public static final String FOREIGN_KEY_CHECK = "foreign_key_check";

		public static final String FOREIGN_KEY_LIST = "foreign_key_list";

		public static final String FOREIGN_KEYS = "foreign_keys";

		public static final String FREELIST_COUNT = "freelist_count";

		public static final String FULLFSYNC = "fullfsync";

		public static final String FUNCTION_LIST = "function_list";

		public static final String IGNORE_CHECK_CONSTRAINTS = "ignore_check_constraints";

		public static final String INCREMENTAL_VACUUM = "incremental_vacuum";

		public static final String INDEX_INFO = "index_info";

		public static final String INDEX_LIST = "index_list";

		public static final String INDEX_XINFO = "index_xinfo";

		public static final String INTEGRITY_CHECK = "integrity_check";

		public static final String JOURNAL_MODE = "journal_mode";

		public static final String JOURNAL_SIZE_LIMIT = "journal_size_limit";

		public static final String LEGACY_FILE_FORMAT = "legacy_file_format";

		public static final String LOCKING_MODE = "locking_mode";

		public static final String MAX_PAGE_COUNT = "max_page_count";

		public static final String MMAP_SIZE = "mmap_size";

		public static final String MODULE_LIST = "module_list";

		public static final String OPTIMIZE = "optimize";

		public static final String PAGE_COUNT = "page_count";

		public static final String PAGE_SIZE = "page_size";

		public static final String PARSER_TRACE = "parser_trace";

		public static final String PRAGMA_LIST = "pragma_list";

		public static final String QUERY_ONLY = "query_only";

		public static final String QUICK_CHECK = "quick_check";

		public static final String READ_UNCOMMITTED = "read_uncommitted";

		public static final String RECURSIVE_TRIGGERS = "recursive_triggers";

		public static final String REVERSE_UNORDERED_SELECTS = "reverse_unordered_selects";

		public static final String SCHEMA_VERSION = "schema_version";

		public static final String SECURE_DELETE = "secure_delete";

		public static final String SHRINK_MEMORY = "shrink_memory";

		public static final String SOFT_HEAP_LIMIT = "soft_heap_limit";

		public static final String STATS = "stats";

		public static final String SYNCHRONOUS = "synchronous";

		public static final String TABLE_INFO = "table_info";

		public static final String TEMP_STORE = "temp_store";

		public static final String THREADS = "threads";

		public static final String USER_VERSION = "user_version";

		public static final String VDBE_ADDOPTRACE = "vdbe_addoptrace";

		public static final String VDBE_DEBUG = "vdbe_debug";

		public static final String VDBE_LISTING = "vdbe_listing";

		public static final String VDBE_TRACE = "vdbe_trace";

		public static final String WAL_AUTOCHECKPOINT = "wal_autocheckpoint";

		public static final String WAL_CHECKPOINT = "wal_checkpoint";

		public static final String WRITABLE_SCHEMA = "writable_schema";

		private Name() { }
	}

	private final CharSequence schemaName; // nullable

	private final String pragma;

	Pragma(CharSequence schemaName, String pragma) {
		this.schemaName = schemaName;
		this.pragma = pragma;
	}

	/**
	 * Adds a boolean parameter to the <code>PRAGMA</code>. The value {@literal true}
	 * will be rendered as <code>ON</code>; the value {@literal false} will be rendered as <code>OFF</code>.
	 * @param value the boolean parameter value
	 * @return the <code>PRAGMA</code> statement with the specified parameter value
	 */
	public SetBoolean withValue(boolean value) {
		return new SetBoolean(this, value);
	}

	/**
	 * Adds an integral parameter to the <code>PRAGMA</code>.
	 * @param value the integral parameter value
	 * @return the <code>PRAGMA</code> statement with the specified parameter value
	 */
	public SetNumber withValue(long value) {
		return new SetNumber(this, value);
	}

	/**
	 * Adds a text parameter to the <code>PRAGMA</code>.
	 * @param value the text parameter value
	 * @return the <code>PRAGMA</code> statement with the specified parameter value
	 */
	public SetString withValue(CharSequence value) {
		return new SetString(this, value);
	}

	/**
	 * Adds an unquoted text (name) parameter to the <code>PRAGMA</code>.
	 * @param name the name to use as theparameter
	 * @return the <code>PRAGMA</code> statement with the specified name as its parameter
	 */
	public ForName forName(CharSequence name) {
		return new ForName(this, name);
	}

	@Override
	public Pragma copy() throws IllegalStateException {
		CharSequence schemaNameCopy = schemaName == null ? null : schemaName.toString();

		return schemaNameCopy == schemaName ? this : new Pragma(schemaNameCopy, pragma);
	}

	@Override
	public void build(StringBuilder receptacle) {
		receptacle.append("PRAGMA ");
		if (schemaName != null) {
			SqliteUtilities.appendQuotedName(receptacle, schemaName);
			receptacle.append('.');
		}
		SqliteUtilities.quoteNameIfNecessary(receptacle, pragma);
	}
}
