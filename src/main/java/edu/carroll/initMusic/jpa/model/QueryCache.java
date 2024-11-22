package edu.carroll.initMusic.jpa.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * QueryCache JPA model, used to store queries and their results. If a user searches for a query,
 * and it's in our database, it will be displayed much faster than using a API to search
 */
@Entity
@Table(name = "queryCache")
public class QueryCache {
    /** Number of days that can pass before a cache is considered expired */
    private static final int EXPIRATION_DURATION_DAYS = 7;

    /** ID of QueryCache, generated by JPA */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long QueryCacheID;

    /** Query that user searched for */
    @Column(name="query",nullable = false,unique = true)
    private String query;

    /** Songs found related to query, many-to-many relationship with Songs */
    @ManyToMany(cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinTable(
            name="query_cache_song",
            joinColumns={@JoinColumn(name="QueryCacheID")},
            inverseJoinColumns = {@JoinColumn(name="SongId")}

    )
    private final Set<Song> results = new HashSet<>();

    /** Keeps track of when the cache was last updated */
    @Column(name = "last_updated", nullable = false)
    private LocalDateTime lastUpdated;

    /** Default Constructor */
    public QueryCache() {

    }

    /**
     * Called before the entity is persisted (on creation) and updates the lastUpdated attribute
     */
    @PrePersist
    private void onCreate() {
        lastUpdated = LocalDateTime.now();  // Set the creation time
    }

    /**
     * Returns if a cache is expired or not. A cache is considered expired if it has been more than
     * a week (7 days) since it's last been updated.
     * @return {@code true} if cache is expired, {@code false} if not
     */
    public boolean isExpired(){
        return lastUpdated == null || ChronoUnit.DAYS.between(lastUpdated, LocalDateTime.now()) > EXPIRATION_DURATION_DAYS;
    }

    /**
     * Sets the time the cache was last updated to given time
     * @param lastUpdated Time cache was last updated
     */
    public void setLastUpdated(LocalDateTime lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    /**
     * Gets the ID of the queryCache
     * @return The QueryCache's ID
     */
    public Long getQueryCacheID() {
        return QueryCacheID;
    }

    /**
     * Sets the ID of the queryCache
     * @param queryCacheID The QueryCache's ID to set
     */
    public void setQueryCacheID(Long queryCacheID) {
        QueryCacheID = queryCacheID;
    }

    /**
     * Gets the query
     * @return The query
     */
    public String getQuery() {
        return query;
    }

    /**
     * Sets the query
     * @param query The query to set
     */
    public void setQuery(String query) {
        this.query = query;
    }

    /**
     * Gets the results, which is a set of Songs
     * @return Set of songs related to query
     */
    public Set<Song> getResults() {
        return results;
    }

    /**
     * Sets the results, which is a set of Songs
     * @param results Set of songs related to query to set
     */
    public void setResults(Set<Song> results) {
        this.results.clear();
        this.results.addAll(results);
    }

    /**
     * Compares object with another object
     * @param o other object to compare to
     * @return True if objects are equal, false otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        QueryCache that = (QueryCache) o;
        return Objects.equals(query, that.query) && Objects.equals(results, that.results);
    }

    /**
     * Converts object to hash code
     * @return Object in hashcode form
     */
    @Override
    public int hashCode() {
        return Objects.hash(query);
    }

    /**
     * Converts object to string
     * @return String version of object
     */
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("QueryCache{");
        sb.append("QueryCacheID=").append(QueryCacheID);
        sb.append(", query='").append(query).append('\'');
        sb.append(", number of songs=").append(results.size());
        sb.append('}');
        return sb.toString();
    }
}
