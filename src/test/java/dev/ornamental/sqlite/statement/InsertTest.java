package dev.ornamental.sqlite.statement;

import static dev.ornamental.sqlite.statement.Functions.function;
import static dev.ornamental.sqlite.statement.Literal.NULL;
import static dev.ornamental.sqlite.statement.Literal.value;
import static dev.ornamental.sqlite.statement.SelectStatements.select;
import static dev.ornamental.sqlite.statement.SelectStatements.values;
import static dev.ornamental.sqlite.statement.SqlStatements.with;
import static dev.ornamental.sqlite.statement.SqlExpressions.column;
import static dev.ornamental.sqlite.statement.SqlExpressions.rowOf;
import static dev.ornamental.sqlite.statement.SqlStatements.insertInto;
import static dev.ornamental.sqlite.statement.SqlStatements.insertOrAbortInto;
import static dev.ornamental.sqlite.statement.SqlStatements.insertOrFailInto;
import static dev.ornamental.sqlite.statement.SqlStatements.insertOrIgnoreInto;
import static dev.ornamental.sqlite.statement.SqlStatements.insertOrReplaceInto;
import static dev.ornamental.sqlite.statement.SqlStatements.insertOrRollbackInto;
import static dev.ornamental.sqlite.statement.TableExpressions.table;

import org.junit.Test;

public final class InsertTest {

	private final StatementTestCases insertCases = new StatementTestCases.Builder()
		.addCase(
			"INSERT INTO \"main\".\"Country\"(\"name\", \"code\", \"dateFormat\") VALUES "
			+ "('Spain', 'ES', 'dd/mm/yyyy'), ('Sweden', 'SE', 'yyyy-mm-dd'), ('Philippines', 'PH', 'mm-dd-yyyy')",

			insertInto("main", "Country").columns("name", "code", "dateFormat")
				.values(
					rowOf("Spain", "ES", "dd/mm/yyyy"),
					rowOf("Sweden", "SE", "yyyy-mm-dd"),
					rowOf("Philippines", "PH", "mm-dd-yyyy"))
		).addCase(
			"INSERT OR ABORT INTO \"Country\" VALUES "
				+ "(1, 'Spain', 'ES', 'dd/mm/yyyy'), (2, 'Sweden', 'SE', 'yyyy-mm-dd')",

			insertOrAbortInto("Country").values()
				.add(value(1), value("Spain"), value("ES"), value("dd/mm/yyyy"))
				.add(value(2), value("Sweden"), value("SE"), value("yyyy-mm-dd"))
		).addCase(
			"INSERT OR FAIL INTO \"Entry\" DEFAULT VALUES",

			insertOrFailInto("Entry").defaultValues()
		).addCase(
			"INSERT OR REPLACE INTO \"Stock\" "
				+ "SELECT 1 AS \"kind\", \"name\", \"qty\" AS \"quantity\" FROM \"Product\" "
				+ "UNION ALL "
				+ "SELECT 2, \"name\", NULL FROM \"Service\"",

			insertOrReplaceInto("Stock").from(
				select(value(1).as("kind"), column("name"), column("qty").as("quantity")).from(table("Product"))
				.unionAll()
				.select(value(2), column("name"), NULL).from(table("Service")))
		).addCase(
			"INSERT OR ROLLBACK INTO \"OrderDigest\"(\"orderId\", \"customerName\", \"timestamp\") "
				+ "SELECT \"Order\".\"id\", \"name\", \"Order\".\"creationTime\" "
				+ "FROM (\"Order\") INNER JOIN (\"Customer\") ON \"customerId\" = \"Customer\".\"id\" "
				+ "WHERE \"timestamp\" >= "
					+ "(SELECT \"timestamp\" FROM \"SyncTime\" WHERE \"sync\" = 'OrderDigest')",

			insertOrRollbackInto("OrderDigest").columns("orderId", "customerName", "timestamp").from(
				select(column("Order", "id"), column("name"), column("Order", "creationTime"))
				.from(table("Order").innerJoin(table("Customer"))
					.on(column("customerId").eq(column("Customer", "id"))))
				.where(column("timestamp").geq(select(column("timestamp"))
					.from(table("SyncTime")).where(column("sync").eq(value("OrderDigest"))))))
		).addCase(
			"INSERT OR IGNORE INTO \"District\"(\"name\") "
				+ "VALUES ('Landstraße'), ('Josefstadt'), ('Favoriten'), ('Währing')",

			insertOrIgnoreInto("District").columns("name").from(
				values("Landstraße", "Josefstadt", "Favoriten", "Währing"))
		).addCase(
			"WITH \"Argument\" AS ("
				+ "SELECT 0 AS \"x\" "
				+ "UNION ALL "
				+ "SELECT \"x\" + 0.001 FROM \"Argument\" LIMIT 1000000) "
			+ "REPLACE INTO \"main\".\"SineValues\" SELECT \"x\", SIN(\"x\") AS \"y\"",

			with("Argument").as(
				select(value(0).as("x"))
				.unionAll()
				.select(column("x").plus(value(0.001))).from(table("Argument")).limit(1_000_000))
			.replaceInto("main", "SineValues").from(select(column("x"), function("SIN", column("x")).as("y")))
		)
		.build();

	@Test
	public void testInsert() {
		insertCases.test();
	}
}