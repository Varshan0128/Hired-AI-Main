package com.HiredAI.heiredAi.IntelligenceRepository;

import com.HiredAI.heiredAi.IntelligenceEntity.AnalyticsEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.UUID;

@Repository
public interface AnalyticsEventRepository extends JpaRepository<AnalyticsEvent, UUID> {
}
