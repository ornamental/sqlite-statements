package dev.ornamental.sqlite.statement;

/**
 * This class has no functionality of its own. It is destined for grouping the classes
 * pertaining to the <code>ALTER TABLE</code> statements.
 */
public final class AlterTable {

	/**
	 * Represents the initial part of the <code>ALTER TABLE</code> statement,
	 * namely the part<br/>
	 * <code><strong>ALTER TABLE schemaName.tableName</strong></code>.<br/>
	 * This stub does not represent a complete SQL statement.
	 */
	public static final class Stub {

		private final CharSequence schemaName;

		private final CharSequence tableName;

		Stub(CharSequence tableName) {
			this(null, tableName);
		}

		Stub(CharSequence schemaName, CharSequence tableName) {
			this.schemaName = schemaName;
			this.tableName = tableName;
		}

		/**
		 * Supplements the stub so that it takes the form<br/>
		 * <code>ALTER TABLE schemaName.tableName <strong>RENAME TO newTableName</strong></code><br/>
		 * serving to rename the table. The result is a complete SQL statement.
		 * @param newTableName the new name of the table
		 * @return the complete <code>ALTER TABLE</code> statement
		 */
		public RenameTo renameTo(CharSequence newTableName) {
			return new RenameTo(schemaName, tableName, newTableName);
		}

		/**
		 * Supplements the stub so that it takes the form<br/>
		 * <code>ALTER TABLE <em>schemaName</em>.<em>tableName</em><strong>
		 * ADD COLUMN <em>columnName</em>
		 * </strong></code><br/>
		 * serving to add a new column to the table. The result is a complete SQL statement which may
		 * be further supplemented with column type info and column constraints.
		 * @param columnName the name of the new column to add to the table
		 * @return the complete <code>ALTER TABLE</code> statement
		 */
		public AddColumnSimpleStatement addColumn(CharSequence columnName) {
			return new AddColumnSimpleStatement(schemaName, tableName, columnName);
		}
	}

	/**
	 * Represents the <code>ALTER TABLE</code> statement variant for table renaming:<br/>
	 * <code><strong>ALTER TABLE <em>schemaName</em>.<em>tableName</em>
	 * RENAME TO <em>newTableName</em></strong></code>.
	 */
	public static final class RenameTo implements ExplicableStatement {

		private final CharSequence schemaName;

		private final CharSequence tableName;

		private final CharSequence newTableName;

		RenameTo(CharSequence schemaName, CharSequence tableName, CharSequence newTableName) {
			this.schemaName = schemaName;
			this.tableName = tableName;
			this.newTableName = newTableName;
		}

		@Override
		public RenameTo copy() throws IllegalStateException {
			CharSequence schemaNameCopy = schemaName == null ? null : schemaName.toString();
			CharSequence tableNameCopy = tableName.toString();
			CharSequence newTableNameCopy = newTableName.toString();

			return schemaNameCopy == schemaName && tableNameCopy == tableName && newTableNameCopy == newTableName
				? this : new RenameTo(schemaNameCopy, tableNameCopy, newTableNameCopy);
		}

		@Override
		public void build(StringBuilder receptacle) {
			receptacle.append("ALTER TABLE ");
			SqliteUtilities.appendQuotedName(receptacle, schemaName, tableName);
			receptacle.append(" RENAME TO ");
			SqliteUtilities.appendQuotedName(receptacle, newTableName);
		}
	}

	/**
	 * Represents the <code>ALTER TABLE</code> statement variant for adding a column to a table:<br/>
	 * <code><strong>ALTER TABLE <em>schemaName</em>.<em>tableName</em>
	 * ADD COLUMN <em>newColumnName</em></strong></code>.
	 */
	public interface AddColumnStatement extends ExplicableStatement {

		@Override
		AddColumnStatement copy() throws IllegalStateException;

		/**
		 * Adds an unnamed column constraint stub to the <code>ALTER TABLE .. ADD COLUMN</code> statement.
		 * The result is not a complete SQL statement.
		 * @return the column constraint stub allowing one to specify the type of the constraint
		 * and its parameters
		 */
		default AddColumnWithConstraintStub constraint() {
			return constraint(null);
		}

		/**
		 * Adds a named column constraint stub to the <code>ALTER TABLE .. ADD COLUMN</code> statement.
		 * The result is not a complete SQL statement.
		 * @param constraintName the name of the constraint to add
		 * @return the column constraint stub allowing one to specify the type of the constraint
		 * and its parameters
		 */
		default AddColumnWithConstraintStub constraint(CharSequence constraintName) {
			return new AddColumnWithConstraintStub(this, constraintName);
		}
	}

	/**
	 * Represents an intention to add a column constraint, named or not, to an
	 * <code>ALTER TABLE .. ADD COLUMN</code> statement.
	 * This class does not represent a complete SQL statement.
	 */
	public static final class AddColumnWithConstraintStub {

		private final AddColumnStatement previous;

		private final CharSequence constraintName; // null is admissible

		AddColumnWithConstraintStub(AddColumnStatement previous, CharSequence constraintName) {
			this.previous = previous;
			this.constraintName = constraintName;
		}

		void appendTo(StringBuilder receptacle) {
			previous.build(receptacle);
			Constraints.appendConstraintName(receptacle, constraintName);
		}

		AddColumnWithConstraintStub copy() {
			AddColumnStatement previousCopy = previous.copy();
			CharSequence constraintNameCopy = constraintName == null ? null : constraintName.toString();

			return previousCopy == previous && constraintNameCopy == constraintName
				? this : new AddColumnWithConstraintStub(previousCopy, constraintNameCopy);
		}

		/**
		 * Makes the constrained column a primary key with the default sorting order. The statement takes
		 * the form<br/>
		 * <code>ALTER TABLE .. ADD COLUMN .. <strong>PRIMARY KEY</strong></code>.<br/>
		 * The result is a complete SQL statement.
		 * @return the <code>ALTER TABLE .. ADD COLUMN</code> statement with a primary key constraint
		 * on the added column
		 */
		public AddColumnPk primaryKey() {
			return new AddColumnPk(this, null);
		}

		/**
		 * Makes the constrained column a primary key with an explicit sorting order. The statement takes
		 * the form<br/>
		 * <code>ALTER TABLE .. ADD COLUMN .. <strong>PRIMARY KEY ASC|DESC</strong></code>.<br/>
		 * The result is a complete SQL statement.
		 * @param order the sorting order, either ascending ({@link SortingOrder#ASC})
		 * or descending ({@link SortingOrder#DESC})
		 * @return the <code>ALTER TABLE .. ADD COLUMN</code> statement with a primary key constraint
		 * on the added column
		 */
		public AddColumnPk primaryKey(SortingOrder order) {
			return new AddColumnPk(this, order);
		}

		/**
		 * Disallows <code>NULL</code> values on the added column. The statement takes
		 * the form<br/>
		 * <code>ALTER TABLE .. ADD COLUMN .. <strong>NOT NULL</strong></code>.<br/>
		 * The result is a complete SQL statement.
		 * @return the <code>ALTER TABLE .. ADD COLUMN</code> statement with a <code>NOT NULL</code> constraint
		 * on the added column
		 */
		public AddColumnNotNull notNull() {
			return new AddColumnNotNull(this, null);
		}

		/**
		 * Disallows <code>NULL</code> values on the added column and adds a conflict
		 * resolution clause. The statement takes the form<br/>
		 * <code>ALTER TABLE .. ADD COLUMN .. <strong>NOT NULL
		 * ON CONFLICT ROLLBACK|ABORT|FAIL|IGNORE|REPLACE</strong></code>.<br/>
		 * The result is a complete SQL statement.
		 * @param action the conflict resolution action; see the {@link OnConflictAction} enumeration
		 * @return the <code>ALTER TABLE .. ADD COLUMN</code> statement with a <code>NOT NULL</code> constraint
		 * (and a conflict resolution option) on the added column
		 */
		public AddColumnNotNull notNullOnConflict(OnConflictAction action) {
			return new AddColumnNotNull(this, action);
		}

		/**
		 * Disallows duplicated values on the added column. The statement takes
		 * the form<br/>
		 * <code>ALTER TABLE .. ADD COLUMN .. <strong>UNIQUE</strong></code>.<br/>
		 * The result is a complete SQL statement.
		 * @return the <code>ALTER TABLE .. ADD COLUMN</code> statement with a uniqueness constraint
		 * on the added column
		 */
		public AddColumnUnique unique() {
			return new AddColumnUnique(this, null);
		}

		/**
		 * Disallows duplicated values on the added column and adds the conflict
		 * resolution clause. The statement takes the form<br/>
		 * <code>ALTER TABLE .. ADD COLUMN .. <strong>UNIQUE
		 * ON CONFLICT ROLLBACK|ABORT|FAIL|IGNORE|REPLACE</strong></code>.<br/>
		 * The result is a complete SQL statement.
		 * @param action the conflict resolution action; see the {@link OnConflictAction} enumeration
		 * @return the <code>ALTER TABLE .. ADD COLUMN</code> statement with a uniqueness constraint
		 * (and a conflict resolution option) on the added column
		 */
		public AddColumnUnique uniqueOnConflict(OnConflictAction action) {
			return new AddColumnUnique(this, action);
		}

		/**
		 * Adds a checking constraint to the column. The statement takes
		 * the form<br/>
		 * <code>ALTER TABLE .. ADD COLUMN .. <strong>CHECK <em>expression</em></strong></code>.<br/>
		 * The result is a complete SQL statement.
		 * @param condition the expression to be evaluated on execution of the
		 * <code>INSERT</code> and <code>UPDATE</code> statements against the table
		 * to check if the checked condition holds
		 * @return the <code>ALTER TABLE .. ADD COLUMN</code> statement with a checking constraint
		 * on the added column
		 */
		public AddColumnCheck check(SqlExpression condition) {
			return new AddColumnCheck(this, condition);
		}

		/**
		 * Adds a default boolean value to the column definition. The statement takes
		 * the form<br/>
		 * <code>ALTER TABLE .. ADD COLUMN .. <strong>DEFAULT TRUE|FALSE</strong></code>.<br/>
		 * The result is a complete SQL statement.<br/>
		 * <strong>Note.</strong> As older versions of SQLite do not recognize <code>TRUE</code> and <code>FALSE</code>
		 * as boolean literals, one may want to use the overloaded method {@link #defaultValue(long)} with
		 * 1 or 0 as argument to achieve the same result.
		 * @param value the default value
		 * @return the <code>ALTER TABLE .. ADD COLUMN</code> statement with a default value of either
		 * <code>TRUE</code> or <code>FALSE</code> for the added column
		 */
		public AddColumnDefault defaultValue(boolean value) {
			return defaultValue(Literal.value(value));
		}

		/**
		 * Adds a default integral value to the column definition. The statement takes
		 * the form<br/>
		 * <code>ALTER TABLE .. ADD COLUMN .. <strong>DEFAULT <em>value</em></strong></code>.<br/>
		 * The result is a complete SQL statement.
		 * @param value the default value
		 * @return the <code>ALTER TABLE .. ADD COLUMN</code> statement with a default value of integral type
		 * for the added column
		 */
		public AddColumnDefault defaultValue(long value) {
			return defaultValue(Literal.value(value));
		}

		/**
		 * Adds a default floating-point value to the column definition. The statement takes
		 * the form<br/>
		 * <code>ALTER TABLE .. ADD COLUMN .. <strong>DEFAULT <em>value</em></strong></code>.<br/>
		 * The result is a complete SQL statement.
		 * @param value the default value
		 * @return the <code>ALTER TABLE .. ADD COLUMN</code> statement with a default value of floating-point
		 * type for the added column
		 */
		public AddColumnDefault defaultValue(double value) {
			return defaultValue(Literal.value(value));
		}

		/**
		 * Adds a default numeric value to the column definition. The statement takes
		 * the form<br/>
		 * <code>ALTER TABLE .. ADD COLUMN .. <strong>DEFAULT <em>value</em></strong></code>.<br/>
		 * The result is a complete SQL statement.
		 * @param value the default value; {@literal null} is admissible
		 * @return the <code>ALTER TABLE .. ADD COLUMN</code> statement with a default numeric value
		 * (or <code>NULL</code>) for the added column
		 */
		public AddColumnDefault defaultValue(Number value) {
			return value == null ? defaultNull() : defaultValue(Literal.value(value));
		}

		/**
		 * Adds a default string value to the column definition. The statement takes
		 * the form<br/>
		 * <code>ALTER TABLE .. ADD COLUMN .. <strong>DEFAULT '<em>value</em>'</strong></code>.<br/>
		 * The result is a complete SQL statement.
		 * @param value the default value; {@literal null} is admissible
		 * @return the <code>ALTER TABLE .. ADD COLUMN</code> statement with a default textual value
		 * (or <code>NULL</code>) for the added column
		 */
		public AddColumnDefault defaultValue(String value) {
			return value == null ? defaultNull() : defaultValue(Literal.value(value));
		}

		/**
		 * Adds a default string value to the column definition. The statement takes
		 * the form<br/>
		 * <code>ALTER TABLE .. ADD COLUMN .. <strong>DEFAULT X'<em>value</em>'</strong></code><br/>
		 * where the value is in hexadecimal notation.<br/>
		 * The result is a complete SQL statement.
		 * @param value the default value; {@literal null} is admissible
		 * @return the <code>ALTER TABLE .. ADD COLUMN</code> statement with a default textual value
		 * (or <code>NULL</code>) for the added column
		 */
		public AddColumnDefault defaultValue(byte[] value) {
			return value == null ? defaultNull() : defaultValue(Literal.value(value));
		}

		/**
		 * Adds a default <code>NULL</code> value to the column definition. The statement takes
		 * the form<br/>
		 * <code>ALTER TABLE .. ADD COLUMN .. <strong>DEFAULT NULL</strong></code>.<br/>
		 * The result is a complete SQL statement.
		 * @return the <code>ALTER TABLE .. ADD COLUMN</code> statement with a default <code>NULL</code> value
		 * for the added column
		 */
		public AddColumnDefault defaultNull() {
			return defaultValue(Literal.NULL);
		}

		/**
		 * Adds a current time default to the column definition. The statement takes
		 * the form<br/>
		 * <code>ALTER TABLE .. ADD COLUMN .. <strong>DEFAULT CURRENT_TIME</strong></code>.<br/>
		 * The result is a complete SQL statement.
		 * @return the <code>ALTER TABLE .. ADD COLUMN</code> statement with the current time default
		 * for the added column
		 */
		public AddColumnDefault defaultCurrentTime() {
			return defaultValue(Literal.CURRENT_TIME);
		}

		/**
		 * Adds a current date default to the column definition. The statement takes
		 * the form<br/>
		 * <code>ALTER TABLE .. ADD COLUMN .. <strong>DEFAULT CURRENT_DATE</strong></code>.<br/>
		 * The result is a complete SQL statement.
		 * @return the <code>ALTER TABLE .. ADD COLUMN</code> statement with the current date default
		 * for the added column
		 */
		public AddColumnDefault defaultCurrentDate() {
			return defaultValue(Literal.CURRENT_DATE);
		}

		/**
		 * Adds a current timestamp default to the column definition. The statement takes
		 * the form<br/>
		 * <code>ALTER TABLE .. ADD COLUMN .. <strong>DEFAULT CURRENT_TIMESTAMP</strong></code>.<br/>
		 * The result is a complete SQL statement.
		 * @return the <code>ALTER TABLE .. ADD COLUMN</code> statement with the current timestamp default
		 * for the added column
		 */
		public AddColumnDefault defaultCurrentTimestamp() {
			return defaultValue(Literal.CURRENT_TIMESTAMP);
		}

		/**
		 * Adds a calculated default to the column definition. The statement takes
		 * the form<br/>
		 * <code>ALTER TABLE .. ADD COLUMN .. <strong>DEFAULT (<em>expression</em>)</strong></code>.<br/>
		 * The result is a complete SQL statement.
		 * @param expression the expression providing the default value of the added column
		 * @return the <code>ALTER TABLE .. ADD COLUMN</code> statement with a calculated default
		 * for the added column
		 */
		public AddColumnDefault defaultValue(SqlExpression expression) {
			return new AddColumnDefault(this, expression);
		}

		/**
		 * Adds a collation constraint to the column definition. The statement takes
		 * the form<br/>
		 * <code>ALTER TABLE .. ADD COLUMN .. <strong>COLLATE <em>collation</em></strong></code>.<br/>
		 * The result is a complete SQL statement.
		 * @param collation the collation to assign to the new column
		 * @return the <code>ALTER TABLE .. ADD COLUMN</code> statement with the specified collation
		 * for the added column
		 */
		public AddColumnCollate collate(Collation collation) {
			return new AddColumnCollate(this, collation);
		}

		/**
		 * Adds a foreign key constraint referencing the primary key of another table
		 * to the column definition. The statement takes the form<br/>
		 * <code>ALTER TABLE .. ADD COLUMN .. <strong>REFERENCES <em>foreignTableName</em></strong></code>.<br/>
		 * The result is a complete SQL statement.
		 * @param foreignTableName the name of the referenced table
		 * @return the <code>ALTER TABLE .. ADD COLUMN</code> statement with a foreign key constraint
		 * for the added column referencing the primary key of the specified table
		 */
		public AddColumnFk references(CharSequence foreignTableName) {
			return new AddColumnFk(this, foreignTableName, null);
		}

		/**
		 * Adds a foreign key constraint referencing a column of another table
		 * to the column definition. The statement takes the form<br/>
		 * <code>ALTER TABLE .. ADD COLUMN .. <strong>REFERENCES
		 * <em>foreignTableName</em>.<em>foreignColumnName</em></strong></code>.<br/>
		 * The result is a complete SQL statement.
		 * @param foreignTableName the referenced table name
		 * @param foreignColumnName the referenced column name
		 * @return the <code>ALTER TABLE .. ADD COLUMN</code> statement with a foreign key constraint
		 * for the added column referencing the specified column of some table
		 */
		public AddColumnFk references(CharSequence foreignTableName, CharSequence foreignColumnName) {
			return new AddColumnFk(this, foreignTableName, foreignColumnName);
		}
	}

	/**
	 * Represents an <code>ALTER TABLE .. ADD COLUMN</code> statement with the last
	 * column constraint being a check constraint.
	 */
	public static final class AddColumnCheck implements AddColumnStatement {

		private final AddColumnWithConstraintStub stub;

		private final SqlExpression condition;

		AddColumnCheck(AddColumnWithConstraintStub stub, SqlExpression condition) {
			this.stub = stub;
			this.condition = condition;
		}

		@Override
		public AddColumnCheck copy() throws IllegalStateException {
			AddColumnWithConstraintStub stubCopy = stub.copy();
			SqlExpression conditionCopy = condition.copy();

			return stubCopy == stub && conditionCopy == condition
				? this : new AddColumnCheck(stubCopy, conditionCopy);
		}

		@Override
		public void build(StringBuilder receptacle) {
			stub.appendTo(receptacle);
			Constraints.appendCheck(receptacle, condition);
		}
	}

	/**
	 * Represents an <code>ALTER TABLE .. ADD COLUMN</code> statement with the last
	 * column constraint being a collation constraint.
	 */
	public static final class AddColumnCollate implements AddColumnStatement {

		private final AddColumnWithConstraintStub stub;

		private final Collation collation;

		AddColumnCollate(AddColumnWithConstraintStub stub, Collation collation) {
			this.stub = stub;
			this.collation = collation;
		}

		@Override
		public AddColumnCollate copy() throws IllegalStateException {
			AddColumnWithConstraintStub stubCopy = stub.copy();

			return stubCopy == stub ? this : new AddColumnCollate(stubCopy, collation);
		}

		@Override
		public void build(StringBuilder receptacle) {
			stub.appendTo(receptacle);
			Constraints.appendCollate(receptacle, collation);
		}
	}

	/**
	 * Represents an <code>ALTER TABLE .. ADD COLUMN</code> statement with the last
	 * column constraint being a default value constraint.
	 */
	public static final class AddColumnDefault implements AddColumnStatement {

		private final AddColumnWithConstraintStub stub;

		private final SqlExpression defaultValue;

		AddColumnDefault(AddColumnWithConstraintStub stub, SqlExpression defaultValue) {
			this.stub = stub;
			this.defaultValue = defaultValue;
		}

		@Override
		public AddColumnDefault copy() throws IllegalStateException {
			AddColumnWithConstraintStub stubCopy = stub.copy();
			SqlExpression defaultValueCopy = defaultValue.copy();

			return stubCopy == stub && defaultValueCopy == defaultValue
				? this : new AddColumnDefault(stubCopy, defaultValueCopy);
		}

		@Override
		public void build(StringBuilder receptacle) {
			stub.appendTo(receptacle);
			Constraints.appendDefault(receptacle, defaultValue);
		}
	}

	/**
	 * Represents an <code>ALTER TABLE .. ADD COLUMN</code> statement with the last
	 * column constraint being a non-nullability constraint with an optional conflict resolution action.
	 */
	public static final class AddColumnNotNull implements AddColumnStatement {

		private final AddColumnWithConstraintStub stub;

		private final OnConflictAction action; // may be null

		AddColumnNotNull(AddColumnWithConstraintStub stub, OnConflictAction action) {
			this.stub = stub;
			this.action = action;
		}

		@Override
		public AddColumnNotNull copy() throws IllegalStateException {
			AddColumnWithConstraintStub stubCopy = stub.copy();

			return stubCopy == stub ? this : new AddColumnNotNull(stubCopy, action);
		}

		@Override
		public void build(StringBuilder receptacle) {
			stub.appendTo(receptacle);
			Constraints.append(receptacle, "NOT NULL", action);
		}
	}

	/**
	 * Represents an <code>ALTER TABLE .. ADD COLUMN</code> statement with the last
	 * column constraint being a primary key constraint.
	 */
	public static final class AddColumnPk implements AddColumnStatement {

		private final AddColumnWithConstraintStub stub;

		private final SortingOrder order; // may legitimately be null

		AddColumnPk(AddColumnWithConstraintStub stub, SortingOrder order) {
			this.stub = stub;
			this.order = order;
		}

		@Override
		public AddColumnPk copy() throws IllegalStateException {
			AddColumnWithConstraintStub stubCopy = stub.copy();

			return stubCopy == stub ? this : new AddColumnPk(stubCopy, order);
		}

		@Override
		public void build(StringBuilder receptacle) {
			stub.appendTo(receptacle);
			Constraints.appendPrimaryKey(receptacle, order);
		}

		/**
		 * Adds an <code>ON CONFLICT</code> clause to the primary key constraint definition.
		 * @param action the action to be taken on conflict; see {@link OnConflictAction}
		 * @return the <code>ALTER TABLE .. ADD COLUMN</code> statement with a primary key constraint
		 * for the added column, supplemented with an <code>ON CONFLICT</code> clause
		 */
		public AddColumnPkConflict onConflict(OnConflictAction action) {
			return new AddColumnPkConflict(this, action);
		}

		/**
		 * Adds an <code>AUTOINCREMENT</code> clause to the primary key constraint definition.
		 * @return the <code>ALTER TABLE .. ADD COLUMN</code> statement with a primary key constraint
		 * for the added column, supplemented with an <code>AUTOINCREMENT</code> clause
		 */
		public AddColumnPkAutoincrement autoincrement() {
			return new AddColumnPkAutoincrement(this);
		}
	}

	/**
	 * Represents an <code>ALTER TABLE .. ADD COLUMN</code> statement with the last
	 * column constraint being a primary key constraint with <code>AUTOINCREMENT</code> modifier.
	 */
	public static final class AddColumnPkAutoincrement implements AddColumnStatement {

		private final AddColumnStatement previous;

		AddColumnPkAutoincrement(AddColumnPk previous) {
			this((AddColumnStatement)previous);
		}

		AddColumnPkAutoincrement(AddColumnPkConflict previous) {
			this((AddColumnStatement)previous);
		}

		private AddColumnPkAutoincrement(AddColumnStatement previous) {
			this.previous = previous;
		}

		@Override
		public AddColumnPkAutoincrement copy() throws IllegalStateException {
			AddColumnStatement previousCopy = previous.copy();

			return previousCopy == previous ? this : new AddColumnPkAutoincrement(previousCopy);
		}

		@Override
		public void build(StringBuilder receptacle) {
			previous.build(receptacle);
			receptacle.append(" AUTOINCREMENT");
		}
	}

	/**
	 * Represents an <code>ALTER TABLE .. ADD COLUMN</code> statement with the last
	 * column constraint being a primary key constraint with <code>ON CONFLICT</code> clause.
	 */
	public static final class AddColumnPkConflict implements AddColumnStatement {

		private final AddColumnPk previous;

		private final OnConflictAction action;

		AddColumnPkConflict(AddColumnPk previous, OnConflictAction action) {
			this.previous = previous;
			this.action = action;
		}

		@Override
		public AddColumnPkConflict copy() throws IllegalStateException {
			AddColumnPk previousCopy = previous.copy();

			return previousCopy == previous ? this : new AddColumnPkConflict(previousCopy, action);
		}

		@Override
		public void build(StringBuilder receptacle) {
			previous.build(receptacle);
			Constraints.appendConflictClause(receptacle, action);
		}

		/**
		 * Adds an <code>AUTOINCREMENT</code> clause to the primary key constraint definition.
		 * @return the <code>ALTER TABLE .. ADD COLUMN</code> statement with a primary key constraint
		 * for the added column, supplemented with an <code>AUTOINCREMENT</code> clause
		 */
		public AddColumnPkAutoincrement autoincrement() {
			return new AddColumnPkAutoincrement(this);
		}
	}

	/**
	 * Represents an <code>ALTER TABLE .. ADD COLUMN</code> statement where the new column
	 * has neither declared type nor constraints.
	 */
	public static final class AddColumnSimpleStatement implements AddColumnStatement {

		private final CharSequence schemaName;

		private final CharSequence tableName;

		private final CharSequence columnName;

		AddColumnSimpleStatement(CharSequence schemaName, CharSequence tableName, CharSequence columnName) {
			this.schemaName = schemaName;
			this.tableName = tableName;
			this.columnName = columnName;
		}

		@Override
		public AddColumnSimpleStatement copy() throws IllegalStateException {
			CharSequence schemaNameCopy = schemaName == null ? null : schemaName.toString();
			CharSequence tableNameCopy = tableName.toString();
			CharSequence columnNameCopy = columnName.toString();

			return schemaNameCopy == schemaName && tableNameCopy == tableName && columnNameCopy == columnName
				? this : new AddColumnSimpleStatement(schemaNameCopy, tableNameCopy, columnNameCopy);
		}

		@Override
		public void build(StringBuilder receptacle) {
			receptacle.append("ALTER TABLE ");
			SqliteUtilities.appendQuotedName(receptacle, schemaName, tableName);
			receptacle.append(" ADD COLUMN ");
			SqliteUtilities.appendQuotedName(receptacle, columnName);
		}

		/**
		 * Adds a type definition to the new column. The type definition may have optional precision part.
		 * @param typeDefinition the type definition; SQLite allows any string to be used as type name
		 * provided it is quoted if necessary; if the type definition ends with a substring recognizable
		 * as precision part, this part will not be quoted
		 * @return the <code>ALTER TABLE .. ADD COLUMN</code> statement ending with the type definition
		 * of the new column
		 */
		public AddTypedColumnStatement ofType(String typeDefinition) {
			return new AddTypedColumnStatement(this, typeDefinition);
		}
	}

	/**
	 * Represents an <code>ALTER TABLE .. ADD COLUMN</code> statement with the last
	 * column constraint being a uniqueness constraint with an optional <code>ON CONFLICT</code> clause.
	 */
	public static final class AddColumnUnique implements AddColumnStatement {

		private final AddColumnWithConstraintStub stub;

		private final OnConflictAction action; // may be null

		AddColumnUnique(AddColumnWithConstraintStub stub, OnConflictAction action) {
			this.stub = stub;
			this.action = action;
		}

		@Override
		public AddColumnUnique copy() throws IllegalStateException {
			AddColumnWithConstraintStub stubCopy = stub.copy();

			return stubCopy == stub ? this : new AddColumnUnique(stubCopy, action);
		}

		@Override
		public void build(StringBuilder receptacle) {
			stub.appendTo(receptacle);
			Constraints.append(receptacle, "UNIQUE", action);
		}
	}

	/**
	 * Represents an <code>ALTER TABLE .. ADD COLUMN</code> statement having a column type defined
	 * and no column constraints.
	 */
	public static final class AddTypedColumnStatement implements AddColumnStatement {

		private final AddColumnStatement previous;

		private final CharSequence typeDefinition;

		AddTypedColumnStatement(AddColumnStatement previous, CharSequence typeDefinition) {
			this.previous = previous;
			this.typeDefinition = typeDefinition;
		}

		@Override
		public AddColumnStatement copy() throws IllegalStateException {
			AddColumnStatement previousCopy = previous.copy();
			CharSequence typeDefinitionCopy = typeDefinition.toString();

			return previousCopy == previous && typeDefinitionCopy == typeDefinition
				? this : new AddTypedColumnStatement(previousCopy, typeDefinitionCopy);
		}

		@Override
		public void build(StringBuilder receptacle) {
			previous.build(receptacle);
			receptacle.append(' ');
			SqliteUtilities.quoteType(receptacle, typeDefinition);
		}
	}

	/**
	 * Represents an <code>ALTER TABLE .. ADD COLUMN</code> statement with a foreign key
	 * constraint to which the following clauses may be appended:
	 * <ul>
	 *     <li><code>DEFERRABLE INITIALLY DEFERRED</code> or <code>NOT DEFERRABLE INITIALLY IMMEDIATE</code></li>
	 *     <li><code>ON DELETE NO ACTION|RESTRICT|CASCADE|SET DEFAULT|SET NULL</code></li>
	 *     <li><code>ON UPDATE NO ACTION|RESTRICT|CASCADE|SET DEFAULT|SET NULL</code>.</li>
	 * </ul>
	 */
	public interface AddColumnFkDeferrable extends AddColumnStatement {

		@Override
		AddColumnFkDeferrable copy() throws IllegalStateException;

		/**
		 * Adds a <code>DEFERRABLE INITIALLY DEFERRED</code> clause to the foreign key constraint
		 * of the added column.
		 * @return the <code>ALTER TABLE .. ADD COLUMN</code> statement with a foreign key constraint
		 * for the added column, supplemented with a <code>DEFERRABLE INITIALLY DEFERRED</code> clause
		 */
		default AddColumnFkTiming deferred() {
			return new AddColumnFkTiming(this, true);
		}

		/**
		 * Adds a <code>NOT DEFERRABLE INITIALLY IMMEDIATE</code> clause to the foreign key constraint
		 * of the added column.
		 * @return the <code>ALTER TABLE .. ADD COLUMN</code> statement with a foreign key constraint
		 * for the added column, supplemented with a <code>NOT DEFERRABLE INITIALLY IMMEDIATE</code> clause
		 */
		default AddColumnFkTiming immediate() {
			return new AddColumnFkTiming(this, false);
		}

		/**
		 * Adds an <code>ON DELETE</code> clause to the foreign key constraint of the added column.
		 * @param action the action to take when the foreign row is deleted; see {@link ForeignKeyAction}
		 * @return the <code>ALTER TABLE .. ADD COLUMN</code> statement with a foreign key constraint
		 * for the added column, supplemented with an <code>ON DELETE</code> clause
		 */
		default AddColumnFkAction onDelete(ForeignKeyAction action) {
			return new AddColumnFkAction(this, true, action);
		}

		/**
		 * Adds an <code>ON UPDATE</code> clause to the foreign key constraint of the added column.
		 * @param action the action to take when the foreign row is updated; see {@link ForeignKeyAction}
		 * @return the <code>ALTER TABLE .. ADD COLUMN</code> statement with a foreign key constraint
		 * for the added column, supplemented with an <code>ON UPDATE</code> clause
		 */
		default AddColumnFkAction onUpdate(ForeignKeyAction action) {
			return new AddColumnFkAction(this, false, action);
		}
	}

	/**
	 * Represents an <code>ALTER TABLE .. ADD COLUMN ..
	 * <strong>REFERENCES <em>tableName</em>[.<em>columnName</em>]</strong></code> statement.
	 */
	public static final class AddColumnFk implements AddColumnFkDeferrable {

		private final AddColumnWithConstraintStub stub;

		private final CharSequence tableName;

		private final CharSequence columnName; // may be null

		AddColumnFk(AddColumnWithConstraintStub stub, CharSequence tableName, CharSequence columnName) {
			this.stub = stub;
			this.tableName = tableName;
			this.columnName = columnName;
		}

		@Override
		public AddColumnFk copy() throws IllegalStateException {
			AddColumnWithConstraintStub stubCopy = stub.copy();
			CharSequence tableNameCopy = tableName.toString();
			CharSequence columnNameCopy = columnName == null ? null : columnName.toString();

			return stubCopy == stub && tableNameCopy == tableName && columnNameCopy == columnName
				? this : new AddColumnFk(stubCopy, tableNameCopy, columnNameCopy);
		}

		@Override
		public void build(StringBuilder receptacle) {
			stub.appendTo(receptacle);
			Constraints.appendForeignKey(receptacle, tableName, columnName);
		}
	}

	/**
	 * Represents an <code>ALTER TABLE .. ADD COLUMN</code> statement with a foreign key on
	 * the added column with an <code><strong>ON DELETE</strong></code> or <code><strong>ON UPDATE</strong></code>
	 * clause.
	 */
	public static final class AddColumnFkAction implements AddColumnFkDeferrable {

		private final AddColumnFkDeferrable previous;

		private final boolean isDeleteAction;

		private final ForeignKeyAction action;

		AddColumnFkAction(
			AddColumnFkDeferrable previous, boolean isDeleteAction, ForeignKeyAction action) {

			this.previous = previous;
			this.isDeleteAction = isDeleteAction;
			this.action = action;
		}

		@Override
		public AddColumnFkAction copy() throws IllegalStateException {
			AddColumnFkDeferrable previousCopy = previous.copy();

			return previousCopy == previous
				? this : new AddColumnFkAction(previousCopy, isDeleteAction, action);
		}

		@Override
		public void build(StringBuilder receptacle) {
			previous.build(receptacle);
			Constraints.appendReferentialAction(receptacle, isDeleteAction, action);
		}
	}

	/**
	 * Represents an <code>ALTER TABLE .. ADD COLUMN</code> statement with a foreign key on
	 * the added column with a <code><strong>DEFERRABLE INITIALLY DEFERRED</strong></code>
	 * or <code><strong>NOT DEFERRABLE INITIALLY IMMEDIATE</strong></code>
	 * clause.
	 */
	public static final class AddColumnFkTiming implements AddColumnStatement {

		private final AddColumnFkDeferrable previous;

		private final boolean deferred;

		AddColumnFkTiming(AddColumnFkDeferrable previous, boolean deferred) {
			this.previous = previous;
			this.deferred = deferred;
		}

		@Override
		public AddColumnFkTiming copy() throws IllegalStateException {
			AddColumnFkDeferrable previousCopy = previous.copy();

			return previousCopy == previous ? this : new AddColumnFkTiming(previousCopy, deferred);
		}

		@Override
		public void build(StringBuilder receptacle) {
			previous.build(receptacle);
			Constraints.appendForeignKeyTiming(receptacle, deferred);
		}
	}

	private AlterTable() { }
}
