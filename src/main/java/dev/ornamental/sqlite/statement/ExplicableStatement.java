package dev.ornamental.sqlite.statement;

/**
 * Marker interface for the classes representing the statements which can be
 * prepended with <code>EXPLAIN</code> and <code>EXPLAIN QUERY PLAN</code>.
 */
public interface ExplicableStatement extends SqlStatement {

	@Override
	ExplicableStatement copy() throws IllegalStateException;
}
