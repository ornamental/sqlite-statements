package dev.ornamental.sqlite.statement;

import static dev.ornamental.sqlite.statement.SqlStatements.vacuum;

import org.junit.Test;

public final class VacuumTest {

	private final StatementTestCases vacuumCases = new StatementTestCases.Builder()
		.addCase(
			"VACUUM",

			vacuum()
		).addCase(
			"VACUUM \"schema name\"",

			vacuum("schema name")
		)
		.build();

	@Test
	public void testVacuum() {
		vacuumCases.test();
	}
}