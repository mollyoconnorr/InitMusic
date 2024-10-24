package edu.carroll.initMusic.jpa.service;

import edu.carroll.initMusic.ResponseStatus;
import edu.carroll.initMusic.jpa.model.User;
import edu.carroll.initMusic.jpa.repo.UserRepository;
import edu.carroll.initMusic.service.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
public class UserServiceTests {

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private UserServiceImpl userService;

    private User validUser;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        validUser = new User();
        validUser.setUsername("testUser");
        validUser.setEmail("test@example.com");
        validUser.setFirstName("John");
        validUser.setLastName("Doe");
        validUser.setHashedPassword("hashedPassword");
    }

    /*
     * Basic tests
     */

    @Test
    public void checkUniqueUsernameTrue() {
        when(userRepository.findByUsernameIgnoreCase("newUser")).thenReturn(Collections.emptyList());
        assertSame(userService.uniqueUserName("newUser"), ResponseStatus.SUCCESS, "Username should be available");
    }

    @Test
    public void checkUniqueUsernameFalse() {
        when(userRepository.findByUsernameIgnoreCase("existingUser")).thenReturn(List.of(new User()));
        assertNotSame(userService.uniqueUserName("existingUser"), ResponseStatus.SUCCESS, "Username should already exist");
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

    /**
     * This checks that all strings considered too long are actually prevented from being deemed 'unique'.
     * It checks from 50 (Max length of a username for us) to 255 (Max limit of a varchar in mysql)
     */
    @Test
    public void checkUniqueUsernameInValidName50to250Characters(){
        for (int i = 50; i <= 255; i++) {
            String name = "K".repeat(i); // Create a string of 'K's of length i
            assertNotEquals(userService.uniqueUserName(name), ResponseStatus.SUCCESS,
                    String.format("Iteration %d: Username with length %d should not be valid as it's too long", i, name.length()));
        }
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
            when(userRepository.findByUsernameIgnoreCase(name)).thenReturn(Collections.emptyList());
            assertSame(userService.uniqueUserName(name), ResponseStatus.SUCCESS,
                    String.format("Username '%s' is valid and shouldn't already exist", name));
        }
    }

    @Test
    public void checkUniqueUsernameValidWithNumbers() {
        when(userRepository.findByUsernameIgnoreCase("validUser123")).thenReturn(Collections.emptyList());
        assertSame(userService.uniqueUserName("validUser123"), ResponseStatus.SUCCESS, "Username should be available");
    }

    @Test
    public void checkUniqueUsernameValidWithSpecialCharacters() {
        when(userRepository.findByUsernameIgnoreCase("user!@#")).thenReturn(Collections.emptyList());
        assertSame(userService.uniqueUserName("user!@#"), ResponseStatus.SUCCESS, "Username should be available");
    }

    @Test
    public void checkUniqueUsernameValidWithAlphanumericAndSpecialCharacters() {
        when(userRepository.findByUsernameIgnoreCase("User123!@")).thenReturn(Collections.emptyList());
        assertSame(userService.uniqueUserName("User123!@"), ResponseStatus.SUCCESS, "Username should be available");
    }

    @Test
    public void checkUniqueUsernameValidWithUnderscore() {
        when(userRepository.findByUsernameIgnoreCase("user_name")).thenReturn(Collections.emptyList());
        assertSame(userService.uniqueUserName("user_name"), ResponseStatus.SUCCESS, "Username should be available");
    }

    @Test
    public void checkUniqueUsernameValidWithDash() {
        when(userRepository.findByUsernameIgnoreCase("user-name")).thenReturn(Collections.emptyList());
        assertSame(userService.uniqueUserName("user-name"), ResponseStatus.SUCCESS, "Username should be available");
    }

    @Test
    public void checkUniqueUsernameValidWithMixedCase() {
        when(userRepository.findByUsernameIgnoreCase("UsEr123")).thenReturn(Collections.emptyList());
        assertSame(userService.uniqueUserName("UsEr123"), ResponseStatus.SUCCESS, "Username should be available");
    }

    @Test
    public void checkUniqueUsernameValidLongUsername() {
        String username = "validUserWithLongLength123!@#"; // 30 characters
        when(userRepository.findByUsernameIgnoreCase(username)).thenReturn(Collections.emptyList());
        assertSame(userService.uniqueUserName(username), ResponseStatus.SUCCESS, "Username should be available");
    }

    @Test
    public void checkUniqueUsernameValidMixedCharacters() {
        when(userRepository.findByUsernameIgnoreCase("user_123$%^&*")).thenReturn(Collections.emptyList());
        assertSame(userService.uniqueUserName("user_123$%^&*"), ResponseStatus.SUCCESS, "Username should be available");
    }

    /*
     * Testing usernames that are not unique (already exist in the database)
     */

    @Test
    public void checkUniqueUsernameNotUniqueWithSameCase() {
        when(userRepository.findByUsernameIgnoreCase("duplicateUser")).thenReturn(List.of(new User()));
        assertSame(userService.uniqueUserName("duplicateUser"), ResponseStatus.USER_ALREADY_EXISTS, "Username should already exist (same case)");
    }

    @Test
    public void checkUniqueUsernameNotUniqueWithDifferentCase() {
        when(userRepository.findByUsernameIgnoreCase("DUPLICATEUSER")).thenReturn(List.of(new User()));
        assertSame(userService.uniqueUserName("DUPLICATEUSER"), ResponseStatus.USER_ALREADY_EXISTS, "Username should already exist (different case)");
    }

    @Test
    public void checkUniqueUsernameNotUniqueWithNumbers() {
        when(userRepository.findByUsernameIgnoreCase("user123")).thenReturn(List.of(new User()));
        assertSame(userService.uniqueUserName("user123"), ResponseStatus.USER_ALREADY_EXISTS, "Username should already exist with numbers");
    }

    @Test
    public void checkUniqueUsernameNotUniqueWithSpecialCharacters() {
        when(userRepository.findByUsernameIgnoreCase("user!@#")).thenReturn(List.of(new User()));
        assertSame(userService.uniqueUserName("user!@#"), ResponseStatus.USER_ALREADY_EXISTS, "Username should already exist with special characters");
    }

    @Test
    public void checkUniqueUsernameNotUniqueWithUnderscore() {
        when(userRepository.findByUsernameIgnoreCase("user_name")).thenReturn(List.of(new User()));
        assertSame(userService.uniqueUserName("user_name"), ResponseStatus.USER_ALREADY_EXISTS, "Username should already exist with underscore");
    }

    @Test
    public void checkUniqueUsernameNotUniqueWithDash() {
        when(userRepository.findByUsernameIgnoreCase("user-name")).thenReturn(List.of(new User()));
        assertSame(userService.uniqueUserName("user-name"), ResponseStatus.USER_ALREADY_EXISTS, "Username should already exist with dash");
    }

    @Test
    public void checkUniqueUsernameNotUniqueWithMixedSymbols() {
        when(userRepository.findByUsernameIgnoreCase("user!$%^&*()")).thenReturn(List.of(new User()));
        assertSame(userService.uniqueUserName("user!$%^&*()"), ResponseStatus.USER_ALREADY_EXISTS, "Username should already exist with mixed symbols");
    }

    @Test
    public void checkUniqueUsernameNotUniqueWithConsecutiveSpecialCharacters() {
        when(userRepository.findByUsernameIgnoreCase("user!!duplicate")).thenReturn(List.of(new User()));
        assertSame(userService.uniqueUserName("user!!duplicate"), ResponseStatus.USER_ALREADY_EXISTS, "Username should already exist with consecutive special characters");
    }

    @Test
    public void checkUniqueUsernameNotUniqueVeryLong() {
        String username = "veryLongDuplicateUsername1234567890!@#$";
        when(userRepository.findByUsernameIgnoreCase(username)).thenReturn(List.of(new User()));
        assertSame(userService.uniqueUserName(username), ResponseStatus.USER_ALREADY_EXISTS, "Username should already exist (very long)");
    }

    @Test
    public void checkUniqueUsernameNotUniqueWithNumbersAtEnd() {
        when(userRepository.findByUsernameIgnoreCase("user456")).thenReturn(List.of(new User()));
        assertSame(userService.uniqueUserName("user456"), ResponseStatus.USER_ALREADY_EXISTS, "Username should already exist with numbers at the end");
    }

    /*
     * Testing uniqueEmail method
     */

    /*
     * Basic Tests of uniqueEmail
     */

    @Test
    public void checkUniqueEmailTrue() {
        when(userRepository.findByEmailIgnoreCase("new@example.com")).thenReturn(Collections.emptyList());
        assertFalse(userService.uniqueEmail("new@example.com").failed(), "Email should be available");
    }

    @Test
    public void checkUniqueEmailFalse() {
        when(userRepository.findByEmailIgnoreCase("existing@example.com")).thenReturn(List.of(new User()));
        assertTrue(userService.uniqueEmail("existing@example.com").failed(), "Email should already exist");
    }

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
        when(passwordEncoder.encode("password123")).thenReturn("hashedPassword");
        when(userRepository.save(any(User.class))).thenReturn(validUser);

        final User savedUser = userService.saveUser("testUser", "test@example.com", "John", "Doe", "password123");

        assertEquals("testUser", savedUser.getUsername());
        assertEquals("test@example.com", savedUser.getEmail());
        assertEquals("hashedPassword", savedUser.getHashedPassword());
        assertEquals("John", savedUser.getFirstName());
        assertEquals("Doe", savedUser.getLastName());
        assertNull(savedUser.getQuestion1());
        assertNull(savedUser.getAnswer1());
        assertNull(savedUser.getQuestion2());
        assertNull(savedUser.getAnswer2());
        verify(userRepository, times(1)).save(any(User.class));
    }

    // Saving a user with a blank username
    @Test
    public void saveUserWithBlankUsername() {
        final User savedUser = userService.saveUser("", "test@example.com", "John", "Doe", "password123");

        assertNull(savedUser, "Expected null when username is blank");
    }

    // Saving a user with a blank email
    @Test
    public void saveUserWithBlankEmail() {
        final User savedUser = userService.saveUser("testUser", "", "John", "Doe", "password123");

        assertNull(savedUser, "Expected null when email is blank");
    }

    // Saving a user with a blank first name
    @Test
    public void saveUserWithBlankFirstName() {
        final User savedUser = userService.saveUser("testUser", "test@example.com", "", "Doe", "password123");

        assertNull(savedUser, "Expected null when first name is blank");
    }

    // Saving a user with a blank last name
    @Test
    public void saveUserWithBlankLastName() {
        final User savedUser = userService.saveUser("testUser", "test@example.com", "John", "", "password123");

        assertNull(savedUser, "Expected null when last name is blank");
    }

    // Saving a user with a blank password
    @Test
    public void saveUserWithBlankPassword() {
        final User savedUser = userService.saveUser("testUser", "test@example.com", "John", "Doe", "");

        assertNull(savedUser, "Expected null when password is blank");
    }

    // Saving a user with valid special characters in username
    @Test
    public void saveUserWithValidSpecialCharacterUsername() {
        when(passwordEncoder.encode("password123")).thenReturn("hashedPassword");
        validUser.setUsername("user.name");
        when(userRepository.save(any(User.class))).thenReturn(validUser);

        final User savedUser = userService.saveUser("user.name", "test@example.com", "John", "Doe", "password123");

        assertEquals("user.name", savedUser.getUsername());
        verify(userRepository, times(1)).save(any(User.class));
    }

    // Saving a user with trailing spaces in username
    @Test
    public void saveUserWithTrailingSpacesInUsername() {
        when(passwordEncoder.encode("password123")).thenReturn("hashedPassword");
        when(userRepository.save(any(User.class))).thenReturn(validUser);

        final User savedUser = userService.saveUser(" testUser ", "test@example.com", "John", "Doe", "password123");

        assertEquals("testUser", savedUser.getUsername());
        verify(userRepository, times(1)).save(any(User.class));
    }

    // Saving a user with spaces in first name and last name
    @Test
    public void saveUserWithSpacesInNames() {
        when(passwordEncoder.encode("password123")).thenReturn("hashedPassword");
        when(userRepository.save(any(User.class))).thenReturn(validUser);

        final User savedUser = userService.saveUser("testUser", "test@example.com", "John ", "Doe ", "password123");

        assertEquals("John", savedUser.getFirstName());
        assertEquals("Doe", savedUser.getLastName());
        verify(userRepository, times(1)).save(any(User.class));
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

        final boolean result = userService.updateUserSecurityQuestions(validUser, question1, answer1, question2, answer2);
        
        assertTrue(result, "Security questions should be updated successfully");
        assertEquals(question1, validUser.getQuestion1());
        assertEquals(answer1, validUser.getAnswer1());
        assertEquals(question2, validUser.getQuestion2());
        assertEquals(answer2, validUser.getAnswer2());
        verify(userRepository, times(1)).save(validUser);
    }

    @Test
    public void updateUserSecurityQuestionsWithBlankQuestion1() {
        final String question1 = "";
        final String answer1 = "Fluffy";
        final String question2 = "What is your hometown?";
        final String answer2 = "Springfield";

        final boolean result = userService.updateUserSecurityQuestions(validUser, question1, answer1, question2, answer2);
        
        assertFalse(result, "Security questions update should fail due to blank question1");
        verify(userRepository, never()).save(validUser);
    }

    @Test
    public void updateUserSecurityQuestionsWithBlankAnswer1() {
        final String question1 = "What is your pet's name?";
        final String answer1 = "";
        final String question2 = "What is your hometown?";
        final String answer2 = "Springfield";

        final boolean result = userService.updateUserSecurityQuestions(validUser, question1, answer1, question2, answer2);
        
        assertFalse(result, "Security questions update should fail due to blank answer1");
        verify(userRepository, never()).save(validUser);
    }

    @Test
    public void updateUserSecurityQuestionsWithBlankQuestion2() {
        final String question1 = "What is your pet's name?";
        final String answer1 = "Fluffy";
        final String question2 = "";
        final String answer2 = "Springfield";

        final boolean result = userService.updateUserSecurityQuestions(validUser, question1, answer1, question2, answer2);
        
        assertFalse(result, "Security questions update should fail due to blank question2");
        verify(userRepository, never()).save(validUser);
    }

    @Test
    public void updateUserSecurityQuestionsWithBlankAnswer2() {
        final String question1 = "What is your pet's name?";
        final String answer1 = "Fluffy";
        final String question2 = "What is your hometown?";
        final String answer2 = "";
        
        boolean result = userService.updateUserSecurityQuestions(validUser, question1, answer1, question2, answer2);
        
        assertFalse(result, "Security questions update should fail due to blank answer2");
        verify(userRepository, never()).save(validUser);
    }

    /*
     * Testing findUserByEmail
     */

    @Test
    public void findByEmailSuccessfully() {
        when(userRepository.findByEmailIgnoreCase("test@example.com")).thenReturn(List.of(validUser));

        final User foundUser = userService.findByEmail("test@example.com");

        assertNotNull(foundUser, "User should be found");
        assertEquals(validUser.getEmail(), foundUser.getEmail());
        verify(userRepository, times(1)).findByEmailIgnoreCase("test@example.com");
    }

    @Test
    public void findByEmailIgnoringCase() {
        when(userRepository.findByEmailIgnoreCase("TEST@EXAMPLE.COM")).thenReturn(List.of(validUser));
        
        final User foundUser = userService.findByEmail("TEST@EXAMPLE.COM");

        assertNotNull(foundUser, "User should be found");
        assertEquals(validUser.getEmail(), foundUser.getEmail());
        verify(userRepository, times(1)).findByEmailIgnoreCase("TEST@EXAMPLE.COM");
    }

    @Test
    public void findByEmailWhenNoUserFound() {
        when(userRepository.findByEmailIgnoreCase("nonexistent@example.com")).thenReturn(Collections.emptyList());

        final User foundUser = userService.findByEmail("nonexistent@example.com");

        assertNull(foundUser, "User should not be found");
        verify(userRepository, times(1)).findByEmailIgnoreCase("nonexistent@example.com");
    }

    @Test
    public void findByEmailWithNullEmail() {
        final User foundUser = userService.findByEmail(null);

        assertNull(foundUser, "User should not be found for null email");
        verify(userRepository, never()).findByEmailIgnoreCase(anyString()); // Should not call the repository
    }

    @Test
    public void findByEmailWithEmptyEmail() {
        when(userRepository.findByEmailIgnoreCase("")).thenReturn(Collections.emptyList());
        
        final User foundUser = userService.findByEmail("");
        
        assertNull(foundUser, "User should not be found for empty email");
        verify(userRepository, times(1)).findByEmailIgnoreCase(anyString()); // Should not call the repository
    }

    @Test
    public void findByEmailWhenMultipleUsersFound() {
        final User anotherUser = new User();
        anotherUser.setUsername("anotherUser");
        anotherUser.setEmail("test@example.com");
        when(userRepository.findByEmailIgnoreCase("test@example.com")).thenReturn(List.of(validUser, anotherUser));
        
        final User foundUser = userService.findByEmail("test@example.com");

        assertNotNull(foundUser, "User should be found");
        assertEquals(validUser.getEmail(), foundUser.getEmail()); // Check that the first user is returned
        verify(userRepository, times(1)).findByEmailIgnoreCase("test@example.com");
    }

    /*
     * Testing updatePassword
     */

    @Test
    public void updatePasswordSuccessfully() {
        final String newPassword = "newPassword123";
        final String hashedPassword = "newHashedPassword";

        when(passwordEncoder.encode(newPassword)).thenReturn(hashedPassword);
        when(userRepository.save(validUser)).thenReturn(validUser); // Simulate saving the user

        final boolean result = userService.updatePassword(validUser, newPassword);

        assertTrue(result, "Password should be updated successfully");
        assertEquals(hashedPassword, validUser.getHashedPassword(), "User's hashed password should be updated");
        verify(passwordEncoder, times(1)).encode(newPassword); // Ensure password was encoded
        verify(userRepository, times(1)).save(validUser); // Ensure user was saved
    }

    @Test
    public void updatePasswordWithNullUser() {
        final String newPassword = "newPassword123";

        // Act & Assert
        assertThrows(NullPointerException.class, () -> userService.updatePassword(null, newPassword), "Updating password for a null user should throw NullPointerException");
    }

    @Test
    public void updatePasswordWithNullNewPassword() {
        final boolean result = userService.updatePassword(validUser, null);

        assertFalse(result, "Updating password with null should return false");
        assertEquals("hashedPassword", validUser.getHashedPassword(), "User's hashed password should not change");
        verify(passwordEncoder, times(0)).encode(null); // Ensure encode was not called
        verify(userRepository, times(0)).save(validUser); // Ensure save was not called
    }

    @Test
    public void updatePasswordWithShortNewPassword() {
        final String newPassword = "short"; // Less than 8 characters

        final boolean result = userService.updatePassword(validUser, newPassword);

        assertFalse(result, "Updating password with a short password should return false");
        assertEquals("hashedPassword", validUser.getHashedPassword(), "User's hashed password should not change");
        verify(passwordEncoder, times(0)).encode(newPassword); // Ensure encode was not called
        verify(userRepository, times(0)).save(validUser); // Ensure save was not called
    }

    @Test
    public void updatePasswordWithValidNewPasswordAfterShortPassword() {
        final String newPassword = "validPassword123"; // Valid password
        final String hashedPassword = "newHashedPassword";

        when(passwordEncoder.encode(newPassword)).thenReturn(hashedPassword);

        final boolean result = userService.updatePassword(validUser, newPassword);

        assertTrue(result, "Password should be updated successfully");
        assertEquals(hashedPassword, validUser.getHashedPassword(), "User's hashed password should be updated");
        verify(passwordEncoder, times(1)).encode(newPassword); // Ensure password was encoded
        verify(userRepository, times(1)).save(validUser); // Ensure user was saved
    }

    @Test
    public void updatePasswordCallsRepositoryOnce() {
        final String newPassword = "validPassword123";
        when(passwordEncoder.encode(newPassword)).thenReturn("hashedPassword");

        userService.updatePassword(validUser, newPassword);

        verify(userRepository, times(1)).save(validUser); // Ensure save was called only once
    }




//    public void testSaveUser() {
//        RegistrationForm form = new RegistrationForm();
//        form.setUsername("newUser");
//        form.setEmail("new@example.com");
//        form.setPassword("password");
//        form.setFirstName("First");
//        form.setLastName("Last");
//
//        when(passwordEncoder.encode("password")).thenReturn("hashedPassword");
//
//        // Mock the user to be returned from the repository
//        User mockUser = new User();
//        mockUser.setUsername("newUser");
//        mockUser.setHashedPassword("hashedPassword");
//        mockUser.setEmail("new@example.com");
//        mockUser.setFirstName("First");
//        mockUser.setLastName("Last");
//
//        when(userRepository.save(any(User.class))).thenReturn(mockUser);
//
//        User savedUser = userService.saveUser(form);
//        assertEquals("newUser", savedUser.getUsername(), "User should have been saved with the correct username");
//        assertEquals("hashedPassword", savedUser.getHashedPassword(), "Password should be hashed");
//    }

    @Test
    public void testUpdatePassword() {
        User user = new User();
        user.setHashedPassword("oldHashedPassword");

        when(passwordEncoder.encode("newPassword")).thenReturn("newHashedPassword");

        userService.updatePassword(user, "newPassword");
        assertEquals("newHashedPassword", user.getHashedPassword(), "Password should be updated with new hash");
    }
}