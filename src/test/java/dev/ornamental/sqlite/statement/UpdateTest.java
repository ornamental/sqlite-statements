package dev.ornamental.sqlite.statement;

import static dev.ornamental.sqlite.statement.Collation.NOCASE;
import static dev.ornamental.sqlite.statement.Functions.abs;
import static dev.ornamental.sqlite.statement.Functions.avg;
import static dev.ornamental.sqlite.statement.Functions.countAll;
import static dev.ornamental.sqlite.statement.Functions.length;
import static dev.ornamental.sqlite.statement.Functions.random;
import static dev.ornamental.sqlite.statement.Literal.FALSE;
import static dev.ornamental.sqlite.statement.Literal.TRUE;
import static dev.ornamental.sqlite.statement.Literal.value;
import static dev.ornamental.sqlite.statement.ResultElements.allOf;
import static dev.ornamental.sqlite.statement.SelectStatements.select;
import static dev.ornamental.sqlite.statement.SelectStatements.selectDistinct;
import static dev.ornamental.sqlite.statement.SelectStatements.valuesInRow;
import static dev.ornamental.sqlite.statement.SqlStatements.with;
import static dev.ornamental.sqlite.statement.SortingOrder.ASC;
import static dev.ornamental.sqlite.statement.SortingOrder.DESC;
import static dev.ornamental.sqlite.statement.SqlExpressions.column;
import static dev.ornamental.sqlite.statement.SqlExpressions.not;
import static dev.ornamental.sqlite.statement.SqlExpressions.rowOf;
import static dev.ornamental.sqlite.statement.SqlStatements.update;
import static dev.ornamental.sqlite.statement.SqlStatements.updateOrAbort;
import static dev.ornamental.sqlite.statement.SqlStatements.updateOrFail;
import static dev.ornamental.sqlite.statement.SqlStatements.updateOrReplace;
import static dev.ornamental.sqlite.statement.TableExpressions.table;

import java.math.BigDecimal;

import org.junit.Test;

public final class UpdateTest {

	private final StatementTestCases updateCases = new StatementTestCases.Builder()
		.addCase(
			"UPDATE \"Building\" INDEXED BY \"streetId\" "
			+ "SET \"streetId\" = ("
				+ "SELECT \"id\" FROM \"Street\" "
				+ "WHERE \"name\" COLLATE NOCASE = 'Avenue de Prés'), "
			+ "\"number\" = \"number\" + 42 "
			+ "WHERE \"streetId\" = ("
				+ "SELECT \"id\" FROM \"Street\" "
				+ "WHERE \"name\" COLLATE NOCASE = 'Rue de Vieille-Chapelle')",

			update("Building").indexedBy("streetId")
				.set(
					"streetId",
					select(column("id")).from(table("Street"))
						.where(column("name").collate(NOCASE).eq(value("Avenue de Prés"))))
				.set("number", column("number").plus(value(42)))
				.where(column("streetId").eq(
					select(column("id")).from(table("Street"))
						.where(column("name").collate(NOCASE).eq(value("Rue de Vieille-Chapelle")))))
		).addCase(
			"UPDATE OR ABORT \"temp\".\"EventRecord\" NOT INDEXED "
				+ "SET \"isDeleted\" = TRUE "
				+ "WHERE NOT \"isDeleted\" "
				+ "ORDER BY \"level\" ASC, \"timestamp\", LENGTH(\"data\") DESC "
				+ "LIMIT (SELECT \"logBatchSize\" FROM \"Option\") "
				+ "OFFSET 0",

			updateOrAbort("temp", "EventRecord").notIndexed()
				.set("isDeleted", TRUE)
				.where(not(column("isDeleted")))
				.orderBy(column("level"), ASC)
				.orderBy(column("timestamp"))
				.orderBy(length(column("data")), DESC)
				.limit(select(column("logBatchSize")).from(table("Option")))
				.offset(0)
		).addCase(
			"UPDATE OR FAIL \"Track\" SET (\"title\", \"duration\") = "
				+ "(SELECT \"title\", \"duration\" FROM \"temp\".\"Reimported\" AS \"r\" "
				+ "WHERE \"r\".\"hash\" = \"Track\".\"md5\")",

			updateOrFail("Track").set(
				new String[] {"title", "duration"},
				select(column("title"), column("duration"))
					.from(table("temp", "Reimported").as("r"))
					.where(column("r", "hash").eq(column("Track", "md5"))))
		).addCase(
			"WITH \"TopRatedBook\" AS ("
				+ "SELECT \"Book\".* FROM \"Book\" ORDER BY \"rank\" LIMIT 100) "
			+ "UPDATE OR IGNORE \"temp\".\"BookRating\" "
				+ "SET \"runLength\" = \"runLength\" + 1 "
				+ "WHERE \"bookId\" IN (SELECT \"id\" FROM \"TopRatedBook\")",

			with("TopRatedBook").as(select(allOf("Book")).from(table("Book")).orderBy(column("rank")).limit(100))
			.updateOrIgnore("temp", "BookRating")
				.set("runLength", column("runLength").plus(value(1)))
				.where(column("bookId").in(select(column("id")).from(table("TopRatedBook"))))
		).addCase(
			"UPDATE OR REPLACE \"Product\" "
				+ "SET (\"licenceName\", \"supportPeriod\") = ('HOME Licence', 366), "
				+ "(\"price\", \"currency\", \"isSubscription\") = (VALUES (35.00, 'EUR', FALSE)) "
				+ "WHERE \"productName\" IN ('home-EU', 'home-Norway', 'home-Iceland')",

			updateOrReplace("Product")
				.set(new String[] {"licenceName", "supportPeriod"}, rowOf(value("HOME Licence"), value(366)))
				.set(new String[] {"price", "currency", "isSubscription"},
					valuesInRow(value(new BigDecimal("35.00")), value("EUR"), FALSE))
				.where(column("productName").in("home-EU", "home-Norway", "home-Iceland"))
		).addCase(
			"WITH \"Cnt\"(\"c\") AS (SELECT COUNT(*) FROM \"Attachment\"), "
			+ "\"Sample\" AS ("
				+ "SELECT \"mime\", \"length\" FROM \"Attachment\" "
				+ "ORDER BY ABS(RANDOM()) % (SELECT \"c\" FROM \"Cnt\") "
				+ "LIMIT 10000) "
			+ "UPDATE OR ROLLBACK \"main\".\"AttachmentStatistic\" "
				+ "SET (\"avgLength\", \"frequency\") = "
					+ "(SELECT AVG(\"length\"), COUNT(*) / 10000 FROM \"Sample\" "
					+ "GROUP BY \"mime\" "
					+ "HAVING \"Sample\".\"mime\" = \"AttachmentStatistic\".\"mime\") "
				+ "WHERE \"mime\" IN (SELECT DISTINCT \"mime\" FROM \"Sample\")",

			with("Cnt").ofColumns("c").as(select(countAll()).from(table("Attachment")))
			.andWith("Sample").as(
				select(column("mime"), column("length")).from(table("Attachment"))
				.orderBy(abs(random()).mod(select(column("c")).from(table("Cnt"))))
				.limit(10_000))
			.updateOrRollback("main", "AttachmentStatistic")
				.set(
					new String[] {"avgLength", "frequency"},
					select(avg(column("length")), countAll().div(value(10_000))).from(table("Sample"))
					.groupBy(column("mime"))
					.having(column("Sample", "mime").eq(column("AttachmentStatistic", "mime"))))
				.where(column("mime").in(selectDistinct(column("mime")).from(table("Sample"))))
		).addCase(
			"UPDATE \"Log\" SET \"deprecated\" = TRUE LIMIT 0.9 * (SELECT COUNT(*) FROM \"Log\")",

			update("Log").set("deprecated", TRUE).limit(value(0.9).mult(select(countAll()).from(table("Log"))))
		)
		.build();

	@Test
	public void testUpdate() {
		updateCases.test();
	}
}
