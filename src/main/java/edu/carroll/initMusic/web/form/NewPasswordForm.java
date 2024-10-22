package edu.carroll.initMusic.web.form;

public class NewPasswordForm {
    String newPassword;
    String oldPassword;

    public String getNewPassword() {
        return newPassword;
    }
    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public String getOldPassword() {
        return oldPassword;
    }
    public void setOldPassword(String oldPassword) {  // Correct setter method
        this.oldPassword = oldPassword;
    }
}
