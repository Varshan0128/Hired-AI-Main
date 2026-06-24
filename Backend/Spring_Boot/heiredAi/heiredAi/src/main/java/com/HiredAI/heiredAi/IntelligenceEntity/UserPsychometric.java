package com.HiredAI.heiredAi.IntelligenceEntity;

import jakarta.persistence.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import java.time.LocalDateTime;

@Entity
@Table(name = "user_psychometric", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"user_id", "module"})
})
public class UserPsychometric {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "module", nullable = false, length = 50)
    private String module;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "results", nullable = false, columnDefinition = "jsonb")
    private String results;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    /**
     * Updates the record timestamp before it is persisted or modified.
     */
    @PrePersist
    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    public UserPsychometric() {}

    /**
     * Creates a psychometric record with the specified user, module, and results.
     *
     * @param userId the user identifier
     * @param module the module name
     * @param results the stored results payload
     */
    public UserPsychometric(Long userId, String module, String results) {
        this.userId = userId;
        this.module = module;
        this.results = results;
    }

    /**
     * Gets the primary key.
     *
     * @return the entity identifier
     */
    public Long getId() {
        return id;
    }

    /**
     * Sets the primary key.
     *
     * @param id the entity identifier
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Gets the ID of the user associated with this record.
     *
     * @return the user ID
     */
    public Long getUserId() {
        return userId;
    }

    /**
     * Sets the user identifier.
     *
     * @param userId the user identifier
     */
    public void setUserId(Long userId) {
        this.userId = userId;
    }

    /**
     * Gets the psychometric module name.
     *
     * @return the module name
     */
    public String getModule() {
        return module;
    }

    /**
     * Sets the module name.
     *
     * @param module the module name
     */
    public void setModule(String module) {
        this.module = module;
    }

    /**
     * Gets the psychometric results.
     *
     * @return the stored results JSON
     */
    public String getResults() {
        return results;
    }

    /**
     * Sets the psychometric results payload.
     *
     * @param results the JSON string to store in the results field
     */
    public void setResults(String results) {
        this.results = results;
    }

    /**
     * Gets the last update timestamp.
     *
     * @return the timestamp of the most recent insert or update
     */
    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    /**
     * Sets the last update timestamp.
     *
     * @param updatedAt the timestamp to assign
     */
    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}
