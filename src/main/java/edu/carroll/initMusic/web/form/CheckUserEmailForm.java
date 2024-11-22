package edu.carroll.initMusic.web.form;

/**
 * This form is used to help handle checking for a valid email
 */
public class CheckUserEmailForm {
    /** User's email */
    private String email;

    /**
     * Gets user's email
     * @return User's email
     */
    public String getEmail() {
        return email;
    }

    /**
     * Sets user's email
     * @param email Email to set
     */
    public void setEmail(String email) {
        this.email = email;
    }
}
