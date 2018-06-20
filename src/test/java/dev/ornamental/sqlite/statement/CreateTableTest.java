package dev.ornamental.sqlite.statement;

import static dev.ornamental.sqlite.statement.Collation.NOCASE;
import static dev.ornamental.sqlite.statement.Collation.RTRIM;
import static dev.ornamental.sqlite.statement.ForeignKeyAction.CASCADE;
import static dev.ornamental.sqlite.statement.ForeignKeyAction.NO_ACTION;
import static dev.ornamental.sqlite.statement.ForeignKeyAction.RESTRICT;
import static dev.ornamental.sqlite.statement.Functions.length;
import static dev.ornamental.sqlite.statement.Literal.value;
import static dev.ornamental.sqlite.statement.OnConflictAction.ABORT;
import static dev.ornamental.sqlite.statement.OnConflictAction.FAIL;
import static dev.ornamental.sqlite.statement.OnConflictAction.IGNORE;
import static dev.ornamental.sqlite.statement.OnConflictAction.REPLACE;
import static dev.ornamental.sqlite.statement.OnConflictAction.ROLLBACK;
import static dev.ornamental.sqlite.statement.SelectStatements.select;
import static dev.ornamental.sqlite.statement.SqlStatements.with;
import static dev.ornamental.sqlite.statement.SortingOrder.ASC;
import static dev.ornamental.sqlite.statement.SortingOrder.DESC;
import static dev.ornamental.sqlite.statement.SqlExpressions.column;
import static dev.ornamental.sqlite.statement.SqlExpressions.rowOf;
import static dev.ornamental.sqlite.statement.SqlStatements.createTable;
import static dev.ornamental.sqlite.statement.SqlStatements.createTableIfNotExists;
import static dev.ornamental.sqlite.statement.SqlStatements.createTemporaryTable;
import static dev.ornamental.sqlite.statement.SqlStatements.createTemporaryTableIfNotExists;
import static dev.ornamental.sqlite.statement.TableExpressions.table;

import org.junit.Test;

public final class CreateTableTest {

	private final StatementTestCases createTableFromDefinitionCases = new StatementTestCases.Builder()
		.addCase(
			"CREATE TABLE \"Column test\"(\"untyped\", \"typed\" INTEGER, "
				+ "\"strangely typed\" \"123 type name ;%%\"(11, 5), "
				+ "\"key\" INTEGER CONSTRAINT \"PK_key\" PRIMARY KEY ASC, "
				+ "\"data\" VARBINARY CHECK (LENGTH(\"data\") < 16) CONSTRAINT \"DFLT_data\" DEFAULT X'00F27D', "
				+ "\"uniqueUntyped\" UNIQUE ON CONFLICT FAIL, "
				+ "UNIQUE(\"typed\", \"strangely typed\" COLLATE RTRIM, \"uniqueUntyped\" DESC, "
					+ "\"untyped\" COLLATE NOCASE ASC) ON CONFLICT ROLLBACK, "
				+ "CONSTRAINT \"FK_compound\" FOREIGN KEY(\"untyped\", \"typed\", \"uniqueUntyped\") "
					+ "REFERENCES \"Other table\"(\"id_1\", \"id_2\", \"id_3\") "
					+ "ON DELETE RESTRICT ON UPDATE CASCADE NOT DEFERRABLE INITIALLY IMMEDIATE) "
				+ "WITHOUT ROWID",

			createTable("Column test")
				.addColumn("untyped")
				.addColumn("typed").ofType("INTEGER")
				.addColumn("strangely typed").ofType("123 type name ;%%(11, 5)")
				.addColumn("key").ofType("INTEGER").withColumnConstraint("PK_key").primaryKey(ASC)
				.addColumn("data").ofType("VARBINARY")
					.withColumnConstraint().check(length(SqlExpressions.column("data")).lt(value(16)))
					.withColumnConstraint("DFLT_data").defaultValue(new byte[] {0, -14, 125})
				.addColumn("uniqueUntyped").withColumnConstraint().uniqueOnConflict(FAIL)
				.withTableConstraint().unique()
					.addColumn("typed")
					.addColumn("strangely typed", RTRIM)
					.addColumn("uniqueUntyped", DESC)
					.addColumn("untyped", NOCASE, ASC)
					.onConflict(ROLLBACK)
				.withTableConstraint("FK_compound").foreignKey("untyped", "typed", "uniqueUntyped")
					.references("Other table").columns("id_1", "id_2", "id_3")
					.onDelete(RESTRICT).onUpdate(CASCADE).immediate()
				.withoutRowId()
		).addCase(
			"CREATE TABLE IF NOT EXISTS \"schemaName\".\"Entity\"("
				+ "\"id\" INTEGER PRIMARY KEY ON CONFLICT REPLACE AUTOINCREMENT, "
				+ "\"text\" VARCHAR COLLATE RTRIM, "
				+ "\"time\" TEXT DEFAULT NULL CHECK (\"time\" > '1970-01-01T00:00:00.000'), "
				+ "\"fk\" \"\" NOT NULL ON CONFLICT ABORT, "
				+ "CONSTRAINT \"CHK \"\"forbiddenText\"\"\" CHECK (\"text\" NOT LIKE '#!%'), "
				+ "FOREIGN KEY(\"fk\") REFERENCES \"OtherEntity\" DEFERRABLE INITIALLY DEFERRED)",

			createTableIfNotExists("schemaName", "Entity")
				.addColumn("id").ofType("INTEGER")
					.withColumnConstraint().primaryKey().onConflict(REPLACE).autoincrement()
				.addColumn("text").ofType("VARCHAR").withColumnConstraint().collate(RTRIM)
				.addColumn("time").ofType("TEXT")
					.withColumnConstraint().defaultNull()
					.withColumnConstraint().check(column("time").gt(value("1970-01-01T00:00:00.000")))
				.addColumn("fk").ofType("").withColumnConstraint().notNullOnConflict(ABORT)
				.withTableConstraint("CHK \"forbiddenText\"").check(column("text").notLike("#!%"))
				.withTableConstraint().foreignKey("fk").references("OtherEntity").deferred()
		).addCase(
			"CREATE TEMPORARY TABLE IF NOT EXISTS \"Artifact\"("
				+ "\"groupId\" NOT NULL REFERENCES \"Group\"(\"id\") ON DELETE NO ACTION, "
				+ "\"artifactId\" NOT NULL, \"version\" NOT NULL, "
				+ "PRIMARY KEY(\"groupId\", \"artifactId\", \"version\") ON CONFLICT IGNORE)",

			createTemporaryTableIfNotExists("Artifact")
				.addColumn("groupId")
					.withColumnConstraint().notNull()
					.withColumnConstraint().references("Group", "id").onDelete(NO_ACTION)
				.addColumn("artifactId").withColumnConstraint().notNull()
				.addColumn("version").withColumnConstraint().notNull()
				.withTableConstraint().primaryKey()
					.addColumn("groupId").addColumn("artifactId").addColumn("version")
					.onConflict(IGNORE)
		).addCase(
			"CREATE TEMPORARY TABLE \"StateReport\"(\"id\" INTEGER UNIQUE, "
				+ "\"timestamp\" TEXT DEFAULT CURRENT_TIMESTAMP NOT NULL, "
				+ "\"isValid\" BIT DEFAULT FALSE NOT NULL, "
				+ "\"stateId\" INTEGER REFERENCES \"State\" ON UPDATE RESTRICT ON DELETE RESTRICT DEFAULT 1 NOT NULL)",

			createTemporaryTable("StateReport")
				.addColumn("id").ofType("INTEGER").withColumnConstraint().unique()
				.addColumn("timestamp").ofType("TEXT")
					.withColumnConstraint().defaultCurrentTimestamp()
					.withColumnConstraint().notNull()
				.addColumn("isValid").ofType("BIT")
					.withColumnConstraint().defaultValue(false)
					.withColumnConstraint().notNull()
				.addColumn("stateId").ofType("INTEGER")
					.withColumnConstraint().references("State").onUpdate(RESTRICT).onDelete(RESTRICT)
					.withColumnConstraint().defaultValue(1)
					.withColumnConstraint().notNull()
		)
		.build();

	private final StatementTestCases createTableFromSelectCases = new StatementTestCases.Builder()
		.addCase(
			"CREATE TABLE \"main\".\"Enum\" AS "
				+ "SELECT 'Item1' AS \"name\", 1 AS \"value\" "
				+ "UNION ALL "
				+ "VALUES ('Item2', 2), ('Item3', 3)",

			createTable("main", "Enum").as(
				select(value("Item1").as("name"), value(1).as("value"))
				.unionAll()
				.values(rowOf(value("Item2"), value(2)), rowOf(value("Item3"), value(3))))
		).addCase(
			"CREATE TEMPORARY TABLE \"Sequence\" AS WITH \"Counter\"(\"x\") AS ("
				+ "SELECT 1 UNION ALL SELECT \"x\" + 1 FROM \"Counter\" "
				+ "LIMIT 1000000) "
				+ "SELECT \"x\" FROM \"Counter\"",

			createTemporaryTable("Sequence").as(
				with("Counter").ofColumns("x").as(
					select(value(1))
					.unionAll()
					.select(column("x").plus(value(1))).from(table("Counter"))
					.limit(1_000_000))
				.select(column("x")).from(table("Counter")))
		)
		.build();

	@Test
	public void testCreateTableFromDefinition() {
		createTableFromDefinitionCases.test();
	}

	@Test
	public void testCreateTableFromSelect() {
		createTableFromSelectCases.test();
	}
}
