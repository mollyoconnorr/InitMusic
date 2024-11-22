package edu.carroll.initMusic.web.form.userManagement;

/**
 * A form object for handling user registration data.
 * This class encapsulates the user's registration information, including
 * the username, email, password, first name, and last name.
 * It provides getter and setter methods for accessing and modifying these fields.
 */
public class RegistrationForm {
    /** User's username */
    private String username;

    /** User's email */
    private String email;

    /** User's password */
    private String password;

    /** User's first Name */
    private String firstName;

    /** User's last Name */
    private String lastName;

    /** User's confirm Password, should match password */
    private String confirmPassword;

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
     * Gets the email.
     *
     * @return the email entered by the user.
     */
    public String getEmail() {
        return email;
    }

    /**
     * Sets the email.
     *
     * @param email the email to set.
     */
    public void setEmail(String email) {
        this.email = email;
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

    /**
     * Gets the password.
     *
     * @return the password entered by the user.
     */
    public String getConfirmPassword() {
        return confirmPassword;
    }

    /**
     * Sets the password.
     *
     * @param confirmPassword the password to set.
     */
    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }


    /**
     * Gets the user's first name.
     *
     * @return the first name entered by the user.
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * Sets the user's first name.
     *
     * @param firstName the first name to set.
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     * Gets the user's last name.
     *
     * @return the last name entered by the user.
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * Sets the user's last name.
     *
     * @param lastName the last name to set.
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
}