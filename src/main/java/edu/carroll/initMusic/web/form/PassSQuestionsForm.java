package edu.carroll.initMusic.web.form;

/**
 * This form is used when a user is setting their security questions
 *
 * @author Molly O'Connor
 * @since September 26, 2024
 */
public class PassSQuestionsForm {
    /**
     * Answer of first question
     */
    private String answer1;

    /**
     * Answer of second question
     */
    private String answer2;

    /**
     * Gets answer to first question
     *
     * @return The answer to the first question
     */
    public String getAnswer1() {
        return answer1;
    }

    /**
     * Sets the answer to the first question
     *
     * @param answer1 Answer to set
     */
    public void setAnswer1(String answer1) {
        this.answer1 = answer1;
    }

    /**
     * Gets answer to second question
     *
     * @return The answer to the second question
     */
    public String getAnswer2() {
        return answer2;
    }

    /**
     * Sets the answer to the second question
     *
     * @param answer2 Answer to set
     */
    public void setAnswer2(String answer2) {
        this.answer2 = answer2;
    }
}
