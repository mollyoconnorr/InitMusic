package edu.carroll.initMusic.web.form;

/**
 * A form object for handling user registration data.
 * <p>
 * This class encapsulates the user's registration information, including
 * the username, email, password, first name, last name, and country.
 * It provides getter and setter methods for accessing and modifying these fields.
 * </p>
 */
public class RegistrationForm {
    private String username;
    private String email;
    private String password;
    private String firstName;  // New attribute
    private String lastName;   // New attribute
    private String country;    // New attribute

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

    /**
     * Gets the user's country.
     *
     * @return the country entered by the user.
     */
    public String getCountry() {
        return country;
    }

    /**
     * Sets the user's country.
     *
     * @param country the country to set.
     */
    public void setCountry(String country) {
        this.country = country;
    }
}