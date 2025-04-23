package org.example.statservice.model.entity;


import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Entity
@Table(name = "statistics")
public class Statistic {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_mapping_id")
    private Long userMappingId;

    @Column(name = "repository_id")
    private Long repositoryId;

    @Column(name = "metric_name")
    private String metricName;

    @Column(name = "metric_value")
    private BigDecimal metricValue;

    @Column(name = "calculated_at")
    private String calculatedAt;
}
