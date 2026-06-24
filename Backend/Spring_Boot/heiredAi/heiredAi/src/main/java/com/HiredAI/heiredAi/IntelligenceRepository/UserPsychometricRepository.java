package com.HiredAI.heiredAi.IntelligenceRepository;

import com.HiredAI.heiredAi.IntelligenceEntity.UserPsychometric;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserPsychometricRepository extends JpaRepository<UserPsychometric, Long> {
    /**
 * Finds all psychometric records for a user.
 *
 * @param userId the user identifier
 * @return the psychometric records associated with the specified user
 */
List<UserPsychometric> findByUserId(Long userId);
    /**
 * Finds the psychometric record for the specified user and module.
 *
 * @param userId the user identifier
 * @param module the module name
 * @return an {@code Optional} containing the matching {@code UserPsychometric}, if present
 */
Optional<UserPsychometric> findByUserIdAndModule(Long userId, String module);
}
