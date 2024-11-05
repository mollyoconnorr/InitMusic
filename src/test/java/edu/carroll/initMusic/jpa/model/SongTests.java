package edu.carroll.initMusic.jpa.model;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.springframework.test.util.AssertionErrors.assertTrue;

/**
 * <p>
 * This class is used to test the Song class
 * </p>
 *
 * @author Nick Clouse
 *
 * @since September 19, 2024
 */
@SpringBootTest
public class SongTests {
    /**
     * Fake song name
     */
    private static final String songName = "Fake Song";

    /**
     * Fake length of song
     */
    private static final int length = 180;

    /**
     * Fake song for testing
     */
    private final Song fakeSong = new Song(1L,songName,length, "name",1,"album",1);

    /**
     * Testing song creation and getters
     */
    @Test
    public void verifyCreationOfUserAndGetters(){
        final String setName = fakeSong.getSongName();
        final int setLength = fakeSong.getLength();

        assertTrue("Set name matches name from getter", setName.equals(songName));
        assertTrue("Set length matches length from getter", setLength == length);
    }

}
