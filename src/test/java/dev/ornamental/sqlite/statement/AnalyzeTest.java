package dev.ornamental.sqlite.statement;

import static dev.ornamental.sqlite.statement.SqlStatements.analyze;

import org.junit.Test;

public final class AnalyzeTest {

	private final StatementTestCases analyzeCases = new StatementTestCases.Builder()
		.addCase(
			"ANALYZE \"Table1\"",

			analyze("Table1")
		).addCase(
			"ANALYZE \"schema name\".\"table name\"",

			analyze("schema name", "table name")
		)
		.build();

	@Test
	public void testAnalyze() {
		analyzeCases.test();
	}
}
