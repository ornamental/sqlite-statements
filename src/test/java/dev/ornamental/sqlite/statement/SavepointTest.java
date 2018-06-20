package dev.ornamental.sqlite.statement;

import static dev.ornamental.sqlite.statement.SqlStatements.savepoint;

import org.junit.Test;

public final class SavepointTest {

	private final StatementTestCases savepointCase = new StatementTestCases.Builder()
		.addCase(
			"SAVEPOINT \"named savepoint\"",

			savepoint("named savepoint")
		)
		.build();

	@Test
	public void testSavepoint() {
		savepointCase.test();
	}
}
