package edu.carroll.initMusic.service;

import edu.carroll.initMusic.MethodOutcome;
import edu.carroll.initMusic.jpa.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests the methods in userService
 * <p>Which include:</p>
 * <ul>
 *   <li>Checking for a unique username</li>
 *   <li>Checking for a unique email</li>
 *   <li>Saving user data to the database</li>
 *   <li>Updating user's security questions</li>
 *   <li>Updating the user's passwords</li>
 *   <li>Finding the user by email</li>
 *   <li>Finding the user by id</li>
 *   <li>Finding user by username</li>
 *   <li>Deleting user data from database using email</li>
 * </ul>
 */
@SpringBootTest
public class UserServiceTests {

    @Autowired
    private UserServiceImpl userService;

    /*
     * First testing invalid usernames
     */

    @Test
    public void checkUniqueUsername4NumbersTooShortUsername(){
        assertEquals(userService.uniqueUserName("1234"), MethodOutcome.USER_TOO_SHORT, "Username should not be available because its too short (4 Numbers)");
    }

    @Test
    public void checkUniqueUsername3NumbersTooShortUsername(){
        assertEquals(userService.uniqueUserName("123"), MethodOutcome.USER_TOO_SHORT, "Username should not be available because its too short (3 Numbers)");
    }

    @Test
    public void checkUniqueUsername2NumbersTooShortUsername(){
        assertEquals(userService.uniqueUserName("12"), MethodOutcome.USER_TOO_SHORT, "Username should not be available because its too short (2 Numbers)");
    }

    @Test
    public void checkUniqueUsername1NumberTooShortUsername(){
        assertEquals(userService.uniqueUserName("1"), MethodOutcome.USER_TOO_SHORT, "Username should not be available because its too short (1 Number)");
    }

    @Test
    public void checkUniqueUsername1LetterTooShortUsername(){
        assertEquals(userService.uniqueUserName("a"), MethodOutcome.USER_TOO_SHORT, "Username should not be available because its too short (1 Character)");
    }

    @Test
    public void checkUniqueUsername2LetterTooShortUsername(){
        assertEquals(userService.uniqueUserName("ab"), MethodOutcome.USER_TOO_SHORT, "Username should not be available because its too short (3 Character)");
    }

    @Test
    public void checkUniqueUsername3LetterTooShortUsername(){
        assertEquals(userService.uniqueUserName("abc"), MethodOutcome.USER_TOO_SHORT, "Username should not be available because its too short (3 Character)");
    }

    @Test
    public void checkUniqueUsername4LetterTooShortUsername(){
        assertEquals(userService.uniqueUserName("abcd"), MethodOutcome.USER_TOO_SHORT, "Username should not be available because its too short (4 Character)");
    }

    @Test
    public void checkUniqueUsernameSpecialSymbolTooShortUsername(){
        assertEquals(userService.uniqueUserName("!"), MethodOutcome.USER_TOO_SHORT, "Username should not be available because its too short (Its just '!')");
    }

    @Test
    public void checkUniqueUsername2SpecialSymbolsTooShortUsername(){
        assertEquals(userService.uniqueUserName("!@"), MethodOutcome.USER_TOO_SHORT, "Username should not be available because its too short (Its just '!@')");
    }

    @Test
    public void checkUniqueUsername3SpecialSymbolsTooShortUsername(){
        assertEquals(userService.uniqueUserName("!@#"), MethodOutcome.USER_TOO_SHORT, "Username should not be available because its too short (Its just '!@#')");
    }

    @Test
    public void checkUniqueUsername4SpecialSymbolsTooShortUsername(){
        assertEquals(userService.uniqueUserName("!@#$"), MethodOutcome.USER_TOO_SHORT, "Username should not be available because its too short (Its just '!@#$')");
    }

    @Test
    public void checkUniqueUsername1BlankSpaceTooShortUsername(){
        assertEquals(userService.uniqueUserName(" "), MethodOutcome.USER_TOO_SHORT, "Username should not be available because its too short (Its just ' ')");
    }

    @Test
    public void checkUniqueUsername2BlankSpaceTooShortUsername(){
        assertEquals(userService.uniqueUserName("  "), MethodOutcome.USER_TOO_SHORT, "Username should not be available because its too short (Its just '  ')");
    }

    @Test
    public void checkUniqueUsername3BlankSpaceTooShortUsername(){
        assertEquals(userService.uniqueUserName("  "), MethodOutcome.USER_TOO_SHORT, "Username should not be available because its too short (Its just '   ')");
    }

    @Test
    public void checkUniqueUsername4BlankSpaceTooShortUsername(){
        assertEquals(userService.uniqueUserName("  "), MethodOutcome.USER_TOO_SHORT, "Username should not be available because its too short (Its just '    ')");
    }

    @Test
    public void checkUniqueUsernameEmptyUsername() {
        assertEquals(userService.uniqueUserName(""), MethodOutcome.USER_TOO_SHORT, "Blank username Username should not be available");
    }

    @Test
    public void checkUniqueUsernameNullUsername(){
        assertEquals(userService.uniqueUserName(null), MethodOutcome.USER_TOO_SHORT, "Null username should not be available");
    }

    /*
     * Testing invalid usernames with various edge cases
     */

    @Test
    public void checkUniqueUsername50CharactersLong() {
        final String username = "a".repeat(50); // Create a string with 50 'a' characters
        assertEquals(userService.uniqueUserName(username), MethodOutcome.USER_TOO_LONG, "Username should not be available because it exceeds the maximum length (50 Characters)");
    }

    @Test
    public void checkUniqueUsername51CharactersLong() {
        final String username = "a".repeat(51); // Create a string with 51 'a' characters
        assertEquals(userService.uniqueUserName(username), MethodOutcome.USER_TOO_LONG, "Username should not be available because it exceeds the maximum length (51 Characters)");
    }

    @Test
    public void checkUniqueUsernameWithSpaces() {
        assertEquals(userService.uniqueUserName("user name"), MethodOutcome.USER_HAS_SPACES, "Username should not be available because it contains spaces");
    }

    @Test
    public void checkUniqueUsernameWithLeadingSpaces() {
        assertEquals(userService.uniqueUserName("  username"), MethodOutcome.USER_HAS_SPACES, "Username should not be available because it starts with spaces");
    }

    @Test
    public void checkUniqueUsernameWithTrailingSpaces() {
        assertEquals(userService.uniqueUserName("username  "), MethodOutcome.USER_HAS_SPACES, "Username should not be available because it ends with spaces");
    }

    @Test
    public void checkUniqueUsernameWithMultipleSpaces() {
        assertEquals(userService.uniqueUserName("user  name"), MethodOutcome.USER_HAS_SPACES, "Username should not be available because it contains multiple spaces");
    }

    @Test
    public void checkUniqueUsernameMixedSpacesAndDigits() {
        assertEquals(userService.uniqueUserName("123 456"), MethodOutcome.USER_HAS_SPACES, "Username should not be available because it contains spaces and digits");
    }

    /*
     * Second, testing valid usernames which are all unique
     */

    @Test
    public void checkUniqueUsernameWithUniqueUsername() {
        assertEquals(userService.uniqueUserName("newUser"), MethodOutcome.SUCCESS, "Username should be available");
    }

    @Test
    public void checkUniqueUsernameWithNotUniqueUsername() {
        final User savedUser = userService.saveUser("testuser1234","password","email12@email.com","John","Doe");
        assertNotNull(savedUser, "User should've been saved in database when testing checkUniqueUsernameWithNotUniqueUsername!");

        assertNotEquals(userService.uniqueUserName("testuser1234"), MethodOutcome.SUCCESS, "Username should already exist");
    }

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
            assertEquals(userService.uniqueUserName(name), MethodOutcome.SUCCESS,
                    String.format("Username '%s' is valid and shouldn't already exist", name));
        }
    }

    @Test
    public void checkUniqueUsernameValidWithNumbers() {
        assertEquals(userService.uniqueUserName("validUser123"), MethodOutcome.SUCCESS, "Username should be available");
    }

    @Test
    public void checkUniqueUsernameValidWithSpecialCharacters() {
        assertEquals(userService.uniqueUserName("user!@#"), MethodOutcome.SUCCESS, "Username should be available");
    }

    @Test
    public void checkUniqueUsernameValidWithAlphanumericAndSpecialCharacters() {
        assertEquals(userService.uniqueUserName("User123!@"), MethodOutcome.SUCCESS, "Username should be available");
    }

    @Test
    public void checkUniqueUsernameValidWithUnderscore() {
        assertEquals(userService.uniqueUserName("user_name"), MethodOutcome.SUCCESS, "Username should be available");
    }

    @Test
    public void checkUniqueUsernameValidWithDash() {
        assertEquals(userService.uniqueUserName("user-name"), MethodOutcome.SUCCESS, "Username should be available");
    }

    @Test
    public void checkUniqueUsernameValidWithMixedCase() {
        assertEquals(userService.uniqueUserName("UsEr123"), MethodOutcome.SUCCESS, "Username should be available");
    }

    @Test
    public void checkUniqueUsernameValidLongUsername() {
        final String username = "validUserWithLongLength123!@#"; // 30 characters
        assertEquals(userService.uniqueUserName(username), MethodOutcome.SUCCESS, "Username should be available");
    }

    @Test
    public void checkUniqueUsernameValidMixedCharacters() {
        assertEquals(userService.uniqueUserName("user_123$%^&*"), MethodOutcome.SUCCESS, "Username should be available");
    }

    /*
     * Testing usernames that are not unique (already exist in the database)
     */

    @Test
    public void checkUniqueUsernameNotUniqueWithSameCase() {
        final User savedUser = userService.saveUser("duplicateUser1","password","email3@email.com","First","Last");
        assertNotNull(savedUser, "User should've been saved in database when testing checkUniqueUsernameNotUniqueWithSameCase!");

        assertEquals(userService.uniqueUserName("duplicateUser1"), MethodOutcome.USER_ALREADY_EXISTS, "Username should already exist (same case)");
    }

    @Test
    public void checkUniqueUsernameNotUniqueWithDifferentCase() {
        final User savedUser = userService.saveUser("duplicateUser2","password","email2@email.com","First","Last");
        assertNotNull(savedUser, "User should've been saved in database when testing checkUniqueUsernameNotUniqueWithDifferentCase!");

        assertEquals(userService.uniqueUserName("DUPLICATEUSER2"), MethodOutcome.USER_ALREADY_EXISTS, "Username should already exist (different case)");
    }

    @Test
    public void checkUniqueUsernameNotUniqueWithConsecutiveSpecialCharacters() {
        final User savedUser = userService.saveUser("user!!duplicate","password","email18@email.com","John","Doe");
        assertNotNull(savedUser, "User should've been saved in database when testing checkUniqueUsernameNotUniqueWithConsecutiveSpecialCharacters!");

        assertEquals(userService.uniqueUserName("user!!duplicate"), MethodOutcome.USER_ALREADY_EXISTS, "Username should already exist with consecutive special characters");
    }

    @Test
    public void checkUniqueUsernameNotUniqueVeryLong() {
        final String username = "veryLongDuplicateUsername1234567890!@#$";
        final User savedUser = userService.saveUser(username,"password","JDoe1@email.com","John","Doe");
        assertNotNull(savedUser, "User should've been saved in database when testing checkUniqueUsernameNotUniqueVeryLong!");

        assertEquals(userService.uniqueUserName(username), MethodOutcome.USER_ALREADY_EXISTS, "Username should already exist (very long)");
    }

    @Test
    public void checkUniqueUsernameNotUniqueWithNumbersAtEnd() {
        final User savedUser = userService.saveUser("user456","password","JDoe2@email.com","John","Doe");
        assertNotNull(savedUser, "User should've been saved in database when testing checkUniqueUsernameNotUniqueWithNumbersAtEnd!");

        assertEquals(userService.uniqueUserName("user456"), MethodOutcome.USER_ALREADY_EXISTS, "Username should already exist with numbers at the end");
    }

    @Test
    public void checkUniqueUsernameUniqueWithOtherUsersInDatabase(){
        final User savedUser = userService.saveUser("user34343","password","JDoe2343@email.com","John","Doe");
        assertNotNull(savedUser, "User should've been saved in database when testing checkUniqueUsernameUniqueWithOtherUsersInDatabase!");

        final User savedUserTwo = userService.saveUser("user343433","password","JDoe23433@email.com","John","Doe");
        assertNotNull(savedUserTwo, "User should've been saved in database when testing checkUniqueUsernameUniqueWithOtherUsersInDatabase!");

        assertEquals(userService.uniqueUserName("username323"),MethodOutcome.SUCCESS, "Username should not exist in database yet");
    }


    /*
     * Testing uniqueEmail method
     */

    /*
     * Testing cases where email is invalid
     */

    @Test
    public void checkInvalidEmailEmpty() {
        assertEquals(userService.uniqueEmail(""), MethodOutcome.EMAIL_INVALID_FORMAT,"Email should be invalid when empty");
    }

    @Test
    public void checkInvalidEmailMissingAtSymbol() {
        assertEquals(userService.uniqueEmail("invalidemail.com"), MethodOutcome.EMAIL_INVALID_FORMAT, "Email should be invalid without @ symbol");
    }

    @Test
    public void checkInvalidEmailMissingDomain() {
        assertEquals(userService.uniqueEmail("invalid@"), MethodOutcome.EMAIL_INVALID_FORMAT, "Email should be invalid without a domain");
    }

    @Test
    public void checkInvalidEmailMultipleAtSymbols() {
        assertEquals(userService.uniqueEmail("invalid@@example.com"), MethodOutcome.EMAIL_INVALID_FORMAT, "Email should be invalid with multiple @ symbols");
    }

    @Test
    public void checkInvalidEmailInvalidDomain() {
        assertEquals(userService.uniqueEmail("user@domain_without_dot"), MethodOutcome.EMAIL_INVALID_FORMAT, "Email should be invalid with an incorrect domain");
    }

    @Test
    public void checkInvalidEmailNoTLD() {
        assertEquals(userService.uniqueEmail("user@example"), MethodOutcome.EMAIL_INVALID_FORMAT, "Email should be invalid without a top-level domain");
    }

    @Test
    public void checkInvalidEmailNull() {
        assertEquals(userService.uniqueEmail(null), MethodOutcome.EMAIL_INVALID_FORMAT, "Email should be invalid when null");
    }

    @Test
    public void checkInvalidEmailOnlySpaces() {
        assertEquals(userService.uniqueEmail("   "), MethodOutcome.EMAIL_INVALID_FORMAT, "Email should be invalid when it only contains spaces");
    }

    @Test
    public void checkInvalidEmailLeadingTrailingSpaces() {
        assertEquals(userService.uniqueEmail("  email@example.com  "), MethodOutcome.EMAIL_INVALID_FORMAT, "Email should be invalid with leading/trailing spaces");
    }

    @Test
    public void checkInvalidEmailSpacesWithin() {
        assertEquals(userService.uniqueEmail("user name@example.com"), MethodOutcome.EMAIL_INVALID_FORMAT, "Email should be invalid when it contains spaces");
    }

    /*
     * Testing where email is valid
     */

    @Test
    public void checkValidEmailWithNumbers() {
        assertEquals(userService.uniqueEmail("user123@example.com"),MethodOutcome.SUCCESS, "Email should be valid with numbers");
    }

    @Test
    public void checkValidEmailWithDotsBeforeAt() {
        assertEquals(userService.uniqueEmail("first.last@example.com"),MethodOutcome.SUCCESS, "Email should be valid with dots before the @ symbol");
    }

    @Test
    public void checkValidEmailWithPlusSigns() {
        assertEquals(userService.uniqueEmail("user+alias@example.com"),MethodOutcome.SUCCESS, "Email should be valid with a plus sign");
    }

    @Test
    public void checkValidEmailWithUnderscores() {
        assertEquals(userService.uniqueEmail("first_last@example.com"),MethodOutcome.SUCCESS, "Email should be valid with underscores");
    }

    @Test
    public void checkValidEmailWithHyphens() {
        assertEquals(userService.uniqueEmail("user@sub-domain.com"),MethodOutcome.SUCCESS, "Email should be valid with hyphens in the domain");
    }

    @Test
    public void checkValidEmailWithMultipleDotsInDomain() {
        assertEquals(userService.uniqueEmail("user@mail.example.com"),MethodOutcome.SUCCESS, "Email should be valid with multiple dots in the domain");
    }

    @Test
    public void checkValidEmailWithTwoLetterTLD() {
        assertEquals(userService.uniqueEmail("user@example.io"),MethodOutcome.SUCCESS, "Email should be valid with a two-letter TLD");
    }

    @Test
    public void checkValidEmailWithThreeOrMoreLetterTLD() {
        assertEquals(userService.uniqueEmail("user@example.com"),MethodOutcome.SUCCESS, "Email should be valid with a three-letter TLD");
    }

    @Test
    public void checkValidEmailWithSpecialCharacters() {
        assertEquals(userService.uniqueEmail("user!name@example.com"),MethodOutcome.SUCCESS, "Email should be valid with allowed special characters");
    }

    @Test
    public void checkValidEmailWithMixedCase() {
        assertEquals(userService.uniqueEmail("User@Example.com"),MethodOutcome.SUCCESS, "Email should be valid with mixed case");
    }

    @Test
    public void checkValidEmailWithHyphenInDomain() {
        assertEquals(userService.uniqueEmail("user@-example.com"),MethodOutcome.SUCCESS, "Email should be valid with a hyphen at the start of the domain");
    }

    @Test
    public void checkValidEmailWithHyphenAtEndOfDomain() {
        assertEquals(userService.uniqueEmail("user@example-.com"),MethodOutcome.SUCCESS, "Email should be valid with a hyphen at the end of the domain");
    }

    @Test
    public void checkValidEmailAllNumeric() {
        assertEquals(userService.uniqueEmail("12345@domain.com"),MethodOutcome.SUCCESS, "Email should be valid with all numeric characters");
    }

    /*
     * Now testing saveUser method
     */

    // Saving a valid user
    @Test
    public void saveValidUser() {
        final String password = "password123";
        final String username = "testUser1";

        final User savedUser = userService.saveUser(username, "password123","test1@example.com", "John", "Doe");

        assertEquals("testUser1", savedUser.getUsername(),"username should match given username");
        assertEquals("test1@example.com", savedUser.getEmail(),"email should match given email");
        assertNotEquals(password, savedUser.getHashedPassword(),"Password should be hashed on creation of new user");
        assertEquals("John", savedUser.getFirstName(),"First name should match given first name");
        assertEquals("Doe", savedUser.getLastName(),"Last name should match given last name");
        assertNull(savedUser.getQuestion1(), "On user creation, security question 1 should be null");
        assertNull(savedUser.getAnswer1(), "On user creation, security question 1 answer should be null");
        assertNull(savedUser.getQuestion2(), "On user creation, security question 2 should be null");
        assertNull(savedUser.getAnswer2(), "On user creation, security question 2 answer should be null");

        assertNotNull(userService.findByUsername(username), "User should exist in database after saveUser is called!");
    }

    // Saving a user with a blank username
    @Test
    public void saveUserWithBlankUsernameDoesntSave() {
        final String username = "";
        final User savedUser = userService.saveUser(username, "password123", "test2@example.com","John", "Doe");

        assertNull(savedUser, "Expected null when username is blank");
        assertNull(userService.findByUsername(username), "User should not exist in database after saveUser is called with a empty username!");
    }

    // Saving a user with a blank email
    @Test
    public void saveUserWithBlankEmailDoesntSave() {
        final String username = "testUser2";
        final User savedUser = userService.saveUser("testUser2", "password123", "", "John", "Doe");

        assertNull(savedUser, "Expected null when email is blank");
        assertNull(userService.findByUsername(username), "User should not exist in database after saveUser is called with a empty email!");
    }

    // Saving a user with a blank first name
    @Test
    public void saveUserWithBlankFirstNameDoesntSave() {
        final String username = "testUser3";
        final User savedUser = userService.saveUser(username, "password123","test3@example.com", "", "Doe");

        assertNull(savedUser, "Expected null when first name is blank");
        assertNull(userService.findByUsername(username), "User should not exist in database after saveUser is called with a empty first name!");
    }

    // Saving a user with a blank last name
    @Test
    public void saveUserWithBlankLastNameDoesntSave() {
        final String username = "testUser4";
        final User savedUser = userService.saveUser(username, "password123","test4@example.com", "John", "");

        assertNull(savedUser, "Expected null when last name is blank");
        assertNull(userService.findByUsername(username), "User should not exist in database after saveUser is called with a empty first name!");
    }

    // Saving a user with a blank password
    @Test
    public void saveUserWithBlankPasswordDoesntSave() {
        final String username = "testUser5";
        final User savedUser = userService.saveUser(username, "","test5@example.com", "John", "Doe");

        assertNull(savedUser, "Expected null when password is blank");
        assertNull(userService.findByUsername(username), "User should not exist in database after saveUser is called with a empty password!");

    }

    // Saving a user with valid special characters in username
    @Test
    public void saveUserWithValidSpecialCharacterUsernameSavesUser() {
        final String username = "user.name";
        final User savedUser = userService.saveUser(username, "password123","test6@example.com", "John", "Doe");

        assertEquals("user.name", savedUser.getUsername(), "Users name should match regardless of special characters");
        assertNotNull(userService.findByUsername(username), "User should  exist in database after saveUser is called with username with special characters!");
    }

    // Saving a user with trailing and leading spaces in username
    @Test
    public void saveUserWithTrailingLeadingSpacesInUsernameSavesUser() {
        final String username = " testUser6 ";

        final User savedUser = userService.saveUser(username, "password123","test7@example.com", "John", "Doe");

        assertEquals("testUser6", savedUser.getUsername(), "Usernames should match regardless if there are trailing/leading spaces");
        assertNull(userService.findByUsername(username), "User should  exist in database after saveUser is called with username with trailing and leading spaces!");
    }

    // Saving a user with spaces in first name and last name
    @Test
    public void saveUserWithSpacesInNamesSavesUser() {
        final String username = "testUser7";
        final User savedUser = userService.saveUser(username, "password123","test8@example.com", "John ", "Doe ");

        assertEquals("John", savedUser.getFirstName(), "First names should match");
        assertEquals("Doe", savedUser.getLastName(), "Last names should match");
        assertNotNull(userService.findByUsername(username), "User should exist in database after saveUser is called with spaces in first and last names!");
    }

    @Test
    public void saveUserSaveUserDeleteUserThenSaveAgainShouldSaveUser(){
        final String username = "favoriteUser";
        final User savedUser = userService.saveUser(username, "password123","favUser@example.com", "John", "Doe");
        assertNotNull(userService.findByUsername(username), "User should exist in database after saveUser is called!");

        final boolean deletedUser = userService.deleteByEmail(savedUser.getEmail());
        assertTrue(deletedUser,"User should've been deleted in saveUserSaveUserDeleteUserThenSaveAgainShouldSaveUser");

        final User savedUserAgain = userService.saveUser(username, "password123","favUser@example.com", "John", "Doe");
        assertNotNull(userService.findByUsername(savedUserAgain.getUsername()), "User should exist in database after saving user, deleting user, then saveUser is called again!");
    }

    @Test
    public void saveUserSaveSameUserTwiceShouldFailSecondTime(){
        final String username = "favoriteUser2";
        final User savedUser = userService.saveUser(username, "password123","favUse2r@example.com", "John", "Doe");
        assertNotNull(userService.findByUsername(savedUser.getUsername()), "User should exist in database after saveUser is called!");

        final String usernameTwo = "favoriteUser2";
        final User savedUserAgain = userService.saveUser(usernameTwo, "password123","favUse2r@example.com", "John", "Doe");
        assertNull(savedUserAgain, "saveUser should return null when a user with the same information is already stored!");
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
        assertNotNull(newUser, "User should be saved in updateUserSecurityQuestionsSuccessfully");

        assertNotEquals(question1, newUser.getQuestion1(), "Security question 1 should not match before being updated!");
        assertNotEquals(answer1, newUser.getAnswer1(), "Security answer 1 should not match before being updated!");
        assertNotEquals(question2, newUser.getQuestion2(), "Security question 2 should not match before being updated!");
        assertNotEquals(answer2, newUser.getAnswer2(), "Security answer 2 should not match before being updated!");

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
        assertNotNull(newUser, "User should be saved in updateUserSecurityQuestionsWithBlankQuestion1");

        final boolean result = userService.updateUserSecurityQuestions(newUser, question1, answer1, question2, answer2);
        
        assertFalse(result, "Security questions update should fail due to blank question1");
        assertNotEquals(question1, newUser.getQuestion1(), "Security question 1 should not match when a question is left blank!");
        assertNotEquals(answer1, newUser.getAnswer1(), "Security answer 1 should not match when a question is left blank!");
        assertNotEquals(question2, newUser.getQuestion2(), "Security question 2 should not when a question is left blank!");
        assertNotEquals(answer2, newUser.getAnswer2(), "Security answer 2 should not match when a question is left blank!");
    }

    @Test
    public void updateUserSecurityQuestionsWithBlankAnswer1() {
        final String question1 = "What is your pet's name?";
        final String answer1 = "";
        final String question2 = "What is your hometown?";
        final String answer2 = "Springfield";

        final User newUser = userService.saveUser("testUser26", "password123","test26@example.com", "John ", "Doe ");
        assertNotNull(newUser, "User should be saved in updateUserSecurityQuestionsWithBlankAnswer1");

        final boolean result = userService.updateUserSecurityQuestions(newUser, question1, answer1, question2, answer2);
        
        assertFalse(result, "Security questions update should fail due to blank answer1");
        assertNotEquals(question1, newUser.getQuestion1(), "Security question 1 should not match when a answer is left blank!");
        assertNotEquals(answer1, newUser.getAnswer1(), "Security answer 1 should not match when a answer is left blank!");
        assertNotEquals(question2, newUser.getQuestion2(), "Security question 2 should not when a answer is left blank!");
        assertNotEquals(answer2, newUser.getAnswer2(), "Security answer 2 should not match when a answer is left blank!");
    }

    @Test
    public void updateUserSecurityQuestionsWithBlankQuestion2() {
        final String question1 = "What is your pet's name?";
        final String answer1 = "Fluffy";
        final String question2 = "";
        final String answer2 = "Springfield";

        final User newUser = userService.saveUser("testUser25", "password123","test25@example.com", "John ", "Doe ");
        assertNotNull(newUser, "User should be saved in updateUserSecurityQuestionsWithBlankQuestion2");

        final boolean result = userService.updateUserSecurityQuestions(newUser, question1, answer1, question2, answer2);
        
        assertFalse(result, "Security questions update should fail due to blank question2");
        assertNotEquals(question1, newUser.getQuestion1(), "Security question 1 should not match when question2 is left blank!");
        assertNotEquals(answer1, newUser.getAnswer1(), "Security answer 1 should not match when a question2 is left blank!");
        assertNotEquals(question2, newUser.getQuestion2(), "Security question 2 should not when a question2 is left blank!");
        assertNotEquals(answer2, newUser.getAnswer2(), "Security answer 2 should not match when a question2 is left blank!");
    }

    @Test
    public void updateUserSecurityQuestionsWithBlankAnswer2() {
        final String question1 = "What is your pet's name?";
        final String answer1 = "Fluffy";
        final String question2 = "What is your hometown?";
        final String answer2 = "";

        final User newUser = userService.saveUser("testUser15", "password123","test20@example.com", "John ", "Doe ");
        assertNotNull(newUser, "User should be saved in updateUserSecurityQuestionsWithBlankAnswer2");

        boolean result = userService.updateUserSecurityQuestions(newUser, question1, answer1, question2, answer2);
        
        assertFalse(result, "Security questions update should fail due to blank answer2");
        assertNotEquals(question1, newUser.getQuestion1(), "Security question 1 should not match when answer2 is left blank!");
        assertNotEquals(answer1, newUser.getAnswer1(), "Security answer 1 should not match when a answer2 is left blank!");
        assertNotEquals(question2, newUser.getQuestion2(), "Security question 2 should not when a answer2 is left blank!");
        assertNotEquals(answer2, newUser.getAnswer2(), "Security answer 2 should not match when a answer2 is left blank!");
    }

    /*
     * Testing findUserByEmail
     */

    @Test
    public void findByEmailSuccessfully() {
        final User newUser = userService.saveUser("testUser16", "password123","test16@example.com", "John ", "Doe ");
        assertNotNull(newUser, "User should be saved in findByEmailSuccessfully");

        final User foundUser = userService.findByEmail("test16@example.com");

        assertNotNull(foundUser, "User should be found");
        assertEquals(newUser.getEmail(), foundUser.getEmail(), "Emails should match");
    }

    @Test
    public void findByEmailIgnoringCase() {
        final User newUser = userService.saveUser("testUser12", "password123","test12@example.com", "John ", "Doe ");
        assertNotNull(newUser, "User should be saved in findByEmailIgnoringCase");

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
    public void updatePasswordValidNewPassword() {
        final User newUser = userService.saveUser("testUser17", "password123","test17@example.com", "John ", "Doe ");
        assertNotNull(newUser, "User should be saved in updatePasswordValidNewPassword");

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
        assertNotNull(newUser, "User should be saved in updatePasswordWithNullNewPassword");

        final String passwordBeforeUpdate = newUser.getHashedPassword();
        final boolean result = userService.updatePassword(newUser, null);

        assertFalse(result, "Updating password with null should return false");
        assertEquals(passwordBeforeUpdate, newUser.getHashedPassword(), "User's hashed password should not change");
    }

    @Test
    public void updatePasswordWithShortNewPassword() {
        final User newUser = userService.saveUser("duplicateUser21","password","email21@email.com","First","Last");
        assertNotNull(newUser, "User should be saved in updatePasswordWithShortNewPassword");

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
    public void findByIdWithPlaylistsUserExists() {
        final User newUser = userService.saveUser("duplicateUser23","password","email23@email.com","First","Last");
        assertNotNull(newUser, "User should be saved in findByIdWithPlaylistsUserExists");

        final Long userID = newUser.getuserID();
        final User foundUser = userService.findByIdWithPlaylists(userID);

        assertNotNull(foundUser, "User should be found");
        assertEquals(newUser.getUsername(), foundUser.getUsername(), "Usernames should match");
        assertEquals(newUser.getEmail(), foundUser.getEmail(), "Emails should match");
    }

    @Test
    public void findByIdWithPlaylistsUserDoesNotExist() {
        final Long userId = 123324334344L;
        final User foundUser = userService.findByIdWithPlaylists(userId);

        assertNull(foundUser, "User should not be found");
    }

    @Test
    public void findByIdWithPlaylistsNegativeUserId() {
        final Long negativeUserId = -1L;

        final User foundUser = userService.findByIdWithPlaylists(negativeUserId);

        assertNull(foundUser, "User should not be found for a negative ID");
    }

    /*
     * Testing findByUsername method
     */

    @Test
    public void findByUsernameExists() {
        final String username = "duplicateUser24";
        final User newUser = userService.saveUser(username,"password","email24@email.com","First","Last");
        assertNotNull(newUser, "User should be saved in findByUsernameExists");

        final User foundUser = userService.findByUsername(username);

        assertNotNull(foundUser, "User should be found");
        assertEquals(newUser.getUsername(), foundUser.getUsername(), "Usernames should match");
    }

    @Test
    public void findByUsernameDoesNotExist() {
        final User foundUser = userService.findByUsername("NathanWilliams");

        assertNull(foundUser, "User should not be found");
    }

    @Test
    void findExistingUserByUsernameReturnsUser() {
        final String username = "existinguser";
        User savedUser = userService.saveUser(username, "password123", "existing@example.com", "Existing", "User");
        assertNotNull(savedUser, "User should be saved successfully.");

        final User foundUser = userService.findByUsername(username);
        assertNotNull(foundUser, "User should be found.");
        assertEquals(savedUser.getUsername(), foundUser.getUsername(), "Found user should have the correct username.");
    }

    @Test
    void findNonExistentUserByUsernameReturnsNull() {
        final String username = "nonexistentuser";

        final User foundUser = userService.findByUsername(username);
        assertNull(foundUser, "Finding a non-existent user should return null.");
    }

    @Test
    void findUserByEmptyUsernameReturnsNull() {
        final String username = "";

        final User foundUser = userService.findByUsername(username);
        assertNull(foundUser, "Finding a user with an empty username should return null.");
    }

    @Test
    void findUserByNullUsernameReturnsNull() {
        final User foundUser = userService.findByUsername(null);
        assertNull(foundUser, "Finding a user with a null username should return null.");
    }

    @Test
    void findUserByUsernameWithSpecialCharactersReturnsUser() {
        final String username = "specialuser!@#";
        final User savedUser = userService.saveUser(username, "password123", "special@example.com", "Special", "Character");
        assertNotNull(savedUser, "User with special characters should be saved successfully.");

        final User foundUser = userService.findByUsername(username);
        assertNotNull(foundUser, "User with special characters should be found.");
        assertEquals(savedUser.getUsername(), foundUser.getUsername(), "Found user should have the correct username.");
    }

    @Test
    void findUserByUsernameWithDifferentCaseReturnsUser() {
        final String username = "caseSensitiveUser";
        final User savedUser = userService.saveUser(username, "password123", "case@example.com", "Case", "Sensitive");
        assertNotNull(savedUser, "Case-sensitive username should be saved successfully.");

        final User foundUser = userService.findByUsername(username.toUpperCase());
        assertNotNull(foundUser, "Finding with a differently cased username should return user regardless of case.");
    }

    /*
     * Testing deleteUserByEmail
     */

    @Test
    void deleteExistingUserShouldReturnTrue() {
        final String email = "test234@example.com";
        final User savedUser = userService.saveUser("testuser", "password123", email, "Test", "User");
        assertNotNull(savedUser, "User should be saved successfully.");
        assertTrue(userService.deleteByEmail(email), "Deleting an existing user should return true.");
    }

    @Test
    void deleteNonExistentUserShouldReturnFalse() {
        final String email = "nonexistent@example.com";
        assertFalse(userService.deleteByEmail(email), "Deleting a non-existent user should return false.");
    }

    @Test
    void deleteWithEmptyEmailShouldReturnFalse() {
        final String email = "";
        assertFalse(userService.deleteByEmail(email), "Deleting with an empty email should return false.");
    }

    @Test
    void deleteWithNullEmailShouldReturnFalse() {
        assertFalse(userService.deleteByEmail(null), "Deleting with a null email should return false.");
    }

    @Test
    void deleteUserWithSpecialCharacterEmailShouldReturnTrue() {
        final String email = "user+test@example.com";
        final User savedUser = userService.saveUser("specialuser", "password123", email, "Special", "Character");
        assertNotNull(savedUser, "User with special character email should be saved successfully.");
        assertTrue(userService.deleteByEmail(email), "Deleting a user with a special character email should return true.");
    }

    @Test
    void deleteUserWithCaseInsensitiveEmailShouldReturnTrueOrFalse() {
        final String email = "CaseSensitive@example.com";
        final User savedUser = userService.saveUser("caseuser", "password123", email, "Case", "Sensitive");
        assertNotNull(savedUser, "User with case-sensitive email should be saved successfully.");
        assertTrue(userService.deleteByEmail(email.toLowerCase()), "Deleting should return true regardless of case.");
    }

    @Test
    void deleteUserWithWhitespaceEmailShouldReturnFalse() {
        final String email = " user234@example.com ";
        final User savedUser = userService.saveUser("whitespaceuser", "password123", email.trim(), "White", "Space");
        assertNotNull(savedUser, "User with trimmed whitespace email should be saved successfully.");
        assertFalse(userService.deleteByEmail(email), "Deleting a user with whitespace in email should return false.");
    }

    @Test
    void deleteSameUserTwiceShouldReturnTrueThenFalse() {
        final String email = "duplicate@example.com";
        final User savedUser = userService.saveUser("duplicateuser", "password123", email, "Duplicate", "User");
        assertNotNull(savedUser, "User should be saved successfully.");
        assertTrue(userService.deleteByEmail(email), "First deletion should return true.");
        assertFalse(userService.deleteByEmail(email), "Second deletion should return false.");
    }
}