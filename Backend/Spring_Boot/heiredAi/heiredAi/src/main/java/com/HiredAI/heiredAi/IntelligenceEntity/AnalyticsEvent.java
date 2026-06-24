package com.HiredAI.heiredAi.IntelligenceEntity;

import jakarta.persistence.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "analytics_events")
public class AnalyticsEvent {

    @Id
    @Column(name = "event_id")
    private UUID eventId;

    @Column(name = "occurred_at", nullable = false)
    private OffsetDateTime occurredAt;

    @Column(name = "user_id")
    private String userId;

    @Column(name = "anonymous_id")
    private String anonymousId;

    @Column(name = "session_id")
    private String sessionId;

    @Column(name = "event_name", nullable = false)
    private String eventName;

    @Column(name = "page_path")
    private String pagePath;

    @Column(name = "page_name")
    private String pageName;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "properties", columnDefinition = "jsonb")
    private String properties;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "context", columnDefinition = "jsonb")
    private String context;

    /**
 * Creates an empty analytics event.
 */
public AnalyticsEvent() {}

    /**
     * Gets the event identifier.
     *
     * @return the event identifier
     */
    public UUID getEventId() {
        return eventId;
    }

    /**
     * Sets the event identifier.
     *
     * @param eventId the event identifier
     */
    public void setEventId(UUID eventId) {
        this.eventId = eventId;
    }

    /**
     * Gets the time when the event occurred.
     *
     * @return the event occurrence time
     */
    public OffsetDateTime getOccurredAt() {
        return occurredAt;
    }

    /**
     * Sets when the event occurred.
     *
     * @param occurredAt the event timestamp
     */
    public void setOccurredAt(OffsetDateTime occurredAt) {
        this.occurredAt = occurredAt;
    }

    /**
     * Gets the user identifier associated with this event.
     *
     * @return the user identifier, or {@code null} if none is set
     */
    public String getUserId() {
        return userId;
    }

    /**
     * Sets the user identifier associated with this event.
     *
     * @param userId the user identifier
     */
    public void setUserId(String userId) {
        this.userId = userId;
    }

    /**
     * Gets the anonymous identifier for the event.
     *
     * @return the anonymous identifier
     */
    public String getAnonymousId() {
        return anonymousId;
    }

    /**
     * Sets the anonymous identifier for this event.
     *
     * @param anonymousId the anonymous identifier to store
     */
    public void setAnonymousId(String anonymousId) {
        this.anonymousId = anonymousId;
    }

    /**
     * Gets the session identifier associated with the event.
     *
     * @return the session identifier
     */
    public String getSessionId() {
        return sessionId;
    }

    /**
     * Sets the session identifier associated with this event.
     *
     * @param sessionId the session identifier
     */
    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    /**
     * Gets the event name.
     *
     * @return the event name
     */
    public String getEventName() {
        return eventName;
    }

    /**
     * Sets the event name.
     *
     * @param eventName the event name
     */
    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    /**
     * Gets the page path associated with this event.
     *
     * @return the page path
     */
    public String getPagePath() {
        return pagePath;
    }

    /**
     * Sets the page path associated with the event.
     *
     * @param pagePath the page path to store
     */
    public void setPagePath(String pagePath) {
        this.pagePath = pagePath;
    }

    /**
     * Gets the page name associated with the event.
     *
     * @return the page name
     */
    public String getPageName() {
        return pageName;
    }

    /**
     * Sets the page name associated with the event.
     *
     * @param pageName the page name to store
     */
    public void setPageName(String pageName) {
        this.pageName = pageName;
    }

    /**
     * Returns the event properties payload.
     *
     * @return the JSON string stored in the properties field
     */
    public String getProperties() {
        return properties;
    }

    /**
     * Sets the JSON properties associated with the event.
     *
     * @param properties the JSON properties value
     */
    public void setProperties(String properties) {
        this.properties = properties;
    }

    /**
     * Gets the analytics event context JSON.
     *
     * @return the context JSON
     */
    public String getContext() {
        return context;
    }

    /**
     * Sets the JSON context for this analytics event.
     *
     * @param context the context payload
     */
    public void setContext(String context) {
        this.context = context;
    }
}
