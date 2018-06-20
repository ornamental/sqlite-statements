package dev.ornamental.sqlite.statement;

import static dev.ornamental.sqlite.statement.Collation.RTRIM;
import static dev.ornamental.sqlite.statement.SqlStatements.reindex;

import org.junit.Test;

public final class ReindexTest {

	private final StatementTestCases reindexCases = new StatementTestCases.Builder()
		.addCase(
			"REINDEX",

			reindex()
		).addCase(
			"REINDEX RTRIM",

			reindex(RTRIM)
		).addCase(
			"REINDEX \"IDX_Order_customerId\"",

			reindex("IDX_Order_customerId")
		).addCase(
			"REINDEX \"temp\".\"OrderOrSubscription\"",

			reindex("temp", "OrderOrSubscription")
		)
		.build();

	@Test
	public void testReindex() {
		reindexCases.test();
	}
}
