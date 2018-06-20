package dev.ornamental.sqlite.statement;

import static dev.ornamental.sqlite.statement.SqlStatements.releaseSavepoint;

import org.junit.Test;

public final class ReleaseSavepointTest {

	private final StatementTestCases releaseSavepointCase = new StatementTestCases.Builder()
		.addCase(
			"RELEASE SAVEPOINT \"savepoint 1\"",

			releaseSavepoint("savepoint 1")
		)
		.build();

	@Test
	public void testReleaseSavepoint() {
		releaseSavepointCase.test();
	}
}
