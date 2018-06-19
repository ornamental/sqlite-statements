package dev.ornamental.sqlite.statement;

/**
 * Defines the events leading to a trigger being fired.
 */
public enum TriggerEvent {

	/**
	 * The row deletion event
	 */
	DELETE,

	/**
	 * The row deletion event
	 */
	INSERT,

	/**
	 * The row modification event
	 */
	UPDATE
}
