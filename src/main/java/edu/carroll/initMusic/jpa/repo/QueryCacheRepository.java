package edu.carroll.initMusic.jpa.repo;

import edu.carroll.initMusic.jpa.model.QueryCache;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Repository used for retrieving QueryCache Entities
 */
public interface QueryCacheRepository extends JpaRepository<QueryCache, Long> {
    /**
     * Finds QueryCaches by query string, which is unique among all query caches
     *
     * @param query Query String to search with
     * @return List of QueryCaches found
     */
    List<QueryCache> findQueryCacheByQueryIgnoreCase(String query);
}
