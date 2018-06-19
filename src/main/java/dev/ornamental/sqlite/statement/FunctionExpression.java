package dev.ornamental.sqlite.statement;

import java.util.Iterator;

/**
 * Represents an SQL expression of either scalar or aggregate function invocation.<br/>
 * This is a complete SQL expression.
 */
public final class FunctionExpression implements SqlExpression {

	private final CharSequence functionName;

	private final boolean distinct;

	private final Iterable<? extends SqlExpression> args;

	FunctionExpression(CharSequence functionName, boolean distinct, Iterable<? extends SqlExpression> args) {
		this.functionName = functionName;
		this.distinct = distinct;
		this.args = args;
	}

	@Override
	public void appendTo(StringBuilder receptacle) {
		SqliteUtilities.quoteNameIfNecessary(receptacle, functionName);
		receptacle.append('(');
		Iterator<? extends SqlExpression> argsIterator = args.iterator();
		boolean hasElements = argsIterator.hasNext();

		if (distinct) {
			final String message = "DISTINCT aggregate requires exactly one argument.";
			if (!hasElements) {
				throw new IllegalArgumentException(message);
			}
			SqlExpression single = argsIterator.next();
			if (argsIterator.hasNext()) {
				throw new IllegalArgumentException(message);
			}

			receptacle.append("DISTINCT ");
			single.appendTo(receptacle);
		} else if (hasElements) {
			SqlExpression last = argsIterator.next();
			while (argsIterator.hasNext()) {
				last.appendTo(receptacle);
				receptacle.append(", ");
				last = argsIterator.next();
			}
			last.appendTo(receptacle);
		}
		receptacle.append(')');
	}

	@Override
	public int getPrecedence() {
		return Integer.MAX_VALUE;
	}

	@Override
	public FunctionExpression copy() {
		CharSequence functionNameCopy = functionName.toString();
		Iterable<SqlExpression> argsCopy = ReadonlyIterable.of(args, SqlExpression::copy);

		return functionNameCopy == functionName && argsCopy == args
			? this : new FunctionExpression(functionNameCopy, distinct, argsCopy);
	}
}
