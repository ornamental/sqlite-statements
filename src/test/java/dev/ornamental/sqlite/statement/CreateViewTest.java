package dev.ornamental.sqlite.statement;

import static dev.ornamental.sqlite.statement.Functions.countAll;
import static dev.ornamental.sqlite.statement.SelectStatements.select;
import static dev.ornamental.sqlite.statement.SelectStatements.selectDistinct;
import static dev.ornamental.sqlite.statement.SelectStatements.values;
import static dev.ornamental.sqlite.statement.SqlExpressions.column;
import static dev.ornamental.sqlite.statement.SqlExpressions.rowOf;
import static dev.ornamental.sqlite.statement.SqlStatements.createTemporaryView;
import static dev.ornamental.sqlite.statement.SqlStatements.createTemporaryViewIfNotExists;
import static dev.ornamental.sqlite.statement.SqlStatements.createView;
import static dev.ornamental.sqlite.statement.SqlStatements.createViewIfNotExists;
import static dev.ornamental.sqlite.statement.TableExpressions.table;

import org.junit.Test;

public final class CreateViewTest {

	private final StatementTestCases createViewCases = new StatementTestCases.Builder()
		.addCase(
			"CREATE VIEW \"schema\".\"FilteredClasses\"(\"id\", \"class\", \"alias\") AS "
				+ "SELECT \"id\", \"className\", \"classAlias\" "
				+ "FROM \"Class\" "
				+ "WHERE \"className\" LIKE 'com.sqlitebuilder.%'",

			createView("schema", "FilteredClasses").withColumnNames("id", "class", "alias")
				.as(select(column("id"), column("className"), column("classAlias"))
					.from(table("Class"))
					.where(column("className").like("com.sqlitebuilder.%")))
		).addCase(
			"CREATE TEMPORARY VIEW \"FrancophoneEmail\" AS "
				+ "SELECT DISTINCT \"email\", \"countryCode\" "
				+ "FROM \"Customer\" "
				+ "WHERE \"countryCode\" IN ('FR', 'BE', 'CH')",

			createTemporaryView("FrancophoneEmail")
				.as(selectDistinct(column("email"), column("countryCode"))
					.from(table("Customer"))
					.where(column("countryCode").in("FR", "BE", "CH")))
		).addCase(
			"CREATE VIEW IF NOT EXISTS \"OrdersByCountry\" AS "
				+ "SELECT \"countryCode\", COUNT(*) AS \"orderCount\" "
				+ "FROM (\"Customer\") INNER JOIN (\"Order\") "
					+ "ON \"Customer\".\"id\" = \"customerId\" "
				+ "GROUP BY \"countryCode\"",

			createViewIfNotExists("OrdersByCountry")
				.as(select(column("countryCode"), countAll().as("orderCount"))
					.from(
						table("Customer")
						.innerJoin(table("Order"))
						.on(column("Customer", "id").eq(column("customerId"))))
					.groupBy(column("countryCode")))
		).addCase(
			"CREATE TEMPORARY VIEW IF NOT EXISTS \"Country\"(\"country\", \"code\", \"language\") AS "
				+ "VALUES ('France', 'FR', 'français'), ('Ελλάδα', 'GR', 'ελληνικά'), ('Suomi', 'FI', 'suomi')",

			createTemporaryViewIfNotExists("Country")
				.withColumnNames("country", "code", "language")
				.as(values(
					rowOf("France", "FR", "français"),
					rowOf("Ελλάδα", "GR", "ελληνικά"),
					rowOf("Suomi", "FI", "suomi")))
		)
		.build();

	@Test
	public void testCreateView() {
		createViewCases.test();
	}
}
