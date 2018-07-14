package dev.ornamental.sqlite.statement;

import static dev.ornamental.sqlite.statement.Functions.count;
import static dev.ornamental.sqlite.statement.Functions.countAll;
import static dev.ornamental.sqlite.statement.Functions.max;
import static dev.ornamental.sqlite.statement.Literal.value;
import static dev.ornamental.sqlite.statement.ResultElements.all;
import static dev.ornamental.sqlite.statement.SelectStatements.select;
import static dev.ornamental.sqlite.statement.SelectStatements.valuesInRow;
import static dev.ornamental.sqlite.statement.SqlStatements.with;
import static dev.ornamental.sqlite.statement.SortingOrder.DESC;
import static dev.ornamental.sqlite.statement.SqlExpressions.column;
import static dev.ornamental.sqlite.statement.SqlExpressions.notExists;
import static dev.ornamental.sqlite.statement.SqlExpressions.rowOf;
import static dev.ornamental.sqlite.statement.TableExpressions.table;
import static dev.ornamental.sqlite.statement.TableExpressions.virtualTable;

import org.junit.Test;

public final class SelectTest {

	private final StatementTestCases selectCases = new StatementTestCases.Builder()
		.addCase(
			"WITH \"Mapping\"(\"kind\", \"primitiveName\") AS ("
				+ "SELECT 1, 'Point' "
				+ "UNION ALL SELECT 2, 'Polyline' "
				+ "UNION ALL VALUES (3, 'Model') "
				+ "UNION VALUES (4, 'Image Overlay') "
				+ "UNION ALL SELECT 0, 'Folder'), "
			+ "\"Feature\" AS ("
				+ "SELECT *, \"name\" AS \"path\" "
				+ "FROM \"MapFeature\" "
				+ "WHERE \"parentId\" ISNULL "
				+ "UNION ALL "
				+ "SELECT *, \"path\" || '/' || \"name\" "
				+ "FROM \"MapFeature\" INNER JOIN \"Feature\" "
					+ "ON \"MapFeature\".\"parentId\" = \"Feature\".\"id\"), "
			+ "\"LeafFeature\" AS ("
				+ "SELECT * FROM \"Feature\" "
				+ "WHERE NOT EXISTS ("
					+ "SELECT 1 FROM \"MapFeature\" "
					+ "WHERE \"MapFeature\".\"parentId\" = \"Feature\".\"id\")) "
			+ "SELECT \"primitiveName\", \"path\" "
			+ "FROM \"LeafFeature\" AS \"leaf\" INNER JOIN \"Mapping\" AS \"map\" USING (\"kind\") "
			+ "WHERE \"map\".\"kind\" <> 0 AND \"leaf\".\"styleId\" NOTNULL",

			with("Mapping").ofColumns("kind", "primitiveName").as(
				select(value(1), value("Point"))
				.unionAll().select(value(2), value("Polyline"))
				.unionAll().values(rowOf(value(3), value("Model")))
				.union(valuesInRow(value(4), value("Image Overlay")))
				.unionAll(select(value(0), value("Folder")))
			).andWith("Feature").as(
				select(all(), column("name").as("path"))
					.from(table("MapFeature"))
					.where(column("parentId").isNull())
				.unionAll()
				.select(all(), column("path").concat(value("/")).concat(column("name")))
				.from(table("MapFeature").innerJoin(table("Feature"))
					.on(column("MapFeature", "parentId").eq(column("Feature", "id"))))
			).andWith("LeafFeature").as(
				select(all()).from(table("Feature"))
				.where(notExists(
					select(value(1)).from(table("MapFeature"))
					.where(column("MapFeature", "parentId").eq(column("Feature", "id")))))
			).select(column("primitiveName"), column("path"))
				.from(table("LeafFeature").alias("leaf").innerJoin(table("Mapping").alias("map")).using("kind"))
				.where(column("map", "kind").neq(value(0)).and(column("leaf", "styleId").isNotNull()))
		).addCase(
			"SELECT \"address\" FROM \"EmailAddress\" WHERE \"domain\" = 'domain1' "
			+ "INTERSECT "
			+ "SELECT \"address\" FROM \"EmailAddress\" WHERE \"domain\" <> 'domain1'",

			select(column("address")).from(table("EmailAddress")).where(column("domain").eq(value("domain1")))
			.intersect()
			.select(column("address")).from(table("EmailAddress")).where(column("domain").neq(value("domain1")))
		).addCase(
			"SELECT \"firstName\", \"lastName\" FROM \"Customer\" "
			+ "EXCEPT "
			+ "SELECT \"firstName\", \"lastName\" FROM \"Employee\"",

			select(column("firstName"), column("lastName")).from(table("Customer"))
			.except(select(column("firstName"), column("lastName")).from(table("Employee")))
		).addCase(
			"SELECT \"customerId\", MAX(\"orderTimestamp\") AS \"lastOrder\", COUNT(1) AS \"orderCount\" "
			+ "FROM \"Order\" GROUP BY \"customerId\" HAVING COUNT(1) >= 10 "
			+ "ORDER BY \"orderCount\" DESC, \"lastOrder\" DESC "
			+ "LIMIT (SELECT COUNT(*) FROM \"Customer\") / 10 "
			+ "OFFSET 100",

			select(
				column("customerId"),
				max(column("orderTimestamp")).as("lastOrder"),
				count(value(1)).as("orderCount"))
			.from(table("Order"))
			.groupBy(column("customerId")).having(count(value(1)).geq(value(10)))
			.orderBy(column("orderCount"), DESC).orderBy(column("lastOrder"), DESC)
			.limit(select(countAll()).from(table("Customer")).div(value(10)))
			.offset(100)
		).addCase(
			"SELECT \"value\" FROM \"generate_series\"(5, 50)",

			select(column("value")).from(virtualTable("generate_series", value(5), value(50)))
		)
		.build();

	@Test
	public void testSelect() {
		selectCases.test();
	}
}
