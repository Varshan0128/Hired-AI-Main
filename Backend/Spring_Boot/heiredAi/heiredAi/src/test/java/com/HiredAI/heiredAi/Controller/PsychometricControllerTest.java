package com.HiredAI.heiredAi.Controller;

import com.HiredAI.heiredAi.Entity.UserEntity;
import com.HiredAI.heiredAi.IntelligenceRepository.UserPsychometricRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

class PsychometricControllerTest {

    private UserPsychometricRepository userPsychometricRepository;
    private ObjectMapper objectMapper;
    private PsychometricController psychometricController;

    @BeforeEach
    void setUp() {
        userPsychometricRepository = mock(UserPsychometricRepository.class);
        objectMapper = mock(ObjectMapper.class);
        psychometricController = new PsychometricController(userPsychometricRepository, objectMapper);
    }

    @Test
    void savePsychometric_handlesExceptionSafely() throws Exception {
        UserEntity mockUser = new UserEntity();
        mockUser.setId(1L);
        UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(mockUser, null);

        Map<String, Object> body = new HashMap<>();
        body.put("module", "module1");
        body.put("results", "some-results");

        when(objectMapper.writeValueAsString(any())).thenThrow(new RuntimeException("Database down or JSON serialization error"));

        ResponseEntity<?> response = psychometricController.savePsychometric(auth, body);

        assertEquals(500, response.getStatusCode().value());
        @SuppressWarnings("unchecked")
        Map<String, Object> responseBody = (Map<String, Object>) response.getBody();
        assertEquals("Failed to save results: An unexpected error occurred.", responseBody.get("Message"));
    }

    @Test
    void getPsychometric_handlesExceptionSafely() {
        UserEntity mockUser = new UserEntity();
        mockUser.setId(1L);
        UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(mockUser, null);

        when(userPsychometricRepository.findByUserId(anyLong())).thenThrow(new RuntimeException("Database error"));

        ResponseEntity<?> response = psychometricController.getPsychometric(auth);

        assertEquals(500, response.getStatusCode().value());
        @SuppressWarnings("unchecked")
        Map<String, Object> responseBody = (Map<String, Object>) response.getBody();
        assertEquals("Failed to fetch results: An unexpected error occurred.", responseBody.get("Message"));
    }
}
