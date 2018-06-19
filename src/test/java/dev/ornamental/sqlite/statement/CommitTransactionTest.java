package dev.ornamental.sqlite.statement;

import static dev.ornamental.sqlite.statement.SqlStatements.commitTransaction;

import org.junit.Test;

public final class CommitTransactionTest {

	private final StatementTestCases commitTransactionCase = new StatementTestCases.Builder()
		.addCase(
			"COMMIT TRANSACTION",

			commitTransaction()
		)
		.build();

	@Test
	public void testCommitTransaction() {
		commitTransactionCase.test();
	}
}