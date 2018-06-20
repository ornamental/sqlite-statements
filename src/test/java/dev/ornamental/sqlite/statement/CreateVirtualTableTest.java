package dev.ornamental.sqlite.statement;

import static dev.ornamental.sqlite.statement.SqlStatements.createVirtualTable;
import static dev.ornamental.sqlite.statement.SqlStatements.createVirtualTableIfNotExists;

import org.junit.Test;

public final class CreateVirtualTableTest {

	private final StatementTestCases createVirtualTableCases = new StatementTestCases.Builder()
		.addCase(
			"CREATE VIRTUAL TABLE \"main\".\"PlacemarkLocation\" "
				+ "USING rtree(id, \"minX\", minY, maxX, maxY)",

			createVirtualTable("main", "PlacemarkLocation")
				.using("rtree").withArguments("id", "\"minX\"", "minY", "maxX", "maxY")
		).addCase(
			"CREATE VIRTUAL TABLE IF NOT EXISTS \"email\" USING fts5(sender, title, body)",

			createVirtualTableIfNotExists("email")
				.using("fts5").withArguments("sender", "title", "body")
		)
		.build();

	@Test
	public void testCreateVirtualTable() {
		createVirtualTableCases.test();
	}
}
