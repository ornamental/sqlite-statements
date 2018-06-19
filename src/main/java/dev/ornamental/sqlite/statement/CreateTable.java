package dev.ornamental.sqlite.statement;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * This class has no functionality of its own. It is destined for grouping the classes pertaining
 * to the <code>CREATE TABLE</code> statement.
 */
public final class CreateTable {

	/**
	 * This interface serves to unify the addition of new column definitions to a table definition
	 * for the classes {@link Stub} and {@link ColumnConstraintList}.
	 */
	public interface ColumnList {

		/**
		 * Adds a new column to the table definition without specifying its type.<br/>
		 * The result is a complete SQL statement.
		 * @param columnName the name of the column to add
		 * @return a <code>CREATE TABLE</code> statement ending with an untyped column definition:<br/>
		 * <code>CREATE [TEMPORARY] TABLE [IF NOT EXISTS] <em>tableName</em>
		 * ({<em>columnDefinition<sub>i</sub>,</em>} <strong><em>columnName</em></strong>)</code>
		 */
		UntypedColumn addColumn(CharSequence columnName);
	}

	/**
	 * Represents the initial part of a <code>CREATE TABLE</code> statement, namely<br/>
	 * <code><strong>CREATE [TEMPORARY] TABLE [IF NOT EXISTS]
	 * [<em>schemaName</em>.]<em>tableName</em></strong></code>.
	 */
	public static final class Stub implements ColumnList {

		private final boolean temporary;

		private final boolean ifNotExists;

		private final CharSequence schemaName; // may be null

		private final CharSequence tableName;

		Stub(boolean temporary, boolean ifNotExists, CharSequence schemaName, CharSequence tableName) {
			this.temporary = temporary;
			this.ifNotExists = ifNotExists;
			this.schemaName = schemaName;
			this.tableName = tableName;
		}

		/**
		 * Completes the <code>CREATE TABLE</code> statement by defining the <code>SELECT</code>
		 * statement the table and its initial content are based on.<br/>
		 * The result is a complete SQL statement.
		 * @param selectStatement the <code>SELECT</code> statement defining the column names and the initial
		 * content (rows) of the new table
		 * @return the <code>CREATE TABLE</code> statement having the form<br/>
		 * <code>CREATE [TEMPORARY] TABLE [IF NOT EXISTS] [<em>schemaName</em>.]<em>tableName</em>
		 * <strong>AS <em>selectStatement</em></strong></code>
		 */
		public FromSelect as(SelectStatement selectStatement) {
			return new FromSelect(this, selectStatement);
		}

		@Override
		public UntypedColumn addColumn(CharSequence columnName) {
			return new UntypedColumn(this, columnName);
		}

		Stub copy() {
			CharSequence schemaNameCopy = schemaName == null ? null : schemaName.toString();
			CharSequence tableNameCopy = tableName.toString();

			return schemaNameCopy == schemaName && tableNameCopy == tableName
				? this : new Stub(temporary, ifNotExists, schemaNameCopy, tableNameCopy);
		}

		void appendTo(StringBuilder receptacle) {
			receptacle.append("CREATE ");
			if (temporary) {
				receptacle.append("TEMPORARY ");
			}
			receptacle.append("TABLE ");
			if (ifNotExists) {
				receptacle.append("IF NOT EXISTS ");
			}
			SqliteUtilities.appendQuotedName(receptacle, schemaName, tableName);
		}
	}

	/**
	 * Represents a <code>CREATE TABLE</code> statement having the form<br/>
	 * <code><strong>CREATE [TEMPORARY] TABLE [IF NOT EXISTS] [<em>schemaName</em>.]<em>tableName</em>
	 * AS <em>selectStatement</em></strong></code>
	 */
	public static final class FromSelect implements ExplicableStatement {

		private final Stub stub;

		private final SelectStatement select;

		FromSelect(Stub stub, SelectStatement select) {
			this.stub = stub;
			this.select = select;
		}

		@Override
		public FromSelect copy() throws IllegalStateException {
			Stub stubCopy = stub.copy();
			SelectStatement selectCopy = select.copy();

			return stubCopy == stub && selectCopy == select
				? this : new FromSelect(stubCopy, selectCopy);
		}

		@Override
		public void build(StringBuilder receptacle) {
			stub.appendTo(receptacle);
			receptacle.append(" AS ");
			select.build(receptacle);
		}
	}

	/**
	 * Represents a <code>CREATE TABLE</code> statement ending with
	 * <code><strong>WITHOUT ROWID</strong></code> clause.
	 */
	public static final class WithoutRowid implements ExplicableStatement {

		private final ConstraintList previous;

		WithoutRowid(ConstraintList previous) {
			this.previous = previous;
		}

		@Override
		public WithoutRowid copy() throws IllegalStateException {
			ConstraintList previousCopy = previous.copy();

			return previousCopy == previous ? this : new WithoutRowid(previousCopy);
		}

		@Override
		public void build(StringBuilder receptacle) {
			previous.build(receptacle);
			receptacle.append(" WITHOUT ROWID");
		}
	}

	/**
	 * The common ancestor class for <code>CREATE TABLE</code> statements which may be supplemented
	 * with table constraints. Such statements may also be supplemented with a <code>WITHOUT ROWID</code>
	 * clause.
	 */
	public abstract static class ConstraintList implements ExplicableStatement {

		/**
		 * Initializes the addition of a new unnamed table constraint.<br/>
		 * The result does not represent a complete SQL statement.
		 * @return the new table constraint stub
		 */
		public ConstraintStub withTableConstraint() {
			return withTableConstraint(null);
		}

		/**
		 * Initializes the addition of a new named table constraint.<br/>
		 * The result does not represent a complete SQL statement.
		 * @param constraintName the name of the new table constraint
		 * @return the new table constraint stub
		 */
		public ConstraintStub withTableConstraint(String constraintName) {
			return new ConstraintStub(this, constraintName);
		}

		/**
		 * Adds a <code><strong>WITHOUT ROWID</strong></code> clause to the statement.<br/>
		 * The result is a complete SQL statement.
		 * @return the <code>CREATE TABLE</code> statement ending with a <code>WITHOUT ROWID</code> clause
		 */
		public WithoutRowid withoutRowId() {
			return new WithoutRowid(this);
		}

		@Override
		public void build(StringBuilder receptacle) {
			appendPrefix(receptacle);
			receptacle.append(')');
		}

		@Override
		public abstract ConstraintList copy();

		abstract void appendPrefix(StringBuilder receptacle);
	}

	/**
	 * The common ancestor class for <code>CREATE TABLE</code> statements which may be supplemented
	 * with a column constraint. Such statements may also be supplemented with another column definition,
	 * table constraint, or a <code>WITHOUT ROWID</code> clause.
	 */
	public abstract static class ColumnConstraintList extends ConstraintList implements ColumnList {

		/**
		 * Initializes the addition of a new unnamed column constraint to the last added column.<br/>
		 * The result does not represent a complete SQL statement.
		 * @return the new column constraint stub
		 */
		public ColumnConstraintStub withColumnConstraint() {
			return withColumnConstraint(null);
		}

		/**
		 * Initializes the addition of a new named column constraint to the last added column.<br/>
		 * The result does not represent a complete SQL statement.
		 * @param constraintName the name of the new column constraint
		 * @return the new column constraint stub
		 */
		public ColumnConstraintStub withColumnConstraint(String constraintName) {
			return new ColumnConstraintStub(this, constraintName);
		}

		@Override
		public UntypedColumn addColumn(CharSequence columnName) {
			return new UntypedColumn(this, columnName);
		}

		@Override
		public abstract ColumnConstraintList copy();
	}

	/**
	 * Represents a <code>CREATE TABLE</code> statement ending with an untyped column definition:<br/>
	 * <code><strong>CREATE [TEMPORARY] TABLE [IF NOT EXISTS] [<em>schemaName</em>.]<em>tableName</em>
	 * ({<em>columnDefinition<sub>i</sub></em>, }<em>columnName</em>)</strong></code>.<br/>
	 * This is a complete SQL statement.
	 */
	public static final class UntypedColumn extends ColumnConstraintList {

		private final Stub stub;

		private final ColumnConstraintList previous;

		private final CharSequence columnName;

		UntypedColumn(Stub stub, CharSequence columnName) {
			this(stub, null, columnName);
		}

		UntypedColumn(ColumnConstraintList previous, CharSequence columnName) {
			this(null, previous, columnName);
		}

		private UntypedColumn(Stub stub, ColumnConstraintList previous, CharSequence columnName) {
			this.stub = stub;
			this.previous = previous;
			this.columnName = columnName;
		}

		@Override
		public UntypedColumn copy() {
			Stub stubCopy = stub == null ? null : stub.copy();
			ColumnConstraintList previousCopy = previous == null ? null : previous.copy();
			CharSequence columnNameCopy = columnName.toString();

			return stubCopy == stub && previousCopy == previous && columnNameCopy == columnName
				? this : new UntypedColumn(stubCopy, previousCopy, columnNameCopy);
		}

		@Override
		void appendPrefix(StringBuilder receptacle) {
			if (stub != null) {
				stub.appendTo(receptacle);
				receptacle.append('(');
			} else {
				previous.appendPrefix(receptacle);
				receptacle.append(", ");
			}
			SqliteUtilities.appendQuotedName(receptacle, columnName);
		}

		/**
		 * Adds a type definition to the last added column.<br/>
		 * The result represents a complete SQL statement.
		 * @param typeDefinition the type definition
		 * @return a <code>CREATE TABLE</code> statement ending with a typed column definition
		 */
		public TypedColumn ofType(String typeDefinition) {
			return new TypedColumn(this, typeDefinition);
		}
	}

	/**
	 * Represents a <code>CREATE TABLE</code> statement ending with a typed column definition.<br/>
	 * This is a complete SQL statement.
	 */
	public static final class TypedColumn extends ColumnConstraintList {

		private final UntypedColumn previous;

		private final CharSequence typeDefinition;

		TypedColumn(UntypedColumn previous, CharSequence typeDefinition) {
			this.previous = previous;
			this.typeDefinition = typeDefinition;
		}

		@Override
		public TypedColumn copy() {
			UntypedColumn previousCopy = previous.copy();
			CharSequence typeDefinitionCopy = typeDefinition.toString();

			return previousCopy == previous && typeDefinitionCopy == typeDefinition
				? this : new TypedColumn(previousCopy, typeDefinitionCopy);
		}

		@Override
		void appendPrefix(StringBuilder receptacle) {
			previous.appendPrefix(receptacle);
			receptacle.append(' ');
			SqliteUtilities.quoteType(receptacle, typeDefinition);
		}
	}

	/**
	 * This class is the initial point of adding a column constraint to the last added column
	 * of a <code>CREATE TABLE</code> statement.
	 */
	public static final class ColumnConstraintStub {

		private final ColumnConstraintList previous;

		private final CharSequence constraintName; // may be null

		ColumnConstraintStub(ColumnConstraintList previous, CharSequence constraintName) {
			this.previous = previous;
			this.constraintName = constraintName;
		}

		/**
		 * Makes the new column constraint a primary key constraint with default sorting order.<br/>
		 * The result is a complete SQL statement.
		 * @return the <code>CREATE TABLE</code> statement whose last column definition contains
		 * a primary key constraint
		 */
		public ColumnPk primaryKey() {
			return new ColumnPk(this, null);
		}

		/**
		 * Makes the new column constraint a primary key constraint with the specified sorting order.<br/>
		 * The result is a complete SQL statement.
		 * @param order the desired sorting order of the primary key index
		 * @return the <code>CREATE TABLE</code> statement whose last column definition contains
		 * a primary key constraint
		 */
		public ColumnPk primaryKey(SortingOrder order) {
			return new ColumnPk(this, order);
		}

		/**
		 * Makes the new column constraint a non-nullability constraint.<br/>
		 * The result is a complete SQL statement.
		 * @return the <code>CREATE TABLE</code> statement whose last column definition contains
		 * a non-nullability constraint
		 */
		public ColumnNotNull notNull() {
			return new ColumnNotNull(this, null);
		}

		/**
		 * Makes the new column constraint a non-nullability constraint with a specific action to
		 * be taken upon constraint violation.<br/>
		 * The result is a complete SQL statement.
		 * @param action the action to be taken upon constraint violation
		 * @return the <code>CREATE TABLE</code> statement whose last column definition contains
		 * a non-nullability constraint with an <code>ON CONFLICT</code> clause
		 */
		public ColumnNotNull notNullOnConflict(OnConflictAction action) {
			return new ColumnNotNull(this, action);
		}

		/**
		 * Makes the new column constraint a uniqueness constraint.<br/>
		 * The result is a complete SQL statement.
		 * @return the <code>CREATE TABLE</code> statement whose last column definition contains
		 * a uniqueness constraint
		 */
		public ColumnUnique unique() {
			return new ColumnUnique(this, null);
		}

		/**
		 * Makes the new column constraint a uniqueness constraint with a specific action to
		 * be taken upon constraint violation.<br/>
		 * The result is a complete SQL statement.
		 * @param action the action to be taken upon constraint violation
		 * @return the <code>CREATE TABLE</code> statement whose last column definition contains
		 * a uniqueness constraint with an <code>ON CONFLICT</code> clause
		 */
		public ColumnUnique uniqueOnConflict(OnConflictAction action) {
			return new ColumnUnique(this, action);
		}

		/**
		 * Makes the new column constraint a <code>CHECK</code> constraint.<br/>
		 * The result is a complete SQL statement.
		 * @param condition the condition which must hold in order for the constraint to be satisfied
		 * @return the <code>CREATE TABLE</code> statement whose last column definition contains
		 * a <code>CHECK</code> constraint
		 */
		public ColumnCheck check(SqlExpression condition) {
			return new ColumnCheck(this, condition);
		}

		/**
		 * Makes the new column constraint a default value constraint.<br/>
		 * The result is a complete SQL statement.
		 * @param defaultValue the value to be set when it is not specified explicitly (also used when
		 * applying the {@link ForeignKeyAction#SET_DEFAULT} referential action)
		 * @return the <code>CREATE TABLE</code> statement whose last column definition contains
		 * a default value constraint
		 */
		public ColumnDefault defaultValue(boolean defaultValue) {
			return defaultValue(Literal.value(defaultValue));
		}

		/**
		 * Makes the new column constraint a default value constraint.<br/>
		 * The result is a complete SQL statement.
		 * @param defaultValue the value to be set when it is not specified explicitly (also used when
		 * applying the {@link ForeignKeyAction#SET_DEFAULT} referential action)
		 * @return the <code>CREATE TABLE</code> statement whose last column definition contains
		 * a default value constraint
		 */
		public ColumnDefault defaultValue(long defaultValue) {
			return defaultValue(Literal.value(defaultValue));
		}

		/**
		 * Makes the new column constraint a default value constraint.<br/>
		 * The result is a complete SQL statement.
		 * @param defaultValue the value to be set when it is not specified explicitly (also used when
		 * applying the {@link ForeignKeyAction#SET_DEFAULT} referential action)
		 * @return the <code>CREATE TABLE</code> statement whose last column definition contains
		 * a default value constraint
		 */
		public ColumnDefault defaultValue(double defaultValue) {
			return defaultValue(Literal.value(defaultValue));
		}

		/**
		 * Makes the new column constraint a default value constraint.<br/>
		 * The result is a complete SQL statement.
		 * @param defaultValue the value to be set when it is not specified explicitly (also used when
		 * applying the {@link ForeignKeyAction#SET_DEFAULT} referential action); may be {@literal null}
		 * in which case <code>DEFAULT NULL</code> constraint will be added
		 * @return the <code>CREATE TABLE</code> statement whose last column definition contains
		 * a default value constraint
		 */
		public ColumnDefault defaultValue(Number defaultValue) {
			return defaultValue == null ? defaultNull() : defaultValue(Literal.value(defaultValue));
		}

		/**
		 * Makes the new column constraint a default value constraint.<br/>
		 * The result is a complete SQL statement.
		 * @param defaultValue the value to be set when it is not specified explicitly (also used when
		 * applying the {@link ForeignKeyAction#SET_DEFAULT} referential action); may be {@literal null}
		 * in which case <code>DEFAULT NULL</code> constraint will be added
		 * @return the <code>CREATE TABLE</code> statement whose last column definition contains
		 * a default value constraint
		 */
		public ColumnDefault defaultValue(String defaultValue) {
			return defaultValue == null ? defaultNull() : defaultValue(Literal.value(defaultValue));
		}

		/**
		 * Makes the new column constraint a default value constraint.<br/>
		 * The result is a complete SQL statement.
		 * @param defaultValue the value to be set when it is not specified explicitly (also used when
		 * applying the {@link ForeignKeyAction#SET_DEFAULT} referential action); may be {@literal null}
		 * in which case <code>DEFAULT NULL</code> constraint will be added
		 * @return the <code>CREATE TABLE</code> statement whose last column definition contains
		 * a default value constraint
		 */
		public ColumnDefault defaultValue(byte[] defaultValue) {
			return defaultValue == null ? defaultNull() : defaultValue(Literal.value(defaultValue));
		}

		/**
		 * Makes the new column constraint a <code>DEFAULT NULL</code> constraint.<br/>
		 * The result is a complete SQL statement.
		 * @return the <code>CREATE TABLE</code> statement whose last column definition contains
		 * a default value constraint
		 */
		public ColumnDefault defaultNull() {
			return defaultValue(Literal.NULL);
		}

		/**
		 * Makes the new column constraint a <code>DEFAULT CURRENT_TIME</code> constraint.<br/>
		 * The result is a complete SQL statement.
		 * @return the <code>CREATE TABLE</code> statement whose last column definition contains
		 * a default value constraint
		 */
		public ColumnDefault defaultCurrentTime() {
			return defaultValue(Literal.CURRENT_TIME);
		}

		/**
		 * Makes the new column constraint a <code>DEFAULT CURRENT_DATE</code> constraint.<br/>
		 * The result is a complete SQL statement.
		 * @return the <code>CREATE TABLE</code> statement whose last column definition contains
		 * a default value constraint
		 */
		public ColumnDefault defaultCurrentDate() {
			return defaultValue(Literal.CURRENT_DATE);
		}

		/**
		 * Makes the new column constraint a <code>DEFAULT CURRENT_TIMESTAMP</code> constraint.<br/>
		 * The result is a complete SQL statement.
		 * @return the <code>CREATE TABLE</code> statement whose last column definition contains
		 * a default value constraint
		 */
		public ColumnDefault defaultCurrentTimestamp() {
			return defaultValue(Literal.CURRENT_TIMESTAMP);
		}

		/**
		 * Makes the new column constraint a default value constraint.<br/>
		 * The result is a complete SQL statement.
		 * @param defaultValue the SQL expression used to calculate the default value each time
		 * it is needed
		 * @return the <code>CREATE TABLE</code> statement whose last column definition contains
		 * a default value constraint
		 */
		public ColumnDefault defaultValue(SqlExpression defaultValue) {
			return new ColumnDefault(this, defaultValue);
		}

		/**
		 * Makes the new column constraint a collation constraint.<br/>
		 * The result is a complete SQL statement.
		 * @param collation the collation to be applied to the last added column
		 * @return the <code>CREATE TABLE</code> statement whose last column definition contains
		 * a collation constraint
		 */
		public ColumnCollate collate(Collation collation) {
			return new ColumnCollate(this, collation);
		}

		/**
		 * Makes the new column constraint a foreign key constraint. The last added column
		 * references the primary key of the specified referenced table.<br/>
		 * The result is a complete SQL statement.
		 * @param foreignTableName the foreign table name
		 * @return the <code>CREATE TABLE</code> statement whose last column definition contains
		 * a foreign key constraint referencing the primary key of the specified table
		 */
		public ColumnFk references(String foreignTableName) {
			return new ColumnFk(this, foreignTableName, null);
		}

		/**
		 * Makes the new column constraint a foreign key constraint. The last added column
		 * references the specified column of the specified referenced table.<br/>
		 * The result is a complete SQL statement.
		 * @param foreignTableName the foreign table name
		 * @param foreignColumnName the referenced column of the foreign table
		 * @return the <code>CREATE TABLE</code> statement whose last column definition contains
		 * a foreign key constraint referencing the specified column of the specified table
		 */
		public ColumnFk references(String foreignTableName, String foreignColumnName) {
			return new ColumnFk(this, foreignTableName, foreignColumnName);
		}

		ColumnConstraintStub copy() {
			ColumnConstraintList previousCopy = previous.copy();
			CharSequence constraintNameCopy = constraintName == null ? null : constraintName.toString();

			return previousCopy == previous && constraintNameCopy == constraintName
				? this : new ColumnConstraintStub(previousCopy, constraintNameCopy);
		}

		void appendTo(StringBuilder receptacle) {
			previous.appendPrefix(receptacle);
			Constraints.appendConstraintName(receptacle, constraintName);
		}
	}

	/**
	 * Represents a <code>CREATE TABLE</code> statement ending with a primary key column constraint
	 * having neither an <code>ON CONFLICT</code> nor an <code>AUTOINCREMENT</code> clause.
	 */
	public static final class ColumnPk extends ColumnConstraintList {

		private final ColumnConstraintStub stub;

		private final SortingOrder order; // may be null

		public ColumnPk(ColumnConstraintStub stub, SortingOrder order) {
			this.stub = stub;
			this.order = order;
		}

		@Override
		public ColumnPk copy() {
			ColumnConstraintStub stubCopy = stub.copy();

			return stubCopy == stub ? this : new ColumnPk(stubCopy, order);
		}

		@Override
		void appendPrefix(StringBuilder receptacle) {
			stub.appendTo(receptacle);
			Constraints.appendPrimaryKey(receptacle, order);
		}

		/**
		 * Adds an <code>ON CONFLICT</code> clause to the primary key column constraint definition.<br/>
		 * The result is a complete SQL statement.
		 * @param action the action to be taken on primary key violation
		 * @return a <code>CREATE TABLE</code> statement whose last column has a primary key constraint
		 * with an <code>ON CONFLICT</code> clause
		 */
		public ColumnPkConflict onConflict(OnConflictAction action) {
			return new ColumnPkConflict(this, action);
		}

		/**
		 * Adds an <code>AUTOINCREMENT</code> clause to the primary key column constraint definition.<br/>
		 * The result is a complete SQL statement.
		 * @return a <code>CREATE TABLE</code> statement whose last column has a primary key constraint
		 * with an <code>AUTOINCREMENT</code> clause
		 */
		public ColumnPkAutoincrement autoincrement() {
			return new ColumnPkAutoincrement(this);
		}
	}

	/**
	 * Represents a <code>CREATE TABLE</code> statement ending with a primary key column constraint
	 * having an <code>ON CONFLICT</code> but having no <code>AUTOINCREMENT</code> clause.
	 */
	public static final class ColumnPkConflict extends ColumnConstraintList {

		private final ColumnPk previous;

		private final OnConflictAction action;

		ColumnPkConflict(ColumnPk previous, OnConflictAction action) {
			this.previous = previous;
			this.action = action;
		}

		@Override
		public ColumnPkConflict copy() {
			ColumnPk previousCopy = previous.copy();

			return previousCopy == previous ? this : new ColumnPkConflict(previousCopy, action);
		}

		@Override
		void appendPrefix(StringBuilder receptacle) {
			previous.appendPrefix(receptacle);
			Constraints.appendConflictClause(receptacle, action);
		}

		/**
		 * Adds an <code>AUTOINCREMENT</code> clause to the primary key column constraint definition.<br/>
		 * The result is a complete SQL statement.
		 * @return a <code>CREATE TABLE</code> statement whose last column has a primary key constraint
		 * with an <code>AUTOINCREMENT</code> clause
		 */
		public ColumnPkAutoincrement autoincrement() {
			return new ColumnPkAutoincrement(this);
		}
	}

	/**
	 * Represents a <code>CREATE TABLE</code> statement ending with a primary key column constraint
	 * having an <code>AUTOINCREMENT</code> clause.
	 */
	public static final class ColumnPkAutoincrement extends ColumnConstraintList {

		private final ColumnConstraintList previous;

		ColumnPkAutoincrement(ColumnPk previous) {
			this((ColumnConstraintList)previous);
		}

		ColumnPkAutoincrement(ColumnPkConflict previous) {
			this((ColumnConstraintList)previous);
		}

		private ColumnPkAutoincrement(ColumnConstraintList previous) {
			this.previous = previous;
		}

		@Override
		public ColumnPkAutoincrement copy() {
			ColumnConstraintList previousCopy = previous.copy();

			return previousCopy == previous ? this : new ColumnPkAutoincrement(previousCopy);
		}

		@Override
		void appendPrefix(StringBuilder receptacle) {
			previous.appendPrefix(receptacle);
			receptacle.append(" AUTOINCREMENT");
		}
	}

	/**
	 * Represents a <code>CREATE TABLE</code> statement ending with a non-nullability column constraint.
	 */
	public static final class ColumnNotNull extends ColumnConstraintList {

		private final ColumnConstraintStub stub;

		private final OnConflictAction action;

		ColumnNotNull(ColumnConstraintStub stub, OnConflictAction action) {
			this.stub = stub;
			this.action = action;
		}

		@Override
		public ColumnNotNull copy() {
			ColumnConstraintStub stubCopy = stub.copy();

			return stubCopy == stub ? this : new ColumnNotNull(stubCopy, action);
		}

		@Override
		void appendPrefix(StringBuilder receptacle) {
			stub.appendTo(receptacle);
			Constraints.append(receptacle, "NOT NULL", action);
		}
	}

	/**
	 * Represents a <code>CREATE TABLE</code> statement ending with a uniqueness column constraint.
	 */
	public static final class ColumnUnique extends ColumnConstraintList {

		private final ColumnConstraintStub stub;

		private final OnConflictAction action;

		ColumnUnique(ColumnConstraintStub stub, OnConflictAction action) {
			this.stub = stub;
			this.action = action;
		}

		@Override
		public ColumnUnique copy() {
			ColumnConstraintStub stubCopy = stub.copy();

			return stubCopy == stub ? this : new ColumnUnique(stubCopy, action);
		}

		@Override
		void appendPrefix(StringBuilder receptacle) {
			stub.appendTo(receptacle);
			Constraints.append(receptacle, "UNIQUE", action);
		}
	}

	/**
	 * Represents a <code>CREATE TABLE</code> statement ending with a <code>CHECK</code> column constraint.
	 */
	public static final class ColumnCheck extends ColumnConstraintList {

		private final ColumnConstraintStub stub;

		private final SqlExpression condition;

		ColumnCheck(ColumnConstraintStub stub, SqlExpression condition) {
			this.stub = stub;
			this.condition = condition;
		}

		@Override
		public ColumnCheck copy() {
			ColumnConstraintStub stubCopy = stub.copy();
			SqlExpression conditionCopy = condition.copy();

			return stubCopy == stub && conditionCopy == condition
				? this : new ColumnCheck(stubCopy, conditionCopy);
		}

		@Override
		void appendPrefix(StringBuilder receptacle) {
			stub.appendTo(receptacle);
			Constraints.appendCheck(receptacle, condition);
		}
	}

	/**
	 * Represents a <code>CREATE TABLE</code> statement ending with a default value column constraint.
	 */
	public static final class ColumnDefault extends ColumnConstraintList {

		private final ColumnConstraintStub stub;

		private final SqlExpression defaultValue;

		ColumnDefault(ColumnConstraintStub stub, SqlExpression defaultValue) {
			this.stub = stub;
			this.defaultValue = defaultValue;
		}

		@Override
		public ColumnDefault copy() {
			ColumnConstraintStub stubCopy = stub.copy();
			SqlExpression defaultValueCopy = defaultValue.copy();

			return stubCopy == stub && defaultValueCopy == defaultValue
				? this : new ColumnDefault(stubCopy, defaultValueCopy);
		}

		@Override
		public void appendPrefix(StringBuilder receptacle) {
			stub.appendTo(receptacle);
			Constraints.appendDefault(receptacle, defaultValue);
		}
	}

	/**
	 * Represents a <code>CREATE TABLE</code> statement ending with a collation column constraint.
	 */
	public static final class ColumnCollate extends ColumnConstraintList {

		private final ColumnConstraintStub stub;

		private final Collation collation;

		ColumnCollate(ColumnConstraintStub stub, Collation collation) {
			this.stub = stub;
			this.collation = collation;
		}

		@Override
		public ColumnCollate copy() {
			ColumnConstraintStub stubCopy = stub.copy();

			return stubCopy == stub ? this : new ColumnCollate(stubCopy, collation);
		}

		@Override
		void appendPrefix(StringBuilder receptacle) {
			stub.appendTo(receptacle);
			Constraints.appendCollate(receptacle, collation);
		}
	}

	/**
	 * A common ancestor class for <code>CREATE TABLE</code> statements which may be
	 * supplemented with a <code>[NOT] DEFERRABLE</code> clause and, by consequence,
	 * with <code>ON DELETE</code> and <code>ON UPDATE</code> referential action clauses.
	 */
	abstract static class ColumnFkDeferrable extends ColumnConstraintList {

		@Override
		public abstract ColumnFkDeferrable copy();

		/**
		 * Adds a <code>DEFERRABLE INITIALLY DEFERRED</code> clause to the foreign key
		 * constraint on the last added column.<br/>
		 * The result is a complete SQL statement.
		 * @return a <code>CREATE TABLE</code> statement whose last column definition has a foreign
		 * key constraint with <code>DEFERRABLE INITIALLY DEFERRED</code> behaviour
		 */
		public ColumnFkTiming deferred() {
			return new ColumnFkTiming(this, true);
		}

		/**
		 * Adds a <code>NOT DEFERRABLE INITIALLY IMMEDIATE</code> clause to the foreign key
		 * constraint on the last added column.<br/>
		 * The result is a complete SQL statement.
		 * @return a <code>CREATE TABLE</code> statement whose last column definition has a foreign
		 * key constraint with <code>NOT DEFERRABLE INITIALLY IMMEDIATE</code> behaviour
		 */
		public ColumnFkTiming immediate() {
			return new ColumnFkTiming(this, false);
		}

		/**
		 * Adds an <code>ON DELETE</code> referential action clause to the foreign key
		 * constraint on the last added column.<br/>
		 * The result is a complete SQL statement.
		 * @param action the action to be taken when the referenced row is deleted
		 * @return a <code>CREATE TABLE</code> statement whose last column definition has a foreign
		 * key constraint with specified <code>ON DELETE</code> behaviour
		 */
		public ColumnFkAction onDelete(ForeignKeyAction action) {
			return new ColumnFkAction(this, true, action);
		}

		/**
		 * Adds an <code>ON UPDATE</code> referential action clause to the foreign key
		 * constraint on the last added column.<br/>
		 * The result is a complete SQL statement.
		 * @param action the action to be taken when the referenced column is updated
		 * @return a <code>CREATE TABLE</code> statement whose last column definition has a foreign
		 * key constraint with specified <code>ON UPDATE</code> behaviour
		 */
		public ColumnFkAction onUpdate(ForeignKeyAction action) {
			return new ColumnFkAction(this, false, action);
		}
	}

	/**
	 * Represents a <code>CREATE TABLE</code> statement whose last column definition has a foreign
	 * key constraint with no additional clauses specified.<br/>
	 * This is a complete SQL statement.
	 */
	public static final class ColumnFk extends ColumnFkDeferrable {

		private final ColumnConstraintStub stub;

		private final CharSequence tableName;

		private final CharSequence columnName; // may be null

		ColumnFk(ColumnConstraintStub stub, CharSequence tableName, CharSequence columnName) {
			this.stub = stub;
			this.tableName = tableName;
			this.columnName = columnName;
		}

		@Override
		public ColumnFk copy() {
			ColumnConstraintStub stubCopy = stub.copy();
			CharSequence tableNameCopy = tableName.toString();
			CharSequence columnNameCopy = columnName == null ? null : columnName.toString();

			return stubCopy == stub && tableNameCopy == tableName && columnNameCopy == columnName
				? this : new ColumnFk(stubCopy, tableNameCopy, columnNameCopy);
		}

		@Override
		void appendPrefix(StringBuilder receptacle) {
			stub.appendTo(receptacle);
			Constraints.appendForeignKey(receptacle, tableName, columnName);
		}
	}

	/**
	 * Represents a <code>CREATE TABLE</code> statement whose last column definition ends with
	 * a foreign key constraint whose last clause is either an <code>ON DELETE</code> or
	 * an <code>ON UPDATE</code> referential action clause. Note that SQLite allows adding more
	 * than one such clause for each of the two events but uses only the last specified action.<br/>
	 * This is a complete SQL statement.
	 */
	public static final class ColumnFkAction extends ColumnFkDeferrable {

		private final ColumnFkDeferrable previous;

		private final boolean isDeleteAction;

		private final ForeignKeyAction action;

		ColumnFkAction(
			ColumnFkDeferrable previous, boolean isDeleteAction, ForeignKeyAction action) {

			this.previous = previous;
			this.isDeleteAction = isDeleteAction;
			this.action = action;
		}

		@Override
		public ColumnFkAction copy() {
			ColumnFkDeferrable previousCopy = previous.copy();

			return previousCopy == previous
				? this : new ColumnFkAction(previousCopy, isDeleteAction, action);
		}

		@Override
		void appendPrefix(StringBuilder receptacle) {
			previous.appendPrefix(receptacle);
			Constraints.appendReferentialAction(receptacle, isDeleteAction, action);
		}
	}

	/**
	 * Represents a <code>CREATE TABLE</code> statement whose last column definition has a foreign
	 * key constraint with either a <code>NOT DEFERRABLE INITIALLY IMMEDIATE</code> or
	 * a <code>DEFERRABLE INITIALLY DEFERRED</code> clause specified.<br/>
	 * This is a complete SQL statement.
	 */
	public static final class ColumnFkTiming extends ColumnConstraintList {

		private final ColumnFkDeferrable previous;

		private final boolean deferred;

		public ColumnFkTiming(ColumnFkDeferrable previous, boolean deferred) {
			this.previous = previous;
			this.deferred = deferred;
		}

		@Override
		public ColumnFkTiming copy() {
			ColumnFkDeferrable previousCopy = previous.copy();

			return previousCopy == previous ? this : new ColumnFkTiming(previousCopy, deferred);
		}

		@Override
		void appendPrefix(StringBuilder receptacle) {
			previous.build(receptacle);
			Constraints.appendForeignKeyTiming(receptacle, deferred);
		}
	}

	/**
	 * Represents the initial stage of adding a table constraint to a <code>CREATE TABLE</code>
	 * statement.<br/>
	 * This is not a complete SQL statement.
	 */
	public static final class ConstraintStub {

		private final ConstraintList previous;

		private final CharSequence constraintName; // may be null

		ConstraintStub(ConstraintList previous, CharSequence constraintName) {
			this.previous = previous;
			this.constraintName = constraintName;
		}

		/**
		 * Specifies that the table constraint being added is a primary key definition.
		 * @return a primary key definition builder (implicitly containing the previous part of the statement)
		 */
		public UniqueStub primaryKey() {
			return new UniqueStub(this, true);
		}

		/**
		 * Specifies that the table constraint being added is a uniqueness constraint.
		 * @return a unique constraint builder (implicitly containing the previous part of the statement)
		 */
		public UniqueStub unique() {
			return new UniqueStub(this, false);
		}

		/**
		 * Specifies that the table constraint being added is a foreign key constraint.
		 * @param columns the names of the referring columns of the table to be created
		 * @return a foreign key constraint builder (implicitly containing the previous part of the statement)
		 */
		public FkStub foreignKey(CharSequence... columns) {
			return foreignKey(Arrays.asList(columns));
		}

		/**
		 * Specifies that the table constraint being added is a foreign key constraint.
		 * @param columns the names of the referring columns of the table to be created
		 * @return a foreign key constraint builder (implicitly containing the previous part of the statement)
		 */
		public FkStub foreignKey(Iterable<? extends CharSequence> columns) {
			return new FkStub(this, columns);
		}

		/**
		 * Adds a <code>CHECK</code> constraint to the <code>CREATE TABLE</code> constraint.<br/>
		 * The result is a complete SQL statement.
		 * @param condition the condition to check upon table inserts and updates
		 * @return a <code>CREATE TABLE</code> ending with a <code>CHECK</code> table constraint
		 */
		public Check check(SqlExpression condition) {
			return new Check(this, condition);
		}

		ConstraintStub copy() {
			ConstraintList previousCopy = previous.copy();
			CharSequence constraintNameCopy = constraintName == null ? null : constraintName.toString();

			return previousCopy == previous && constraintNameCopy == constraintName
				? this : new ConstraintStub(previousCopy, constraintNameCopy);
		}

		void appendTo(StringBuilder receptacle) {
			previous.appendPrefix(receptacle);
			receptacle.append(',');
			Constraints.appendConstraintName(receptacle, constraintName);
		}
	}

	/**
	 * This interface serves to add columns to the unique and primary key table constraints
	 * in a uniform way. The implementing constraint builder classes are {@link UniqueStub}
	 * and {@link UniqueColumn}.
	 */
	public interface UniqueKeyColumnList {

		/**
		 * Adds a column to a uniqueness or primary key constraint.<br/>
		 * The result is a complete SQL statement.
		 * @param column the name of the column to add to the constraint
		 * @return the <code>CREATE TABLE</code> statement ending with
		 * a uniqueness or primary key constraint whose last column is the specified one
		 */
		UniqueColumn addColumn(CharSequence column);

		/**
		 * Adds a column with specified collation to a uniqueness or primary key constraint.<br/>
		 * The result is a complete SQL statement.
		 * @param column the name of the column to add to the constraint
		 * @param collation the collation sequence to use when enforcing the constraint
		 * @return the <code>CREATE TABLE</code> statement ending with
		 * a uniqueness or primary key constraint whose last column is the specified one
		 */
		UniqueColumn addColumn(CharSequence column, Collation collation);

		/**
		 * Adds a column with specified sorting order to a uniqueness or primary key constraint.<br/>
		 * The result is a complete SQL statement.
		 * @param column the name of the column to add to the constraint
		 * @param order the sorting order used for the column
		 * @return the <code>CREATE TABLE</code> statement ending with
		 * a uniqueness or primary key constraint whose last column is the specified one
		 */
		UniqueColumn addColumn(CharSequence column, SortingOrder order);

		/**
		 * Adds a column with specified collation and sorting order to a uniqueness or primary key constraint.<br/>
		 * The result is a complete SQL statement.
		 * @param column the name of the column to add to the constraint
		 * @param collation the collation sequence to use when enforcing the constraint
		 * @param order the sorting order used for the column
		 * @return the <code>CREATE TABLE</code> statement ending with
		 * a uniqueness or primary key constraint whose last column is the specified one
		 */
		UniqueColumn addColumn(CharSequence column, Collation collation, SortingOrder order);
	}

	/**
	 * Represents the initial stage of uniqueness or primary key table constraint definition.
	 * Contains the type of the constraint (<code>UNIQUE</code> or <code>PRIMARY KEY</code>) and
	 * allows adding the first column to the constraint.
	 */
	public static final class UniqueStub implements UniqueKeyColumnList {

		private final ConstraintStub previous;

		private final boolean isPrimaryKey;

		UniqueStub(ConstraintStub previous, boolean isPrimaryKey) {
			this.previous = previous;
			this.isPrimaryKey = isPrimaryKey;
		}

		@Override
		public UniqueColumn addColumn(CharSequence column) {
			return new UniqueColumn(this, column, null, null);
		}

		@Override
		public UniqueColumn addColumn(CharSequence column, SortingOrder order) {
			return new UniqueColumn(this, column, null, order);
		}

		@Override
		public UniqueColumn addColumn(CharSequence column, Collation collation) {
			return new UniqueColumn(this, column, collation, null);
		}

		@Override
		public UniqueColumn addColumn(CharSequence column, Collation collation, SortingOrder order) {
			return new UniqueColumn(this, column, collation, order);
		}

		UniqueStub copy() {
			ConstraintStub previousCopy = previous.copy();

			return previousCopy == previous ? this : new UniqueStub(previousCopy, isPrimaryKey);
		}

		void appendTo(StringBuilder receptacle) {
			previous.appendTo(receptacle);
			receptacle.append(isPrimaryKey ? " PRIMARY KEY" : " UNIQUE");
		}
	}

	/**
	 * Represents a <code>CREATE TABLE</code> statement with its last table constraint
	 * being a uniqueness constraint or a primary key constraint without an <code>ON CONFLICT</code> clause.<br/>
	 * This is a complete SQL statement.
	 */
	public static final class UniqueColumn extends ConstraintList implements UniqueKeyColumnList {

		private final UniqueStub stub;

		private final UniqueColumn previous;

		private final CharSequence columnName;

		private final Collation collation; // may be null

		private final SortingOrder order; // may be null

		UniqueColumn(UniqueStub stub,
			CharSequence columnName, Collation collation, SortingOrder order) {

			this(stub, null, columnName, collation, order);
		}

		UniqueColumn(UniqueColumn previous,
			CharSequence columnName, Collation collation, SortingOrder order) {

			this(null, previous, columnName, collation, order);
		}

		private UniqueColumn(UniqueStub stub,
			UniqueColumn previous, CharSequence columnName, Collation collation, SortingOrder order) {

			this.stub = stub;
			this.previous = previous;
			this.columnName = columnName;
			this.collation = collation;
			this.order = order;
		}

		@Override
		public UniqueColumn addColumn(CharSequence column) {
			return new UniqueColumn(this, column, null, null);
		}

		@Override
		public UniqueColumn addColumn(CharSequence column, SortingOrder order) {
			return new UniqueColumn(this, column, null, order);
		}

		@Override
		public UniqueColumn addColumn(CharSequence column, Collation collation) {
			return new UniqueColumn(this, column, collation, null);
		}

		@Override
		public UniqueColumn addColumn(CharSequence column, Collation collation, SortingOrder order) {
			return new UniqueColumn(this, column, collation, order);
		}

		/**
		 * Adds an <code>ON CONFLICT</code> clause to the table constraint.<br/>
		 * The result is a complete SQL statement.
		 * @param action the action to take upon the constraint violation
		 * @return the <code>CREATE TABLE</code> statement having the specified
		 * <code>ON CONFLICT</code> action for the last (uniqueness or primary key) table constraint
		 */
		public UniqueColumnConflict onConflict(OnConflictAction action) {
			return new UniqueColumnConflict(this, action);
		}

		@Override
		public UniqueColumn copy() {
			UniqueStub stubCopy = stub == null ? null : stub.copy();
			UniqueColumn previousCopy = previous == null ? null : previous.copy();
			CharSequence columnNameCopy = columnName.toString();

			return stubCopy == stub && previousCopy == previous && columnNameCopy == columnName
				? this : new UniqueColumn(stubCopy, previousCopy, columnNameCopy, collation, order);
		}

		@Override
		void appendPrefix(StringBuilder receptacle) {
			List<UniqueColumn> columns = new ArrayList<>();
			for (UniqueColumn current = this; current != null; current = current.previous) {
				columns.add(current);
			}

			columns.get(columns.size() - 1).stub.appendTo(receptacle);
			receptacle.append('(');
			for (int i = columns.size() - 1; i >= 0; i--) {
				UniqueColumn column = columns.get(i);
				SqliteUtilities.appendQuotedName(receptacle, column.columnName);
				if (column.collation != null) {
					receptacle.append(" COLLATE ");
					column.collation.appendTo(receptacle);
				}
				if (column.order != null) {
					receptacle.append(' ').append(column.order.toString());
				}
				if (i != 0) {
					receptacle.append(", ");
				}
			}
			receptacle.append(')');
		}
	}

	/**
	 * Represents a <code>CREATE TABLE</code> statement with its last table constraint
	 * being a uniqueness constraint or a primary key constraint with an <code>ON CONFLICT</code> clause.<br/>
	 * This is a complete SQL statement.
	 */
	public static final class UniqueColumnConflict extends ConstraintList {

		private final UniqueColumn previous;

		private final OnConflictAction action;

		UniqueColumnConflict(UniqueColumn previous, OnConflictAction action) {
			this.previous = previous;
			this.action = action;
		}

		@Override
		public UniqueColumnConflict copy() {
			UniqueColumn previousCopy = previous.copy();

			return previousCopy == previous ? this : new UniqueColumnConflict(previousCopy, action);
		}

		@Override
		void appendPrefix(StringBuilder receptacle) {
			previous.appendPrefix(receptacle);
			Constraints.appendConflictClause(receptacle, action);
		}
	}

	/**
	 * Represents the initial stage of foreign key table constraint definition.
	 * Contains the tuple of referring columns.
	 */
	public static final class FkStub {

		private final ConstraintStub previous;

		private final Iterable<? extends CharSequence> columns;

		FkStub(ConstraintStub previous, Iterable<? extends CharSequence> columns) {
			this.previous = previous;
			this.columns = columns;
		}

		/**
		 * Defines the referenced table. If the referenced columns are not specified on the next
		 * step, the foreign column tuple will be inferred from the table's primary key.<br/>
		 * The result is a complete SQL statement.
		 * @param foreignTableName the name of the foreign table
		 * @return the <code>CREATE TABLE</code> statement whose last table constraint is
		 * a foreign key constraint referring to the specified table
		 */
		public FkTable references(CharSequence foreignTableName) {
			return new FkTable(this, foreignTableName);
		}

		FkStub copy() {
			ConstraintStub previousCopy = previous.copy();
			Iterable<? extends CharSequence> columnsCopy =
				ReadonlyIterable.of(columns, CharSequence::toString);

			return previousCopy == previous && columnsCopy == columns
				? this : new FkStub(previousCopy, columnsCopy);
		}

		void appendTo(StringBuilder receptacle) {
			if (!columns.iterator().hasNext()) {
				throw new IllegalArgumentException("At least one column name is expected.");
			}

			previous.appendTo(receptacle);
			receptacle.append(" FOREIGN KEY(");
			SqliteUtilities.appendQuotedDelimited(receptacle, columns);
			receptacle.append(')');
		}
	}

	/**
	 * The common ancestor class for the <code>CREATE TABLE</code> statements whose
	 * last table constraint is a foreign key constrain which may be supplemented
	 * with a <code>[NOT] DEFERRABLE</code> clause and, by consequence, with
	 * <code>ON DELETE</code> and <code>ON UPDATE</code> referential action clauses.<br/>
	 * This is a complete SQL statement.
	 */
	public abstract static class FkDeferrable extends ConstraintList {

		@Override
		public abstract FkDeferrable copy();

		/**
		 * Adds a <code>DEFERRABLE INITIALLY DEFERRED</code> clause to the foreign key
		 * constraint.<br/>
		 * The result is a complete SQL statement.
		 * @return a <code>CREATE TABLE</code> statement whose last table constraint is a foreign
		 * key constraint with <code>DEFERRABLE INITIALLY DEFERRED</code> behaviour
		 */
		public FkTiming deferred() {
			return new FkTiming(this, true);
		}

		/**
		 * Adds a <code>NOT DEFERRABLE INITIALLY IMMEDIATE</code> clause to the foreign key
		 * constraint.<br/>
		 * The result is a complete SQL statement.
		 * @return a <code>CREATE TABLE</code> statement whose last table constraint is a foreign
		 * key constraint with <code>NOT DEFERRABLE INITIALLY IMMEDIATE</code> behaviour
		 */
		public FkTiming immediate() {
			return new FkTiming(this, false);
		}

		/**
		 * Adds an <code>ON DELETE</code> referential action clause to the foreign key
		 * constraint.<br/>
		 * The result is a complete SQL statement.
		 * @param action the action to be taken when the referenced row is deleted
		 * @return a <code>CREATE TABLE</code> statement whose last table constraint is a foreign
		 * key constraint with specified <code>ON DELETE</code> behaviour
		 */
		public FkAction onDelete(ForeignKeyAction action) {
			return new FkAction(this, true, action);
		}

		/**
		 * Adds an <code>ON UPDATE</code> referential action clause to the foreign key
		 * constraint.<br/>
		 * The result is a complete SQL statement.
		 * @param action the action to be taken when the referenced columns are updated
		 * @return a <code>CREATE TABLE</code> statement whose last table constraint is a foreign
		 * key constraint with specified <code>ON UPDATE</code> behaviour
		 */
		public FkAction onUpdate(ForeignKeyAction action) {
			return new FkAction(this, false, action);
		}
	}

	/**
	 * Represents a <code>CREATE TABLE</code> statement whose last table constraint is
	 * a foreign key constraint having the form<br/>
	 * <code>FOREIGN KEY (<em>columnName<sub>0</sub></em>{, <em>columnName<sub>i</sub></em>})
	 * REFERENCES <em>foreignTableName</em></code>.<br/>
	 * This is a complete SQL statement.
	 */
	public static final class FkTable extends FkDeferrable {

		private final FkStub stub;

		private final CharSequence foreignTableName;

		FkTable(FkStub stub, CharSequence foreignTableName) {
			this.stub = stub;
			this.foreignTableName = foreignTableName;
		}

		/**
		 * Specifies the referenced table columns of the foreign key table constraint.<br/>
		 * The result is a complete SQL statement.
		 * @param foreignColumnNames the names of the referenced table columns corresponding
		 * to the previously specified foreign key columns
		 * @return the <code>CREATE TABLE</code> statement whose last table constraint is
		 * a foreign key constraint having the form<br/>
		 * <code>FOREIGN KEY (<em>columnName<sub>0</sub></em>{, <em>columnName<sub>i</sub></em>})
		 * REFERENCES <em>foreignTableName</em>(<strong><em>foreignColumnName<sub>0</sub></em>{,
		 * <em>foreignColumnName<sub>i</sub></em>}</strong>)</code>
		 */
		public FkColumns columns(CharSequence... foreignColumnNames) {
			return columns(Arrays.asList(foreignColumnNames));
		}

		/**
		 * Specifies the referenced table columns of the foreign key table constraint.<br/>
		 * The result is a complete SQL statement.
		 * @param foreignColumnNames the names of the referenced table columns corresponding
		 * to the previously specified foreign key columns
		 * @return the <code>CREATE TABLE</code> statement whose last table constraint is
		 * a foreign key constraint having the form<br/>
		 * <code>FOREIGN KEY (<em>columnName<sub>0</sub></em>{, <em>columnName<sub>i</sub></em>})
		 * REFERENCES <em>foreignTableName</em>(<strong><em>foreignColumnName<sub>0</sub></em>{,
		 * <em>foreignColumnName<sub>i</sub></em>}</strong>)</code>
		 */
		public FkColumns columns(Iterable<? extends CharSequence> foreignColumnNames) {
			return new FkColumns(this, foreignColumnNames);
		}

		@Override
		public FkTable copy() {
			FkStub stubCopy = stub.copy();
			CharSequence foreignTableNameCopy = foreignTableName.toString();

			return stubCopy == stub && foreignTableNameCopy == foreignTableName
				? this : new FkTable(stubCopy, foreignTableNameCopy);
		}

		@Override
		void appendPrefix(StringBuilder receptacle) {
			stub.appendTo(receptacle);
			receptacle.append(" REFERENCES ");
			SqliteUtilities.appendQuotedName(receptacle, foreignTableName);
		}
	}

	/**
	 * Represents a <code>CREATE TABLE</code> statement whose last table constraint is
	 * a foreign key constraint having the form<br/>
	 * <code>FOREIGN KEY (<em>columnName<sub>0</sub></em>{, <em>columnName<sub>i</sub></em>})
	 * REFERENCES <em>foreignTableName</em>(<em>foreignColumnName<sub>0</sub></em>{,
	 * <em>foreignColumnName<sub>i</sub></em>})</code>.<br/>
	 * This is a complete SQL statement.
	 */
	public static final class FkColumns extends FkDeferrable {

		private final FkTable previous;

		private final Iterable<? extends CharSequence> foreignColumnNames;

		FkColumns(FkTable previous, Iterable<? extends CharSequence> foreignColumnNames) {
			this.previous = previous;
			this.foreignColumnNames = foreignColumnNames;
		}

		@Override
		public FkColumns copy() {
			FkTable previousCopy = previous.copy();
			Iterable<? extends CharSequence> foreignColumnNamesCopy =
				ReadonlyIterable.of(foreignColumnNames, CharSequence::toString);

			return previousCopy == previous && foreignColumnNamesCopy == foreignColumnNames
				? this : new FkColumns(previousCopy, foreignColumnNamesCopy);
		}

		@Override
		void appendPrefix(StringBuilder receptacle) {
			previous.appendPrefix(receptacle);
			receptacle.append('(');
			SqliteUtilities.appendQuotedDelimited(receptacle, foreignColumnNames);
			receptacle.append(')');
		}
	}

	/**
	 * Represents a <code>CREATE TABLE</code> statement whose last table constraint is
	 * a foreign key constraint ending with an <code>ON DELETE</code> or an <code>ON UPDATE</code>
	 * clause.<br/>
	 * This is a complete SQL statement.
	 */
	public static final class FkAction extends FkDeferrable {

		private final FkDeferrable previous;

		private final boolean isDeleteAction;

		private final ForeignKeyAction action;

		FkAction(FkDeferrable previous, boolean isDeleteAction, ForeignKeyAction action) {
			this.previous = previous;
			this.isDeleteAction = isDeleteAction;
			this.action = action;
		}

		@Override
		public FkAction copy() {
			FkDeferrable previousCopy = previous.copy();

			return previousCopy == previous
				? this : new FkAction(previousCopy, isDeleteAction, action);
		}

		@Override
		void appendPrefix(StringBuilder receptacle) {
			previous.appendPrefix(receptacle);
			Constraints.appendReferentialAction(receptacle, isDeleteAction, action);
		}
	}

	/**
	 * Represents a <code>CREATE TABLE</code> statement whose last table constraint is
	 * a foreign key constraint ending with a <code>[NOT] DEFERRED</code> clause.<br/>
	 * This is a complete SQL statement.
	 */
	public static final class FkTiming extends ConstraintList {

		private final FkDeferrable previous;

		private final boolean deferred;

		FkTiming(FkDeferrable previous, boolean deferred) {
			this.previous = previous;
			this.deferred = deferred;
		}

		@Override
		public FkTiming copy() {
			FkDeferrable previousCopy = previous.copy();

			return previousCopy == previous ? this : new FkTiming(previousCopy, deferred);
		}

		@Override
		void appendPrefix(StringBuilder receptacle) {
			previous.appendPrefix(receptacle);
			Constraints.appendForeignKeyTiming(receptacle, deferred);
		}
	}

	/**
	 * Represents a <code>CREATE TABLE</code> statement whose last table constraint is
	 * a <code>CHECK</code> constraint.<br/>
	 * This is a complete SQL statement.
	 */
	public static final class Check extends ConstraintList {

		private final ConstraintStub stub;

		private final SqlExpression condition;

		Check(ConstraintStub stub, SqlExpression condition) {
			this.stub = stub;
			this.condition = condition;
		}

		@Override
		public Check copy() {
			ConstraintStub stubCopy = stub.copy();
			SqlExpression conditionCopy = condition.copy();

			return stubCopy == stub && conditionCopy == condition
				? this : new Check(stubCopy, conditionCopy);
		}

		@Override
		void appendPrefix(StringBuilder receptacle) {
			stub.appendTo(receptacle);
			Constraints.appendCheck(receptacle, condition);
		}
	}

	private CreateTable() { }
}
