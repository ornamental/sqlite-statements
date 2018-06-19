package dev.ornamental.sqlite.statement;

abstract class IntegralValueClause {

	static final class Offset extends IntegralValueClause {

		Offset(SqlExpression asExpression) {
			this(asExpression, -1);
		}

		Offset(long asNumber) {
			this(null, asNumber);
		}

		private Offset(SqlExpression asExpression, long asNumber) {
			super(asExpression, asNumber);
		}

		@Override
		Offset copy() {
			SqlExpression asExpressionCopy = asExpression == null ? null : asExpression.copy();

			return asExpressionCopy == asExpression ? this : new Offset(asExpressionCopy, asNumber);
		}

		@Override
		String clauseName() {
			return "OFFSET";
		}
	}

	static final class Limit extends IntegralValueClause {

		Limit(SqlExpression asExpression) {
			this(asExpression, -1);
		}

		Limit(long asNumber) {
			this(null, asNumber);
		}

		private Limit(SqlExpression asExpression, long asNumber) {
			super(asExpression, asNumber);
		}

		@Override
		Limit copy() {
			SqlExpression asExpressionCopy = asExpression == null ? null : asExpression.copy();

			return asExpressionCopy == asExpression ? this : new Limit(asExpressionCopy, asNumber);
		}

		@Override
		String clauseName() {
			return "LIMIT";
		}
	}

	protected final SqlExpression asExpression;

	protected final long asNumber;

	protected IntegralValueClause(SqlExpression asExpression, long asNumber) {
		this.asExpression = asExpression;
		this.asNumber = asNumber;
	}

	void appendTo(StringBuilder receptacle) {
		receptacle.append(' ').append(clauseName()).append(' ');
		if (asExpression != null) {
			asExpression.appendTo(receptacle);
		} else {
			receptacle.append(asNumber);
		}
	}

	abstract IntegralValueClause copy();

	abstract String clauseName();
}
