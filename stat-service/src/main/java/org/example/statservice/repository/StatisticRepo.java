package org.example.statservice.repository;

import org.example.statservice.model.entity.Statistic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.swing.text.html.Option;
import java.math.BigDecimal;
import java.util.Optional;

public interface StatisticRepo extends JpaRepository<Statistic, Long> {
    Optional<Statistic> findByUserMappingIdAndMetricName(Long userMappingId, String metricName);

    @Query("SELECT SUM(s.metricValue) FROM Statistic s WHERE s.metricName = :metricName")
    Optional<Long> sumMetricValueByMetricName(@Param("metricName") String metricName);
    
    
}