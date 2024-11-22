package edu.carroll.initMusic.jpa.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests the bast methods in QueryCache class
 */
public class QueryCacheTests {
    /** Querycache object to test */
    private QueryCache queryCache;

    /** Song to use when needed */
    private Song song1;

    /** Another song to use when needed */
    private Song song2;

    @BeforeEach
    public void setUp() {
        // Initialize QueryCache object and related test data
        queryCache = new QueryCache();
        queryCache.setQuery("Test Query");

        song1 = new Song(1L, "song", 23, "artist", 2L, "album name", 3L);
        song2 = new Song(2L, "song2", 2, "artist", 2L, "album name", 3L);
    }

    @Test
    public void testAddResults() {
        // Add songs to the results
        Set<Song> results = new HashSet<>();
        results.add(song1);
        results.add(song2);

        queryCache.setResults(results);

        // Verify the results are added
        assertEquals(2, queryCache.getResults().size(), "The results set should contain two songs.");
        assertTrue(queryCache.getResults().contains(song1), "The results set should contain song1.");
        assertTrue(queryCache.getResults().contains(song2), "The results set should contain song2.");
    }

    @Test
    public void testIsExpired_NotExpired() {
        // Set last updated to now
        queryCache.setLastUpdated(LocalDateTime.now());

        // Verify the cache is not expired
        assertFalse(queryCache.isExpired(), "The cache should not be expired immediately after being updated.");
    }

    @Test
    public void testIsExpired_Expired() {
        // Set last updated to more than 7 days ago
        queryCache.setLastUpdated(LocalDateTime.now().minusDays(8));

        // Verify the cache is expired
        assertTrue(queryCache.isExpired(), "The cache should be expired after 8 days.");
    }

    @Test
    public void testGetQueryCacheID() {
        // Set a QueryCacheID
        queryCache.setQueryCacheID(1L);

        // Verify the QueryCacheID is returned correctly
        assertEquals(1L, queryCache.getQueryCacheID(), "The QueryCacheID should match the value that was set.");
    }

    @Test
    public void testGetQuery() {
        // Set a query
        queryCache.setQuery("Another Test Query");

        // Verify the query is returned correctly
        assertEquals("Another Test Query", queryCache.getQuery(), "The query should match the value that was set.");
    }

    @Test
    public void testEqualsSameObject() {
        // Verify equals with the same object
        assertEquals(queryCache, queryCache, "An object should be equal to itself.");
    }

    @Test
    public void testEqualsDifferentObjects() {
        QueryCache anotherCache = new QueryCache();
        anotherCache.setQuery("Test Query");

        // Verify equals with another object with the same query
        assertEquals(queryCache, anotherCache, "Two QueryCache objects with the same query should be equal.");
    }

    @Test
    public void testEqualsDifferentQuery() {
        QueryCache anotherCache = new QueryCache();
        anotherCache.setQuery("Different Query");

        // Verify equals with another object with a different query
        assertNotEquals(queryCache, anotherCache, "Two QueryCache objects with different queries should not be equal.");
    }

    @Test
    public void testHashCodeSameQuery() {
        QueryCache anotherCache = new QueryCache();
        anotherCache.setQuery("Test Query");

        // Verify hashCode for objects with the same query
        assertEquals(queryCache.hashCode(), anotherCache.hashCode(), "Hash codes for objects with the same query should match.");
    }

    @Test
    public void testHashCodeDifferentQuery() {
        QueryCache anotherCache = new QueryCache();
        anotherCache.setQuery("Different Query");

        // Verify hashCode for objects with different queries
        assertNotEquals(queryCache.hashCode(), anotherCache.hashCode(), "Hash codes for objects with different queries should not match.");
    }

    @Test
    public void testToString() {
        queryCache.setQueryCacheID(1L);

        String expectedString = "QueryCache{QueryCacheID=1, query='Test Query', number of songs=0}";

        // Verify the toString method
        assertEquals(expectedString, queryCache.toString(), "The toString method should return the correct string representation.");
    }
}

