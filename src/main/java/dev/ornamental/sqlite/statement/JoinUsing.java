package dev.ornamental.sqlite.statement;

/**
 * Represents a table expression being a join of tables or subquery results constrained
 * by a <code>USING</code> clause:<br>
 * <code><strong>(<em>tableOrSubquery<sub>L</sub></em> INNER|LEFT|CROSS JOIN
 * <em>tableOrSubquery<sub>R</sub></em> USING (<em>column<sub>0</sub></em>{,
 * <em>column<sub>i</sub></em>}))</strong></code>.
 */
public final class JoinUsing implements TableExpression {

	private final Join previous;

	private final Iterable<? extends CharSequence> columns; // may be empty

	JoinUsing(Join previous, Iterable<? extends CharSequence> columns) {
		this.previous = previous;
		this.columns = columns;
	}

	@Override
	public void appendTo(StringBuilder receptacle) {
		previous.appendTo(receptacle);
		if (columns.iterator().hasNext()) {
			receptacle.append(" USING (");
			SqliteUtilities.appendQuotedDelimited(receptacle, columns);
			receptacle.append(')');
		}
	}

	@Override
	public JoinUsing copy() {
		Join previousCopy = previous.copy();
		Iterable<? extends CharSequence> columnsCopy = ReadonlyIterable.of(columns, CharSequence::toString);

		return previousCopy == previous && columnsCopy == columns
			? this : new JoinUsing(previousCopy, columnsCopy);
	}
}
