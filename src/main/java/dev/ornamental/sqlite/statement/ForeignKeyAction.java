package dev.ornamental.sqlite.statement;

/**
 * Defines possible actions on the referencing row taken when the row referenced by a foreign key constraint
 * is deleted or updated.
 */
public enum ForeignKeyAction {

	/**
	 * No action is taken.
	 */
	NO_ACTION("NO ACTION"),

	/**
	 * The statement causing the referenced row deletion or modification fails.
	 */
	RESTRICT("RESTRICT"),

	/**
	 * The deletion of the referenced row causes the deletion of the referencing row;
	 * the modification of the referenced column(s) causes the corresponding change
	 * if the referencing columns.
	 */
	CASCADE("CASCADE"),

	/**
	 * The values in the referencing columns are substituted with the defaults.
	 */
	SET_DEFAULT("SET DEFAULT"),

	/**
	 * The values in the referencing columns are set to <code>NULL</code>.
	 */
	SET_NULL("SET NULL");

	private final String action;

	ForeignKeyAction(String action) {
		this.action = action;
	}

	@Override
	public String toString() {
		return action;
	}
}
