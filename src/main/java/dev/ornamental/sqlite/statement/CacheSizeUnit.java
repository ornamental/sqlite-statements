package dev.ornamental.sqlite.statement;

/**
 * Units used when setting the cache size using the corresponding <code>PRAGMA</code> statement
 * (see {@link Pragmas#cacheSize(int, CacheSizeUnit)} and its overload).
 */
public enum CacheSizeUnit {

	/**
	 * The cache size is set in pages. Page size is a separate database property.
	 */
	PAGE,

	/**
	 * The cache size is set in kibibytes.
	 */
	KB
}
