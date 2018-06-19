package dev.ornamental.sqlite.statement;

import static dev.ornamental.sqlite.statement.Collation.NOCASE;
import static dev.ornamental.sqlite.statement.Literal.value;
import static dev.ornamental.sqlite.statement.SortingOrder.ASC;
import static dev.ornamental.sqlite.statement.SortingOrder.DESC;
import static dev.ornamental.sqlite.statement.SqlExpressions.column;
import static dev.ornamental.sqlite.statement.SqlStatements.createIndex;
import static dev.ornamental.sqlite.statement.SqlStatements.createIndexIfNotExists;
import static dev.ornamental.sqlite.statement.SqlStatements.createUniqueIndex;
import static dev.ornamental.sqlite.statement.SqlStatements.createUniqueIndexIfNotExists;

import org.junit.Test;

public final class CreateIndexTest {

	private final StatementTestCases createIndexCases = new StatementTestCases.Builder()
		.addCase(
			"CREATE INDEX \"IDX_event_date_time\" ON \"Event\"(\"date\", \"time\" DESC)",

			createIndex().named("IDX_event_date_time")
				.onTable("Event").addColumn("date").addColumn("time", DESC)
		).addCase(
			"CREATE UNIQUE INDEX \"schema1\".\"A \"\"useful\"\" index\" ON \"Name\"(\"firstName\" COLLATE NOCASE)",

			createUniqueIndex().named("schema1", "A \"useful\" index")
				.onTable("Name").addColumn(column("firstName").collate(NOCASE))
		).addCase(
			"CREATE INDEX IF NOT EXISTS \"IDX_totalCost\" ON \"Order\"(\"price\" * \"qty\" ASC)",

			createIndexIfNotExists().named("IDX_totalCost")
				.onTable("Order").addColumn(column("price").mult(column("qty")), ASC)
		).addCase(
			"CREATE UNIQUE INDEX IF NOT EXISTS \"IDX_lastDigit\" ON \"Measurement\"(\"value\" % 10)",

			createUniqueIndexIfNotExists().named("IDX_lastDigit")
				.onTable("Measurement").addColumn(column("value").mod(value(10)))
		)
		.build();

	@Test
	public void testCreateIndex() {
		createIndexCases.test();
	}
}