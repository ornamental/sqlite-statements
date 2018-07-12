package dev.ornamental.sqlite.statement;

import java.util.Iterator;

/**
 * This is the common ancestor class for the classes representing <code>[NOT] IN</code> expressions.
 * The specific implementations are the nested subclasses {@link ForRow}, {@link ForSelect},
 * {@link ForTable}, and {@link ForTableFunction}.<br>
 * Each implementation is a complete SQL expression.
 */
public abstract class InExpression implements SqlExpression {

	/**
	 * Represents a kind of <code>[NOT] IN</code> expression having the form<br>
	 * <code><strong><em>comparandExpression</em> [NOT] IN (<em>expression<sub>0</sub></em>{,
	 * <em>expression<sub>i</sub></em>})</strong></code>.<br>
	 * This is a complete SQL expression.
	 */
	public static final class ForRow extends InExpression {

		private final RowExpression right; // may be null

		ForRow(boolean not, SqlExpression left, RowExpression right) {
			super(not, left);
			this.right = right;
		}

		@Override
		public SqlExpression copy() {
			SqlExpression leftCopy = left.copy();
			RowExpression rightCopy = right == null ? null : right.copy();

			return leftCopy == left && rightCopy == right
				? this : new ForRow(not, leftCopy, rightCopy);
		}

		@Override
		protected void appendRight(StringBuilder receptacle) {
			if (right != null) {
				right.appendTo(receptacle);
			} else {
				receptacle.append("()");
			}
		}
	}

	/**
	 * Represents a kind of <code>[NOT] IN</code> expression having the form<br>
	 * <code><strong><em>comparandExpression</em> [NOT] IN (<em>selectStatement</em>)</strong></code>.<br>
	 * This is a complete SQL expression.
	 */
	public static final class ForSelect extends InExpression {

		private final SelectStatement right;

		ForSelect(boolean not, SqlExpression left, SelectStatement right) {
			super(not, left);
			this.right = right;
		}

		@Override
		public SqlExpression copy() {
			SqlExpression leftCopy = left.copy();
			SelectStatement rightCopy = right.copy();

			return leftCopy == left && rightCopy == right
				? this : new ForSelect(not, leftCopy, rightCopy);
		}

		@Override
		protected void appendRight(StringBuilder receptacle) {
			receptacle.append('(');
			right.build(receptacle);
			receptacle.append(')');
		}
	}

	/**
	 * Represents a kind of <code>[NOT] IN</code> expression having the form<br>
	 * <code><strong><em>comparandExpression</em> [NOT] IN
	 * [<em>schemaName</em>.]<em>tableName</em></strong></code>.<br>
	 * This is a complete SQL expression.
	 */
	public static final class ForTable extends InExpression {

		private final CharSequence schemaName;

		private final CharSequence tableName;

		ForTable(boolean not, SqlExpression left, CharSequence schemaName, CharSequence tableName) {
			super(not, left);
			this.schemaName = schemaName;
			this.tableName = tableName;
		}

		@Override
		public SqlExpression copy() {
			SqlExpression leftCopy = left.copy();
			CharSequence schemaNameCopy = schemaName == null ? null : schemaName.toString();
			CharSequence tableNameCopy = tableName.toString();

			return leftCopy == left && schemaNameCopy == schemaName && tableNameCopy == tableName
				? this : new ForTable(not, leftCopy, schemaNameCopy, tableNameCopy);
		}

		@Override
		protected void appendRight(StringBuilder receptacle) {
			SqliteUtilities.appendQuotedName(receptacle, schemaName, tableName);
		}
	}

	/**
	 * Represents a kind of <code>[NOT] IN</code> expression having the form<br>
	 * <code><strong><em>comparandExpression</em> [NOT] IN
	 * [<em>schemaName</em>.]<em>virtualTableName</em>([<em>arg<sub>0</sub></em>{,
	 * <em>arg<sub>i</sub></em>}])</strong></code>.<br>
	 * This is a complete SQL expression.
	 */
	public static final class ForTableFunction extends InExpression {

		private final CharSequence schemaName;

		private final CharSequence tableName;

		private final Iterable<? extends SqlExpression> args;

		ForTableFunction(boolean not, SqlExpression left,
			CharSequence schemaName, CharSequence tableName, Iterable<? extends SqlExpression> args) {

			super(not, left);
			this.schemaName = schemaName;
			this.tableName = tableName;
			this.args = args;
		}

		@Override
		public SqlExpression copy() {
			SqlExpression leftCopy = left.copy();
			CharSequence schemaNameCopy = schemaName == null ? null : schemaName.toString();
			CharSequence tableNameCopy = tableName.toString();
			Iterable<SqlExpression> argsCopy = ReadonlyIterable.of(args, SqlExpression::copy);

			return
				leftCopy == left && schemaNameCopy == schemaName
				&& tableNameCopy == tableName && argsCopy == args
					? this : new ForTableFunction(not, leftCopy, schemaNameCopy, tableNameCopy, argsCopy);
		}

		@Override
		protected void appendRight(StringBuilder receptacle) {
			SqliteUtilities.appendQuotedName(receptacle, schemaName, tableName);
			receptacle.append('(');
			Iterator<? extends SqlExpression> iterator = args.iterator();
			if (iterator.hasNext()) {
				SqlExpression last = iterator.next();
				while (iterator.hasNext()) {
					last.appendTo(receptacle);
					receptacle.append(", ");
					last = iterator.next();
				}
				last.appendTo(receptacle);
			}
			receptacle.append(')');
		}
	}

	private static final int PRECEDENCE = 4;

	protected final SqlExpression left;

	protected final boolean not;

	private InExpression(boolean not, SqlExpression left) {
		this.not = not;
		this.left = left;
	}

	@Override
	public void appendTo(StringBuilder receptacle) {
		boolean leftParentheses = left.getPrecedence() < PRECEDENCE;

		SqliteUtilities.parentheses(receptacle, leftParentheses, left::appendTo);
		if (not) {
			receptacle.append(" NOT");
		}
		receptacle.append(" IN ");
		appendRight(receptacle);
	}

	@Override
	public int getPrecedence() {
		return PRECEDENCE;
	}

	/**
	 * Appends the right argument in parentheses.
	 * @param receptacle the {@link StringBuilder} to append to
	 */
	protected abstract void appendRight(StringBuilder receptacle);
}
