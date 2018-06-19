package dev.ornamental.sqlite.statement;

import static dev.ornamental.sqlite.statement.SqlStatements.dropIndex;
import static dev.ornamental.sqlite.statement.SqlStatements.dropIndexIfExists;
import static dev.ornamental.sqlite.statement.SqlStatements.dropTable;
import static dev.ornamental.sqlite.statement.SqlStatements.dropTableIfExists;
import static dev.ornamental.sqlite.statement.SqlStatements.dropTrigger;
import static dev.ornamental.sqlite.statement.SqlStatements.dropTriggerIfExists;
import static dev.ornamental.sqlite.statement.SqlStatements.dropView;
import static dev.ornamental.sqlite.statement.SqlStatements.dropViewIfExists;

import org.junit.Test;

public class DropTest {

	private final StatementTestCases dropTableCases = new StatementTestCases.Builder()
		.addCase(
			"DROP TABLE \"main\".\"Entity\"",

			dropTable("main", "Entity")
		).addCase(
			"DROP TABLE IF EXISTS \"Entity\"",

			dropTableIfExists("Entity")
		)
		.build();

	private final StatementTestCases dropIndexCases = new StatementTestCases.Builder()
		.addCase(
			"DROP INDEX \"main\".\"TRG_Entity_foreignKey\"",

			dropIndex("main", "TRG_Entity_foreignKey")
		).addCase(
			"DROP INDEX IF EXISTS \"TRG_Entity_foreignKey\"",

			dropIndexIfExists("TRG_Entity_foreignKey")
		)
		.build();

	private final StatementTestCases dropTriggerCases = new StatementTestCases.Builder()
		.addCase(
			"DROP TRIGGER \"main\".\"TRG_before_EntityInsert\"",

			dropTrigger("main", "TRG_before_EntityInsert")
		).addCase(
			"DROP TRIGGER IF EXISTS \"TRG_before_EntityInsert\"",

			dropTriggerIfExists("TRG_before_EntityInsert")
		)
		.build();

	private final StatementTestCases dropViewCases = new StatementTestCases.Builder()
		.addCase(
			"DROP VIEW \"main\".\"FilteredEntity\"",

			dropView("main", "FilteredEntity")
		).addCase(
			"DROP VIEW IF EXISTS \"FilteredEntity\"",

			dropViewIfExists("FilteredEntity")
		)
		.build();	

	@Test
	public void testDropTable() {
		dropTableCases.test();
	}

	@Test
	public void testDropIndex() {
		dropIndexCases.test();
	}

	@Test
	public void testDropTrigger() {
		dropTriggerCases.test();
	}

	@Test
	public void testDropView() {
		dropViewCases.test();
	}
}