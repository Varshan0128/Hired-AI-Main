package com.HiredAI.heiredAi.Controller;

import com.HiredAI.heiredAi.Entity.UserEntity;
import com.HiredAI.heiredAi.IntelligenceEntity.UserPsychometric;
import com.HiredAI.heiredAi.IntelligenceRepository.UserPsychometricRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/user/psychometric")
public class PsychometricController {

    private final UserPsychometricRepository userPsychometricRepository;
    private final ObjectMapper objectMapper;

    /**
     * Creates a controller for storing and retrieving psychometric results.
     *
     * @param userPsychometricRepository the repository used to access psychometric records
     * @param objectMapper the JSON mapper used to serialize and deserialize results
     */
    public PsychometricController(UserPsychometricRepository userPsychometricRepository, ObjectMapper objectMapper) {
        this.userPsychometricRepository = userPsychometricRepository;
        this.objectMapper = objectMapper;
    }

    /**
     * Saves psychometric results for the authenticated user.
     *
     * @param authentication the current authentication principal
     * @param body the request payload containing the module name and results data
     * @return a response indicating whether the results were saved successfully
     */
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
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("Message", "Failed to save results: " + e.getMessage()));
        }
    }

    /**
     * Retrieves the authenticated user's saved psychometric results grouped by module.
     *
     * @return a response containing a map of module names to their saved results, or an error message if the request is unauthorized or the results cannot be fetched
     */
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
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("Message", "Failed to fetch results: " + e.getMessage()));
        }
    }
}
