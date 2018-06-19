package dev.ornamental.sqlite.statement;

/**
 * Defines the built-in functions, including the aggregate functions.
 */
public enum StandardFunction {

	/**
	 * The minimum function; may serve both as scalar function of multiple arguments
	 * and as an aggregate function.
	 */
	MIN,

	/**
	 * The maximum function; may serve both as scalar function of multiple arguments
	 * and as an aggregate function.
	 */
	MAX,

	/**
	 * The average aggregate function
	 */
	AVG,

	/**
	 * The counting aggregate function; this is the only one able to take the special
	 * <code>[<em>tableName</em>.]*</code> argument.
	 */
	COUNT,

	/**
	 * The summation aggregate function; returns <code>NULL</code> if there are no non-null values to add.
	 */
	SUM,

	/**
	 * The summation aggregate function; returns <code>0</code> if there are no non-null values to add.
	 */
	TOTAL,

	/**
	 * The concatenation aggregate function; may take optional argument for the delimiter (which
	 * by default is the comma character) unless the <code>DISTINCT</code> keyword is specified.
	 */
	GROUP_CONCAT,

	/**
	 * The absolute value function
	 */
	ABS,

	/**
	 * The function returning the number of database rows that were changed or inserted or deleted
	 * by the most recently completed <code>INSERT</code>, <code>DELETE</code>, or <code>UPDATE</code> statement,
	 * exclusive of statements in lower-level triggers
	 */
	CHANGES,

	/**
	 * The function returning the string composed of the characters having the specified unicode code point values
	 */
	CHAR,

	/**
	 * The function returning a copy of its first non-null argument, or <code>NULL</code>
	 * if all arguments are <code>NULL</code>
	 */
	COALESCE,

	/**
	 * The function equivalent to <code>GLOB</code> expression with reversed argument order
	 */
	GLOB,

	/**
	 * The function returning the string which is the upper-case hexadecimal rendering
	 * of the BLOB passed as parameter
	 */
	HEX,

	/**
	 * The function returning a copy of its first non-null argument, or <code>NULL</code>
	 * if both arguments are <code>NULL</code>
	 */
	IFNULL,

	/**
	 * The function finding the index of the first occurrence of a string in another string
	 */
	INSTR,

	/**
	 * The function returning the ROWID of the last row insert from the database connection
	 * which invoked the function
	 */
	LAST_INSERT_ROWID,

	/**
	 * For a string value, the function returns the number of characters in the argument prior
	 * to the first NUL character; for a BLOB value, returns the number of bytes in the BLOB.
	 */
	LENGTH,

	/**
	 * The function equivalent to a <code>LIKE</code> expression with the first two (mandatory)
	 * arguments swapped
	 */
	LIKE,

	/**
	 * The no-op function providing a hint to the query planner that the first argument is
	 * a boolean value being <code>TRUE</code> with the probability specified as the second argument
	 */
	LIKELIHOOD,

	/**
	 * The no-op function providing a hint to the query planner that the first argument is
	 * a boolean value being <code>TRUE</code> with probability 0.9375
	 */
	LIKELY,

	/**
	 * The function loading an SQLite extension; always returns <code>NULL</code>.
	 */
	LOAD_EXTENSION,

	/**
	 * The function returning a copy of the passed string with all ASCII characters converted to lower case;
	 * in order for the function to work with Unicode characters, the ICU Internationalization Extension
	 * must be loaded.
	 */
	LOWER,

	/**
	 * The function returning a string formed by removing any and all characters that appear
	 * in the optional second argument (spaces, if it is absent) from the left side of the first argument
	 */
	LTRIM,

	/**
	 * The function returning its first argument if the arguments are different
	 * and <code>NULL</code> if the arguments are the same
	 */
	NULLIF,

	/**
	 * Works like <code>printf()</code> function from the standard C library.
	 */
	PRINTF,

	/**
	 * The function returning the text of an SQL literal which is the value of its argument
	 * suitable for inclusion into an SQL statement; strings are surrounded by single-quotes
	 * with escapes on interior quotes as needed; BLOBs are encoded as hexadecimal literals;
	 * strings with embedded NUL characters cannot be represented as string literals in SQL
	 * and hence the returned string literal is truncated prior to the first NUL.
	 */
	QUOTE,

	/**
	 * The function returning a pseudo-random integer between -9223372036854775808 and +9223372036854775807
	 */
	RANDOM,

	/**
	 * The function returning an <em>n</em>-byte blob containing pseudo-random bytes
	 */
	RANDOMBLOB,

	/**
	 * The substring replacement function
	 */
	REPLACE,

	/**
	 * The function rounding a floating-point value to the specified number
	 * of digits after the decimal separator (zero by default)
	 */
	ROUND,

	/**
	 * The function returning a string formed by removing any and all characters that appear
	 * in the optional second argument (spaces, if it is absent) from the right side of the first argument
	 */
	RTRIM,

	/**
	 * The function returning the string that is the Soundex encoding of the string
	 */
	SOUNDEX,

	/**
	 * The function returning the <em>n</em>-th compile-time option used to build SQLite
	 * or <code>NULL</code> if <em>n</em> is out of range
	 */
	SQLITE_COMPILEOPTION_GET,

	/**
	 * The function returning the flag indicatingif the specified compile option was
	 * specified when building SQLite
	 */
	SQLITE_COMPILEOPTION_USED,

	/**
	 * The function returning the byte offset in the database file for the beginning
	 * of the record from which value would be read
	 */
	SQLITE_OFFSET,

	/**
	 * The function returning the string that identifies the specific version of the source code
	 * that was used to build the SQLite library
	 */
	SQLITE_SOURCE_ID,

	/**
	 * The function returning the version of SQLite library
	 */
	SQLITE_VERSION,

	/**
	 * The function returning a substring of the specified string within the
	 * specified character index range
	 */
	SUBSTR,

	/**
	 * The function returning the number of row changes caused by <code>INSERT</code>,
	 * <code>UPDATE</code> or <code>DELETE</code> statements since the current database
	 * connection was opened
	 */
	TOTAL_CHANGES,

	/**
	 * The function returning a string formed by removing any and all characters that appear
	 * in the optional second argument (spaces, if it is absent) from both sides of the first argument
	 */
	TRIM,

	/**
	 * The function returning the string that indicates the data type of the expression:
	 * <code>null</code>, <code>integer</code>, <code>real</code>, <code>text</code>,
	 * or <code>blob</code>.
	 */
	TYPEOF,

	/**
	 * The function returning the numeric unicode code point corresponding to the first character
	 * of the string argument
	 */
	UNICODE,

	/**
	 * The no-op function providing a hint to the query planner that the first argument is
	 * a boolean value being <code>FALSE</code> with probability 0.9375
	 */
	UNLIKELY,

	/**
	 * The function returning a copy of the passed string with all ASCII characters converted to upper case;
	 * in order for the function to work with Unicode characters, the ICU Internationalization Extension
	 * must be loaded.
	 */
	UPPER,

	/**
	 * The function returning the BLOB consisting of <em>n</em> 0x00 bytes
	 */
	ZEROBLOB,

	/**
	 * The function returning the date corresponding to the specified date and time
	 * after applying optional date and time modifiers
	 */
	DATE,

	/**
	 * The function returning the time corresponding to the specified date and time
	 * after applying optional date and time modifiers
	 */
	TIME,

	/**
	 * The function returning the date and time corresponding to the specified date and time
	 * after applying optional date and time modifiers
	 */
	DATETIME,

	/**
	 * The function returning the number of days since the beginning of the Julian Period
	 * for the specified date and time after applying optional date and time modifiers
	 */
	JULIANDAY,

	/**
	 * The function formatting the specified date and time according to a format string
	 * after applying optional date and time modifiers
	 */
	STRFTIME
}
