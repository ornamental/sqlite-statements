package dev.ornamental.sqlite.statement;

import java.util.Iterator;
import java.util.function.Consumer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * The utility class whose primary goal is character manipulation as required in many SQLite statements.
 */
final class SqliteUtilities {

	private static final Pattern TYPE_PRECISION_PATTERN = Pattern.compile("\\(\\s*[+-]?\\d+\\s*(,\\s*[+-]?\\d+)?\\)$");

	private static final Pattern UNQUOTED_TYPE_PATTERN = Pattern.compile("^\\s*(\\p{L}(\\p{Alnum}|_)*\\s*)+$");

	private SqliteUtilities() { }

	/**
	 * Doubles the single quote characters and appends the result to the specified {@link StringBuilder}.
	 * @param receptacle the receiving {@link StringBuilder} instance
	 * @param s the original character sequence
	 */
	public static void escapeSingleQuotes(StringBuilder receptacle, CharSequence s) {
		doubleCharacter('\'', s, receptacle);
	}

	/**
	 * Doubles the double quote characters and appends the result to the specified {@link StringBuilder}.
	 * @param receptacle the receiving {@link StringBuilder} instance
	 * @param s the original character sequence
	 */
	public static void escapeDoubleQuotes(StringBuilder receptacle, CharSequence s) {
		doubleCharacter('"', s, receptacle);
	}

	/**
	 * Conditionally adds an opening parenthesis before executing an action on a {@link StringBuilder}
	 * instance and a closing one after.
	 * @param receptacle the receiving {@link StringBuilder} instance
	 * @param set the flag signalling if parentheses must be added
	 * @param action the action to execute on the passed {@link StringBuilder} instance
	 */
	public static void parentheses(StringBuilder receptacle, boolean set, Consumer<StringBuilder> action) {
		if (set) {
			receptacle.append('(');
		}
		action.accept(receptacle);
		if (set) {
			receptacle.append(')');
		}
	}

	/**
	 * Quotes an SQLite name if it contains any characters requiring quoting, appending the result
	 * to a {@link StringBuilder} instance.
	 * @param receptacle the receiving {@link StringBuilder} instance
	 * @param name the name which might need quoting
	 */
	public static void quoteNameIfNecessary(StringBuilder receptacle, CharSequence name) {
		if (nameNeedsQuotation(name)) {
			receptacle.append('"');
			escapeDoubleQuotes(receptacle, name);
			receptacle.append('"');
		} else {
			receptacle.append(name);
		}
	}

	/**
	 * Appends the non-null name parts to the {@link StringBuilder} while unconditionally quoting
	 * each of them and putting the dot symbol ('.') between the parts.
	 * The leading {@literal null} parts are skipped.
	 * @param receptacle the {@link StringBuilder} instance receiving the output
	 * @param parts the name parts in the desired order; the leading elements may be {@literal null};
	 * there must be at least one non-null part
	 */
	public static void appendQuotedName(StringBuilder receptacle, CharSequence... parts) {
		int k = -1;
		for (int i = 0; i < parts.length; i++) {
			if (k == -1 && parts[i] != null) {
				k = i;
			} else if (k != -1 && parts[i] == null) {
				throw new IllegalArgumentException("Only the leading parts of the name might be null.");
			}
		}

		if (k == -1) {
			throw new IllegalArgumentException("The name must contain at least one part.");
		}

		for (int i = k; i < parts.length; i++) {
			receptacle.append('"');
			escapeDoubleQuotes(receptacle, parts[i]);
			receptacle.append('"');
			if (i < parts.length - 1) {
				receptacle.append('.');
			}
		}
	}

	/**
	 * Appends the quoted names delimited by commas and appends the result to a {@link StringBuilder} instance
	 * @param receptacle the {@link StringBuilder} instance receiving the output
	 * @param names the sequence of names
	 */
	public static void appendQuotedDelimited(StringBuilder receptacle, Iterable<? extends CharSequence> names) {
		Iterator<? extends CharSequence> iterator = names.iterator();
		if (!iterator.hasNext()) {
			return;
		}

		CharSequence last = iterator.next();
		while (true) {
			SqliteUtilities.appendQuotedName(receptacle, last);
			if (iterator.hasNext()) {
				receptacle.append(", ");
				last = iterator.next();
			} else {
				break;
			}
		}
	}

	/**
	 * Performs type definition quoting if necessary and appends the  result to the supplied
	 * {@link StringBuilder} instance. If a trailing precision part is discovered, it is not quoted.
	 * @param receptacle the {@link StringBuilder} instance receiving the output
	 * @param typeDefinition the type definition to quote if necessary
	 */
	public static void quoteType(StringBuilder receptacle, CharSequence typeDefinition) {
		CharSequence typeName;
		String typePrecision;

		Matcher matcher = TYPE_PRECISION_PATTERN.matcher(typeDefinition);
		if (matcher.find()) {
			typeName = typeDefinition.subSequence(0, matcher.start());
			typePrecision = matcher.group();
		} else {
			typeName = typeDefinition;
			typePrecision = "";
		}

		if (UNQUOTED_TYPE_PATTERN.matcher(typeName).matches()) {
			receptacle.append(typeName);
		} else {
			receptacle.append('"');
			SqliteUtilities.escapeDoubleQuotes(receptacle, typeName);
			receptacle.append('"');
		}
		receptacle.append(typePrecision);
	}

	private static boolean nameNeedsQuotation(CharSequence name) {
		// SQLite names may be most anything; they only need quotation if empty, start with a digit,
		// or contain symbols having special meaning for SQLite; we perform quotation if there is
		// at least one character not from Unicode alphanumeric ranges
		// (nevertheless, we always quote schema, table and column names, including the table function names,
		// trigger and constraint names)
		return
			name.length() == 0
				|| Character.isDigit(name.charAt(0))
				|| !name.chars().allMatch(ch -> Character.isLetterOrDigit((char)ch) || ch == '_');
	}

	private static void doubleCharacter(char c, CharSequence s, StringBuilder receptacle) {
		if (s.chars().noneMatch(ch -> ch == c)) {
			receptacle.append(s);
		} else {
			for (int i = 0, length = s.length(); i < length; i++) {
				char ch = s.charAt(i);
				if (ch == c) {
					receptacle.append(c);
				}
				receptacle.append(ch);
			}
		}
	}

	public static SqlExpression[] prependArray(SqlExpression[] tail, SqlExpression... head) {
		SqlExpression[] result = new SqlExpression[head.length + tail.length];
		System.arraycopy(head, 0, result, 0, head.length);
		System.arraycopy(tail, 0, result, head.length, tail.length);
		return result;
	}
}
