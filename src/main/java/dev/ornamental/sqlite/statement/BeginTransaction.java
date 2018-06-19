package dev.ornamental.sqlite.statement;

/**
 * Represents a <code><strong>BEGIN [DEFERRED|IMMEDIATE|EXCLUSIVE] TRANSACTION </strong></code> statement.
 */
public final class BeginTransaction implements ExplicableStatement {

	static final BeginTransaction DEFAULT = new BeginTransaction(null);

	private final TransactionType type; // may be null

	BeginTransaction(TransactionType type) {
		this.type = type;
	}

	@Override
	public BeginTransaction copy() throws IllegalStateException {
		return this;
	}

	@Override
	public void build(StringBuilder receptacle) {
		receptacle.append("BEGIN ");
		if (type != null) {
			receptacle.append(type.toString()).append(' ');
		}
		receptacle.append("TRANSACTION");
	}
}
