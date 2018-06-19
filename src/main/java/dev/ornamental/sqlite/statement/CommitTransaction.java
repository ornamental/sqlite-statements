package dev.ornamental.sqlite.statement;

public final class CommitTransaction implements ExplicableStatement {

	static final ExplicableStatement INSTANCE = new CommitTransaction();

	private CommitTransaction() { }

	@Override
	public ExplicableStatement copy() throws IllegalStateException {
		return this;
	}

	@Override
	public void build(StringBuilder receptacle) {
		receptacle.append("COMMIT TRANSACTION");
	}
}
