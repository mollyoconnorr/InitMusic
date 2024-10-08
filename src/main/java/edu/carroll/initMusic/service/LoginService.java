package edu.carroll.initMusic.service;

/**
 * Interface for LoginService, defines methods related to logging in and creating accounts.
 */
public interface LoginService {
    /**
     * Given a loginForm, determine if the information provided is valid, and the user exists in the system.
     * @param username Users username
     * @param password Users password
     * @return true if data exists and matches what's on record, false otherwise
     */
    boolean validateUser(String username, String password);

    /**
     * Hashes given password
     * @param password Password to hash
     * @return Hashed password
     */
    String hashPassword(String password);
}
