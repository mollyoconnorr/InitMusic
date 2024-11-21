package edu.carroll.initMusic.web.form;


/**
 * This form is used for setting user's security questions and their answers
 *
 * @author Molly O'Connor
 * @since September 26, 2024
 */
public class SecurityQuestionsForm {
    /**
     * Security Question 1
     */
    private String question1;

    /**
     * Security answer 1
     */
    private String answer1;

    /**
     * Security Question 2
     */
    private String question2;

    /**
     * Security answer 2
     */
    private String answer2;

    /**
     * Gets the first security question
     *
     * @return The first security question
     */
    public String getQuestion1() {
        return question1;
    }

    /**
     * Sets the first security question
     *
     * @param question1 Question to set
     */
    public void setQuestion1(String question1) {
        this.question1 = question1;
    }

    /**
     * Gets the answer to the first security question
     *
     * @return The answer to the first security question
     */
    public String getAnswer1() {
        return answer1;
    }

    /**
     * Sets the answer to the first security question
     *
     * @param answer1 Question to set
     */
    public void setAnswer1(String answer1) {
        this.answer1 = answer1;
    }

    /**
     * Gets the second security question
     *
     * @return The second security question
     */
    public String getQuestion2() {
        return question2;
    }

    /**
     * Sets the second security question
     *
     * @param question2 Question to set
     */
    public void setQuestion2(String question2) {
        this.question2 = question2;
    }

    /**
     * Gets the answer to the second security question
     *
     * @return The answer to the second security question
     */
    public String getAnswer2() {
        return answer2;
    }

    /**
     * Sets the answer to the second security question
     *
     * @param answer2 Question to set
     */
    public void setAnswer2(String answer2) {
        this.answer2 = answer2;
    }
}
