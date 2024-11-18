package edu.carroll.initMusic.service;

import edu.carroll.initMusic.jpa.model.QueryCache;
import edu.carroll.initMusic.jpa.model.Song;
import edu.carroll.initMusic.jpa.repo.QueryCacheRepository;
import edu.carroll.initMusic.jpa.repo.SongRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * This services handles Searching for songs using the deezer api.
 *
 * @author Nick Clouse
 *
 * @since September 30, 2024
 */
@Service
public class SongServiceDeezerImpl implements SongService{
    /** Logger object used for logging */
    private static final Logger log = LoggerFactory.getLogger(SongServiceDeezerImpl.class);

    /** QueryCache repository */
    private final QueryCacheRepository queryCacheRepository;

    /** Song repository */
    private final SongRepository songRepository;

    /**
     * Constructor
     */
    public SongServiceDeezerImpl(QueryCacheRepository queryCacheRepository, SongRepository songRepository) {
        this.queryCacheRepository = queryCacheRepository;
        this.songRepository = songRepository;
    }

    /**
     * Gets the local cache with the given query, if there is one
     * @param query Query to search for
     * @return Set of songs related to given query, null if cache wasn't found
     *
     * @see QueryCache
     */
    @Override
    public Set<Song> getLocalCache(String query) {
        if(query == null || query.isEmpty()){
            return null;
        }
        query = query.strip().toLowerCase();
        final List<QueryCache> queryCacheList = queryCacheRepository.findQueryCacheByQueryIgnoreCase(query);
        if (queryCacheList != null && !queryCacheList.isEmpty()) {
            log.info("getLocalCache: Found query cache for {} with {} songs found", query, queryCacheList.getFirst().getResults().size());
            return queryCacheList.getFirst().getResults();
        }

        return null;
    }

    /**
     * Creates a new QueryCache with the given query and songs
     * @param query Query String
     * @param songs Songs found related to query
     *
     * @see QueryCache
     */
    public boolean createCache(String query, Set<Song> songs){
        if(query == null || query.isEmpty() || songs == null){
            return false;
        }
        if(queryCacheRepository == null){
            return false;
        }
        final QueryCache newCache;

        //Check if there is already a cache with the given query, if so, rewrite its data
        final List<QueryCache> queryCacheList = queryCacheRepository.findQueryCacheByQueryIgnoreCase(query);
        if (queryCacheList.size() == 1) {
            //cache found
            log.info("createCache: Editing found query cache for {} with {} songs found", query, queryCacheList.getFirst().getResults().size());
            newCache = queryCacheList.getFirst();
        }else{
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

            if (songFound.isEmpty()) {  //Song is new
                newSongs.add(song);
                song.addQueryCache(newCache);  //Link new song to new cache
            } else {  //Song exists in DB
                final Song existingSong = songFound.getFirst();
                existingSong.addQueryCache(newCache);  //Link existing song to new cache
                allSongsForCache.add(existingSong);  //Add existing song to final set for cache
            }
        }

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

        log.info("Saved new cache with associated songs {}", newCache);
        return true;
    }
}
