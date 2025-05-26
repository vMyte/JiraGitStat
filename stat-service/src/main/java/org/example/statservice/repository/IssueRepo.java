package org.example.statservice.repository;

import org.example.statservice.model.entity.IssueEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IssueRepo extends JpaRepository<IssueEntity, Long> {
    @Query(value = """
        WITH last_date AS (
            SELECT MAX(updated_at::date) AS max_date FROM issues
        ),
        date_series AS (
            SELECT (max_date - (n || ' days')::INTERVAL)::date AS date
            FROM last_date
            CROSS JOIN generate_series(0, 6) AS n
        )
        SELECT
            to_char(ds.date, 'YYYY-MM-DD') AS date_str,
            COUNT(i.id) AS count
        FROM
            date_series ds
        LEFT JOIN
            issues i ON i.updated_at::date = ds.date
        GROUP BY
            ds.date
        ORDER BY
            ds.date
        """, nativeQuery = true)
    List<Object[]> getDoneIssuesOnWeekRaw();
    
}