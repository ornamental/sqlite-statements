package dev.ornamental.sqlite.statement;

import static dev.ornamental.sqlite.statement.Functions.trim;
import static dev.ornamental.sqlite.statement.SelectStatements.selectDistinct;
import static dev.ornamental.sqlite.statement.SqlExpressions.column;
import static dev.ornamental.sqlite.statement.SqlStatements.explain;
import static dev.ornamental.sqlite.statement.SqlStatements.explainQueryPlan;
import static dev.ornamental.sqlite.statement.SqlStatements.update;
import static dev.ornamental.sqlite.statement.TableExpressions.table;

import org.junit.Test;

public class ExplainTest {

	private final StatementTestCases explainCase = new StatementTestCases.Builder()
		.addCase(
			"EXPLAIN SELECT DISTINCT \"customer_id\" FROM \"Order\"",

			explain(selectDistinct(column("customer_id")).from(table("Order")))
		)
		.build();

	private final StatementTestCases explainQueryPlanCase = new StatementTestCases.Builder()
		.addCase(
			"EXPLAIN QUERY PLAN UPDATE \"Customer\" "
				+ "SET \"firstName\" = TRIM(\"firstName\"), \"lastName\" = TRIM(\"lastName\")",

			explainQueryPlan(update("Customer")
				.set("firstName", trim(column("firstName")))
				.set("lastName", trim(column("lastName"))))
		)
		.build();

	@Test
	public void testExplain() {
		explainCase.test();
	}

	@Test
	public void testExplainQueryPlan() {
		explainQueryPlanCase.test();
	}
}