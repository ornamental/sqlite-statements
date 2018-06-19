package dev.ornamental.sqlite.statement;

import static dev.ornamental.sqlite.statement.Functions.countAll;
import static dev.ornamental.sqlite.statement.Functions.length;
import static dev.ornamental.sqlite.statement.Literal.value;
import static dev.ornamental.sqlite.statement.SelectStatements.select;
import static dev.ornamental.sqlite.statement.SortingOrder.ASC;
import static dev.ornamental.sqlite.statement.SqlExpressions.column;
import static dev.ornamental.sqlite.statement.SqlStatements.deleteFrom;
import static dev.ornamental.sqlite.statement.TableExpressions.table;

import org.junit.Test;

public final class DeleteTest {

	private final StatementTestCases deleteCases = new StatementTestCases.Builder()
		.addCase(
			"DELETE FROM \"main\".\"Log\" INDEXED BY \"timestamp\" "
				+ "WHERE \"timestamp\" >= 1510272000 "
				+ "ORDER BY \"timestamp\" ASC "
				+ "LIMIT 100 OFFSET 1000",

			deleteFrom("main", "Log").indexedBy("timestamp")
				.where(column("timestamp").geq(value(1_510_272_000)))
				.orderBy(column("timestamp"), ASC).limit(100).offset(1000)
		).addCase(
			"DELETE FROM \"Log\" NOT INDEXED WHERE \"message\" LIKE '[subsystem-1]%'",

			deleteFrom("Log").notIndexed().where(column("message").like("[subsystem-1]%"))
		).addCase(
			"DELETE FROM \"Log\" ORDER BY LENGTH(\"message\") LIMIT 100",

			deleteFrom("Log").orderBy(length(column("message"))).limit(100)
		).addCase(
			"DELETE FROM \"Log\" LIMIT (SELECT COUNT(*) / 2.5 FROM \"Log\")",

			deleteFrom("Log").limit(select(countAll().div(value(2.5))).from(table("Log")))
		)
		.build();

	@Test
	public void testDelete() {
		deleteCases.test();
	}
}