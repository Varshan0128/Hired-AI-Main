package com.HiredAI.heiredAi.Controller;

import com.HiredAI.heiredAi.IntelligenceEntity.AnalyticsEvent;
import com.HiredAI.heiredAi.IntelligenceRepository.AnalyticsEventRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.OffsetDateTime;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/analytics")
public class AnalyticsController {

    private final AnalyticsEventRepository analyticsEventRepository;
    private final ObjectMapper objectMapper;

    public AnalyticsController(AnalyticsEventRepository analyticsEventRepository, ObjectMapper objectMapper) {
        this.analyticsEventRepository = analyticsEventRepository;
        this.objectMapper = objectMapper;
    }

    @PostMapping("/events")
    public ResponseEntity<?> ingestEvent(@RequestBody Map<String, Object> body) {
        try {
            AnalyticsEvent event = new AnalyticsEvent();
            
            String eventIdStr = (String) body.get("event_id");
            if (eventIdStr != null && !eventIdStr.isBlank()) {
                event.setEventId(UUID.fromString(eventIdStr));
            } else {
                event.setEventId(UUID.randomUUID());
            }

            String occurredAtStr = (String) body.get("occurred_at");
            if (occurredAtStr != null && !occurredAtStr.isBlank()) {
                event.setOccurredAt(OffsetDateTime.parse(occurredAtStr));
            } else {
                event.setOccurredAt(OffsetDateTime.now());
            }

            event.setUserId((String) body.get("user_id"));
            event.setAnonymousId((String) body.get("anonymous_id"));
            event.setSessionId((String) body.get("session_id"));
            event.setEventName((String) body.get("event_name"));
            event.setPagePath((String) body.get("page_path"));
            event.setPageName((String) body.get("page_name"));

            Object propsObj = body.get("properties");
            if (propsObj != null) {
                event.setProperties(objectMapper.writeValueAsString(propsObj));
            }

            Object contextObj = body.get("context");
            if (contextObj != null) {
                event.setContext(objectMapper.writeValueAsString(contextObj));
            }

            analyticsEventRepository.save(event);
        } catch (Exception e) {
            // Swallow error and fail silently, return 200 Success as requested
            System.err.println("Analytics ingestion failed: " + e.getMessage());
        }

        return ResponseEntity.ok(Map.of("Status", "Success"));
    }
}
