package edu.carroll.initMusic.jpa.model;

import static org.springframework.test.util.AssertionErrors.assertFalse;
import static org.springframework.test.util.AssertionErrors.assertTrue;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashSet;
import java.util.Set;

/**
 * <p>
 * This class is used to test the Album class
 * </p>
 *
 * @author Nick Clouse
 *
 * @since September 19, 2024
 */
@SpringBootTest
public class AlbumTests {
    /**
     * Album name
     */
    private static final String albumName = "Init to Win it";

    /**
     * Genre of album
     */
    private static final String genre = "Software Engineering";

    /**
     * Release date of album
     */
    private static final String releaseDate = "00/00/0000";

    /**
     * Album used for testing
     */
    private final Album fakeAlbum = new Album(albumName, genre, releaseDate);

    /**
     * Testing album creation and getters
     */
    @Test
    public void verifyCreationOfUserAndGetters(){
        final String setName = fakeAlbum.getAlbumName();
        final String setGenre = fakeAlbum.getGenre();
        final String setReleaseDate = fakeAlbum.getReleaseDate();
        final int setTotalLength = fakeAlbum.getTotalSongLength();
        final int numSongs = fakeAlbum.getNumberOfSongs();
        final Set<Song> songs = fakeAlbum.getSongs();
        
        assertTrue("Name should equal name from getter", albumName.equals(setName));
        assertTrue("Genre should equal genre from getter", genre.equals(setGenre));
        assertTrue("ReleaseDate should equal releaseDate from getter", releaseDate.equals(setReleaseDate));
        assertTrue("Total length of album should be 0", setTotalLength == 0);
        assertTrue("Number of songs should be 0", numSongs == 0);
        assertTrue("Size of album should be 0", songs.isEmpty());
    }

    /**
     * Verifies editing of album, like adding/removing songs, and adding/removing artists
     */
    @Test
    public void verifyAlbumEditing(){
        //Populating album with songs
        final Set<Song> songs = new HashSet<>();
        for(int i = 0; i < 3; i++){
            //String songName, String genre, String releaseDate, int length, int numberOfStreams
            final Song tempSong = new Song(1L,"Random" + i,"00/00/0000", 60,"Name",1,"Album",1);
            fakeAlbum.addSong(tempSong);
            songs.add(tempSong);
        }

        //Making sure there are songs in album and the totalSongLength is correct
        assertTrue("Three songs should be in album",fakeAlbum.getSongs().size() == 3);
        assertTrue("Album total length should be 3*60=180", fakeAlbum.getTotalSongLength() == 3*60);
        //Now making sure there are actually three songs
        for(Song s: songs){
            assertTrue("Song should be in album", fakeAlbum.containsSong(s));
        }
        //Now lets remove a song and see if that works
        final Object songToRemove = songs.toArray()[0];
        fakeAlbum.removeSong((Song)songToRemove);
        songs.remove((Song)songToRemove);
        for(Song s: songs){
            assertTrue("Should only be two songs in album", fakeAlbum.containsSong(s));
        }
        assertTrue("Two songs should be in album",fakeAlbum.getSongs().size() == 2);
        assertTrue("Album total length should be 2*60=120", fakeAlbum.getTotalSongLength() == 2*60);

        //Now, lets test adding and removing artists
        final Set<Artist> artists = new HashSet<>();
        for(int i = 0; i < 3; i++){
            //String songName, String genre, String releaseDate, int length, int numberOfStreams
            final Artist tempArtist = new Artist("Nathan Williams" + i);
            fakeAlbum.addArtist(tempArtist);
            artists.add(tempArtist);
        }

        assertTrue("Three artists should be in album",fakeAlbum.getArtists().size() == 3);
        //Now making the right artists were added
        for(Artist a: artists){
            assertTrue("Artist should be in album", fakeAlbum.madeByArtist(a));
        }

        //Let's remove an artist and see what happens
        final Object artistToRemove = artists.toArray()[0];
        fakeAlbum.removeArtist((Artist)artistToRemove);
        artists.remove((Artist)artistToRemove);
        for(Artist a: artists){
            assertTrue("Should only be two artists connected to album", fakeAlbum.madeByArtist(a));
        }
        assertTrue("Two Artists should be connected to album",fakeAlbum.getArtists().size() == 2);

    }

    /**
     * Testing equals, hashCode, and two setters.
     */
    @Test
    public void verifyMiscFunctions(){
        //Testing equals
        final Album fakeAlbumTwo = new Album(albumName, genre, releaseDate);
        assertTrue("Fake albums should be equal", fakeAlbum.equals(fakeAlbumTwo));
        fakeAlbumTwo.setAlbumName("Random");

        //Test equals function and
        assertFalse("Fake albums should not be equal", fakeAlbum.equals(fakeAlbumTwo));

        //Testing hashCode
        final int userHash = fakeAlbum.hashCode();
        final int userTwoHash = fakeAlbumTwo.hashCode();
        assertFalse("Fake albums hash should not be equal", userHash == userTwoHash);
    }
    
}
