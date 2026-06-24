package com.HiredAI.heiredAi.Controller;

import com.HiredAI.heiredAi.Entity.UserEntity;
import com.HiredAI.heiredAi.IntelligenceEntity.AnalyticsEvent;
import com.HiredAI.heiredAi.IntelligenceRepository.AnalyticsEventRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AnalyticsControllerTest {

    private AnalyticsEventRepository analyticsEventRepository;
    private ObjectMapper objectMapper;
    private AnalyticsController analyticsController;

    @BeforeEach
    void setUp() {
        analyticsEventRepository = mock(AnalyticsEventRepository.class);
        objectMapper = new ObjectMapper();
        analyticsController = new AnalyticsController(analyticsEventRepository, objectMapper);
        SecurityContextHolder.clearContext();
    }

    @AfterEach
    void tearDown() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void ingestEvent_unauthenticated_ignoresRequestBodyUserId() {
        Map<String, Object> body = new HashMap<>();
        body.put("user_id", "client-supplied-id-123");
        body.put("anonymous_id", "anon-id-456");
        body.put("session_id", "session-id-789");
        body.put("event_name", "test-event");

        ResponseEntity<?> response = analyticsController.ingestEvent(body, null);

        assertEquals(200, response.getStatusCode().value());

        ArgumentCaptor<AnalyticsEvent> captor = ArgumentCaptor.forClass(AnalyticsEvent.class);
        verify(analyticsEventRepository, times(1)).save(captor.capture());

        AnalyticsEvent savedEvent = captor.getValue();
        assertNull(savedEvent.getUserId());
        assertEquals("anon-id-456", savedEvent.getAnonymousId());
        assertEquals("session-id-789", savedEvent.getSessionId());
        assertEquals("test-event", savedEvent.getEventName());
    }

    @Test
    void ingestEvent_authenticated_usesSecurityPrincipalUserId() {
        UserEntity mockUser = new UserEntity();
        mockUser.setId(99L);
        mockUser.setEmail("test@example.com");

        UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(mockUser, null, java.util.Collections.emptyList());
        SecurityContextHolder.getContext().setAuthentication(auth);

        Map<String, Object> body = new HashMap<>();
        body.put("user_id", "client-supplied-id-123");
        body.put("anonymous_id", "anon-id-456");
        body.put("session_id", "session-id-789");
        body.put("event_name", "test-event");

        ResponseEntity<?> response = analyticsController.ingestEvent(body, auth);

        assertEquals(200, response.getStatusCode().value());

        ArgumentCaptor<AnalyticsEvent> captor = ArgumentCaptor.forClass(AnalyticsEvent.class);
        verify(analyticsEventRepository, times(1)).save(captor.capture());

        AnalyticsEvent savedEvent = captor.getValue();
        assertEquals("99", savedEvent.getUserId());
        assertEquals("anon-id-456", savedEvent.getAnonymousId());
        assertEquals("session-id-789", savedEvent.getSessionId());
        assertEquals("test-event", savedEvent.getEventName());
    }
}
