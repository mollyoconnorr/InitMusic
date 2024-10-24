package edu.carroll.initMusic.service;

import edu.carroll.initMusic.ResponseStatus;
import edu.carroll.initMusic.jpa.model.User;

/**
 * Interface for UserService, defines methods related to user transactions like
 * creating and updating user objects, and creating and updating playlist objects
 * for users.
 *
 * @author Nick Clouse
 *
 * @since October 21, 2024
 */
public interface UserService {

    /**
     * Checks if given username is unique
     * @param username Username to check
     * @return ResponseStatus Enum that tells outcome of method.
     */
    ResponseStatus uniqueUserName(String username);

    /**
     * Checks if given email is unique
     * @param email Email to check
     * @return ResponseStatus Enum that tells outcome of method.
     */
    ResponseStatus uniqueEmail(String email);

    /**
     * Saves a user and their information to the database
     * @param username Username of user
     * @param email Email of user
     * @param firstName First name of user
     * @param lastName Last name of user
     * @param password Password of user
     * @return User object, which contains all the users information
     */
    User saveUser(String username,String email,String firstName,String lastName,String password);

    /**
     * Updates the security questions and answers for the specified user.
     * @param user User object to update
     * @param question1 First security question
     * @param answer1 First answer
     * @param question2 Second security question
     * @param answer2 Second answer
     * @return true if security questions were updated, false otherwise
     */
    boolean updateUserSecurityQuestions(User user, String question1,String answer1, String question2, String answer2);

    /**
     * Updates the users password
     * @param user User to update
     * @param newPassword New password to use
     */
    void updatePassword(User user, String newPassword);

    /**
     * Finds user by email address
     * @param email Email to search by
     * @return The user object found, if any
     */
    User findByEmail(String email);

    /**
     * Finds user by id, will also eagerly fetch
     * the users playlists in case operations are needed
     * to be done on them.
     * @param userId User id to search by
     * @return The user object found, if any
     */
    User findByIdWithPlaylists(Long userId);

    /**
     * Gets user by username
     * @param username Username to search by
     * @return The user object found, if any
     */
    User getUser(String username);
}
