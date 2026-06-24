package com.HiredAI.heiredAi.Controller;

import com.HiredAI.heiredAi.Entity.UserEntity;
import com.HiredAI.heiredAi.IntelligenceEntity.UserPsychometric;
import com.HiredAI.heiredAi.IntelligenceRepository.UserPsychometricRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/user/psychometric")
public class PsychometricController {

    private static final Logger logger = LoggerFactory.getLogger(PsychometricController.class);

    private final UserPsychometricRepository userPsychometricRepository;
    private final ObjectMapper objectMapper;

    public PsychometricController(UserPsychometricRepository userPsychometricRepository, ObjectMapper objectMapper) {
        this.userPsychometricRepository = userPsychometricRepository;
        this.objectMapper = objectMapper;
    }

    @PostMapping
    public ResponseEntity<?> savePsychometric(Authentication authentication, @RequestBody Map<String, Object> body) {
        if (authentication == null || !(authentication.getPrincipal() instanceof UserEntity user)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("Message", "Unauthorized"));
        }

        String module = (String) body.get("module");
        Object resultsObj = body.get("results");

        if (module == null || resultsObj == null) {
            return ResponseEntity.badRequest().body(Map.of("Message", "Module and results are required"));
        }

        try {
            String resultsJson = objectMapper.writeValueAsString(resultsObj);
            
            // Upsert: update existing module results or save new entry
            Optional<UserPsychometric> existingOpt = userPsychometricRepository.findByUserIdAndModule(user.getId(), module);
            UserPsychometric psychometric;
            if (existingOpt.isPresent()) {
                psychometric = existingOpt.get();
                psychometric.setResults(resultsJson);
            } else {
                psychometric = new UserPsychometric(user.getId(), module, resultsJson);
            }

            userPsychometricRepository.save(psychometric);
            return ResponseEntity.ok(Map.of("Message", "Psychometric results saved successfully"));
        } catch (Exception e) {
            logger.error("Failed to save psychometric results", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("Message", "Failed to save results: An unexpected error occurred."));
        }
    }

    @GetMapping
    public ResponseEntity<?> getPsychometric(Authentication authentication) {
        if (authentication == null || !(authentication.getPrincipal() instanceof UserEntity user)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("Message", "Unauthorized"));
        }

        try {
            List<UserPsychometric> list = userPsychometricRepository.findByUserId(user.getId());
            Map<String, Object> response = new HashMap<>();
            for (UserPsychometric item : list) {
                Object resultsMap = objectMapper.readValue(item.getResults(), Object.class);
                response.put(item.getModule(), resultsMap);
            }
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Failed to fetch psychometric results", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("Message", "Failed to fetch results: An unexpected error occurred."));
        }
    }
}
