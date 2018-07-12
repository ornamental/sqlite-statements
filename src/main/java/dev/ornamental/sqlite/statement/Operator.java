package dev.ornamental.sqlite.statement;

/**
 * The unary and binary operators recognized by SQLite. Each operator has a string representation
 * and a precedence value (the higher the value, the higher the precedence).
 */
public enum Operator {

	/**
	 * Unary prefix bit inversion operator <code><strong>~</strong></code> performing
	 * bitwise negation of the argument (which is cast to numeric if necessary).
	 */
	INV("~", 10),

	/**
	 * Unary prefix no-operation operator <code><strong>+</strong></code> is sometimes used
	 * in <code>WHERE</code> clauses to prevent SQLite from constraining a certain index.
	 */
	NOP("+", 10),

	/**
	 * Unary prefix negation operator <code><strong>-</strong></code> inverts the sign of
	 * a numeric operand or operand implicitly cast to the numeric type.
	 */
	NEG("-", 10),

	/**
	 * Unary prefix logical negation operator <code><strong>NOT</strong></code> negates
	 * the operand interpreted as a boolean value. Always delimited from its operand
	 * by at least one space symbol.
	 */
	NOT("NOT ", 10),

	/**
	 * Binary operator <code><strong>||</strong></code> of string concatenation.
	 */
	CONCAT("||", 9),

	/**
	 * Binary operator <code><strong>*</strong></code> of number multiplication.
	 */
	MULT("*", 8),

	/**
	 * Binary operator <code><strong>/</strong></code> of number division.
	 */
	DIV("/", 8),

	/**
	 * Binary operator <code><strong>%</strong></code> finding division remainder (modulo operator).
	 */
	MOD("%", 8),

	/**
	 * Binary operator <code><strong>+</strong></code> of number summation.
	 */
	PLUS("+", 7),

	/**
	 * Binary operator <code><strong>-</strong></code> of number subtraction.
	 */
	MINUS("-", 7),

	/**
	 * Binary operator <code><strong>&lt;&lt;</strong></code> of left bit shift.
	 */
	LSHIFT("<<", 6),

	/**
	 * Binary operator <code><strong>&gt;&gt;</strong></code> of right bit shift.
	 */
	RSHIFT(">>", 6),

	/**
	 * Binary operator <code><strong>&amp;</strong></code> (bitwise AND).
	 */
	BIT_AND("&", 6),

	/**
	 * Binary operator <code><strong>|</strong></code> (bitwise OR).
	 */
	BIT_OR("|", 6),

	/**
	 * Binary comparison operator <code><strong>&lt;</strong></code> (less than).
	 */
	LESS("<", 5),

	/**
	 * Binary comparison operator <code><strong>&lt;=</strong></code> (less or equals to).
	 */
	LESS_EQ("<=", 5),

	/**
	 * Binary comparison operator <code><strong>&gt;</strong></code> (greater than).
	 */
	GREATER(">", 5),

	/**
	 * Binary comparison operator <code><strong>&gt;=</strong></code> (greater or equals to).
	 */
	GREATER_EQ(">=", 4),

	/**
	 * Binary comparison operator <code><strong>=</strong></code> (equals to).
	 */
	EQ("=", 4),

	/**
	 * Binary comparison operator <code><strong>&lt;&gt;</strong></code> (not equals to).
	 */
	NOT_EQ("<>", 4),

	/**
	 * Binary comparison operator <code><strong>IS</strong></code>
	 * (equality with <code>NULL</code> handling). Always delimited
	 * from its operands by at least one space symbol.
	 */
	IS("IS", 4),

	/**
	 * Binary comparison operator <code><strong>IS</strong></code>
	 * (non-equality with <code>NULL</code> handling). Always delimited
	 * from its operands by at least one space symbol.
	 */
	IS_NOT("IS NOT", 4),

	/**
	 * Unary postfix operator <code><strong>ISNULL</strong></code>
	 * (always delimited from its operand by at least one space symbol)
	 * checks if the operand is <code>NULL</code>.
	 */
	ISNULL(" ISNULL", 3),

	/**
	 * Unary postfix operator <code><strong>NOTNULL</strong></code>
	 * (always delimited from its operand by at least one space symbol)
	 * checks if the operand is not <code>NULL</code>.<br>
	 * This operator's alternative form <code>NOT NULL</code> is unused.
	 */
	NOTNULL(" NOTNULL", 3),

	/**
	 * Binary operator <code><strong>AND</strong></code> of logical conjunction.
	 * Always delimited from its operands by at least one space symbol.
	 */
	AND("AND", 2),

	/**
	 * Binary operator <code><strong>OR</strong></code> of logical disjunction.
	 * Always delimited from its operands by at least one space symbol.
	 */
	OR("OR", 1);

	private final String symbol;

	private final int precedence;

	Operator(String symbol, int precedence) {
		this.symbol = symbol;
		this.precedence = precedence;
	}

	public String getSymbol() {
		return symbol;
	}

	public int getPrecedence() {
		return precedence;
	}
}
