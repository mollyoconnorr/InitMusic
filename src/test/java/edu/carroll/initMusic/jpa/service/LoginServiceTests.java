package edu.carroll.initMusic.jpa.service;

import static org.springframework.test.util.AssertionErrors.assertFalse;
import static org.springframework.test.util.AssertionErrors.assertNotNull;
import static org.springframework.test.util.AssertionErrors.assertTrue;

import edu.carroll.initMusic.jpa.model.User;
import edu.carroll.initMusic.jpa.repo.UserRepository;
import edu.carroll.initMusic.service.LoginService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;


import java.util.List;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class LoginServiceTests {
    private static final String username = "username";
    private static final String password = "password";
    private static final String firstName = "John";
    private static final String lastName = "Doe";
    private static final String email = "john.doe@example.com";
    private static final String country = "United States";

    @Autowired
    private LoginService loginService;

    @Autowired
    private UserRepository userRepo;

    private User fakeUser = new User(username, password, firstName, lastName, email, country);

    @BeforeAll
    public void beforeAllTest(){
        assertNotNull("loginService must be injected", loginService);
        fakeUser.setHashedPassword(loginService.hashPassword(password));
    }

    @BeforeEach
    public void beforeTest() {
        assertNotNull("UserRepository must be injected", userRepo);

        // Ensure dummy record is in the DB
        final List<User> users = userRepo.findByUsernameIgnoreCase(username);
        if (users.isEmpty()){
            userRepo.save(fakeUser);
            }
    }

    @Test
    public void validateUserSuccessTest() {
        assertTrue("validateUserSuccessTest: should succeed using the same user/pass info", loginService.validateUser(username, password));
    }

    @Test
    public void validateUserExistingUserInvalidPasswordTest() {
        assertFalse("validateUserExistingUserInvalidPasswordTest: should fail using a valid user, invalid pass", loginService.validateUser(username, password + "extra"));
        assertFalse("validateUserExistingUserInvalidPasswordTest: should fail using a valid user, empty pass", loginService.validateUser(username, " "));
        assertFalse("validateUserExistingUserInvalidPasswordTest: should fail using a valid user, valid pass + a space", loginService.validateUser(username, password + " "));
    }

    @Test
    public void validateUserInvalidUserValidPasswordTest() {
        assertFalse("validateUserInvalidUserValidPasswordTest: should fail using an invalid user, valid pass", loginService.validateUser(username + "not", password));
        assertFalse("validateUserInvalidUserValidPasswordTest: should fail using an empty user, valid pass", loginService.validateUser(" ", password));
        assertFalse("validateUserInvalidUserValidPasswordTest: should fail using an valid user + a space, valid pass", loginService.validateUser(username + " ", password));


    }

    @Test
    public void validateUserInvalidUserInvalidPasswordTest() {
        assertFalse("validateUserInvalidUserInvalidPasswordTest: should fail using an invalid user, invalid pass", loginService.validateUser(username + "not", password + "extra"));
        assertFalse("validateUserInvalidUserInvalidPasswordTest: should fail using an empty user, empty pass", loginService.validateUser(" ", " "));
        assertFalse("validateUserInvalidUserInvalidPasswordTest: should fail using an valid user + space, valid pass + space", loginService.validateUser(username + " ", password + " "));
        assertFalse("validateUserInvalidUserInvalidPasswordTest: should fail using an username of all integers, pass of all integers", loginService.validateUser("123456", "123456"));
        assertFalse("validateUserInvalidUserInvalidPasswordTest: should fail using an username of all special characters, pass of all special characters", loginService.validateUser("-!@#$%$", "!@#$@%"));
        assertFalse("validateUserInvalidUserInvalidPasswordTest: should fail using an username of all ', pass of all '", loginService.validateUser("''", "''"));

    }

}
