package dev.ornamental.sqlite.statement;

import static dev.ornamental.sqlite.statement.Collation.RTRIM;
import static dev.ornamental.sqlite.statement.Literal.value;
import static dev.ornamental.sqlite.statement.OnConflictAction.ABORT;
import static dev.ornamental.sqlite.statement.OnConflictAction.FAIL;
import static dev.ornamental.sqlite.statement.OnConflictAction.IGNORE;
import static dev.ornamental.sqlite.statement.OnConflictAction.REPLACE;
import static dev.ornamental.sqlite.statement.OnConflictAction.ROLLBACK;
import static dev.ornamental.sqlite.statement.SortingOrder.ASC;
import static dev.ornamental.sqlite.statement.SqlExpressions.column;
import static dev.ornamental.sqlite.statement.SqlStatements.alterTable;

import java.math.BigDecimal;

import org.junit.Test;

public final class AlterTableTest {

	private final StatementTestCases alterTableRenameCases = new StatementTestCases.Builder()
		.addCase(
			"ALTER TABLE \"Table1\" RENAME TO \"Other Table\"",

			alterTable("Table1").renameTo("Other Table")
		).addCase(
			"ALTER TABLE \"Some Schema\".\"Table1\" RENAME TO \"Other Table\"",

			alterTable("Some Schema", "Table1").renameTo("Other Table")
		)
		.build();

	/**
	 * Actually, SQLite currently does not implement adding PRIMARY KEY / UNIQUE columns
	 * after the table has been created; still, we test that.
	 */
	private final StatementTestCases alterTableAddColumnCases = new StatementTestCases.Builder()
		.addCase(
			"ALTER TABLE \"Table 1\" ADD COLUMN \"integer PK\" "
				+ "INTEGER PRIMARY KEY ASC ON CONFLICT ABORT AUTOINCREMENT "
				+ "CONSTRAINT \"check constraint\" CHECK (\"integer PK\" > 0)",

			alterTable("Table 1").addColumn("integer PK")
				.ofType("INTEGER")
				.constraint().primaryKey(ASC).onConflict(ABORT).autoincrement()
				.constraint("check constraint").check(column("integer PK").gt(value(0)))
		).addCase(
			"ALTER TABLE \"Schema name\".\"Table 1\" ADD COLUMN \"fk_column\" "
				+ "awkward type(12, 13) DEFAULT 12.333333333 UNIQUE ON CONFLICT IGNORE "
				+ "REFERENCES \"Table 2\" NOT NULL ON CONFLICT FAIL",

			alterTable("Schema name", "Table 1").addColumn("fk_column")
				.ofType("awkward type(12, 13)")
				.constraint().defaultValue(new BigDecimal("12.333333333"))
				.constraint().uniqueOnConflict(IGNORE)
				.constraint().references("Table 2")
				.constraint().notNullOnConflict(FAIL)
		).addCase(
			"ALTER TABLE \"Schema name\".\"Table 1\" ADD COLUMN \"fk_column2\" "
				+ "CONSTRAINT \"default time\" DEFAULT CURRENT_TIMESTAMP "
				+ "UNIQUE "
				+ "CONSTRAINT \"another constraint\" NOT NULL "
				+ "REFERENCES \"Table 2\"(\"timestamp\") "
				+ "COLLATE RTRIM",

			alterTable("Schema name", "Table 1").addColumn("fk_column2")
				.constraint("default time").defaultCurrentTimestamp()
				.constraint().unique()
				.constraint("another constraint").notNull()
				.constraint().references("Table 2", "timestamp")
				.constraint().collate(RTRIM)
		).addCase(
			"ALTER TABLE \"Table\" ADD COLUMN \"newColumn\" "
				+ "CONSTRAINT \"time\" DEFAULT CURRENT_TIME "
				+ "UNIQUE ON CONFLICT ROLLBACK",

			alterTable("Table").addColumn("newColumn")
				.constraint("time").defaultCurrentTime()
				.constraint().uniqueOnConflict(ROLLBACK)
		).addCase(
			"ALTER TABLE \"Table\" ADD COLUMN \"newColumn\" "
				+ "CONSTRAINT \"date\" DEFAULT CURRENT_DATE "
				+ "UNIQUE ON CONFLICT REPLACE",

			alterTable("Table").addColumn("newColumn")
				.constraint("date").defaultCurrentDate()
				.constraint().uniqueOnConflict(REPLACE)
		)
		.build();

	@Test
	public void testAlterTableRename() {
		alterTableRenameCases.test();
	}

	@Test
	public void testAlterTableAddColumn() {
		alterTableAddColumnCases.test();
	}
}