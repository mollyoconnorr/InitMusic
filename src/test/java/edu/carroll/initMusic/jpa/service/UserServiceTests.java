package edu.carroll.initMusic.jpa.service;

import edu.carroll.initMusic.ResponseStatus;
import edu.carroll.initMusic.jpa.model.User;
import edu.carroll.initMusic.service.UserServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class UserServiceTests {

    @Autowired
    private UserServiceImpl userService;

    @Autowired
    private UserServiceImpl userServiceImpl;

    /*
     * Basic tests
     */

    @Test
    public void checkUniqueUsernameUniqueUsername() {
        assertSame(userService.uniqueUserName("newUser"), ResponseStatus.SUCCESS, "Username should be available");
    }

    @Test
    public void checkUniqueUsernameNotUniqueUsername() {
        userService.saveUser("testuser12","password","email12@email.com","John","Doe");

        assertNotSame(userService.uniqueUserName("testuser12"), ResponseStatus.SUCCESS, "Username should already exist");
    }

    @Test
    public void checkUniqueUsernameEmptyUsername() {
        assertNotEquals(userService.uniqueUserName(""), ResponseStatus.SUCCESS, "Blank username Username should not be available");
    }

    @Test
    public void checkUniqueUsernameNullUsername(){
        assertNotEquals(userService.uniqueUserName(null), ResponseStatus.SUCCESS, "Null username should not be available");
    }

    /*
     * First testing invalid usernames
     */

    @Test
    public void checkUniqueUsername4NumbersTooShortUsername(){
        assertNotEquals(userService.uniqueUserName("1234"), ResponseStatus.SUCCESS, "Username should not be available because its too short (4 Numbers)");
    }

    @Test
    public void checkUniqueUsername3NumbersTooShortUsername(){
        assertNotEquals(userService.uniqueUserName("123"), ResponseStatus.SUCCESS, "Username should not be available because its too short (3 Numbers)");
    }

    @Test
    public void checkUniqueUsername2NumbersTooShortUsername(){
        assertNotEquals(userService.uniqueUserName("12"), ResponseStatus.SUCCESS, "Username should not be available because its too short (2 Numbers)");
    }

    @Test
    public void checkUniqueUsername1NumberTooShortUsername(){
        assertNotEquals(userService.uniqueUserName("1"), ResponseStatus.SUCCESS, "Username should not be available because its too short (1 Number)");
    }

    @Test
    public void checkUniqueUsername1LetterTooShortUsername(){
        assertNotEquals(userService.uniqueUserName("a"), ResponseStatus.SUCCESS, "Username should not be available because its too short (1 Character)");
    }

    @Test
    public void checkUniqueUsername2LetterTooShortUsername(){
        assertNotEquals(userService.uniqueUserName("ab"), ResponseStatus.SUCCESS, "Username should not be available because its too short (3 Character)");
    }

    @Test
    public void checkUniqueUsername3LetterTooShortUsername(){
        assertNotEquals(userService.uniqueUserName("abc"), ResponseStatus.SUCCESS, "Username should not be available because its too short (3 Character)");
    }

    @Test
    public void checkUniqueUsername4LetterTooShortUsername(){
        assertNotEquals(userService.uniqueUserName("abcd"), ResponseStatus.SUCCESS, "Username should not be available because its too short (4 Character)");
    }

    @Test
    public void checkUniqueUsernameSpecialSymbolTooShortUsername(){
        assertNotEquals(userService.uniqueUserName("!"), ResponseStatus.SUCCESS, "Username should not be available because its too short (Its just '!')");
    }

    @Test
    public void checkUniqueUsername2SpecialSymbolsTooShortUsername(){
        assertNotEquals(userService.uniqueUserName("!@"), ResponseStatus.SUCCESS, "Username should not be available because its too short (Its just '!@')");
    }

    @Test
    public void checkUniqueUsername3SpecialSymbolsTooShortUsername(){
        assertNotEquals(userService.uniqueUserName("!@#"), ResponseStatus.SUCCESS, "Username should not be available because its too short (Its just '!@#')");
    }

    @Test
    public void checkUniqueUsername4SpecialSymbolsTooShortUsername(){
        assertNotEquals(userService.uniqueUserName("!@#$"), ResponseStatus.SUCCESS, "Username should not be available because its too short (Its just '!@#$')");
    }

    @Test
    public void checkUniqueUsername1BlankSpaceTooShortUsername(){
        assertNotEquals(userService.uniqueUserName(" "), ResponseStatus.SUCCESS, "Username should not be available because its too short (Its just ' ')");
    }

    @Test
    public void checkUniqueUsername2BlankSpaceTooShortUsername(){
        assertNotEquals(userService.uniqueUserName("  "), ResponseStatus.SUCCESS, "Username should not be available because its too short (Its just '  ')");
    }

    @Test
    public void checkUniqueUsername3BlankSpaceTooShortUsername(){
        assertNotEquals(userService.uniqueUserName("  "), ResponseStatus.SUCCESS, "Username should not be available because its too short (Its just '   ')");
    }

    @Test
    public void checkUniqueUsername4BlankSpaceTooShortUsername(){
        assertNotEquals(userService.uniqueUserName("  "), ResponseStatus.SUCCESS, "Username should not be available because its too short (Its just '    ')");
    }

    /*
     * Testing invalid usernames with various edge cases
     */

    @Test
    public void checkUniqueUsername50CharactersLong() {
        String username = "a".repeat(50); // Create a string with 50 'a' characters
        assertNotEquals(userService.uniqueUserName(username), ResponseStatus.SUCCESS, "Username should not be available because it exceeds the maximum length (50 Characters)");
    }

    @Test
    public void checkUniqueUsername51CharactersLong() {
        String username = "a".repeat(51); // Create a string with 51 'a' characters
        assertNotEquals(userService.uniqueUserName(username), ResponseStatus.SUCCESS, "Username should not be available because it exceeds the maximum length (51 Characters)");
    }

    @Test
    public void checkUniqueUsernameWithSpaces() {
        assertNotEquals(userService.uniqueUserName("user name"), ResponseStatus.SUCCESS, "Username should not be available because it contains spaces");
    }

    @Test
    public void checkUniqueUsernameWithLeadingSpaces() {
        assertNotEquals(userService.uniqueUserName("  username"), ResponseStatus.SUCCESS, "Username should not be available because it starts with spaces");
    }

    @Test
    public void checkUniqueUsernameWithTrailingSpaces() {
        assertNotEquals(userService.uniqueUserName("username  "), ResponseStatus.SUCCESS, "Username should not be available because it ends with spaces");
    }

    @Test
    public void checkUniqueUsernameWithMultipleSpaces() {
        assertNotEquals(userService.uniqueUserName("user  name"), ResponseStatus.SUCCESS, "Username should not be available because it contains multiple spaces");
    }

    @Test
    public void checkUniqueUsernameMixedSpacesAndDigits() {
        assertNotEquals(userService.uniqueUserName("123 456"), ResponseStatus.SUCCESS, "Username should not be available because it contains spaces and digits");
    }

    /*
     * Second, testing valid usernames which are all unique
     */

    @Test
    public void checkUniqueUsernameValidName5to49Characters(){
        String[] validUsernames = {
                "kevin",
                "kevin123",
                "user_name",
                "validUser",
                "username123456",
                "12345",
                "987654321",
                "KeViN",
                "UserWithCaps",
                "user_name",
                "first_last",
                "a_b_c_d",
                "thisusernameisvalid",
                "validUserWithMoreChars",
                "user12345",
                "valid123username",
                "user_name_2024",
                "validusername12345678901234567890",
                "validusernametotestlengthfortyone"
        };

        for (String name : validUsernames) {
            assertSame(userService.uniqueUserName(name), ResponseStatus.SUCCESS,
                    String.format("Username '%s' is valid and shouldn't already exist", name));
        }
    }

    @Test
    public void checkUniqueUsernameValidWithNumbers() {
        assertSame(userService.uniqueUserName("validUser123"), ResponseStatus.SUCCESS, "Username should be available");
    }

    @Test
    public void checkUniqueUsernameValidWithSpecialCharacters() {
        assertSame(userService.uniqueUserName("user!@#"), ResponseStatus.SUCCESS, "Username should be available");
    }

    @Test
    public void checkUniqueUsernameValidWithAlphanumericAndSpecialCharacters() {
        assertSame(userService.uniqueUserName("User123!@"), ResponseStatus.SUCCESS, "Username should be available");
    }

    @Test
    public void checkUniqueUsernameValidWithUnderscore() {
        assertSame(userService.uniqueUserName("user_name"), ResponseStatus.SUCCESS, "Username should be available");
    }

    @Test
    public void checkUniqueUsernameValidWithDash() {
        assertSame(userService.uniqueUserName("user-name"), ResponseStatus.SUCCESS, "Username should be available");
    }

    @Test
    public void checkUniqueUsernameValidWithMixedCase() {
        assertSame(userService.uniqueUserName("UsEr123"), ResponseStatus.SUCCESS, "Username should be available");
    }

    @Test
    public void checkUniqueUsernameValidLongUsername() {
        final String username = "validUserWithLongLength123!@#"; // 30 characters
        assertSame(userService.uniqueUserName(username), ResponseStatus.SUCCESS, "Username should be available");
    }

    @Test
    public void checkUniqueUsernameValidMixedCharacters() {
        assertSame(userService.uniqueUserName("user_123$%^&*"), ResponseStatus.SUCCESS, "Username should be available");
    }

    /*
     * Testing usernames that are not unique (already exist in the database)
     */

    @Test
    public void checkUniqueUsernameNotUniqueWithSameCase() {
        userService.saveUser("duplicateUser1","password","email3@email.com","First","Last");

        assertSame(userService.uniqueUserName("duplicateUser1"), ResponseStatus.USER_ALREADY_EXISTS, "Username should already exist (same case)");
    }

    @Test
    public void checkUniqueUsernameNotUniqueWithDifferentCase() {
        userService.saveUser("duplicateUser2","password","email2@email.com","First","Last");

        assertSame(userService.uniqueUserName("DUPLICATEUSER2"), ResponseStatus.USER_ALREADY_EXISTS, "Username should already exist (different case)");
    }

    @Test
    public void checkUniqueUsernameNotUniqueWithConsecutiveSpecialCharacters() {
        userService.saveUser("user!!duplicate","password","email18@email.com","John","Doe");

        assertSame(userService.uniqueUserName("user!!duplicate"), ResponseStatus.USER_ALREADY_EXISTS, "Username should already exist with consecutive special characters");
    }

    @Test
    public void checkUniqueUsernameNotUniqueVeryLong() {
        final String username = "veryLongDuplicateUsername1234567890!@#$";
        userServiceImpl.saveUser(username,"password","JDoe1@email.com","John","Doe");
        assertSame(userService.uniqueUserName(username), ResponseStatus.USER_ALREADY_EXISTS, "Username should already exist (very long)");
    }

    @Test
    public void checkUniqueUsernameNotUniqueWithNumbersAtEnd() {
        userServiceImpl.saveUser("user456","password","JDoe2@email.com","John","Doe");

        assertSame(userService.uniqueUserName("user456"), ResponseStatus.USER_ALREADY_EXISTS, "Username should already exist with numbers at the end");
    }

    /*
     * Testing uniqueEmail method
     */

    /*
     * Testing cases where email is invalid
     */

    @Test
    public void checkInvalidEmailEmpty() {
        assertTrue(userService.uniqueEmail("").failed(), "Email should be invalid when empty");
    }

    @Test
    public void checkInvalidEmailMissingAtSymbol() {
        assertTrue(userService.uniqueEmail("invalidemail.com").failed(), "Email should be invalid without @ symbol");
    }

    @Test
    public void checkInvalidEmailMissingDomain() {
        assertTrue(userService.uniqueEmail("invalid@").failed(), "Email should be invalid without a domain");
    }

    @Test
    public void checkInvalidEmailMultipleAtSymbols() {
        assertTrue(userService.uniqueEmail("invalid@@example.com").failed(), "Email should be invalid with multiple @ symbols");
    }

    @Test
    public void checkInvalidEmailInvalidDomain() {
        assertTrue(userService.uniqueEmail("user@domain_without_dot").failed(), "Email should be invalid with an incorrect domain");
    }

    @Test
    public void checkInvalidEmailNoTLD() {
        assertTrue(userService.uniqueEmail("user@example").failed(), "Email should be invalid without a top-level domain");
    }

    @Test
    public void checkInvalidEmailNull() {
        assertTrue(userService.uniqueEmail(null).failed(), "Email should be invalid when null");
    }

    @Test
    public void checkInvalidEmailOnlySpaces() {
        assertTrue(userService.uniqueEmail("   ").failed(), "Email should be invalid when it only contains spaces");
    }

    @Test
    public void checkInvalidEmailLeadingTrailingSpaces() {
        assertTrue(userService.uniqueEmail("  email@example.com  ").failed(), "Email should be invalid with leading/trailing spaces");
    }

    @Test
    public void checkInvalidEmailSpacesWithin() {
        assertTrue(userService.uniqueEmail("user name@example.com").failed(), "Email should be invalid when it contains spaces");
    }


    /*
     * Testing where email is valid
     */

    @Test
    public void checkValidEmailSimple() {
        assertFalse(userService.uniqueEmail("user@example.com").failed(), "Email should be valid");
    }

    @Test
    public void checkValidEmailWithNumbers() {
        assertFalse(userService.uniqueEmail("user123@example.com").failed(), "Email should be valid with numbers");
    }

    @Test
    public void checkValidEmailWithDotsBeforeAt() {
        assertFalse(userService.uniqueEmail("first.last@example.com").failed(), "Email should be valid with dots before the @ symbol");
    }

    @Test
    public void checkValidEmailWithPlusSigns() {
        assertFalse(userService.uniqueEmail("user+alias@example.com").failed(), "Email should be valid with a plus sign");
    }

    @Test
    public void checkValidEmailWithUnderscores() {
        assertFalse(userService.uniqueEmail("first_last@example.com").failed(), "Email should be valid with underscores");
    }

    @Test
    public void checkValidEmailWithHyphens() {
        assertFalse(userService.uniqueEmail("user@sub-domain.com").failed(), "Email should be valid with hyphens in the domain");
    }

    @Test
    public void checkValidEmailWithMultipleDotsInDomain() {
        assertFalse(userService.uniqueEmail("user@mail.example.com").failed(), "Email should be valid with multiple dots in the domain");
    }

    @Test
    public void checkValidEmailWithTwoLetterTLD() {
        assertFalse(userService.uniqueEmail("user@example.io").failed(), "Email should be valid with a two-letter TLD");
    }

    @Test
    public void checkValidEmailWithThreeOrMoreLetterTLD() {
        assertFalse(userService.uniqueEmail("user@example.com").failed(), "Email should be valid with a three-letter TLD");
    }

    @Test
    public void checkValidEmailWithSpecialCharacters() {
        assertFalse(userService.uniqueEmail("user!name@example.com").failed(), "Email should be valid with allowed special characters");
    }

    @Test
    public void checkValidEmailWithMixedCase() {
        assertFalse(userService.uniqueEmail("User@Example.com").failed(), "Email should be valid with mixed case");
    }

    @Test
    public void checkValidEmailWithHyphenInDomain() {
        assertFalse(userService.uniqueEmail("user@-example.com").failed(), "Email should be valid with a hyphen at the start of the domain");
    }

    @Test
    public void checkValidEmailWithHyphenAtEndOfDomain() {
        assertFalse(userService.uniqueEmail("user@example-.com").failed(), "Email should be valid with a hyphen at the end of the domain");
    }

    @Test
    public void checkValidEmailAllNumeric() {
        assertFalse(userService.uniqueEmail("12345@domain.com").failed(), "Email should be valid with all numeric characters");
    }

    /*
     * Now testing saveUser method
     */

    // Saving a valid user
    @Test
    public void saveValidUser() {
        final String password = "password123";

        final User savedUser = userService.saveUser("testUser1", "password123","test1@example.com", "John", "Doe");

        assertEquals("testUser1", savedUser.getUsername(),"username should match given username");
        assertEquals("test1@example.com", savedUser.getEmail(),"email should match given email");
        assertNotEquals(password, savedUser.getHashedPassword(),"Password should be hashed on creation of new user");
        assertEquals("John", savedUser.getFirstName(),"First name should match given first name");
        assertEquals("Doe", savedUser.getLastName(),"Last name should match given last name");
        assertNull(savedUser.getQuestion1(), "On user creation, security question 1 should be null");
        assertNull(savedUser.getAnswer1(), "On user creation, security question 1 answer should be null");
        assertNull(savedUser.getQuestion2(), "On user creation, security question 2 should be null");
        assertNull(savedUser.getAnswer2(), "On user creation, security question 2 answer should be null");
    }

    // Saving a user with a blank username
    @Test
    public void saveUserWithBlankUsername() {
        final User savedUser = userService.saveUser("", "password123", "test2@example.com","John", "Doe");

        assertNull(savedUser, "Expected null when username is blank");
    }

    // Saving a user with a blank email
    @Test
    public void saveUserWithBlankEmail() {
        final User savedUser = userService.saveUser("testUser2", "password123", "", "John", "Doe");

        assertNull(savedUser, "Expected null when email is blank");
    }

    // Saving a user with a blank first name
    @Test
    public void saveUserWithBlankFirstName() {
        final User savedUser = userService.saveUser("testUser3", "password123","test3@example.com", "", "Doe");

        assertNull(savedUser, "Expected null when first name is blank");
    }

    // Saving a user with a blank last name
    @Test
    public void saveUserWithBlankLastName() {
        final User savedUser = userService.saveUser("testUser4", "password123","test4@example.com", "John", "");

        assertNull(savedUser, "Expected null when last name is blank");
    }

    // Saving a user with a blank password
    @Test
    public void saveUserWithBlankPassword() {
        final User savedUser = userService.saveUser("testUser5", "","test5@example.com", "John", "Doe");

        assertNull(savedUser, "Expected null when password is blank");
    }

    // Saving a user with valid special characters in username
    @Test
    public void saveUserWithValidSpecialCharacterUsername() {
        final User savedUser = userService.saveUser("user.name", "password123","test6@example.com", "John", "Doe");

        assertEquals("user.name", savedUser.getUsername(), "Users name should match regardless of special characters");
    }

    // Saving a user with trailing spaces in username
    @Test
    public void saveUserWithTrailingSpacesInUsername() {
        final User savedUser = userService.saveUser(" testUser6 ", "password123","test7@example.com", "John", "Doe");

        assertEquals("testUser6", savedUser.getUsername(), "Usernames should match regardless if there are trailing spaces");
    }

    // Saving a user with spaces in first name and last name
    @Test
    public void saveUserWithSpacesInNames() {
        final User savedUser = userService.saveUser("testUser7", "password123","test8@example.com", "John ", "Doe ");

        assertEquals("John", savedUser.getFirstName(), "First names should match");
        assertEquals("Doe", savedUser.getLastName(), "Last names should match");
    }

    /*
     * Testing updateUserSecurityQuestions
     */

    @Test
    public void updateUserSecurityQuestionsSuccessfully() {
        final String question1 = "What is your pet's name?";
        final String answer1 = "Fluffy";
        final String question2 = "What is your hometown?";
        final String answer2 = "Springfield";

        final User newUser = userService.saveUser("testUser13", "password123","test13@example.com", "John ", "Doe ");
        final boolean result = userService.updateUserSecurityQuestions(newUser, question1, answer1, question2, answer2);
        
        assertTrue(result, "Security questions should be updated successfully");
        assertEquals(question1, newUser.getQuestion1(),"Questions for security question 1 should match");
        assertEquals(answer1, newUser.getAnswer1(),"Answers for security question 1 should match");
        assertEquals(question2, newUser.getQuestion2(),"Questions for security question 2 should match");
        assertEquals(answer2, newUser.getAnswer2(), "Answers for security question 2 should match");
    }

    @Test
    public void updateUserSecurityQuestionsWithBlankQuestion1() {
        final String question1 = "";
        final String answer1 = "Fluffy";
        final String question2 = "What is your hometown?";
        final String answer2 = "Springfield";

        final User newUser = userService.saveUser("testUser14", "password123","test14@example.com", "John ", "Doe ");
        final boolean result = userService.updateUserSecurityQuestions(newUser, question1, answer1, question2, answer2);
        
        assertFalse(result, "Security questions update should fail due to blank question1");
    }

    @Test
    public void updateUserSecurityQuestionsWithBlankAnswer1() {
        final String question1 = "What is your pet's name?";
        final String answer1 = "";
        final String question2 = "What is your hometown?";
        final String answer2 = "Springfield";

        final User newUser = userService.saveUser("testUser26", "password123","test26@example.com", "John ", "Doe ");
        final boolean result = userService.updateUserSecurityQuestions(newUser, question1, answer1, question2, answer2);
        
        assertFalse(result, "Security questions update should fail due to blank answer1");
    }

    @Test
    public void updateUserSecurityQuestionsWithBlankQuestion2() {
        final String question1 = "What is your pet's name?";
        final String answer1 = "Fluffy";
        final String question2 = "";
        final String answer2 = "Springfield";

        final User newUser = userService.saveUser("testUser25", "password123","test25@example.com", "John ", "Doe ");
        final boolean result = userService.updateUserSecurityQuestions(newUser, question1, answer1, question2, answer2);
        
        assertFalse(result, "Security questions update should fail due to blank question2");
    }

    @Test
    public void updateUserSecurityQuestionsWithBlankAnswer2() {
        final String question1 = "What is your pet's name?";
        final String answer1 = "Fluffy";
        final String question2 = "What is your hometown?";
        final String answer2 = "";

        final User newUser = userService.saveUser("testUser15", "password123","test20@example.com", "John ", "Doe ");
        boolean result = userService.updateUserSecurityQuestions(newUser, question1, answer1, question2, answer2);
        
        assertFalse(result, "Security questions update should fail due to blank answer2");
    }

    /*
     * Testing findUserByEmail
     */

    @Test
    public void findByEmailSuccessfully() {
        final User newUser = userService.saveUser("testUser16", "password123","test16@example.com", "John ", "Doe ");

        final User foundUser = userService.findByEmail("test16@example.com");

        assertNotNull(foundUser, "User should be found");
        assertEquals(newUser.getEmail(), foundUser.getEmail(), "Emails should match");
    }

    @Test
    public void findByEmailIgnoringCase() {
        final User newUser = userService.saveUser("testUser12", "password123","test12@example.com", "John ", "Doe ");
        final User foundUser = userService.findByEmail("TEST12@EXAMPLE.COM");

        assertNotNull(foundUser, "User should be found");
        assertEquals(newUser.getEmail(), foundUser.getEmail(), "Emails should match");
    }

    @Test
    public void findByEmailWhenNoUserFound() {
        final User foundUser = userService.findByEmail("nonexistent@example.com");

        assertNull(foundUser, "User should not be found");
    }

    @Test
    public void findByEmailWithNullEmail() {
        final User foundUser = userService.findByEmail(null);

        assertNull(foundUser, "User should not be found for null email");
    }

    @Test
    public void findByEmailWithEmptyEmail() {
        final User foundUser = userService.findByEmail("");
        
        assertNull(foundUser, "User should not be found for empty email");
    }

    /*
     * Testing updatePassword
     */

    @Test
    public void updatePasswordSuccessfully() {
        final User newUser = userService.saveUser("testUser17", "password123","test17@example.com", "John ", "Doe ");

        final String newPassword = "newPassword123";

        final boolean result = userService.updatePassword(newUser, newPassword);

        assertTrue(result, "Password should be updated successfully");
        assertNotEquals(newPassword, newUser.getHashedPassword(), "User's new password should be hashed");
    }

    @Test
    public void updatePasswordWithNullUser() {
        final String newPassword = "newPassword123";

        assertThrows(NullPointerException.class, () -> userService.updatePassword(null, newPassword), "Updating password for a null user should throw NullPointerException");
    }

    @Test
    public void updatePasswordWithNullNewPassword() {
        final User newUser = userService.saveUser("duplicateUser20","password","email20@email.com","First","Last");

        final String passwordBeforeUpdate = newUser.getHashedPassword();
        final boolean result = userService.updatePassword(newUser, null);

        assertFalse(result, "Updating password with null should return false");
        assertEquals(passwordBeforeUpdate, newUser.getHashedPassword(), "User's hashed password should not change");
    }

    @Test
    public void updatePasswordWithShortNewPassword() {
        final User newUser = userService.saveUser("duplicateUser21","password","email21@email.com","First","Last");
        final String newPassword = "short"; // Less than 8 characters

        final String passwordBeforeUpdate = newUser.getHashedPassword();
        final boolean result = userService.updatePassword(newUser, newPassword);

        assertFalse(result, "Updating password with a short password should return false");
        assertEquals(passwordBeforeUpdate, newUser.getHashedPassword(), "User's hashed password should not change");
    }

    /*
     * Testing findByIDWithPlaylists
     */

    @Test
    public void findByIdWithPlaylists_UserExists() {
        final User newUser = userService.saveUser("duplicateUser23","password","email23@email.com","First","Last");
        final Long userID = newUser.getuserID();
        final User foundUser = userService.findByIdWithPlaylists(userID);

        assertNotNull(foundUser, "User should be found");
        assertEquals(newUser.getUsername(), foundUser.getUsername(), "Usernames should match");
        assertEquals(newUser.getEmail(), foundUser.getEmail(), "Emails should match");
    }

    @Test
    public void findByIdWithPlaylists_UserDoesNotExist() {
        final Long userId = 123324334344L;
        final User foundUser = userService.findByIdWithPlaylists(userId);

        assertNull(foundUser, "User should not be found");
    }

    @Test
    public void findByIdWithPlaylists_NegativeUserId() {
        final Long negativeUserId = -1L;

        final User foundUser = userService.findByIdWithPlaylists(negativeUserId);

        assertNull(foundUser, "User should not be found for a negative ID");
    }

    /*
     * Testing getUser method
     */

    @Test
    public void getUser_UserExists() {
        final String username = "duplicateUser24";
        final User newUser = userService.saveUser(username,"password","email24@email.com","First","Last");

        final User foundUser = userService.getUser(username);

        assertNotNull(foundUser, "User should be found");
        assertEquals(newUser.getUsername(), foundUser.getUsername(), "Usernames should match");
    }

    @Test
    public void getUser_UserDoesNotExist() {
        final User foundUser = userService.getUser("NathanWilliams");

        assertNull(foundUser, "User should not be found");
    }
}