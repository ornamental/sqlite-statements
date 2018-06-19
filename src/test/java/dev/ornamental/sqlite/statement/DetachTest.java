package dev.ornamental.sqlite.statement;

import static dev.ornamental.sqlite.statement.SqlStatements.detach;

import org.junit.Test;

public final class DetachTest {

	private final StatementTestCases detachCase = new StatementTestCases.Builder()
		.addCase(
			"DETACH DATABASE \"schema name\"",

			detach("schema name")
		)
		.build();

	@Test
	public void testDetach() {
		detachCase.test();
	}
}