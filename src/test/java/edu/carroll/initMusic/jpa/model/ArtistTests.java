package edu.carroll.initMusic.jpa.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashSet;
import java.util.Set;

import static org.springframework.test.util.AssertionErrors.assertFalse;
import static org.springframework.test.util.AssertionErrors.assertTrue;

/**
 * <p>
 * This class is used to test the Artist class
 * </p>
 *
 * @author Nick Clouse
 *
 * @since September 19, 2024
 */
@SpringBootTest
public class ArtistTests {
    /**
     * Artist first name
     */
    private static final String firstName = "Nathan";

    /**
     * Artist last name
     */
    private static final String lastName = "Williams";

    /**
     * Artist country
     */
    private static final String country = "USA";

    /**
     * Artist used for testing
     */
    private static Artist fakeArtist = new Artist(firstName, lastName, country);

    /**
     * Reinitialize fakeArtist before each test, so each test has a fresh
     * Artist for manipulating
     */
    @BeforeEach
    public void setUp() {
        // Initialize fakeArtist with no albums and the correct properties
        fakeArtist = new Artist(firstName, lastName, country);
    }

    /**
     * Testing artist creation and getters
     */
    @Test
    public void verifyCreationOfArtistAndGetters(){
        final String setFirstName = fakeArtist.getArtistFirstName();
        final String setLastName = fakeArtist.getArtistLastName();
        final String setCountry = fakeArtist.getCountry();
        final Set<Album> albums = fakeArtist.getAlbums();

        assertTrue("First name should match name from getter", setFirstName.equals(firstName));
        assertTrue("Last name should match name from getter", setLastName.equals(lastName));
        assertTrue("Country should match name from getter", setCountry.equals(country));
        assertTrue("Albums size should be 0", albums.isEmpty());
    }

    /**
     * Testing artist editing, like adding/removing songs and albums
     */
    @Test
    public void verifyArtistEditing(){
        //Populating artist with songs
        final Set<Song> songs = new HashSet<>();
        for(int i = 0; i < 3; i++){
            //String songName, String genre, String releaseDate, int length, int numberOfStreams
            final Song tempSong = new Song("Random" + i, "Software Engineering","00/00/0000", 60, 1);
            fakeArtist.addSong(tempSong);
            songs.add(tempSong);
        }

        //Making sure there are songs related to artist
        assertTrue("Three songs should be related to artist",fakeArtist.getSongs().size() == 3);
        //Now making sure there are actually three songs
        for(Song s: songs){
            assertTrue("Song should be related to artist", fakeArtist.createdSong(s));
        }

        //Now lets remove a song and see if that works
        final Object songToRemove = songs.toArray()[0];
        fakeArtist.removeSong((Song)songToRemove);
        songs.remove((Song)songToRemove);
        for(Song s: songs){
            assertTrue("Should only be two songs related to artist", fakeArtist.createdSong(s));
        }
        assertTrue("Two songs should be related to artist",fakeArtist.getNumberOfSongs()== 2);

        //Now, lets do the same thing but for albums
        //Populating artist with albums
        final Set<Album> albums = new HashSet<>();
        for(int i = 0; i < 3; i++){
            final Album tempAlbum = new Album("Name"+i,"Genre","00/00/0000");
            fakeArtist.addAlbum(tempAlbum);
            albums.add(tempAlbum);
        }

        //Making sure there are albums related to artist
        assertTrue("Three albums should be related to artist",fakeArtist.getNumberOfAlbums()==3);
        //Now making sure there are actually three albums
        for(Album a: albums){
            assertTrue("Album should be related to artist", fakeArtist.createdAlbum(a));
        }

        //Now lets remove a album and see if that works
        final Object albumToRemove = albums.toArray()[0];
        fakeArtist.removeAlbum((Album)albumToRemove);
        albums.remove((Album)albumToRemove);
        for(Album a: albums){
            assertTrue("Should only be two songs related to artist", fakeArtist.createdAlbum(a));
        }
        assertTrue("Two songs should be related to artist",fakeArtist.getNumberOfSongs()== 2);

    }

    /**
     * Testing equals, hashCode, and two setters.
     */
    @Test
    public void verifyMiscFunctions(){
        //Testing equals
        final Artist fakeArtistTwo = new Artist(firstName, lastName, country);
        assertTrue("Fake artists should be equal", fakeArtist.equals(fakeArtistTwo));
        fakeArtistTwo.setArtistID(10);

        //Test equals function and that setArtstID
        assertFalse("Fake artists should not be equal", fakeArtist.equals(fakeArtistTwo));

        //Testing hashCode
        final int userHash = fakeArtist.hashCode();
        final int userTwoHash = fakeArtistTwo.hashCode();
        assertFalse("Fake artists hash should not be equal", userHash == userTwoHash);
    }
}
