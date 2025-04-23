package org.example.statservice.repository;

import org.example.statservice.model.entity.Statistic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StatisticRepo extends JpaRepository<Statistic,Long> {
}
