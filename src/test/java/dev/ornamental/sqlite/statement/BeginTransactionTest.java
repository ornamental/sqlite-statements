package dev.ornamental.sqlite.statement;

import static dev.ornamental.sqlite.statement.SqlStatements.beginDeferredTransaction;
import static dev.ornamental.sqlite.statement.SqlStatements.beginExclusiveTransaction;
import static dev.ornamental.sqlite.statement.SqlStatements.beginImmediateTransaction;
import static dev.ornamental.sqlite.statement.SqlStatements.beginTransaction;

import org.junit.Test;

public final class BeginTransactionTest {

	private final StatementTestCases beginTransactionCases = new StatementTestCases.Builder()
		.addCase(
			"BEGIN TRANSACTION",

			beginTransaction()
		).addCase(
			"BEGIN IMMEDIATE TRANSACTION",

			beginImmediateTransaction()
		).addCase(
			"BEGIN DEFERRED TRANSACTION",

			beginDeferredTransaction()
		).addCase(
			"BEGIN EXCLUSIVE TRANSACTION",

			beginExclusiveTransaction()
		)
		.build();

	@Test
	public void testBeginTransaction() {
		beginTransactionCases.test();
	}
}