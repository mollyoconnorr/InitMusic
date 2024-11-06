package edu.carroll.initMusic.web.form;

/**
 * This form is used to help handle setting a new password
 *
 * @author Molly O'Connor
 *
 * @since September 26, 2024
 */
public class NewPasswordForm {
    /** User's new password */
    String newPassword;

    /** User's old password */
    String oldPassword;

    /**
     * Gets the users new password
     * @return New password
     */
    public String getNewPassword() {
        return newPassword;
    }

    /**
     * Sets user's new password in the form
     * @param newPassword New password to set
     */
    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    /**
     * Gets the users old password
     * @return Old password
     */
    public String getOldPassword() {
        return oldPassword;
    }

    /**
     * Sets user's old password in the form
     * @param oldPassword Old password to set
     */
    public void setOldPassword(String oldPassword) {  // Correct setter method
        this.oldPassword = oldPassword;
    }
}
