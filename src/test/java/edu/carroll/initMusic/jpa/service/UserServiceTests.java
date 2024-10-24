package edu.carroll.initMusic.jpa.service;

import edu.carroll.initMusic.ResponseStatus;
import edu.carroll.initMusic.jpa.model.User;
import edu.carroll.initMusic.jpa.repo.UserRepository;
import edu.carroll.initMusic.service.UserServiceImpl;
import edu.carroll.initMusic.web.form.RegistrationForm;
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
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;

@SpringBootTest
public class UserServiceTests {

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private UserServiceImpl userService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this); // Optional with @MockBean; kept for clarity
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











    @Test
    public void checkUniqueUsernameSpecialCharacter(){
        when(userRepository.findByUsernameIgnoreCase("!!!!!")).thenReturn(Collections.emptyList());
        assertEquals(userService.uniqueUserName("!!!!!"), ResponseStatus.SUCCESS, "Username should be available regardless of it its special character");
    }






    @Test
    public void checkUniqueEmailTrue() {
        when(userRepository.findByEmailIgnoreCase("new@example.com")).thenReturn(Collections.emptyList());
        assertTrue(userService.uniqueEmail("new@example.com"), "Email should be available");
    }

    @Test
    public void checkUniqueEmailFalse() {
        when(userRepository.findByEmailIgnoreCase("existing@example.com")).thenReturn(List.of(new User()));
        assertFalse(userService.uniqueEmail("existing@example.com"), "Email should already exist");
    }

    @Test
    public void testSaveUser() {
        RegistrationForm form = new RegistrationForm();
        form.setUsername("newUser");
        form.setEmail("new@example.com");
        form.setPassword("password");
        form.setFirstName("First");
        form.setLastName("Last");

        when(passwordEncoder.encode("password")).thenReturn("hashedPassword");

        // Mock the user to be returned from the repository
        User mockUser = new User();
        mockUser.setUsername("newUser");
        mockUser.setHashedPassword("hashedPassword");
        mockUser.setEmail("new@example.com");
        mockUser.setFirstName("First");
        mockUser.setLastName("Last");

        when(userRepository.save(any(User.class))).thenReturn(mockUser);

        User savedUser = userService.saveUser(form);
        assertEquals("newUser", savedUser.getUsername(), "User should have been saved with the correct username");
        assertEquals("hashedPassword", savedUser.getHashedPassword(), "Password should be hashed");
    }

    @Test
    public void testUpdatePassword() {
        User user = new User();
        user.setHashedPassword("oldHashedPassword");

        when(passwordEncoder.encode("newPassword")).thenReturn("newHashedPassword");

        userService.updatePassword(user, "newPassword");
        assertEquals("newHashedPassword", user.getHashedPassword(), "Password should be updated with new hash");
    }
}