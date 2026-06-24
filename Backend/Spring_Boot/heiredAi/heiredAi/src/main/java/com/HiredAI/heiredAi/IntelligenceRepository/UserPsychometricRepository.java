package com.HiredAI.heiredAi.IntelligenceRepository;

import com.HiredAI.heiredAi.IntelligenceEntity.UserPsychometric;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserPsychometricRepository extends JpaRepository<UserPsychometric, Long> {
    List<UserPsychometric> findByUserId(Long userId);
    Optional<UserPsychometric> findByUserIdAndModule(Long userId, String module);
}
