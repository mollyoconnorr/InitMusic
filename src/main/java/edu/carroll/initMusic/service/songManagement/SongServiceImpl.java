package edu.carroll.initMusic.service.songManagement;

import edu.carroll.initMusic.MethodOutcome;
import edu.carroll.initMusic.jpa.model.QueryCache;
import edu.carroll.initMusic.jpa.model.Song;
import edu.carroll.initMusic.jpa.repo.QueryCacheRepository;
import edu.carroll.initMusic.jpa.repo.SongRepository;
import org.apache.commons.text.similarity.JaroWinklerDistance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

/**
 * This services handles Searching for songs using the deezer api.
 */
@Service
public class SongServiceImpl implements SongService {
    /** Maximum length a query can be */
    private static final int MAX_QUERY_LENGTH = 40;

    /** Minimum length a query can be */
    private static final int MIN_QUERY_LENGTH = 3;

    /** Logger object used for logging */
    private static final Logger log = LoggerFactory.getLogger(SongServiceImpl.class);
    /** QueryCache repository */
    private final QueryCacheRepository queryCacheRepository;
    /** Song repository */
    private final SongRepository songRepository;
    /** Service used to search externally for songs */
    private final SongSearchService songSearchService;
    /**
     * JaroWinklerDistance object for calculating differences between strings. Used
     * when sorting songs returned from the database so they maintain a order.
     */
    private final JaroWinklerDistance jaroWinkler = new JaroWinklerDistance();

    /**
     * Constructor
     */
    public SongServiceImpl(QueryCacheRepository queryCacheRepository, SongRepository songRepository, SongSearchService songSearchService) {
        this.queryCacheRepository = queryCacheRepository;
        this.songRepository = songRepository;
        this.songSearchService = songSearchService;
    }

    /**
     * Formats the given song name and artist name into a format like {@code Song:{songName}+Artist:{artistName}}.
     * If there is only a songName, it will look like {@code Song:{SongName}}. If there is only a artistName, it will
     * look like {@code Artist:{artistName}}
     *
     * @param songName   Song name
     * @param artistName Artist name
     * @return Formatted String
     */
    public static String getQueryString(String songName, String artistName) {
        String query = "";
        songName = songName.trim();
        artistName = artistName.trim();
        /*
        'Song' or 'Artist' is added before the strings so if a user searches for a song named 'song'
        it'll have a different cache than if a user searched for a artist named 'song'.
         */
        if (!songName.isEmpty() && !artistName.isEmpty()) {
            query = "Song:" + songName + "+" + "Artist:" + artistName;
            //Add song in front of song name
        } else if (!songName.isEmpty()) {
            query = "Song:" + songName;
            //Add artist in front of artist name
        } else if (!artistName.isEmpty()) {
            query = "Artist:" + artistName;
        }
        return query;
    }

    /**
     * Searches for songs related to the given query. First checks if there is a cache with the
     * given query. If there is, the set of songs is returned. If not, uses the songSearchService
     * to search externally for songs using an API.
     *
     * <p>
     * If a cache is found, its set of songs gets converted to a tree set so it can be sorted. The set is sorted by the JaroWinklerDistance the song name or artist name is from the target song name or artist name.
     * If the distance is the same, it compares the Deezer ID of each song, which is always unique. Same sorting
     * logic as in {@link SongSearchDeezerImpl#externalSearchForSongs(String, String)}.
     * </p>
     *
     * @param songName   Name of song to look for
     * @param artistName Name of artist to look for
     * @return Set of songs related to the query, empty set if no songs were found
     * @see SongSearchService
     */
    public Set<Song> searchForSongs(String songName, String artistName) {
        if (!isValidQuery(songName) && !isValidQuery(artistName)) {
            return new HashSet<>();
        }

        //Create query string, used for logging and caching queries
        final String query = getQueryString(songName, artistName);
        log.info("searchForSongs: User searched for query: {}", query);

        //Check for local cache
        Set<Song> songsFound = getLocalCache(query);
        //if there was no cache found, search externally
        if (songsFound == null || songsFound.isEmpty()) {
            songsFound = songSearchService.externalSearchForSongs(songName, artistName);
            final MethodOutcome cacheCreated = createCache(query, songsFound);
            if (cacheCreated.failed()) {
                log.error("searchForSongs: An error occurred when trying to create cache with query '{}' | {}", query, cacheCreated.getMessage());
            }
            return songsFound;
        } else {
            //If at least a song name was given, sort by song name
            if (!songName.isEmpty()) {
                /*
                Creates a new tree set, which is a sorted set, and make a custom comparator that
                compares the song names and how different they are from the target name (songSearch)
                If they are the same, they are compared by deezerID, which should always be different
                 */
                final Set<Song> sortedSongsFound = new TreeSet<>((s1, s2) -> {
                    int distanceComparison = Double.compare(jaroWinkler.apply(s1.getSongName(), songName), jaroWinkler.apply(s2.getSongName(), songName));
                    if (distanceComparison != 0) {
                        return distanceComparison;
                    }
                    // If distances are the same, compare by song name
                    return s1.getDeezerID().compareTo(s2.getDeezerID());
                });
                sortedSongsFound.addAll(songsFound);
                return sortedSongsFound;
                //If a artist name was given, sort by artist name
            } else if (!artistName.isEmpty()) {
                /*
                Creates a new tree set, which is a sorted set, and make a custom comparator that
                compares the artist names and how different they are from the target name (artistSearch)
                If they are the same, they are compared by deezerID, which should always be different
                 */
                final Set<Song> sortedSongsFound = new TreeSet<>((s1, s2) -> {
                    int distanceComparison = Double.compare(jaroWinkler.apply(s1.getArtistName(), artistName), jaroWinkler.apply(s2.getArtistName(), artistName));
                    if (distanceComparison != 0) {
                        return distanceComparison;
                    }
                    // If distances are the same, compare by deezer id (always unique)
                    return s1.getDeezerID().compareTo(s2.getDeezerID());
                });
                sortedSongsFound.addAll(songsFound);
                return sortedSongsFound;
                //If both queries are empty, return a empty set
            } else {
                return new HashSet<>();
            }
        }
    }

    /**
     * Gets the local cache with the given query, if there is one
     *
     * <p>
     * For this to return the correct cache, the passed query needs to be in the same format the
     * {@link #getQueryString(String, String)} would put it in.
     * </p>
     *
     * @param query Query to search for
     * @return Set of songs related to given query, null if cache wasn't found
     * @see QueryCache
     */
    public Set<Song> getLocalCache(String query) {
        if (query == null || query.trim().isEmpty() || query.length() < MIN_QUERY_LENGTH || query.length() > MAX_QUERY_LENGTH) {
            return null;
        }
        query = query.strip().toLowerCase();
        //Search for cache
        final List<QueryCache> queryCacheList = queryCacheRepository.findQueryCacheByQueryIgnoreCase(query);
        if (queryCacheList != null && !queryCacheList.isEmpty()) {
            log.info("getLocalCache: Found query cache for {} with {} songs found", query, queryCacheList.getFirst().getResults().size());

            //If the cache is expired (has been more then a week since last queried, return null
            //So the cache data is rewritten/updated
            final QueryCache foundCache = queryCacheList.getFirst();
            if (foundCache.isExpired()) {
                log.info("getLocalCache: Query cache for query '{}' is expired", queryCacheList.getFirst().getQuery());
                return null;
            }

            return foundCache.getResults();
        }

        return null;
    }

    /**
     * Creates a new QueryCache with the given query and songs
     *
     * @param query Query that was searched for
     * @param songs Songs found related to query
     * @return A MethodOutcome enum, which represents the outcome of the method
     * @see QueryCache
     */
    public MethodOutcome createCache(String query, Set<Song> songs) {
        if (query == null || query.isEmpty() || songs == null) {
            return MethodOutcome.QUERY_EMPTY;
        }

        final QueryCache newCache;

        //Check if there is already a cache with the given query, if so, rewrite its data
        final List<QueryCache> queryCacheList = queryCacheRepository.findQueryCacheByQueryIgnoreCase(query);
        if (queryCacheList.size() == 1) {
            //cache found
            log.info("createCache: Editing found query cache for {} with {} songs found", query, queryCacheList.getFirst().getResults().size());
            newCache = queryCacheList.getFirst();
        } else {
            //No cache found
            log.info("createCache: Creating new cache object for query {}", query);
            newCache = new QueryCache();
        }

        query = query.strip().toLowerCase();
        newCache.setQuery(query);

        // Separate new and existing songs
        final Set<Song> newSongs = new HashSet<>();
        final Set<Song> allSongsForCache = new HashSet<>();

        //Go through each song and check if its in repository yet
        for (Song song : songs) {
            final List<Song> songFound = songRepository.findByDeezerID(song.getDeezerID());

            if (songFound == null || songFound.isEmpty()) {  //Song is new
                newSongs.add(song);
                song.addQueryCache(newCache);  //Link new song to new cache
            } else {  //Song exists in DB
                final Song existingSong = songFound.getFirst();
                existingSong.addQueryCache(newCache);  //Link existing song to new cache
                allSongsForCache.add(existingSong);  //Add existing song to final set for cache
            }
        }

        //Update the time the query was last updated
        newCache.setLastUpdated(LocalDateTime.now());

        //Save the new cache, have to do this here as well
        //as below so each song doesn't try to save a new queryCache with the same data
        queryCacheRepository.save(newCache);

        //Persist only new songs
        if (!newSongs.isEmpty()) {
            songRepository.saveAll(newSongs);
        }

        //Add both new and old songs to the cache results
        allSongsForCache.addAll(newSongs);  //Combine new and existing songs for cache
        newCache.setResults(allSongsForCache);
        queryCacheRepository.save(newCache);

        log.info("createCache: Saved new cache {}", newCache);
        return MethodOutcome.SUCCESS;
    }

    /**
     * Checks if the given query is valid.
     * <p>
     * A query is valid if:
     *     <ul>
     *         <li>It's not null</li>
     *         <li>It's not empty</li>
     *         <li>It's length is greater than MIN_QUERY_LENGTH</li>
     *         <li>It's length is less than MAX_QUERY_LENGTH</li>
     *     </ul>
     * </p>
     * Same method as in {@link SongSearchDeezerImpl}, didn't really feel the need to make something separate
     * to reduce duplicate code since it's just used in two services.
     *
     * @param query Query to check if valid
     * @return {@code true} if query is valid, {@code false} otherwise.
     * @see #MAX_QUERY_LENGTH
     * @see #MIN_QUERY_LENGTH
     */
    public boolean isValidQuery(String query) {
        return query != null && !query.trim().isEmpty() && !(query.length() < MIN_QUERY_LENGTH || query.length() > MAX_QUERY_LENGTH);
    }

    /**
     * Retrieves the number of songs currently stored in the repository.
     * @return the size of the song repository.
     */
    public long getRepoSize() {
        long size = songRepository.findAll().size();  // Or use any other method that correctly counts all songs in the repository
        log.info("getRepoSize: Song repository size is {}", size);
        return size;
    }

    /**
     * Clears all songs from the repository.
     * This is primarily useful for testing or resetting data.
     */
    public void clearRepo() {
        songRepository.deleteAll();
        log.info("clearRepo: All songs have been cleared from the repository");
    }

    public Song findSong(Song song) {
        final List<Song> songFound = songRepository.findByDeezerID(song.getDeezerID());
        // Check if the list contains any song
        if (!songFound.isEmpty()) {
            log.info("Song found {}", songFound.getFirst());
            // Return the first song in the list (assuming DeezerID is unique)
            return songFound.getFirst();
        } else {
            log.info("No song found for {}", song);
            return null;
        }
    }

    /**
     * Gets the preview link for the given id using songSearchService, since we have to get it externally
     * and reload it each time because some links expire after a day.
     *
     * @param id id to get preview for
     * @return String of URL
     */
    @Override
    public String getSongPreview(Long id) {
        return songSearchService.getSongPreview(id);
    }
}

