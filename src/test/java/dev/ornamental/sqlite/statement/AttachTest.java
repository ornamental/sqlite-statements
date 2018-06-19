package dev.ornamental.sqlite.statement;

import static dev.ornamental.sqlite.statement.SelectStatements.select;
import static dev.ornamental.sqlite.statement.SqlExpressions.column;
import static dev.ornamental.sqlite.statement.SqlStatements.attach;
import static dev.ornamental.sqlite.statement.TableExpressions.table;

import org.junit.Test;

public final class AttachTest {

	private final StatementTestCases attachCases = new StatementTestCases.Builder()
		.addCase(
			"ATTACH DATABASE 'C:/''file1''.db' AS \"schema 1\"",

			attach("C:/'file1'.db").as("schema 1")
		).addCase(
			"ATTACH DATABASE (SELECT \"fileName\" FROM \"DatabaseFile\" LIMIT 1) AS \"schema 2\"",

			attach(
				select(column("fileName")).from(table("DatabaseFile")).limit(1))
				.as("schema 2")
		)
		.build();

	@Test
	public void testAttach() {
		attachCases.test();
	}
}