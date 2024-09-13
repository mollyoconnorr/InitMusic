package edu.carroll.initMusic.service;

import edu.carroll.initMusic.web.form.LoginForm;

public interface LoginService {
    /**
     * Given a loginForm, determine if the information provided is valid, and the user exists in the system.
     * @param username Users username
     * @param password Users password
     * @return true if data exists and matches what's on record, false otherwise
     */
    boolean validateUser(String username, String password);

    /**
     * Given a createForm, determine if the information provided is valid and all conditions have been met,
     * and add the user to the system
     * @return true if the user has been added to the system, false otherwise
     */
    boolean addUser();
}
