package edu.carroll.initMusic.jpa.model;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Tests the basic methods in the song class
 */
@SpringBootTest
public class SongTests {
    @Test
    public void testConstructorAndGetters() {
        final Long deezerID = 12345L;
        final String songName = "Test Song";
        final int length = 180;
        final String artistName = "Test Artist";
        final long artistDeezerID = 54321L;
        final String albumName = "Test Album";
        final long albumDeezerID = 98765L;

        final Song song = new Song(deezerID, songName, length, artistName, artistDeezerID, albumName, albumDeezerID);

        assertEquals(deezerID, song.getDeezerID(), "Deezer id should match one given");
        assertEquals(songName, song.getSongName(), "Song name should match one given");
        assertEquals(length, song.getLength(), "Length should match one given");
        assertEquals(artistName, song.getArtistName(), "Artist name should match one given");
        assertEquals(artistDeezerID, song.getArtistDeezerID(), "Artist deezer id should match one given");
        assertEquals(albumName, song.getAlbumName(), "Album name should match one given");
        assertEquals(albumDeezerID, song.getAlbumDeezerID(), "Album deezer id should match one given");
    }

    @Test
    public void testSetters() {
        final Song song = new Song();
        song.setDeezerID(12345L);
        song.setSongName("Updated Song");
        song.setLength(200);
        song.setArtistName("Updated Artist");
        song.setArtistDeezerID(54321L);
        song.setAlbumName("Updated Album");
        song.setAlbumDeezerID(98765L);
        song.setSongImg("http://example.com/img.jpg");
        song.setSongPreview("http://example.com/preview.mp3");

        assertEquals(12345L, song.getDeezerID(), "ID should equal one given");
        assertEquals("Updated Song", song.getSongName(), "Name should equal one given");
        assertEquals(200, song.getLength(), "Length should equal one given");
        assertEquals("Updated Artist", song.getArtistName(), "Artist Name should equal one given");
        assertEquals(54321L, song.getArtistDeezerID(), "Artist Deezer ID should equal one given");
        assertEquals("Updated Album", song.getAlbumName(), "Album Name should equal one given");
        assertEquals(98765L, song.getAlbumDeezerID(), "Album Deezer ID should equal one given");
        assertEquals("http://example.com/img.jpg", song.getSongImg(), "Song img should equal one given");
        assertEquals("http://example.com/preview.mp3", song.getSongPreview(), "Song preview should equal one given");
    }

    @Test
    public void testPlaylistManagement() {
        final Song song = new Song();

        song.addPlaylist(new Playlist());
        assertEquals(1, song.getPlaylists().size(), "Song should have 1 playlist");
    }

    @Test
    public void testEqualsAndHashCode() {
        final Song song1 = new Song(12345L, "Test Song", 180, "Test Artist", 54321L, "Test Album", 98765L);
        final Song song2 = new Song(12345L, "Test Song", 180, "Test Artist", 54321L, "Test Album", 98765L);

        assertEquals(song1, song2, "Songs should be equal");
        assertEquals(song1.hashCode(), song2.hashCode(), "Songs hashcode should be equal");
    }

    @Test
    public void testToString() {
        final Song song = new Song(12345L, "Test Song", 180, "Test Artist", 54321L, "Test Album", 98765L);
        final String expectedString = "Song{songName='Test Song', deezerID=12345, artistName='Test Artist', artistDeezerID=54321, albumName='Test Album', albumDeezerID=98765, length=180, songImg='null', songPreview='null'}";

        assertEquals(expectedString, song.toString(), "Song string should equal expected string");
    }

    @Test
    public void testQueryCachesManagement() {
        final Song song = new Song();

        song.addQueryCache(new QueryCache());
        assertEquals(1, song.getQueryCaches().size(), "Song should have one cache");
    }

}
