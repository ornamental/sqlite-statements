package dev.ornamental.sqlite.statement;

import static dev.ornamental.sqlite.statement.Literal.value;
import static dev.ornamental.sqlite.statement.Raise.raiseAbort;
import static dev.ornamental.sqlite.statement.SelectStatements.select;
import static dev.ornamental.sqlite.statement.SqlExpressions.column;
import static dev.ornamental.sqlite.statement.SqlStatements.createTemporaryTrigger;
import static dev.ornamental.sqlite.statement.SqlStatements.createTemporaryTriggerIfNotExists;
import static dev.ornamental.sqlite.statement.SqlStatements.createTrigger;
import static dev.ornamental.sqlite.statement.SqlStatements.createTriggerIfNotExists;
import static dev.ornamental.sqlite.statement.SqlStatements.update;

import org.junit.Test;

public final class CreateTriggerTest {

	private final StatementTestCases createTriggerCases = new StatementTestCases.Builder()
		.addCase(
			"CREATE TRIGGER \"main\".\"TRG_fastCount_insert\" "
			+ "AFTER INSERT ON \"Entity\" "
			+ "WHEN \"NEW\".\"isValid\" = 1 AND \"NEW\".\"qty\" > 0 "
			+ "BEGIN "
				+ "UPDATE \"main\".\"EntityCount\" SET \"cnt\" = \"cnt\" + \"NEW\".\"qty\"; "
			+ "END",

			createTrigger("main", "TRG_fastCount_insert")
				.after().insert().on("Entity")
				.when(column("NEW", "isValid").eq(value(1)).and(column("NEW", "qty").gt(value(0))))
				.execute(update("main", "EntityCount").set("cnt", column("cnt").plus(column("NEW", "qty"))))
		).addCase(
			"CREATE TEMPORARY TRIGGER \"TRG_fastCount_update\" "
			+ "AFTER UPDATE OF \"isValid\", \"qty\" ON \"main\".\"Entity\" "
			+ "BEGIN "
				+ "UPDATE \"main\".\"EntityCount\" "
				+ "SET \"cnt\" = \"cnt\" + "
					+ "\"NEW\".\"qty\" * \"NEW\".\"isValid\" - \"OLD\".\"qty\" * \"OLD\".\"isValid\" "
				+ "WHERE \"NEW\".\"isValid\" <> \"OLD\".\"isValid\" OR \"NEW\".\"qty\" <> \"OLD\".\"qty\"; "
			+ "END",

			createTemporaryTrigger("TRG_fastCount_update")
				.after().updateOf("isValid", "qty").on("main", "Entity")
				.execute(
					update("main", "EntityCount")
						.set("cnt",
							column("cnt")
								.plus(column("NEW", "qty").mult(column("NEW", "isValid")))
								.minus(column("OLD", "qty").mult(column("OLD", "isValid"))))
						.where(
							column("NEW", "isValid").neq(column("OLD", "isValid"))
							.or(column("NEW", "qty").neq(column("OLD", "qty"))))
				)
		).addCase(
			"CREATE TEMPORARY TRIGGER IF NOT EXISTS \"TRG_fastCount_delete\" "
			+ "BEFORE DELETE ON \"main\".\"Entity\" "
			+ "BEGIN "
				+ "SELECT RAISE(ABORT, 'Cannot delete when ''isValid'' = 0.') "
					+ "WHERE \"OLD\".\"isValid\" = 0; "
				+ "UPDATE \"main\".\"EntityCount\" "
				+ "SET \"cnt\" = \"cnt\" - \"OLD\".\"qty\" WHERE \"OLD\".\"isValid\" = 1; "
			+ "END",

			createTemporaryTriggerIfNotExists("TRG_fastCount_delete")
				.before().delete().on("main", "Entity")
				.execute(
					select(raiseAbort("Cannot delete when 'isValid' = 0."))
						.where(column("OLD", "isValid").eq(value(0))),
					update("main", "EntityCount")
						.set("cnt", column("cnt").minus(column("OLD", "qty")))
						.where(column("OLD", "isValid").eq(value(1)))
				)
		).addCase(
			"CREATE TRIGGER IF NOT EXISTS \"main\".\"TRG_noUpdate\" "
			+ "INSTEAD OF UPDATE ON \"Entity\" WHEN \"OLD\".\"isProtected\" "
			+ "BEGIN "
				+ "SELECT RAISE(ABORT, 'Cannot update a protected row.'); "
			+ "END",

			createTriggerIfNotExists("main", "TRG_noUpdate")
				.insteadOf().update().on("Entity")
				.when(column("OLD", "isProtected"))
				.execute(select(raiseAbort("Cannot update a protected row.")))
		)
		.build();

	@Test
	public void testCreateTrigger() {
		createTriggerCases.test();
	}
}