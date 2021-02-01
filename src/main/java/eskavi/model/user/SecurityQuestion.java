package eskavi.model.user;

/**
 * This enum represents all security questions available to a user.
 *
 * @author Niv Adam
 * @version 1.0.0
 */
public enum SecurityQuestion {
    MAIDEN_NAME("What's your mother's maiden name?"),
    PET_NAME("What was your first pet's name?");

    private String question;

    private SecurityQuestion(String question) {
        this.question = question;
    }

    /**
     * Gets the string-representation of the question.
     *
     * @return string with question
     */
    public String getQuestion() {
        return this.question;
    }
}
