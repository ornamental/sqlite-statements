package dev.ornamental.sqlite.statement;

/**
 * Marker interface for statements which may be included in a trigger body.
 * These are <code>SELECT</code>, <code>INSERT</code>, <code>DELETE</code>, and <code>UPDATE</code>
 * statements (including their flavours such as, for instance, <code>REPLACE</code> or
 * <code>UPDATE OR IGNORE</code> statements).
 */
public interface TriggerStatement extends ExplicableStatement {

	@Override
	TriggerStatement copy();
}
