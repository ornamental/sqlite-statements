package dev.ornamental.sqlite.statement;

import javax.xml.bind.DatatypeConverter;

/**
 * The abstract ancestor of all the literal SQL expressions.<br/>
 * Contains static factory methods for the literals.<br/>
 * The implementations of this class represent complete SQL expressions.
 */
public abstract class Literal implements SqlExpression {

	private static final class LongLiteral extends Literal {

		private final long value;

		private LongLiteral(long value) {
			this.value = value;
		}

		@Override
		public void appendTo(StringBuilder receptacle) {
			receptacle.append(value);
		}
	}

	private static final class DoubleLiteral extends Literal {

		private final double value;

		private DoubleLiteral(double value) {
			this.value = value;
		}

		@Override
		public void appendTo(StringBuilder receptacle) {
			receptacle.append(value);
		}
	}

	private static final class NumberLiteral extends Literal {

		private final Number value;

		private NumberLiteral(Number value) {
			this.value = value;
		}

		@Override
		public void appendTo(StringBuilder receptacle) {
			receptacle.append(value);
		}
	}

	static final class BlobLiteral extends Literal {

		private final byte[] value;

		private BlobLiteral(byte[] value) {
			// NB array is used as immutable here; this is not necessarily the case
			this.value = value;
		}

		@Override
		public void appendTo(StringBuilder receptacle) {
			append(value, receptacle);
		}

		public static void append(byte[] value, StringBuilder receptacle) {
			receptacle.append('X').append('\'').append(DatatypeConverter.printHexBinary(value)).append('\'');
		}
	}

	static final class StringLiteral extends Literal {

		private final String value;

		public StringLiteral(String value) {
			this.value = value;
		}

		@Override
		public void appendTo(StringBuilder receptacle) {
			append(value, receptacle);
		}

		public static void append(CharSequence value, StringBuilder receptacle) {
			receptacle.append('\'');
			SqliteUtilities.escapeSingleQuotes(receptacle, value);
			receptacle.append('\'');
		}
	}

	/**
	 * The <code><strong>NULL</strong></code> literal
	 */
	public static final Literal NULL = new Literal() {

		@Override
		public void appendTo(StringBuilder receptacle) {
			receptacle.append("NULL");
		}
	};

	/**
	 * Represents the <code><strong>TRUE</strong></code> literal.<br/>
	 * Note that the older versions of SQLite do not support this literal
	 * (in which case numeric literal <code>1</code> should be used).
	 */
	public static final Literal TRUE = new Literal() {

		@Override
		public void appendTo(StringBuilder receptacle) {
			receptacle.append("TRUE");
		}
	};

	/**
	 * Represents the <code><strong>FALSE</strong></code> literal.<br/>
	 * Note that the older versions of SQLite do not support this literal
	 * (in which case numeric literal <code>0</code> should be used).
	 */
	public static final Literal FALSE = new Literal() {

		@Override
		public void appendTo(StringBuilder receptacle) {
			receptacle.append("FALSE");
		}
	};

	/**
	 * The <code><strong>CURRENT_TIME</strong></code> literal
	 */
	public static final Literal CURRENT_TIME = new Literal() {

		@Override
		public void appendTo(StringBuilder receptacle) {
			receptacle.append("CURRENT_TIME");
		}
	};

	/**
	 * The <code><strong>CURRENT_DATE</strong></code> literal
	 */
	public static final Literal CURRENT_DATE = new Literal() {

		@Override
		public void appendTo(StringBuilder receptacle) {
			receptacle.append("CURRENT_DATE");
		}
	};

	/**
	 * The <code><strong>CURRENT_TIMESTAMP</strong></code> literal
	 */
	public static final Literal CURRENT_TIMESTAMP = new Literal() {

		@Override
		public void appendTo(StringBuilder receptacle) {
			receptacle.append("CURRENT_TIMESTAMP");
		}
	};

	/**
	 * The <code><strong>NOW</strong></code> literal used in some date and time functions
	 */
	public static final Literal NOW = Literal.value("NOW");

	private Literal() { }

	/**
	 * Returns either {@link #TRUE} or {@link #FALSE} literal depending on the passed value.
	 * @param value the logical value corresponding to the desired literal
	 * @return {@link #TRUE} literal if the passed value is {@literal true}; otherwise, {@link #FALSE}
	 */
	public static Literal value(boolean value) {
		return value ? TRUE : FALSE;
	}

	/**
	 * Returns the SQL literal corresponding to the given integral value.
	 * @param value the value to wrap in an SQL literal
	 * @return the SQL literal corresponding to the given integral value
	 */
	public static Literal value(long value) {
		return new LongLiteral(value);
	}

	/**
	 * Returns the SQL literal corresponding to the given floating-point value.
	 * @param value the value to wrap in an SQL literal
	 * @return the SQL literal corresponding to the given floating-point value
	 */
	public static Literal value(double value) {
		return new DoubleLiteral(value);
	}

	/**
	 * Returns the SQL literal corresponding to the given numeric value.
	 * @param value the value to wrap in an SQL literal
	 * @return the SQL literal corresponding to the given numeric value
	 */
	public static Literal value(Number value) {
		return new NumberLiteral(value);
	}

	/**
	 * Returns the SQL literal corresponding to the given byte sequence.
	 * @param value the value to wrap in an SQL literal
	 * @return the SQL literal corresponding to the given byte sequence
	 */
	public static Literal value(byte[] value) {
		return new BlobLiteral(value);
	}

	/**
	 * Returns the SQL literal corresponding to the given {@link String}.
	 * @param value the value to wrap in an SQL literal
	 * @return the SQL literal corresponding to the given {@link String}
	 */
	public static Literal value(String value) {
		return new StringLiteral(value);
	}

	@Override
	public int getPrecedence() {
		return Integer.MAX_VALUE; // literal is an unbreakable expression thus never has to be put in parentheses
	}

	@Override
	public SqlExpression copy() {
		return this;
	}
}
