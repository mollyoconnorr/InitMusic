package edu.carroll.initMusic.jpa.model;

import static org.springframework.test.util.AssertionErrors.assertFalse;
import static org.springframework.test.util.AssertionErrors.assertTrue;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashSet;
import java.util.Set;

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
     * Fake release Date
     */
    private static final String releaseDate = "09/19/2024";

    /**
     * Fake length of song
     */
    private static final int length = 180;

    /**
     * Fake song for testing
     */
    private final Song fakeSong = new Song(songName, releaseDate,length);

    /**
     * Testing song creation and getters
     */
    @Test
    public void verifyCreationOfUserAndGetters(){
        final String setName = fakeSong.getSongName();
        final String setReleaseDate = fakeSong.getReleaseDate();
        final int setLength = fakeSong.getLength();

        assertTrue("Set name matches name from getter", setName.equals(songName));
        assertTrue("Set release date matches release date from getter", setReleaseDate.equals(releaseDate));
        assertTrue("Set length matches length from getter", setLength == length);
    }

    /**
     * Testing functions related to artists
     */
    @Test
    public void verifyArtistFunctions(){
        final Artist fakeArtist = new Artist();

        //Testing add artist, getArtists, and isCreatedBy
        fakeSong.addArtist(fakeArtist);
        assertTrue("There should be one artist attached to the song", fakeSong.getArtists().size() == 1 && fakeSong.isCreatedBy(fakeArtist));

        //Testing remove artist
        assertTrue("There should be no artist attached to the song", fakeSong.removeArtist(fakeArtist) && fakeSong.getArtists().isEmpty());

        //Testing setting artists to new set
        final Set<Artist> artists = new HashSet<>();
        artists.add(fakeArtist);
        final Artist fakeArtistTwo = new Artist("name");
        artists.add(fakeArtistTwo);
        fakeSong.setArtists(artists);
        assertTrue("There should be two artists attached to the song", fakeSong.getArtists().size() == 2);
    }

    /**
     * Testing functions related to Album class
     */
    @Test
    public void verifyAlbumFunctions(){
        final Album fakeAlbum = new Album();
        fakeSong.setAlbum(fakeAlbum);
        assertTrue("There should be an album attached to the song", fakeSong.getAlbum() == fakeAlbum);
    }

    /**
     * Testing equals, hashcode functions
     */
    @Test
    public void verifyMiscFunctions(){
        final Song fakeSongTwo = new Song(songName, releaseDate, length);
        assertTrue("Songs should be equal", fakeSongTwo.equals(fakeSong));

        fakeSongTwo.setSongName("NewName");
        assertFalse("Songs should not be equal", fakeSongTwo.equals(fakeSong));

        assertTrue("Songs shouldn't be equal", fakeSong.hashCode() != fakeSongTwo.hashCode());
    }

}
