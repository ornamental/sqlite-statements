package dev.ornamental.sqlite.statement;

/**
 * Represents the transaction type in what concerns database locking timing.
 */
public enum TransactionType {

	/**
	 * The transaction acquires no locks until the database is first accessed.
	 * The first read operation against a database creates a shared lock
	 * and the first write operation creates a reserved lock.
	 */
	DEFERRED,

	/**
	 * Reserved locks are acquired on all the attached databases as soon as the transaction begins.
	 */
	IMMEDIATE,

	/**
	 * Exclusive locks are acquired on all the attached databases as soon as the transaction begins.
	 */
	EXCLUSIVE;

	private BeginTransaction beginStatement;

	TransactionType() {
		this.beginStatement = new BeginTransaction(this);
	}

	/**
	 * Returns the statement beginning a transaction of this type.
	 * @return the statement of the form<br>
	 * <code><strong>BEGIN DEFERRED|IMMEDIATE|EXCLUSIVE TRANSACTION</strong></code><br>
	 * where the transaction type is defined by this enum element.
	 */
	public BeginTransaction beginStatement() {
		return beginStatement;
	}
}
