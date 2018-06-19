package dev.ornamental.sqlite.statement;

import java.util.Arrays;
import java.util.Iterator;

/**
 * This class has no functionality of its own. It is only destined for grouping the classes pertaining
 * to <code>CREATE VIRTUAL TABLE</code> statements. The statement flavours themselves are represented
 * by the {@link NoArguments} and {@link WithArguments} nested classes.
 */
public final class CreateVirtualTable {

	/**
	 * Represents the initial stage of defining a <code>CREATE VIRTUAL TABLE</code> statement.
	 * It has the form<br/>
	 * <code><strong>CREATE VIRTUAL TABLE [IF NOT EXISTS]
	 * [<em>schemaName</em>.]<em>tableName</em></strong></code>.
	 */
	public static final class Stub {

		private final boolean ifNotExists;

		private final CharSequence schemaName; // nullable

		private final CharSequence tableName;

		Stub(boolean ifNotExists, CharSequence schemaName, CharSequence tableName) {
			this.ifNotExists = ifNotExists;
			this.schemaName = schemaName;
			this.tableName = tableName;
		}

		/**
		 * Adds the name of the module controlling the behaviour of the virtual table to be created.<br/>
		 * The result is a complete SQL statement.
		 * @param moduleName the name of the module operating behind the virtual table
		 * @return the <code>CREATE VIRTUAL TABLE</code> statement without module arguments:<br/>
		 * <code>CREATE VIRTUAL TABLE [IF NOT EXISTS]
		 * [<em>schemaName</em>.]<em>tableName</em> <strong>USING <em>moduleName</em></strong></code>
		 */
		public NoArguments using(CharSequence moduleName) {
			return new NoArguments(this, moduleName);
		}

		Stub copy() {
			CharSequence schemaNameCopy = schemaName == null ? null : schemaName.toString();
			CharSequence tableNameCopy = tableName.toString();

			return schemaNameCopy == schemaName && tableNameCopy == tableName
				? this : new Stub(ifNotExists, schemaNameCopy, tableNameCopy);
		}

		void appendTo(StringBuilder receptacle) {
			receptacle.append("CREATE VIRTUAL TABLE ");
			if (ifNotExists) {
				receptacle.append("IF NOT EXISTS ");
			}
			SqliteUtilities.appendQuotedName(receptacle, schemaName, tableName);
		}
	}

	/**
	 * Represents a <code>CREATE VIRTUAL TABLE</code> statement without module arguments:<br/>
	 * <code><strong>CREATE VIRTUAL TABLE [IF NOT EXISTS]
	 * [<em>schemaName</em>.]<em>tableName</em> USING <em>moduleName</em></strong></code>.<br/>
	 * This is a complete SQL statement.
	 */
	public static final class NoArguments implements ExplicableStatement {

		private final Stub stub;

		private final CharSequence moduleName;

		NoArguments(Stub stub, CharSequence moduleName) {
			this.stub = stub;
			this.moduleName = moduleName;
		}

		/**
		 * Adds the arguments to pass to the module controlling the behaviour of the virtual table.<br/>
		 * The result is a complete SQL statement.
		 * @param args the arguments to pass to the module behind the virtual table (e. g. the virtual table
		 * column names)
		 * @return the most complete form of the <code>CREATE VIRTUAL TABLE</code> statement:<br/>
		 * <code>CREATE VIRTUAL TABLE [IF NOT EXISTS]
		 * [<em>schemaName</em>.]<em>tableName</em> USING
		 * <em>moduleName</em><strong>(<em>argument<sub>0</sub></em>{, <em>argument<sub>i</sub></em>})</strong></code>
		 */
		public WithArguments withArguments(CharSequence... args) {
			return withArguments(Arrays.asList(args));
		}

		/**
		 * Adds the arguments to pass to the module controlling the behaviour of the virtual table.<br/>
		 * The result is a complete SQL statement.
		 * @param args the arguments to pass to the module behind the virtual table (e. g. the virtual table
		 * column names)
		 * @return the most complete form of the <code>CREATE VIRTUAL TABLE</code> statement:<br/>
		 * <code>CREATE VIRTUAL TABLE [IF NOT EXISTS]
		 * [<em>schemaName</em>.]<em>tableName</em> USING
		 * <em>moduleName</em><strong>(<em>argument<sub>0</sub></em>{, <em>argument<sub>i</sub></em>})</strong></code>
		 */
		public WithArguments withArguments(Iterable<? extends CharSequence> args) {
			return new WithArguments(this, args);
		}

		@Override
		public NoArguments copy() throws IllegalStateException {
			Stub stubCopy = stub.copy();
			CharSequence moduleNameCopy = moduleName.toString();

			return stubCopy == stub && moduleNameCopy == moduleName
				? this : new NoArguments(stubCopy, moduleNameCopy);
		}

		@Override
		public void build(StringBuilder receptacle) {
			stub.appendTo(receptacle);
			receptacle.append(" USING ");
			SqliteUtilities.quoteNameIfNecessary(receptacle, moduleName);
		}
	}

	/**
	 * Represents the most complete form of the <code>CREATE VIRTUAL TABLE</code> statement:<br/>
	 * <code><strong>CREATE VIRTUAL TABLE [IF NOT EXISTS]
	 * [<em>schemaName</em>.]<em>tableName</em> USING
	 * <em>moduleName</em>(<em>argument<sub>0</sub></em>{, <em>argument<sub>i</sub></em>})</strong></code>.<br/>
	 * This is a complete SQL statement.
	 */
	public static final class WithArguments implements ExplicableStatement {

		private final NoArguments previous;

		private final Iterable<? extends CharSequence> moduleArguments;

		WithArguments(NoArguments previous, Iterable<? extends CharSequence> moduleArguments) {
			this.previous = previous;
			this.moduleArguments = moduleArguments;
		}

		@Override
		public WithArguments copy() throws IllegalStateException {
			NoArguments previousCopy = previous.copy();
			Iterable<? extends CharSequence> moduleArgumentsCopy =
				ReadonlyIterable.of(moduleArguments, CharSequence::toString);

			return previousCopy == previous && moduleArgumentsCopy == moduleArguments
				? this : new WithArguments(previousCopy, moduleArgumentsCopy);
		}

		@Override
		public void build(StringBuilder receptacle) {
			Iterator<? extends CharSequence> iterator = moduleArguments.iterator();
			if (!iterator.hasNext()) {
				throw new IllegalStateException("The module argument list must not be empty.");
			}

			previous.build(receptacle);
			receptacle.append('(');
			CharSequence last = iterator.next();
			while (iterator.hasNext()) {
				receptacle.append(last).append(", ");
				last = iterator.next();
			}
			receptacle.append(last).append(')');
		}
	}

	private CreateVirtualTable() { }
}
