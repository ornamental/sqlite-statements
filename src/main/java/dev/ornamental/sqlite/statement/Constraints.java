package dev.ornamental.sqlite.statement;

/**
 * The utility class used to output parts of constraint declarations.
 */
final class Constraints {

	private Constraints() { }

	public static void appendConstraintName(StringBuilder receptacle, CharSequence constraintName) {
		if (constraintName != null) {
			receptacle.append(" CONSTRAINT ");
			SqliteUtilities.appendQuotedName(receptacle, constraintName);
		}
	}

	public static void append(StringBuilder receptacle, String constraint, OnConflictAction action) {
		receptacle.append(' ').append(constraint);
		if (action != null) {
			receptacle.append(" ON CONFLICT ").append(action.toString());
		}
	}

	public static void appendPrimaryKey(StringBuilder receptacle, SortingOrder order) {
		receptacle.append(" PRIMARY KEY");
		if (order != null) {
			receptacle.append(' ').append(order.toString());
		}
	}

	public static void appendCheck(StringBuilder receptacle, SqlExpression condition) {
		receptacle.append(" CHECK (");
		condition.appendTo(receptacle);
		receptacle.append(')');
	}

	public static void appendDefault(StringBuilder receptacle, SqlExpression defaultValue) {
		receptacle.append(" DEFAULT ");
		SqliteUtilities.parentheses(
			receptacle, !(defaultValue instanceof Literal), defaultValue::appendTo);
	}

	public static void appendCollate(StringBuilder receptacle, Collation collation) {
		receptacle.append(" COLLATE ");
		collation.appendTo(receptacle);
	}

	public static void appendForeignKey(StringBuilder receptacle, CharSequence tableName, CharSequence columnName) {
		receptacle.append(" REFERENCES ");
		SqliteUtilities.appendQuotedName(receptacle, tableName);
		if (columnName != null) {
			receptacle.append('(');
			SqliteUtilities.appendQuotedName(receptacle, columnName);
			receptacle.append(')');
		}
	}

	public static void appendReferentialAction(
		StringBuilder receptacle, boolean isDeleteAction, ForeignKeyAction action) {

		receptacle.append(isDeleteAction ? " ON DELETE " : " ON UPDATE ").append(action.toString());
	}

	public static void appendForeignKeyTiming(StringBuilder receptacle, boolean deferred) {
		receptacle.append(deferred ? " DEFERRABLE INITIALLY DEFERRED" : " NOT DEFERRABLE INITIALLY IMMEDIATE");
	}

	public static void appendConflictClause(StringBuilder receptacle, OnConflictAction action) {
		receptacle.append(" ON CONFLICT ").append(action.toString());
	}
}
