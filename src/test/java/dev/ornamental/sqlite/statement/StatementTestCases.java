package dev.ornamental.sqlite.statement;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

public final class StatementTestCases {

	public static final class Builder {

		private final List<String> expected = new ArrayList<>();

		private final List<SqlStatement> statements = new ArrayList<>();

		public Builder addCase(String expectedString, SqlStatement statement) {
			expected.add(expectedString);
			statements.add(statement);

			return this;
		}

		public StatementTestCases build() {
			return new StatementTestCases(expected, statements);
		}
	}

	private final ArrayList<String> expected;

	private final ArrayList<SqlStatement> statements;

	private StatementTestCases(List<String> expected, List<SqlStatement> statements) {
		this.expected = new ArrayList<>(expected);
		this.statements = new ArrayList<>(statements);
	}

	public void test() {
		for (int i = 0; i < expected.size(); i++) {
			assertEquals(expected.get(i), statements.get(i).build());
		}
	}
}
