package dev.ornamental.sqlite.statement;

import java.util.ArrayList;
import java.util.List;

/**
 * This class has no functionality of its own. It is destined for grouping the classes pertaining
 * to <code>CASE</code> expressions, namely {@link NoDefault} and {@link WithDefault}.
 */
public final class CaseExpression {

	/**
	 * This interface may be used to add clauses to a <code>CASE</code> expression in a manner
	 * uniform both for the initial expression stub (i. e. {@link Stub}) and for a valid expression
	 * already having at least one <code>WHEN .. THEN</code> clause (i. e. {@link NoDefault}).
	 */
	public interface ClauseList {

		/**
		 * Adds a new incomplete <code><strong>WHEN <em>conditionOrValueExpression</em> THEN</strong></code>
		 * clause to the <code>CASE</code> expression.<br/>
		 * Note that the instance this method is invoked on is not modified.
		 * @param conditionOrValueExpression the clause condition expression (or value for comparison clause)
		 * @return the incomplete <code>WHEN .. THEN</code> clause having the specified
		 * condition / value for comparison
		 */
		Clause when(SqlExpression conditionOrValueExpression);
	}

	/**
	 * Represents an incomplete <code>CASE</code> expression to which no condition clauses
	 * have been added yet (namely, the <code><strong>CASE [<em>comparandExpression</em>]</strong></code>
	 * part).
	 */
	public static final class Stub implements ClauseList {

		private final SqlExpression comparand;

		Stub(SqlExpression comparand) {
			this.comparand = comparand;
		}

		@Override
		public Clause when(SqlExpression conditionOrValueExpression) {
			return new Clause(this, null, conditionOrValueExpression);
		}

		Stub copy() {
			SqlExpression comparandCopy =  comparand == null ? null : comparand.copy();
			return comparandCopy == comparand ? this : new Stub(comparandCopy);
		}
	}

	/**
	 * Represents an SQL <code>CASE</code> expression to which more condition clauses
	 * or an <code>ELSE</code> clause may be added (and at least one has already been added).
	 * It has one of the following forms:<br/>
	 * <ul>
	 *     <li><code><strong>CASE <em>comparand</em> WHEN <em>valueExpression<sub>0</sub></em>
	 *         THEN <em>resultExpression<sub>0</sub></em> {WHEN <em>valueExpression<sub>i</sub></em>
	 *         THEN <em>resultExpression<sub>i</sub></em>} END</strong></code></li>
	 *     <li><code><strong>CASE WHEN <em>conditionExpression<sub>0</sub></em>
	 *         THEN <em>resultExpression<sub>0</sub></em> {WHEN <em>conditionExpression<sub>i</sub></em>
	 *         THEN <em>resultExpression<sub>i</sub></em>} END</strong></code></li>
	 * </ul>
	 * This is a complete SQL expression.
	 */
	public static final class NoDefault implements SqlExpression, ClauseList {

		private final Stub stub;

		private final NoDefault previous;

		private final SqlExpression condition;

		private final SqlExpression result;

		private NoDefault(
			Stub stub, NoDefault previous, SqlExpression condition, SqlExpression result) {

			this.stub = stub;
			this.previous = previous;
			this.condition = condition;
			this.result = result;
		}

		@Override
		public Clause when(SqlExpression conditionOrValueExpression) {
			return new Clause(null, this, conditionOrValueExpression);
		}

		/**
		 * Adds an <code><strong>ELSE <em>resultExpression</em></strong></code> clause
		 * to the <code>CASE</code> expression.
		 * @param resultExpression the expression for result calculation in case no condition of
		 * the <code>WHEN .. THEN</code> clauses holds
		 * @return the complete SQL expression having the form
		 * <code><strong>CASE WHEN <em>conditionExpression<sub>0</sub></em>
		 * THEN <em>resultExpression<sub>0</sub></em> {WHEN <em>conditionExpression<sub>i</sub></em>
		 * THEN <em>resultExpression<sub>i</sub></em>}
		 * ELSE <em>resultExpression</em> END</strong></code>
		 */
		public WithDefault orElse(SqlExpression resultExpression) {
			return new WithDefault(this, resultExpression);
		}

		@Override
		public int getPrecedence() {
			return Integer.MAX_VALUE;
		}

		@Override
		public void appendTo(StringBuilder receptacle) {
			CaseExpression.appendTo(receptacle, this, null);
		}

		@Override
		public NoDefault copy() {
			Stub stubCopy = stub == null ? null : stub.copy();
			NoDefault previousCopy = previous == null ? null : previous.copy();
			SqlExpression conditionCopy = condition.copy();
			SqlExpression resultCopy = result.copy();

			return
				stubCopy == stub && previousCopy == previous
				&& conditionCopy == condition && resultCopy == result
					? this : new NoDefault(stubCopy, previousCopy, conditionCopy, resultCopy);
		}
	}

	/**
	 * Represents an incomplete <code><strong>WHEN <em>conditionOrValueExpression</em> THEN</strong></code>
	 * clause. It references the original {@link ClauseList} thus implicitly containing all the previously
	 * defined parts of the <code>CASE</code> expression.
	 */
	public static final class Clause {

		private final Stub stub;

		private final NoDefault previous;

		private final SqlExpression condition;

		Clause(Stub stub, NoDefault previous, SqlExpression condition) {
			this.stub = stub;
			this.previous = previous;
			this.condition = condition;
		}

		/**
		 * Completes the <code>WHEN .. THEN</code> clause returning the new {@link ClauseList}
		 * containing all the previous parts of the <code>CASE</code> expression and the new clause.
		 * @param result the result returned by the <code>CASE</code> statement if this is the first
		 * clause whose condition holds (or the value matches the comparand)
		 * @return the completed <code>CASE</code> expression having this completed clause as the last clause
		 */
		public NoDefault then(SqlExpression result) {
			return new NoDefault(stub, previous, condition, result);
		}
	}

	/**
	 * Represents an SQL <code>CASE</code> expression in its most complete form:
	 * <ul>
	 *     <li>either <code><strong>CASE <em>comparand</em> WHEN <em>valueExpression<sub>0</sub></em>
	 *         THEN <em>resultExpression<sub>0</sub></em> {WHEN <em>valueExpression<sub>i</sub></em>
	 *         THEN <em>resultExpression<sub>i</sub></em>} ELSE <em>elseResult</em> END</strong></code></li>
	 *     <li>or <code><strong>CASE WHEN <em>conditionExpression<sub>0</sub></em>
	 *         THEN <em>resultExpression<sub>0</sub></em> {WHEN <em>conditionExpression<sub>i</sub></em>
	 *         THEN <em>resultExpression<sub>i</sub></em>} ELSE <em>elseResult</em> END</strong></code></li>
	 * </ul>
	 * This is a complete SQL expression.
	 */
	public static final class WithDefault implements SqlExpression {

		private final NoDefault clauses;

		private final SqlExpression orElseResult;

		WithDefault(NoDefault clauses, SqlExpression orElseResult) {
			this.clauses = clauses;
			this.orElseResult = orElseResult;
		}

		@Override
		public void appendTo(StringBuilder receptacle) {
			CaseExpression.appendTo(receptacle, clauses, orElseResult);
		}

		@Override
		public int getPrecedence() {
			return Integer.MAX_VALUE;
		}

		@Override
		public WithDefault copy() {
			NoDefault clausesCopy = clauses.copy();
			SqlExpression orElseResultCopy = orElseResult == null ? null : orElseResult.copy();

			return clausesCopy == clauses && orElseResultCopy == orElseResult
				? this : new WithDefault(clausesCopy, orElseResultCopy);
		}
	}

	private static void appendTo(StringBuilder receptacle, NoDefault clauses, SqlExpression orElseResult) {
		// TODO [MEDIUM] the precedence and nested expressions' needing to be in parentheses is debatable
		receptacle.append("CASE ");

		List<NoDefault> chain = new ArrayList<>();
		NoDefault current = clauses;
		while (current != null) {
			chain.add(current);
			current = current.previous;
		}

		SqlExpression comparand = chain.get(chain.size() - 1).stub.comparand;
		if (comparand != null) {
			comparand.appendTo(receptacle);
			receptacle.append(' ');
		}

		for (int i = chain.size() - 1; i >= 0; i--) {
			NoDefault clause = chain.get(i);

			receptacle.append("WHEN ");
			clause.condition.appendTo(receptacle);
			receptacle.append(" THEN ");
			clause.result.appendTo(receptacle);
			receptacle.append(' ');
		}

		if (orElseResult != null) {
			receptacle.append("ELSE ");
			orElseResult.appendTo(receptacle);
			receptacle.append(' ');
		}
		receptacle.append("END");
	}
}
