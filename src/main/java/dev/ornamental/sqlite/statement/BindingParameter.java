package dev.ornamental.sqlite.statement;

import java.util.regex.Pattern;

/**
 * Represents an SQL expression used in parametrized statements to be substituted with
 * specific values before statement execution (a binding parameter, named or unnamed).
 * May take one of the following forms:
 * <ul>
 *     <li><code><strong>?</strong></code></li>
 *     <li><code><strong>?<em>number</em></strong></code></li>
 *     <li><code><strong>:<em>name</em></strong></code></li>
 *     <li><code><strong>@<em>name</em></strong></code></li>
 *     <li><code><strong>$<em>name</em></strong></code></li>
 * </ul>
 * where <code><em>number</em></code> is the parameter positive ordinal number
 * and <code><em>name</em></code> is a character sequence (as SQLite documentation
 * does not define the precise rules, names are expected to consist of latin letters
 * and arabic digits).<br/>
 * This is a complete SQL expression.
 */
public final class BindingParameter implements SqlExpression {

	static final BindingParameter NAMELESS = new BindingParameter("?");

	private final String parameter;

	private static final Pattern NAME_PATTERN = Pattern.compile("^\\?([1-9]\\d*)?|([:@$])[a-zA-Z0-9]+$");

	BindingParameter(String parameter) { // do not support variability
		checkName(parameter);
		this.parameter = parameter;
	}

	@Override
	public void appendTo(StringBuilder receptacle) {
		receptacle.append(parameter);
	}

	@Override
	public int getPrecedence() {
		return Integer.MAX_VALUE; // binding parameter is an unbreakable expression thus never needs parentheses
	}

	@Override
	public BindingParameter copy() {
		return this;
	}

	private static void checkName(CharSequence parameter) {
		if (!NAME_PATTERN.matcher(parameter).matches()) {
			throw new IllegalArgumentException(
				"The parameter name must match the following regular expression:\\n"
				+ NAME_PATTERN.pattern());
		}
	}
}
