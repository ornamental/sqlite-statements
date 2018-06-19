package dev.ornamental.sqlite.statement;

/**
 * Defines the moment, relative to the triggering event, when the trigger is executed.
 */
public enum TriggerTiming {

	/**
	 * The trigger is executed just before the event.
	 */
	BEFORE("BEFORE"),

	/**
	 * The trigger is executed right after the event.
	 */
	AFTER("AFTER"),

	/**
	 * The trigger is executed instead of the triggering modification.
	 */
	INSTEAD_OF("INSTEAD OF");

	private final String keyword;

	TriggerTiming(String keyword) {
		this.keyword = keyword;
	}

	@Override
	public String toString() {
		return keyword;
	}
}
