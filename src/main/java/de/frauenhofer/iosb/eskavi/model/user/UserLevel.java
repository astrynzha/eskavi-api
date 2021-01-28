package de.frauenhofer.iosb.eskavi.model.user;

/**
 * This enum represents the access level a potential user might have.
 *
 * @author Niv Adam
 * @version 1.0.0
 */
public enum UserLevel {
    BASIC_USER(1),
    PUBLISHING_USER(2),
    ADMINISTRATOR(3);

    private int intVal;

    private UserLevel(int intVal) {
        this.intVal = intVal;
    }

    /**
     * Returns the Integer value of the respective user level.
     *
     * @return integer value of user level
     */
    public int getIntVal() {
        return this.intVal;
    }

}
