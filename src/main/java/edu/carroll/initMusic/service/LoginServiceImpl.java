package edu.carroll.initMusic.service;

import edu.carroll.initMusic.jpa.model.User;
import edu.carroll.initMusic.jpa.repo.UserRepository;
import edu.carroll.initMusic.web.form.LoginForm;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LoginServiceImpl implements LoginService {
    private final UserRepository loginRepo;

    public LoginServiceImpl(UserRepository loginRepo) {
        this.loginRepo = loginRepo;
    }

    /**
     * Given a loginForm, determine if the information provided is valid, and the user exists in the system.
     *
     * @param username Users username
     * @param password Users password
     * @return true if data exists and matches what's on record, false otherwise
     */
    @Override
    public boolean validateUser(String username, String password) {
        // Always do the lookup in a case-insensitive manner (lower-casing the data).
        List<User> users = loginRepo.findByUsernameIgnoreCase(username);

        // We expect 0 or 1, so if we get more than 1, bail out as this is an error we don't deal with properly.
        if (users.size() != 1)
            return false;
        User u = users.getFirst();
        // XXX - Using Java's hashCode is wrong on SO many levels, but is good enough for demonstration purposes.
        // NEVER EVER do this in production code!

        //DELETE THIS AFTER TESTING WORKS!!!!
        //Replace with better method for hashing
        final String userProvidedHash = Integer.toString(password.hashCode());
        return u.getHashedPassword().equals(userProvidedHash);

        // User exists, and the provided password matches the hashed password in the database.
    }

    @Override
    public boolean addUser() {
        return false;
    }
}