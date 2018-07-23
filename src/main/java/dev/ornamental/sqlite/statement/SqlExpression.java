package dev.ornamental.sqlite.statement;

import static dev.ornamental.sqlite.statement.SqlExpressions.cast;
import static dev.ornamental.sqlite.statement.SqlExpressions.rowOf;
import static dev.ornamental.sqlite.statement.SqlExpressions.rowOfBlobs;
import static dev.ornamental.sqlite.statement.SqlExpressions.rowOfNumbers;
import static dev.ornamental.sqlite.statement.SqlExpressions.rowOfStrings;

import java.util.Arrays;

/**
 * This interface is implemented by all the classes representing SQL expressions.
 */
public interface SqlExpression extends Variable<SqlExpression>, ResultElement {

	/**
	 * Returns the precedence of the expression
	 * @return the precedence of the expression
	 */
	int getPrecedence();

	/**
	 * Given this expression, adds an alias to it; the result may be used
	 * in the result column list following the <code>SELECT</code> keyword.
	 * @param columnAlias the alias to assign to the expression
	 * @return the aliased expression
	 */
	default ResultElement as(CharSequence columnAlias) {
		return new NamedResultColumn(this, columnAlias);
	}

	/**
	 * Creates a concatenation expression having the form<br>
	 * <code><strong><em>expression</em> || <em>otherExpression</em></strong></code><br>
	 * where <code><em>expression</em></code> is this expression.
	 * @param otherExpression the right operand
	 * @return the concatenation expression
	 */
	default SqlExpression concat(SqlExpression otherExpression) {
		return new BinaryOperator(this, otherExpression, Operator.CONCAT);
	}

	/**
	 * Creates a multiplication expression having the form<br>
	 * <code><strong><em>expression</em> * <em>otherExpression</em></strong></code><br>
	 * where <code><em>expression</em></code> is this expression.
	 * @param otherExpression the right operand
	 * @return the multiplication expression
	 */
	default SqlExpression mult(SqlExpression otherExpression) {
		return new BinaryOperator(this, otherExpression, Operator.MULT);
	}

	/**
	 * Creates a division expression having the form<br>
	 * <code><strong><em>expression</em> / <em>otherExpression</em></strong></code><br>
	 * where <code><em>expression</em></code> is this expression.
	 * @param otherExpression the right operand
	 * @return the division expression
	 */
	default SqlExpression div(SqlExpression otherExpression) {
		return new BinaryOperator(this, otherExpression, Operator.DIV);
	}

	/**
	 * Creates a residue (modulo) expression having the form<br>
	 * <code><strong><em>expression</em> % <em>otherExpression</em></strong></code><br>
	 * where <code><em>expression</em></code> is this expression.
	 * @param otherExpression the right operand
	 * @return the residue (modulo) expression
	 */
	default SqlExpression mod(SqlExpression otherExpression) {
		return new BinaryOperator(this, otherExpression, Operator.MOD);
	}

	/**
	 * Creates an addition expression having the form<br>
	 * <code><strong><em>expression</em> + <em>otherExpression</em></strong></code><br>
	 * where <code><em>expression</em></code> is this expression.
	 * @param otherExpression the right operand
	 * @return the addition expression
	 */
	default SqlExpression plus(SqlExpression otherExpression) {
		return new BinaryOperator(this, otherExpression, Operator.PLUS);
	}

	/**
	 * Creates a subtraction expression having the form<br>
	 * <code><strong><em>expression</em> - <em>otherExpression</em></strong></code><br>
	 * where <code><em>expression</em></code> is this expression.
	 * @param otherExpression the right operand
	 * @return the subtraction expression
	 */
	default SqlExpression minus(SqlExpression otherExpression) {
		return new BinaryOperator(this, otherExpression, Operator.MINUS);
	}

	/**
	 * Creates a left bit shift expression having the form<br>
	 * <code><strong><em>expression</em> &lt;&lt; <em>otherExpression</em></strong></code><br>
	 * where <code><em>expression</em></code> is this expression.
	 * @param otherExpression the right operand
	 * @return the left bit shift expression
	 */
	default SqlExpression shiftL(SqlExpression otherExpression) {
		return new BinaryOperator(this, otherExpression, Operator.LSHIFT);
	}

	/**
	 * Creates a right bit shift expression having the form<br>
	 * <code><strong><em>expression</em> &gt;&gt; <em>otherExpression</em></strong></code><br>
	 * where <code><em>expression</em></code> is this expression.
	 * @param otherExpression the right operand
	 * @return the right bit shift expression
	 */
	default SqlExpression shiftR(SqlExpression otherExpression) {
		return new BinaryOperator(this, otherExpression, Operator.RSHIFT);
	}

	/**
	 * Creates a bit AND expression having the form<br>
	 * <code><strong><em>expression</em> &amp; <em>otherExpression</em></strong></code><br>
	 * where <code><em>expression</em></code> is this expression.
	 * @param otherExpression the right operand
	 * @return the bit AND expression
	 */
	default SqlExpression bitAnd(SqlExpression otherExpression) {
		return new BinaryOperator(this, otherExpression, Operator.BIT_AND);
	}

	/**
	 * Creates a bit OR expression having the form<br>
	 * <code><strong><em>expression</em> | <em>otherExpression</em></strong></code><br>
	 * where <code><em>expression</em></code> is this expression.
	 * @param otherExpression the right operand
	 * @return the bit OR expression
	 */
	default SqlExpression bitOr(SqlExpression otherExpression) {
		return new BinaryOperator(this, otherExpression, Operator.BIT_OR);
	}

	/**
	 * Creates a less-than comparison expression having the form<br>
	 * <code><strong><em>expression</em> &lt; <em>otherExpression</em></strong></code><br>
	 * where <code><em>expression</em></code> is this expression.
	 * @param otherExpression the right operand
	 * @return the less-than comparison expression
	 */
	default SqlExpression lt(SqlExpression otherExpression) {
		return new BinaryOperator(this, otherExpression, Operator.LESS);
	}

	/**
	 * Creates a less-than-or-equal-to comparison expression having the form<br>
	 * <code><strong><em>expression</em> &lt;= <em>otherExpression</em></strong></code><br>
	 * where <code><em>expression</em></code> is this expression.
	 * @param otherExpression the right operand
	 * @return the less-than-or-equal-to comparison expression
	 */
	default SqlExpression leq(SqlExpression otherExpression) {
		return new BinaryOperator(this, otherExpression, Operator.LESS_EQ);
	}

	/**
	 * Creates a greater-than comparison expression having the form<br>
	 * <code><strong><em>expression</em> &gt; <em>otherExpression</em></strong></code><br>
	 * where <code><em>expression</em></code> is this expression.
	 * @param otherExpression the right operand
	 * @return the greater-than comparison expression
	 */
	default SqlExpression gt(SqlExpression otherExpression) {
		return new BinaryOperator(this, otherExpression, Operator.GREATER);
	}

	/**
	 * Creates a greater-than-or-equal-to comparison expression having the form<br>
	 * <code><strong><em>expression</em> &gt;= <em>otherExpression</em></strong></code><br>
	 * where <code><em>expression</em></code> is this expression.
	 * @param otherExpression the right operand
	 * @return the greater-than-or-equal-to comparison expression
	 */
	default SqlExpression geq(SqlExpression otherExpression) {
		return new BinaryOperator(this, otherExpression, Operator.GREATER_EQ);
	}

	/**
	 * Creates an equality expression having the form<br>
	 * <code><strong><em>expression</em> = <em>otherExpression</em></strong></code><br>
	 * where <code><em>expression</em></code> is this expression.
	 * @param otherExpression the right operand
	 * @return the equality expression
	 */
	default SqlExpression eq(SqlExpression otherExpression) {
		return new BinaryOperator(this, otherExpression, Operator.EQ);
	}

	/**
	 * Creates a non-equality expression having the form<br>
	 * <code><strong><em>expression</em> &lt;&gt; <em>otherExpression</em></strong></code><br>
	 * where <code><em>expression</em></code> is this expression.
	 * @param otherExpression the right operand
	 * @return the non-equality expression
	 */
	default SqlExpression neq(SqlExpression otherExpression) {
		return new BinaryOperator(this, otherExpression, Operator.NOT_EQ);
	}

	/**
	 * Creates an <code>IS</code> expression having the form<br>
	 * <code><strong><em>expression</em> IS <em>otherExpression</em></strong></code><br>
	 * where <code><em>expression</em></code> is this expression.
	 * @param otherExpression the right operand
	 * @return the <code>IS</code> expression
	 */
	default SqlExpression is(SqlExpression otherExpression) {
		return new BinaryOperator(this, otherExpression, Operator.IS);
	}

	/**
	 * Creates an <code>IS NOT</code> expression having the form<br>
	 * <code><strong><em>expression</em> IS NOT <em>otherExpression</em></strong></code><br>
	 * where <code><em>expression</em></code> is this expression.
	 * @param otherExpression the right operand
	 * @return the <code>IS NOT</code> expression
	 */
	default SqlExpression isNot(SqlExpression otherExpression) {
		return new BinaryOperator(this, otherExpression, Operator.IS_NOT);
	}

	/**
	 * Creates a logical conjunction expression having the form<br>
	 * <code><strong><em>expression</em> AND <em>otherExpression</em></strong></code><br>
	 * where <code><em>expression</em></code> is this expression.
	 * @param otherExpression the right operand
	 * @return the conjunction expression
	 */
	default SqlExpression and(SqlExpression otherExpression) {
		return new BinaryOperator(this, otherExpression, Operator.AND);
	}

	/**
	 * Creates a logical disjunction expression having the form<br>
	 * <code><strong><em>expression</em> OR <em>otherExpression</em></strong></code><br>
	 * where <code><em>expression</em></code> is this expression.
	 * @param otherExpression the right operand
	 * @return the disjunction expression
	 */
	default SqlExpression or(SqlExpression otherExpression) {
		return new BinaryOperator(this, otherExpression, Operator.OR);
	}

	/**
	 * Creates an <code>ISNULL</code> expression having the form<br>
	 * <code><strong><em>expression</em> ISNULL</strong></code><br>
	 * where <code><em>expression</em></code> is this expression.
	 * @return the <code>ISNULL</code> expression
	 */
	default SqlExpression isNull() {
		return new PostfixUnaryOperator(this, Operator.ISNULL);
	}

	/**
	 * Creates a <code>NOTNULL</code> expression having the form<br>
	 * <code><strong><em>expression</em> NOTNULL</strong></code><br>
	 * where <code><em>expression</em></code> is this expression.
	 * @return the <code>NOTNULL</code> expression
	 */
	default SqlExpression isNotNull() {
		return new PostfixUnaryOperator(this, Operator.NOTNULL);
	}

	/**
	 * Creates a <code>COLLATE</code> expression having the form<br>
	 * <code><strong><em>expression</em> COLLATE <em>collationName</em></strong></code><br>
	 * where <code><em>expression</em></code> is this expression.
	 * @param collationName the name of the collation
	 * @return the <code>COLLATE</code> expression
	 */
	default SqlExpression collate(String collationName) {
		return new CollationExpression(this, Collation.named(collationName));
	}

	/**
	 * Creates a <code>COLLATE</code> expression having the form<br>
	 * <code><strong><em>expression</em> COLLATE <em>collation</em></strong></code><br>
	 * where <code><em>expression</em></code> is this expression.
	 * @param collation the desired collation sequence
	 * @return the <code>COLLATE</code> expression
	 */
	default SqlExpression collate(Collation collation) {
		return new CollationExpression(this, collation);
	}

	/**
	 * Creates a <code>LIKE</code> expression having the form<br>
	 * <code><strong><em>expression</em> LIKE <em>patternExpression</em></strong></code><br>
	 * where <code><em>expression</em></code> is this expression.
	 * @param patternExpression the pattern to use for matching
	 * @return the <code>LIKE</code> expression
	 */
	default MatchExpression like(SqlExpression patternExpression) {
		return new MatchExpression(MatchOperator.LIKE, false, this, patternExpression);
	}

	/**
	 * Creates a <code>NOT LIKE</code> expression having the form<br>
	 * <code><strong><em>expression</em> NOT LIKE <em>patternExpression</em></strong></code><br>
	 * where <code><em>expression</em></code> is this expression.
	 * @param patternExpression the pattern to use for matching
	 * @return the <code>NOT LIKE</code> expression
	 */
	default MatchExpression notLike(SqlExpression patternExpression) {
		return new MatchExpression(MatchOperator.LIKE, true, this, patternExpression);
	}

	/**
	 * Creates a <code>LIKE</code> expression having the form<br>
	 * <code><strong><em>expression</em> LIKE <em>pattern</em></strong></code><br>
	 * where <code><em>expression</em></code> is this expression.
	 * @param pattern the pattern to use for matching
	 * @return the <code>LIKE</code> expression
	 */
	default MatchExpression like(String pattern) {
		return new MatchExpression(MatchOperator.LIKE, false, this, Literal.value(pattern));
	}

	/**
	 * Creates a <code>NOT LIKE</code> expression having the form<br>
	 * <code><strong><em>expression</em> NOT LIKE <em>pattern</em></strong></code><br>
	 * where <code><em>expression</em></code> is this expression.
	 * @param pattern the pattern to use for matching
	 * @return the <code>NOT LIKE</code> expression
	 */
	default MatchExpression notLike(String pattern) {
		return new MatchExpression(MatchOperator.LIKE, true, this, Literal.value(pattern));
	}

	/**
	 * Creates a <code>GLOB</code> expression having the form<br>
	 * <code><strong><em>expression</em> GLOB <em>patternExpression</em></strong></code><br>
	 * where <code><em>expression</em></code> is this expression.
	 * @param patternExpression the pattern to use for matching
	 * @return the <code>GLOB</code> expression
	 */
	default MatchExpression glob(SqlExpression patternExpression) {
		return new MatchExpression(MatchOperator.GLOB, false, this, patternExpression);
	}

	/**
	 * Creates a <code>NOT GLOB</code> expression having the form<br>
	 * <code><strong><em>expression</em> NOT GLOB <em>patternExpression</em></strong></code><br>
	 * where <code><em>expression</em></code> is this expression.
	 * @param patternExpression the pattern to use for matching
	 * @return the <code>NOT GLOB</code> expression
	 */
	default MatchExpression notGlob(SqlExpression patternExpression) {
		return new MatchExpression(MatchOperator.GLOB, true, this, patternExpression);
	}

	/**
	 * Creates a <code>GLOB</code> expression having the form<br>
	 * <code><strong><em>expression</em> GLOB <em>pattern</em></strong></code><br>
	 * where <code><em>expression</em></code> is this expression.
	 * @param pattern the pattern to use for matching
	 * @return the <code>GLOB</code> expression
	 */
	default MatchExpression glob(String pattern) {
		return new MatchExpression(MatchOperator.GLOB, false, this, Literal.value(pattern));
	}

	/**
	 * Creates a <code>NOT GLOB</code> expression having the form<br>
	 * <code><strong><em>expression</em> NOT GLOB <em>pattern</em></strong></code><br>
	 * where <code><em>expression</em></code> is this expression.
	 * @param pattern the pattern to use for matching
	 * @return the <code>NOT GLOB</code> expression
	 */
	default MatchExpression notGlob(String pattern) {
		return new MatchExpression(MatchOperator.GLOB, true, this, Literal.value(pattern));
	}

	/**
	 * Creates a <code>REGEXP</code> expression having the form<br>
	 * <code><strong><em>expression</em> REGEXP <em>patternExpression</em></strong></code><br>
	 * where <code><em>expression</em></code> is this expression.
	 * @param patternExpression the pattern to use for matching
	 * @return the <code>REGEXP</code> expression
	 */
	default MatchExpression regexp(SqlExpression patternExpression) {
		return new MatchExpression(MatchOperator.REGEXP, false, this, patternExpression);
	}

	/**
	 * Creates a <code>NOT REGEXP</code> expression having the form<br>
	 * <code><strong><em>expression</em> NOT REGEXP <em>patternExpression</em></strong></code><br>
	 * where <code><em>expression</em></code> is this expression.
	 * @param patternExpression the pattern to use for matching
	 * @return the <code>NOT REGEXP</code> expression
	 */
	default MatchExpression notRegexp(SqlExpression patternExpression) {
		return new MatchExpression(MatchOperator.REGEXP, true, this, patternExpression);
	}

	/**
	 * Creates a <code>REGEXP</code> expression having the form<br>
	 * <code><strong><em>expression</em> REGEXP <em>pattern</em></strong></code><br>
	 * where <code><em>expression</em></code> is this expression.
	 * @param pattern the pattern to use for matching
	 * @return the <code>REGEXP</code> expression
	 */
	default MatchExpression regexp(String pattern) {
		return new MatchExpression(MatchOperator.REGEXP, false, this, Literal.value(pattern));
	}

	/**
	 * Creates a <code>NOT REGEXP</code> expression having the form<br>
	 * <code><strong><em>expression</em> NOT REGEXP <em>pattern</em></strong></code><br>
	 * where <code><em>expression</em></code> is this expression.
	 * @param pattern the pattern to use for matching
	 * @return the <code>NOT REGEXP</code> expression
	 */
	default MatchExpression notRegexp(String pattern) {
		return new MatchExpression(MatchOperator.REGEXP, true, this, Literal.value(pattern));
	}

	/**
	 * Creates a <code>MATCH</code> expression having the form<br>
	 * <code><strong><em>expression</em> MATCH <em>patternExpression</em></strong></code><br>
	 * where <code><em>expression</em></code> is this expression.
	 * @param patternExpression the pattern to use for matching
	 * @return the <code>MATCH</code> expression
	 */
	default MatchExpression match(SqlExpression patternExpression) {
		return new MatchExpression(MatchOperator.MATCH, false, this, patternExpression);
	}

	/**
	 * Creates a <code>NOT MATCH</code> expression having the form<br>
	 * <code><strong><em>expression</em> NOT MATCH <em>patternExpression</em></strong></code><br>
	 * where <code><em>expression</em></code> is this expression.
	 * @param patternExpression the pattern to use for matching
	 * @return the <code>NOT MATCH</code> expression
	 */
	default MatchExpression notMatch(SqlExpression patternExpression) {
		return new MatchExpression(MatchOperator.MATCH, true, this, patternExpression);
	}

	/**
	 * Creates a <code>MATCH</code> expression having the form<br>
	 * <code><strong><em>expression</em> MATCH <em>pattern</em></strong></code><br>
	 * where <code><em>expression</em></code> is this expression.
	 * @param pattern the pattern to use for matching
	 * @return the <code>MATCH</code> expression
	 */
	default MatchExpression match(String pattern) {
		return new MatchExpression(MatchOperator.MATCH, false, this, Literal.value(pattern));
	}

	/**
	 * Creates a <code>NOT MATCH</code> expression having the form<br>
	 * <code><strong><em>expression</em> NOT MATCH <em>pattern</em></strong></code><br>
	 * where <code><em>expression</em></code> is this expression.
	 * @param pattern the pattern to use for matching
	 * @return the <code>NOT MATCH</code> expression
	 */
	default MatchExpression notMatch(String pattern) {
		return new MatchExpression(MatchOperator.MATCH, true, this, Literal.value(pattern));
	}

	/**
	 * Creates a <code>BETWEEN</code> expression having the form<br>
	 * <code><strong><em>expression</em> BETWEEN <em>minExpression</em>
	 * AND <em>maxExpression</em></strong></code><br>
	 * where <code><em>expression</em></code> is this expression.
	 * @param minExpression the lower value bound expression
	 * @param maxExpression the upper value bound expression
	 * @return the <code>BETWEEN</code> expression with the specified bounds
	 */
	default SqlExpression between(SqlExpression minExpression, SqlExpression maxExpression) {
		return new BetweenExpression(false, this, minExpression, maxExpression);
	}

	/**
	 * Creates a <code>BETWEEN</code> expression having the form<br>
	 * <code><strong><em>expression</em> BETWEEN <em>min</em> AND <em>max</em></strong></code><br>
	 * where <code><em>expression</em></code> is this expression.
	 * @param min the lower value bound
	 * @param max the upper value bound
	 * @return the <code>BETWEEN</code> expression with the specified bounds
	 */
	default SqlExpression between(long min, long max) {
		return between(Literal.value(min), Literal.value(max));
	}

	/**
	 * Creates a <code>BETWEEN</code> expression having the form<br>
	 * <code><strong><em>expression</em> BETWEEN <em>min</em> AND <em>max</em></strong></code><br>
	 * where <code><em>expression</em></code> is this expression.
	 * @param min the lower value bound
	 * @param max the upper value bound
	 * @return the <code>BETWEEN</code> expression with the specified bounds
	 */
	default SqlExpression between(double min, double max) {
		return between(Literal.value(min), Literal.value(max));
	}

	/**
	 * Creates a <code>BETWEEN</code> expression having the form<br>
	 * <code><strong><em>expression</em> BETWEEN <em>min</em> AND <em>max</em></strong></code><br>
	 * where <code><em>expression</em></code> is this expression.
	 * @param min the lower value bound
	 * @param max the upper value bound
	 * @return the <code>BETWEEN</code> expression with the specified bounds
	 */
	default SqlExpression between(String min, String max) {
		return between(Literal.value(min), Literal.value(max));
	}

	/**
	 * Creates a <code>NOT BETWEEN</code> expression having the form<br>
	 * <code><strong><em>expression</em> NOT BETWEEN <em>minExpression</em>
	 * AND <em>maxExpression</em></strong></code><br>
	 * where <code><em>expression</em></code> is this expression.
	 * @param minExpression the lower value bound expression
	 * @param maxExpression the upper value bound expression
	 * @return the <code>NOT BETWEEN</code> expression with the specified bounds
	 */
	default SqlExpression notBetween(SqlExpression minExpression, SqlExpression maxExpression) {
		return new BetweenExpression(true, this, minExpression, maxExpression);
	}

	/**
	 * Creates a <code>NOT BETWEEN</code> expression having the form<br>
	 * <code><strong><em>expression</em> NOT BETWEEN <em>min</em> AND <em>max</em></strong></code><br>
	 * where <code><em>expression</em></code> is this expression.
	 * @param min the lower value bound
	 * @param max the upper value bound
	 * @return the <code>NOT BETWEEN</code> expression with the specified bounds
	 */
	default SqlExpression notBetween(long min, long max) {
		return notBetween(Literal.value(min), Literal.value(max));
	}

	/**
	 * Creates a <code>NOT BETWEEN</code> expression having the form<br>
	 * <code><strong><em>expression</em> NOT BETWEEN <em>min</em> AND <em>max</em></strong></code><br>
	 * where <code><em>expression</em></code> is this expression.
	 * @param min the lower value bound
	 * @param max the upper value bound
	 * @return the <code>NOT BETWEEN</code> expression with the specified bounds
	 */
	default SqlExpression notBetween(double min, double max) {
		return notBetween(Literal.value(min), Literal.value(max));
	}

	/**
	 * Creates a <code>NOT BETWEEN</code> expression having the form<br>
	 * <code><strong><em>expression</em> NOT BETWEEN <em>min</em> AND <em>max</em></strong></code><br>
	 * where <code><em>expression</em></code> is this expression.
	 * @param min the lower value bound
	 * @param max the upper value bound
	 * @return the <code>NOT BETWEEN</code> expression with the specified bounds
	 */
	default SqlExpression notBetween(String min, String max) {
		return notBetween(Literal.value(min), Literal.value(max));
	}

	/**
	 * Creates a <code>CAST</code> expression having the form<br>
	 * <code><strong>CAST (<em>expression</em> AS <em>type</em>)</strong></code><br>
	 * where <code><em>expression</em></code> is this expression.
	 * @param type the definition of the SQL type to cast the expression to
	 * @return the <code>CAST</code> expression with the specified bounds
	 */
	default SqlExpression castAs(String type) {
		return cast(this, type);
	}

	/**
	 * Creates an <code>IN</code> expression having the form<br>
	 * <code><strong><em>expression</em> IN ([<em>value<sub>0</sub></em>{,
	 * <em>value<sub>i</sub></em>}])</strong></code><br>
	 * where <code><em>expression</em></code> is this expression.
	 * @param values the expressions among whose values to search
	 * @return the <code>IN</code> expression
	 */
	default SqlExpression in(SqlExpression... values) {
		return new InExpression.ForRow(false, this, values.length == 0 ? null : rowOf(values));
	}

	/**
	 * Creates a <code>NOT IN</code> expression having the form<br>
	 * <code><strong><em>expression</em> NOT IN ([<em>value<sub>0</sub></em>{,
	 * <em>value<sub>i</sub></em>}])</strong></code><br>
	 * where <code><em>expression</em></code> is this expression.
	 * @param values the expressions among whose values to search
	 * @return the <code>NOT IN</code> expression
	 */
	default SqlExpression notIn(SqlExpression... values) {
		return new InExpression.ForRow(true, this, values.length == 0 ? null : rowOf(values));
	}

	/**
	 * Creates an <code>IN</code> expression having the form<br>
	 * <code><strong><em>expression</em> IN ([<em>value<sub>0</sub></em>{,
	 * <em>value<sub>i</sub></em>}])</strong></code><br>
	 * where <code><em>expression</em></code> is this expression.
	 * @param values the expressions among whose values to search
	 * @return the <code>IN</code> expression
	 */
	default SqlExpression in(Iterable<? extends SqlExpression> values) {
		return new InExpression.ForRow(false, this, values.iterator().hasNext() ? rowOf(values) : null);
	}

	/**
	 * Creates a <code>NOT IN</code> expression having the form<br>
	 * <code><strong><em>expression</em> NOT IN ([<em>value<sub>0</sub></em>{,
	 * <em>value<sub>i</sub></em>}])</strong></code><br>
	 * where <code><em>expression</em></code> is this expression.
	 * @param values the expressions among whose values to search
	 * @return the <code>NOT IN</code> expression
	 */
	default SqlExpression notIn(Iterable<? extends SqlExpression> values) {
		return new InExpression.ForRow(true, this, values.iterator().hasNext() ? rowOf(values) : null);
	}

	/**
	 * Creates an <code>IN</code> expression having the form<br>
	 * <code><strong><em>expression</em> IN ([<em>value<sub>0</sub></em>{,
	 * <em>value<sub>i</sub></em>}])</strong></code><br>
	 * where <code><em>expression</em></code> is this expression.
	 * @param values the values to search among
	 * @return the <code>IN</code> expression
	 */
	default SqlExpression in(int... values) {
		return new InExpression.ForRow(false, this, values.length == 0 ? null : rowOf(values));
	}

	/**
	 * Creates a <code>NOT IN</code> expression having the form<br>
	 * <code><strong><em>expression</em> NOT IN ([<em>value<sub>0</sub></em>{,
	 * <em>value<sub>i</sub></em>}])</strong></code><br>
	 * where <code><em>expression</em></code> is this expression.
	 * @param values the values to search among
	 * @return the <code>NOT IN</code> expression
	 */
	default SqlExpression notIn(int... values) {
		return new InExpression.ForRow(true, this, values.length == 0 ? null : rowOf(values));
	}

	/**
	 * Creates an <code>IN</code> expression having the form<br>
	 * <code><strong><em>expression</em> IN ([<em>value<sub>0</sub></em>{,
	 * <em>value<sub>i</sub></em>}])</strong></code><br>
	 * where <code><em>expression</em></code> is this expression.
	 * @param values the values to search among
	 * @return the <code>IN</code> expression
	 */
	default SqlExpression in(long... values) {
		return new InExpression.ForRow(false, this, values.length == 0 ? null : rowOf(values));
	}

	/**
	 * Creates a <code>NOT IN</code> expression having the form<br>
	 * <code><strong><em>expression</em> NOT IN ([<em>value<sub>0</sub></em>{,
	 * <em>value<sub>i</sub></em>}])</strong></code><br>
	 * where <code><em>expression</em></code> is this expression.
	 * @param values the values to search among
	 * @return the <code>NOT IN</code> expression
	 */
	default SqlExpression notIn(long... values) {
		return new InExpression.ForRow(true, this, values.length == 0 ? null : rowOf(values));
	}

	/**
	 * Creates an <code>IN</code> expression having the form<br>
	 * <code><strong><em>expression</em> IN ([<em>value<sub>0</sub></em>{,
	 * <em>value<sub>i</sub></em>}])</strong></code><br>
	 * where <code><em>expression</em></code> is this expression.
	 * @param values the values to search among
	 * @return the <code>IN</code> expression
	 */
	default SqlExpression in(float... values) {
		return new InExpression.ForRow(false, this, values.length == 0 ? null : rowOf(values));
	}

	/**
	 * Creates a <code>NOT IN</code> expression having the form<br>
	 * <code><strong><em>expression</em> NOT IN ([<em>value<sub>0</sub></em>{,
	 * <em>value<sub>i</sub></em>}])</strong></code><br>
	 * where <code><em>expression</em></code> is this expression.
	 * @param values the values to search among
	 * @return the <code>NOT IN</code> expression
	 */
	default SqlExpression notIn(float... values) {
		return new InExpression.ForRow(true, this, values.length == 0 ? null : rowOf(values));
	}

	/**
	 * Creates an <code>IN</code> expression having the form<br>
	 * <code><strong><em>expression</em> IN ([<em>value<sub>0</sub></em>{,
	 * <em>value<sub>i</sub></em>}])</strong></code><br>
	 * where <code><em>expression</em></code> is this expression.
	 * @param values the values to search among
	 * @return the <code>IN</code> expression
	 */
	default SqlExpression in(double... values) {
		return new InExpression.ForRow(false, this, values.length == 0 ? null : rowOf(values));
	}

	/**
	 * Creates a <code>NOT IN</code> expression having the form<br>
	 * <code><strong><em>expression</em> NOT IN ([<em>value<sub>0</sub></em>{,
	 * <em>value<sub>i</sub></em>}])</strong></code><br>
	 * where <code><em>expression</em></code> is this expression.
	 * @param values the values to search among
	 * @return the <code>NOT IN</code> expression
	 */
	default SqlExpression notIn(double... values) {
		return new InExpression.ForRow(true, this, values.length == 0 ? null : rowOf(values));
	}

	/**
	 * Creates an <code>IN</code> expression having the form<br>
	 * <code><strong><em>expression</em> IN ([<em>value<sub>0</sub></em>{,
	 * <em>value<sub>i</sub></em>}])</strong></code><br>
	 * where <code><em>expression</em></code> is this expression.
	 * @param values the values to search among
	 * @return the <code>IN</code> expression
	 */
	default SqlExpression in(CharSequence... values) {
		return new InExpression.ForRow(false, this, values.length == 0 ? null : rowOf(values));
	}

	/**
	 * Creates a <code>NOT IN</code> expression having the form<br>
	 * <code><strong><em>expression</em> NOT IN ([<em>value<sub>0</sub></em>{,
	 * <em>value<sub>i</sub></em>}])</strong></code><br>
	 * where <code><em>expression</em></code> is this expression.
	 * @param values the values to search among
	 * @return the <code>NOT IN</code> expression
	 */
	default SqlExpression notIn(CharSequence... values) {
		return new InExpression.ForRow(true, this, values.length == 0 ? null : rowOf(values));
	}

	/**
	 * Creates an <code>IN</code> expression having the form<br>
	 * <code><strong><em>expression</em> IN ([<em>value<sub>0</sub></em>{,
	 * <em>value<sub>i</sub></em>}])</strong></code><br>
	 * where <code><em>expression</em></code> is this expression.
	 * @param values the values to search among
	 * @return the <code>IN</code> expression
	 */
	default SqlExpression in(byte[]... values) {
		return new InExpression.ForRow(false, this, values.length == 0 ? null : rowOf(values));
	}

	/**
	 * Creates a <code>NOT IN</code> expression having the form<br>
	 * <code><strong><em>expression</em> NOT IN ([<em>value<sub>0</sub></em>{,
	 * <em>value<sub>i</sub></em>}])</strong></code><br>
	 * where <code><em>expression</em></code> is this expression.
	 * @param values the values to search among
	 * @return the <code>NOT IN</code> expression
	 */
	default SqlExpression notIn(byte[]... values) {
		return new InExpression.ForRow(true, this, values.length == 0 ? null : rowOf(values));
	}

	/**
	 * Creates an <code>IN</code> expression having the form<br>
	 * <code><strong><em>expression</em> IN ([<em>value<sub>0</sub></em>{,
	 * <em>value<sub>i</sub></em>}])</strong></code><br>
	 * where <code><em>expression</em></code> is this expression.
	 * @param values the values to search among
	 * @return the <code>IN</code> expression
	 */
	default SqlExpression inStrings(Iterable<? extends CharSequence> values) {
		return new InExpression.ForRow(
			false, this, values.iterator().hasNext() ? rowOfStrings(values) : null);
	}

	/**
	 * Creates a <code>NOT IN</code> expression having the form<br>
	 * <code><strong><em>expression</em> NOT IN ([<em>value<sub>0</sub></em>{,
	 * <em>value<sub>i</sub></em>}])</strong></code><br>
	 * where <code><em>expression</em></code> is this expression.
	 * @param values the values to search among
	 * @return the <code>NOT IN</code> expression
	 */
	default SqlExpression notInStrings(Iterable<? extends CharSequence> values) {
		return new InExpression.ForRow(
			true, this, values.iterator().hasNext() ? rowOfStrings(values) : null);
	}

	/**
	 * Creates an <code>IN</code> expression having the form<br>
	 * <code><strong><em>expression</em> IN ([<em>value<sub>0</sub></em>{,
	 * <em>value<sub>i</sub></em>}])</strong></code><br>
	 * where <code><em>expression</em></code> is this expression.
	 * @param values the values to search among
	 * @return the <code>IN</code> expression
	 */
	default SqlExpression inNumbers(Iterable<? extends Number> values) {
		return new InExpression.ForRow(
			false, this, values.iterator().hasNext() ? rowOfNumbers(values) : null);
	}

	/**
	 * Creates a <code>NOT IN</code> expression having the form<br>
	 * <code><strong><em>expression</em> NOT IN ([<em>value<sub>0</sub></em>{,
	 * <em>value<sub>i</sub></em>}])</strong></code><br>
	 * where <code><em>expression</em></code> is this expression.
	 * @param values the values to search among
	 * @return the <code>NOT IN</code> expression
	 */
	default SqlExpression notInNumbers(Iterable<? extends Number> values) {
		return new InExpression.ForRow(
			true, this, values.iterator().hasNext() ? rowOfNumbers(values) : null);
	}

	/**
	 * Creates an <code>IN</code> expression having the form<br>
	 * <code><strong><em>expression</em> IN ([<em>value<sub>0</sub></em>{,
	 * <em>value<sub>i</sub></em>}])</strong></code><br>
	 * where <code><em>expression</em></code> is this expression.
	 * @param values the values to search among
	 * @return the <code>IN</code> expression
	 */
	default SqlExpression inBlobs(Iterable<byte[]> values) {
		return new InExpression.ForRow(false, this, values.iterator().hasNext() ? rowOfBlobs(values) : null);
	}

	/**
	 * Creates a <code>NOT IN</code> expression having the form<br>
	 * <code><strong><em>expression</em> NOT IN ([<em>value<sub>0</sub></em>{,
	 * <em>value<sub>i</sub></em>}])</strong></code><br>
	 * where <code><em>expression</em></code> is this expression.
	 * @param values the values to search among
	 * @return the <code>NOT IN</code> expression
	 */
	default SqlExpression notInBlobs(Iterable<byte[]> values) {
		return new InExpression.ForRow(true, this, values.iterator().hasNext() ? rowOfBlobs(values) : null);
	}

	/**
	 * Creates an <code>IN</code> expression having the form<br>
	 * <code><strong><em>expression</em> IN (<em>selectStatement</em>)</strong></code><br>
	 * where <code><em>expression</em></code> is this expression.
	 * @param selectStatement the <code>SELECT</code> statement providing the set to search in
	 * @return the <code>IN</code> expression
	 */
	default SqlExpression in(SelectStatement selectStatement) {
		return new InExpression.ForSelect(false, this, selectStatement);
	}

	/**
	 * Creates a <code>NOT IN</code> expression having the form<br>
	 * <code><strong><em>expression</em> IN (<em>selectStatement</em>)</strong></code><br>
	 * where <code><em>expression</em></code> is this expression.
	 * @param selectStatement the <code>SELECT</code> statement providing the set to search in
	 * @return the <code>NOT IN</code> expression
	 */
	default SqlExpression notIn(SelectStatement selectStatement) {
		return new InExpression.ForSelect(true, this, selectStatement);
	}

	/**
	 * Creates an <code>IN</code> expression having the form<br>
	 * <code><strong><em>expression</em> IN <em>tableName</em></strong></code><br>
	 * where <code><em>expression</em></code> is this expression.
	 * @param tableName the name of the table to search in
	 * @return the <code>IN</code> expression
	 */
	default SqlExpression in(CharSequence tableName) {
		return in(null, tableName);
	}

	/**
	 * Creates a <code>NOT IN</code> expression having the form<br>
	 * <code><strong><em>expression</em> NOT IN <em>tableName</em></strong></code><br>
	 * where <code><em>expression</em></code> is this expression.
	 * @param tableName the name of the table to search in
	 * @return the <code>NOT IN</code> expression
	 */
	default SqlExpression notIn(CharSequence tableName) {
		return notIn(null, tableName);
	}

	/**
	 * Creates an <code>IN</code> expression having the form<br>
	 * <code><strong><em>expression</em> IN <em>schemaName</em>.<em>tableName</em></strong></code><br>
	 * where <code><em>expression</em></code> is this expression.
	 * @param schemaName the name of the schema the table belongs to
	 * @param tableName the name of the table to search in
	 * @return the <code>IN</code> expression
	 */
	default SqlExpression in(CharSequence schemaName, CharSequence tableName) {
		return new InExpression.ForTable(false, this, schemaName, tableName);
	}

	/**
	 * Creates an <code>IN</code> expression having the form<br>
	 * <code><strong><em>expression</em> IN [<em>schemaName</em>.]<em>tableName</em></strong></code><br>
	 * where <code><em>expression</em></code> is this expression.
	 * @param table the table to search in
	 * @return the <code>IN</code> expression
	 */
	default SqlExpression in(Table table) {
		return in(table.schemaName(), table.tableName());
	}

	/**
	 * Creates a <code>NOT IN</code> expression having the form<br>
	 * <code><strong><em>expression</em> NOT IN <em>schemaName</em>.<em>tableName</em></strong></code><br>
	 * where <code><em>expression</em></code> is this expression.
	 * @param schemaName the name of the schema the table belongs to
	 * @param tableName the name of the table to search in
	 * @return the <code>NOT IN</code> expression
	 */
	default SqlExpression notIn(CharSequence schemaName, CharSequence tableName) {
		return new InExpression.ForTable(true, this, schemaName, tableName);
	}

	/**
	 * Creates a <code>NOT IN</code> expression having the form<br>
	 * <code><strong><em>expression</em> NOT IN [<em>schemaName</em>.]<em>tableName</em></strong></code><br>
	 * where <code><em>expression</em></code> is this expression.
	 * @param table the table to search in
	 * @return the <code>NOT IN</code> expression
	 */
	default SqlExpression notIn(Table table) {
		return notIn(table.schemaName(), table.tableName());
	}

	/**
	 * Creates an <code>IN</code> expression having the form<br>
	 * <code><strong><em>expression</em> IN <em>virtualTableName</em>([<em>arg<sub>0</sub></em>{,
	 * <em>arg<sub>i</sub></em>}])</strong></code><br>
	 * where <code><em>expression</em></code> is this expression.
	 * @param virtualTableName the name of the virtual table to search in
	 * @param args the arguments to pass to the virtual table (the hidden columns filtering values)
	 * @return the <code>IN</code> expression
	 */
	default SqlExpression inVirtual(CharSequence virtualTableName, SqlExpression... args) {
		return inVirtual(null, virtualTableName, args);
	}

	/**
	 * Creates a <code>NOT IN</code> expression having the form<br>
	 * <code><strong><em>expression</em> NOT IN <em>virtualTableName</em>([<em>arg<sub>0</sub></em>{,
	 * <em>arg<sub>i</sub></em>}])</strong></code><br>
	 * where <code><em>expression</em></code> is this expression.
	 * @param virtualTableName the name of the virtual table to search in
	 * @param args the arguments to pass to the virtual table (the hidden columns filtering values)
	 * @return the <code>NOT IN</code> expression
	 */
	default SqlExpression notInVirtual(CharSequence virtualTableName, SqlExpression... args) {
		return notInVirtual(null, virtualTableName, args);
	}

	/**
	 * Creates an <code>IN</code> expression having the form<br>
	 * <code><strong><em>expression</em> IN <em>schemaName</em>.<em>virtualTableName</em>([<em>arg<sub>0</sub></em>{,
	 * <em>arg<sub>i</sub></em>}])</strong></code><br>
	 * where <code><em>expression</em></code> is this expression.
	 * @param schemaName the name of the schema the virtual table is defined in
	 * @param virtualTableName the name of the virtual table to search in
	 * @param args the arguments to pass to the virtual table (the hidden columns filtering values)
	 * @return the <code>IN</code> expression
	 */
	default SqlExpression inVirtual(CharSequence schemaName, CharSequence virtualTableName, SqlExpression... args) {
		return new InExpression.ForTableFunction(false, this, schemaName, virtualTableName, Arrays.asList(args));
	}

	/**
	 * Creates an <code>IN</code> expression having the form<br>
	 * <code><strong><em>expression</em> IN [<em>schemaName</em>.]<em>virtualTableName</em>([<em>arg<sub>0</sub></em>{,
	 * <em>arg<sub>i</sub></em>}])</strong></code><br>
	 * where <code><em>expression</em></code> is this expression.
	 * @param virtualTable the virtual table to search in
	 * @param args the arguments to pass to the virtual table (the hidden columns filtering values)
	 * @return the <code>IN</code> expression
	 */
	default SqlExpression inVirtual(Table virtualTable, SqlExpression... args) {
		return inVirtual(virtualTable.schemaName(), virtualTable.tableName(), args);
	}

	/**
	 * Creates a <code>NOT IN</code> expression having the form<br>
	 * <code><strong><em>expression</em> NOT IN
	 * <em>schemaName</em>.<em>virtualTableName</em>([<em>arg<sub>0</sub></em>{,
	 * <em>arg<sub>i</sub></em>}])</strong></code><br>
	 * where <code><em>expression</em></code> is this expression.
	 * @param schemaName the name of the schema the virtual table is defined in
	 * @param virtualTableName the name of the virtual table to search in
	 * @param args the arguments to pass to the virtual table (the hidden columns filtering values)
	 * @return the <code>NOT IN</code> expression
	 */
	default SqlExpression notInVirtual(CharSequence schemaName, CharSequence virtualTableName, SqlExpression... args) {
		return new InExpression.ForTableFunction(true, this, schemaName, virtualTableName, Arrays.asList(args));
	}

	/**
	 * Creates a <code>NOT IN</code> expression having the form<br>
	 * <code><strong><em>expression</em> NOT IN
	 * [<em>schemaName</em>.]<em>virtualTableName</em>([<em>arg<sub>0</sub></em>{,
	 * <em>arg<sub>i</sub></em>}])</strong></code><br>
	 * where <code><em>expression</em></code> is this expression.
	 * @param virtualTable the virtual table to search in
	 * @param args the arguments to pass to the virtual table (the hidden columns filtering values)
	 * @return the <code>NOT IN</code> expression
	 */
	default SqlExpression notInVirtual(Table virtualTable, SqlExpression... args) {
		return notInVirtual(virtualTable.schemaName(), virtualTable.tableName(), args);
	}
}
