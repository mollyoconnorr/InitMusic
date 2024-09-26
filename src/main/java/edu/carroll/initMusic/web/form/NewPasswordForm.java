package edu.carroll.initMusic.web.form;

import edu.carroll.initMusic.service.UserService;
import edu.carroll.initMusic.web.controller.LoginController;

public class NewPasswordForm {
    String newPassword;

    public String getNewPassword() {
        return newPassword;
    }
    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }
}
