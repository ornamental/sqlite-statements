package dev.ornamental.sqlite.statement;

import java.util.Arrays;

/**
 * Represents a complete <code>CREATE TRIGGER</code> statement.
 */
public final class CreateTrigger implements ExplicableStatement {

	/**
	 * Represents the initial stage of construction of a <code>CREATE TRIGGER</code> statement.
	 * Contains the trigger name and optional schema name as well as the flags indicating
	 * if the trigger will be temporary and if its existence must be checked before the creation.
	 * The corresponding part of the statement is<br>
	 * <code><strong>CREATE [TEMPORARY] TRIGGER [IF NOT EXISTS]
	 * [<em>schemaName</em>.]<em>triggerName</em></strong></code>.<br>
	 * Note that though SQLite allows not specifying the trigger timing using one of the keywords
	 * <code>BEFORE</code>, <code>AFTER</code>, or <code>INSTEAD OF</code>, defaulting
	 * to the <code>BEFORE</code> case, this is not supported here for the sake of class hierarchy
	 * simplicity and output statement readability; thus one would have to explicitly use
	 * the {@link #before()} method.
	 */
	public static final class Stub {

		private final boolean temporary;

		private final boolean ifNotExists;

		private final CharSequence schemaName; // may be null

		private final CharSequence triggerName;

		Stub(boolean temporary, boolean ifNotExists, CharSequence schemaName, CharSequence triggerName) {
			this.temporary = temporary;
			this.ifNotExists = ifNotExists;
			this.schemaName = schemaName;
			this.triggerName = triggerName;
		}

		/**
		 * Specifies that the trigger must be executed before the triggering changes are made.
		 * @return the initial part of a <code>CREATE TRIGGER</code> statement having the form<br>
		 * <code>CREATE [TEMPORARY] TRIGGER [IF NOT EXISTS]
		 * [<em>schemaName</em>.]<em>triggerName</em> <strong>BEFORE</strong></code>
		 */
		public WithTiming before() {
			return new WithTiming(this, TriggerTiming.BEFORE);
		}

		/**
		 * Specifies that the trigger must be executed after the triggering changes are made.
		 * @return the initial part of a <code>CREATE TRIGGER</code> statement having the form<br>
		 * <code>CREATE [TEMPORARY] TRIGGER [IF NOT EXISTS]
		 * [<em>schemaName</em>.]<em>triggerName</em> <strong>AFTER</strong></code>
		 */
		public WithTiming after() {
			return new WithTiming(this, TriggerTiming.AFTER);
		}

		/**
		 * Specifies that the trigger must be executed instead of making the triggering changes.
		 * @return the initial part of a <code>CREATE TRIGGER</code> statement having the form<br>
		 * <code>CREATE [TEMPORARY] TRIGGER [IF NOT EXISTS]
		 * [<em>schemaName</em>.]<em>triggerName</em> <strong>INSTEAD OF</strong></code>
		 */
		public WithTiming insteadOf() {
			return new WithTiming(this, TriggerTiming.INSTEAD_OF);
		}

		void appendTo(StringBuilder receptacle) {
			receptacle.append("CREATE ");
			if (temporary) {
				receptacle.append("TEMPORARY ");
			}
			receptacle.append("TRIGGER ");
			if (ifNotExists) {
				receptacle.append("IF NOT EXISTS ");
			}
			SqliteUtilities.appendQuotedName(receptacle, schemaName, triggerName);
		}

		Stub copy() {
			CharSequence schemaNameCopy = schemaName == null ? null : schemaName.toString();
			CharSequence triggerNameCopy = triggerName.toString();

			return schemaNameCopy == schemaName && triggerNameCopy == triggerName
				? this : new Stub(temporary, ifNotExists, schemaNameCopy, triggerNameCopy);
		}
	}

	/**
	 * Represents the initial part of a <code>CREATE TRIGGER</code> statement, namely<br>
	 * <code><strong>CREATE [TEMPORARY] TRIGGER [IF NOT EXISTS]
	 * [<em>schemaName</em>.]<em>triggerName</em> BEFORE|AFTER|INSTEAD OF</strong></code>.
	 */
	public static final class WithTiming {

		private final Stub previous;

		private final TriggerTiming timing;

		WithTiming(Stub previous, TriggerTiming timing) {
			this.previous = previous;
			this.timing = timing;
		}

		/**
		 * Specifies that the triggering change is row deletion.
		 * @return the initial part of a <code>CREATE TRIGGER</code> statement having the form<br>
		 * <code>CREATE [TEMPORARY] TRIGGER [IF NOT EXISTS]
		 * [<em>schemaName</em>.]<em>triggerName</em> BEFORE|AFTER|INSTEAD OF <strong>DELETE</strong></code>
		 */
		public OnEvent delete() {
			return new OnEvent(this, TriggerEvent.DELETE);
		}

		/**
		 * Specifies that the triggering change is row insertion.
		 * @return the initial part of a <code>CREATE TRIGGER</code> statement having the form<br>
		 * <code>CREATE [TEMPORARY] TRIGGER [IF NOT EXISTS]
		 * [<em>schemaName</em>.]<em>triggerName</em> BEFORE|AFTER|INSTEAD OF <strong>INSERT</strong></code>
		 */
		public OnEvent insert() {
			return new OnEvent(this, TriggerEvent.INSERT);
		}

		/**
		 * Specifies that the triggering change is row update.
		 * @return the initial part of a <code>CREATE TRIGGER</code> statement having the form<br>
		 * <code>CREATE [TEMPORARY] TRIGGER [IF NOT EXISTS]
		 * [<em>schemaName</em>.]<em>triggerName</em> BEFORE|AFTER|INSTEAD OF <strong>UPDATE</strong></code>
		 */
		public OnEvent update() {
			return new OnEvent(this, TriggerEvent.UPDATE);
		}

		/**
		 * Specifies that the triggering change is row update concerning at least one of the columns
		 * named as this method's parameters.
		 * @param columns the names of table columns whose update will cause the trigger to fire
		 * @return the initial part of a <code>CREATE TRIGGER</code> statement having the form<br>
		 * <code>CREATE [TEMPORARY] TRIGGER [IF NOT EXISTS]
		 * [<em>schemaName</em>.]<em>triggerName</em> BEFORE|AFTER|INSTEAD OF
		 * <strong>UPDATE OF <em>columnName<sub>0</sub></em>{, <em>columnName<sub>i</sub></em>}</strong></code>
		 */
		public OnEvent updateOf(CharSequence... columns) {
			return updateOf(Arrays.asList(columns));
		}

		/**
		 * Specifies that the triggering change is row update concerning at least one of the columns
		 * named as this method's parameters.
		 * @param columns the names of table columns whose update will cause the trigger to fire
		 * @return the initial part of a <code>CREATE TRIGGER</code> statement having the form<br>
		 * <code>CREATE [TEMPORARY] TRIGGER [IF NOT EXISTS]
		 * [<em>schemaName</em>.]<em>triggerName</em> BEFORE|AFTER|INSTEAD OF
		 * <strong>UPDATE OF <em>columnName<sub>0</sub></em>{, <em>columnName<sub>i</sub></em>}</strong></code>
		 */
		public OnEvent updateOf(Iterable<? extends CharSequence> columns) {
			return new OnUpdateOf(this, TriggerEvent.UPDATE, columns);
		}

		void appendTo(StringBuilder receptacle) {
			previous.appendTo(receptacle);
			receptacle.append(' ').append(timing.toString());
		}

		WithTiming copy() {
			Stub previousCopy = previous.copy();

			return previousCopy == previous ? this : new WithTiming(previousCopy, timing);
		}
	}

	/**
	 * Represents the initial part of a <code>CREATE TRIGGER</code> with specified
	 * triggering event type and firing timing but no target table name yet.
	 */
	public static class OnEvent {

		protected final WithTiming previous;

		protected final TriggerEvent event;

		OnEvent(WithTiming previous, TriggerEvent event) {
			this.previous = previous;
			this.event = event;
		}

		/**
		 * Specifies the table whose modification is meant to fire the trigger.
		 * @param tableName the name of the table watched by the trigger
		 * @return the initial part of a <code>CREATE TRIGGER</code> statement ending with
		 * an <code>ON <em>tableName</em></code> clause
		 */
		public Targeted on(CharSequence tableName) {
			return new Targeted(this, null, tableName);
		}

		/**
		 * Specifies the table whose modification is meant to fire the trigger.
		 * @param schemaName the name of the schema containing the target table
		 * @param tableName the name of the table watched by the trigger
		 * @return the initial part of a <code>CREATE TRIGGER</code> statement ending with
		 * an <code>ON <em>schemaName</em>.<em>tableName</em></code> clause
		 */
		public Targeted on(CharSequence schemaName, CharSequence tableName) {
			if (!previous.previous.temporary) {
				throw new IllegalArgumentException(
					"Schema name must not be specified unless the trigger is temporary.");
			}

			return new Targeted(this, schemaName, tableName);
		}

		void appendTo(StringBuilder receptacle) {
			previous.appendTo(receptacle);
			receptacle.append(' ').append(event.toString());
		}

		OnEvent copy() {
			WithTiming previousCopy = previous.copy();

			return previousCopy == previous ? this : new OnEvent(previousCopy, event);
		}
	}

	/**
	 * Represents the initial part of a <code>CREATE TRIGGER</code> statement with <code>ON UPDATE OF</code>
	 * triggering event:<br>
	 * <code><strong>CREATE [TEMPORARY] TRIGGER [IF NOT EXISTS]
	 * [<em>schemaName</em>.]<em>triggerName</em> BEFORE|AFTER|INSTEAD OF
	 *UPDATE OF <em>columnName<sub>0</sub></em>{, <em>columnName<sub>i</sub></em>}</strong></code>.
	 */
	public static class OnUpdateOf extends OnEvent {

		private final Iterable<? extends CharSequence> columns;

		OnUpdateOf(
			WithTiming previous, TriggerEvent event, Iterable<? extends CharSequence> columns) {

			super(previous, event);
			this.columns = columns;
		}

		@Override
		void appendTo(StringBuilder receptacle) {
			super.appendTo(receptacle);
			receptacle.append(" OF ");
			SqliteUtilities.appendQuotedDelimited(receptacle, columns);
		}

		@Override
		OnEvent copy() {
			WithTiming previousCopy = previous.copy();
			Iterable<? extends CharSequence> columnsCopy =
				ReadonlyIterable.of(columns, CharSequence::toString);

			return previousCopy == previous && columnsCopy == columns
				? this : new OnUpdateOf(previousCopy, event, columnsCopy);
		}
	}

	/**
	 * Represents the initial part of a <code>CREATE TRIGGER</code> ending with
	 * an <code>ON [<em>schemaName</em>.]<em>tableName</em></code> clause.
	 */
	public static final class Targeted {

		private final OnEvent previous;

		private final CharSequence schemaName;

		private final CharSequence tableName;

		Targeted(OnEvent previous, CharSequence schemaName, CharSequence tableName) {
			this.previous = previous;
			this.schemaName = schemaName;
			this.tableName = tableName;
		}

		/**
		 * Adds a trigger condition (<code>WHEN</code>) clause to the incomplete trigger definition.
		 * @param conditionExpression the condition to hold for a row in order for the trigger to fire
		 * @return the initial part of the trigger definition ending with a
		 * <code>WHEN <em>condtionExpression</em></code> clause.
		 */
		public WithCondition when(SqlExpression conditionExpression) {
			return new WithCondition(this, conditionExpression);
		}

		/**
		 * Adds a trigger body (the statement sequence to be executed if the trigger fires).<br>
		 * The result is a complete SQL statement.
		 * @param statements the sequence of statements to be executed by the trigger
		 * @return the <code>CREATE TRIGGER</code> statement with the supplied statements comprising its body
		 */
		public CreateTrigger execute(TriggerStatement... statements) {
			return execute(Arrays.asList(statements));
		}

		/**
		 * Adds a trigger body (the statement sequence to be executed if the trigger fires).<br>
		 * The result is a complete SQL statement.
		 * @param statements the sequence of statements to be executed by the trigger
		 * @return the <code>CREATE TRIGGER</code> statement with the supplied statements comprising its body
		 */
		public CreateTrigger execute(Iterable<? extends TriggerStatement> statements) {
			return new CreateTrigger(this, statements);
		}

		void appendTo(StringBuilder receptacle) {
			previous.appendTo(receptacle);
			receptacle.append(" ON ");
			SqliteUtilities.appendQuotedName(receptacle, schemaName, tableName);
		}

		Targeted copy() {
			OnEvent previousCopy = previous.copy();
			CharSequence schemaNameCopy = schemaName == null ? null : schemaName.toString();
			CharSequence tableNameCopy = tableName.toString();

			return previousCopy == previous && schemaNameCopy == schemaName && tableNameCopy == tableName
				? this : new Targeted(previousCopy, schemaNameCopy, tableNameCopy);
		}
	}

	/**
	 * Represents an incomplete trigger definition ending with a <code>WHEN
	 * <em>conditionExpression</em></code> clause.
	 */
	public static final class WithCondition {

		private final Targeted previous;

		private final SqlExpression condition;

		WithCondition(Targeted previous, SqlExpression condition) {
			this.previous = previous;
			this.condition = condition;
		}

		/**
		 * Adds a trigger body (the statement sequence to be executed if the trigger fires).<br>
		 * The result is a complete SQL statement.
		 * @param statements the sequence of statements to be executed by the trigger
		 * @return the <code>CREATE TRIGGER</code> statement with the supplied statements comprising its body
		 */
		public CreateTrigger execute(TriggerStatement... statements) {
			return execute(Arrays.asList(statements));
		}

		/**
		 * Adds a trigger body (the statement sequence to be executed if the trigger fires).<br>
		 * The result is a complete SQL statement.
		 * @param statements the sequence of statements to be executed by the trigger
		 * @return the <code>CREATE TRIGGER</code> statement with the supplied statements comprising its body
		 */
		public CreateTrigger execute(Iterable<? extends TriggerStatement> statements) {
			return new CreateTrigger(this, statements);
		}

		void appendTo(StringBuilder receptacle) {
			previous.appendTo(receptacle);
			receptacle.append(" WHEN ");
			condition.appendTo(receptacle);
		}

		WithCondition copy() {
			Targeted previousCopy = previous.copy();
			SqlExpression conditionCopy = condition.copy();

			return previousCopy == previous && conditionCopy == condition
				? this : new WithCondition(previousCopy, conditionCopy);
		}
	}

	private final Targeted previousUnconditional;

	private final WithCondition previousConditional;

	private final Iterable<? extends TriggerStatement> statements;

	CreateTrigger(Targeted previous, Iterable<? extends TriggerStatement> statements) {
		this(previous, null, statements);
	}

	CreateTrigger(WithCondition previous, Iterable<? extends TriggerStatement> statements) {
		this(null, previous, statements);
	}

	private CreateTrigger(
		Targeted previousUnconditional, WithCondition previousConditional,
		Iterable<? extends TriggerStatement> statements) {

		this.previousUnconditional = previousUnconditional;
		this.previousConditional = previousConditional;
		this.statements = statements;
	}

	@Override
	public CreateTrigger copy() throws IllegalStateException {
		Targeted previousUnconditionalCopy =
			previousUnconditional == null ? null : previousUnconditional.copy();
		WithCondition previousConditionalCopy =
			previousConditional == null ? null : previousConditional.copy();
		Iterable<? extends TriggerStatement> statementsCopy =
			ReadonlyIterable.of(statements, TriggerStatement::copy);

		return previousUnconditionalCopy == previousUnconditional
			&& previousConditionalCopy == previousConditional && statementsCopy == statements
				? this
				: new CreateTrigger(previousUnconditionalCopy, previousConditionalCopy, statementsCopy);
	}

	@Override
	public void build(StringBuilder receptacle) {
		if (previousUnconditional != null) {
			previousUnconditional.appendTo(receptacle);
		} else {
			previousConditional.appendTo(receptacle);
		}

		receptacle.append(" BEGIN ");
		statements.forEach(s -> {
			s.build(receptacle);
			receptacle.append("; ");
		});
		receptacle.append("END");
	}
}
