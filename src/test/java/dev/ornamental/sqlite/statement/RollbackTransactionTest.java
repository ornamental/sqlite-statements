package dev.ornamental.sqlite.statement;

import static dev.ornamental.sqlite.statement.SqlStatements.rollbackTransaction;
import static dev.ornamental.sqlite.statement.SqlStatements.rollbackTransactionToSavepoint;

import org.junit.Test;

public final class RollbackTransactionTest {

	private final StatementTestCases rollbackTransactionCases = new StatementTestCases.Builder()
		.addCase(
			"ROLLBACK TRANSACTION",

			rollbackTransaction()
		).addCase(
			"ROLLBACK TRANSACTION TO SAVEPOINT \"savepoint 1\"",

			rollbackTransactionToSavepoint("savepoint 1")
		)
		.build();

	@Test
	public void testRollbackTransaction() {
		rollbackTransactionCases.test();
	}
}