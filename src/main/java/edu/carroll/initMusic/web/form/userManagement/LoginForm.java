package edu.carroll.initMusic.web.form;

/**
 * A form object for handling user login data.
 * This class encapsulates the user's login information, including
 * the username and password, providing getter and setter methods
 * for accessing and modifying these fields.
 */
public class LoginForm {
    /** Username */
    private String username;

    /** Password */
    private String password;

    /**
     * Gets the username.
     *
     * @return the username entered by the user.
     */
    public String getUsername() {
        return username;
    }

    /**
     * Sets the username.
     *
     * @param username the username to set.
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Gets the password.
     *
     * @return the password entered by the user.
     */
    public String getPassword() {
        return password;
    }

    /**
     * Sets the password.
     *
     * @param password the password to set.
     */
    public void setPassword(String password) {
        this.password = password;
    }
}
