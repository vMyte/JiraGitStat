package org.example.statservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
public class StatisticService {
    private final JdbcTemplate jdbcTemplate;
   // private final StatisticRepo statisticRepo;

    @Transactional
    public void updateCommitStatistics(Long repoId) {
        //Статистика по добавленным строкам в commits
        String addedLinesSQL = """
            WITH commit_stats AS (
                SELECT 
                    um.id as user_mapping_id,
                    'LINES_ADDED' as metric_name,
                    SUM(c.add)::numeric as metric_value,
                    NOW() as calculated_at 
                FROM user_mapping um
                JOIN commits c ON um.github_username = c.author 
                WHERE c.repository_id = ? AND um.repository_id = ?
                GROUP BY um.id
            )
            INSERT INTO statistics 
                (user_mapping_id, metric_name, metric_value, calculated_at)
            SELECT * FROM commit_stats
            ON CONFLICT (user_mapping_id, metric_name) 
             DO UPDATE SET
                            metric_value = EXCLUDED.metric_value,
                            calculated_at = CASE
                                WHEN statistics.metric_value IS DISTINCT FROM EXCLUDED.metric_value
                                THEN NOW()
                                ELSE statistics.calculated_at::TIMESTAMP WITH TIME ZONE
                            END
            """;

        //Статистика по удаленным строкам в commits
        String deletedLinesSQL = """
            WITH commit_stats AS (
                SELECT 
                    um.id as user_mapping_id,
                    'LINES_DELETED' as metric_name,
                    SUM(c.delete)::numeric as metric_value,
                    NOW() as calculated_at 
                FROM user_mapping um
                JOIN commits c ON um.github_username = c.author 
                WHERE c.repository_id = ? AND um.repository_id = ?
                GROUP BY um.id
            )
            INSERT INTO statistics 
                (user_mapping_id, metric_name, metric_value, calculated_at)
            SELECT * FROM commit_stats
            ON CONFLICT (user_mapping_id, metric_name) 
             DO UPDATE SET
                            metric_value = EXCLUDED.metric_value,
                            calculated_at = CASE
                                WHEN statistics.metric_value IS DISTINCT FROM EXCLUDED.metric_value
                                THEN NOW()
                                ELSE statistics.calculated_at::TIMESTAMP WITH TIME ZONE
                            END
            """;

        //Статистика по кол-ву commits
        String countCommitsSQL = """
            WITH commit_stats AS (
                SELECT 
                    um.id as user_mapping_id,
                    'COUNT_COMMITS' as metric_name,
                    COUNT(c.id)::numeric as metric_value,
                    NOW() as calculated_at 
                FROM user_mapping um
                JOIN commits c ON um.github_username = c.author 
                WHERE c.repository_id = ? AND um.repository_id = ?
                GROUP BY um.id
            )
            INSERT INTO statistics 
                (user_mapping_id, metric_name, metric_value, calculated_at)
            SELECT * FROM commit_stats
            ON CONFLICT (user_mapping_id, metric_name) 
             DO UPDATE SET
                            metric_value = EXCLUDED.metric_value,
                            calculated_at = CASE
                                WHEN statistics.metric_value IS DISTINCT FROM EXCLUDED.metric_value
                                THEN NOW()
                                  ELSE statistics.calculated_at::TIMESTAMP WITH TIME ZONE
                            END
            """;

        //Статистика по средему кол-ву измененных строк в commits
        String AVGTotalEditLinesSQL = """
            WITH commit_stats AS (
                SELECT 
                    um.id as user_mapping_id,
                    'LINES_AVG_TOTAL' as metric_name,
                    (SUM(c.total)::numeric / COUNT(c.id)) as metric_value,
                    NOW() as calculated_at 
                FROM user_mapping um
                JOIN commits c ON um.github_username = c.author 
                WHERE c.repository_id = ? AND um.repository_id = ?
                GROUP BY um.id
            )
            INSERT INTO statistics 
                (user_mapping_id, metric_name, metric_value, calculated_at)
            SELECT * FROM commit_stats
            ON CONFLICT (user_mapping_id, metric_name) 
             DO UPDATE SET
                            metric_value = EXCLUDED.metric_value,
                            calculated_at = CASE
                                WHEN statistics.metric_value IS DISTINCT FROM EXCLUDED.metric_value
                                THEN NOW()
                                ELSE statistics.calculated_at::TIMESTAMP WITH TIME ZONE
                            END
            """;

        jdbcTemplate.update(addedLinesSQL, repoId, repoId);
        jdbcTemplate.update(deletedLinesSQL,repoId,repoId);
        jdbcTemplate.update(countCommitsSQL,repoId,repoId);
        jdbcTemplate.update(AVGTotalEditLinesSQL,repoId,repoId);
    }

    @Transactional
    public void updateIssueStatistics(Long repoId) {
        // Статистика по задачам
        String issuesSql = """
            WITH issue_stats AS (
                SELECT 
                    um.id as user_mapping_id,
                    'TOTAL_ISSUES' as metric_name,
                    COUNT(i.id) as metric_value,
                    NOW() as calculated_at 
                FROM user_mapping um
                JOIN issues i ON um.jira_username = i.assignee
                WHERE i.repository_id = ? AND um.repository_id = ?
                GROUP BY um.id
            )
            INSERT INTO statistics 
                (user_mapping_id, metric_name, metric_value, calculated_at)
            SELECT * FROM issue_stats
            ON CONFLICT (user_mapping_id, metric_name) 
             DO UPDATE SET
                            metric_value = EXCLUDED.metric_value,
                            calculated_at = CASE
                                WHEN statistics.metric_value IS DISTINCT FROM EXCLUDED.metric_value
                                THEN NOW()
                                ELSE statistics.calculated_at::TIMESTAMP WITH TIME ZONE
                            END
            """;

        // Статистика по выполненным задачам
        String doneIssuesSql = """
            WITH done_issue_stats AS (
                SELECT 
                    um.id as user_mapping_id,
                    'DONE_ISSUES' as metric_name,
                    COUNT(i.id) as metric_value,
                    NOW() as calculated_at 
                FROM user_mapping um
                JOIN issues i ON um.jira_username = i.assignee
                WHERE i.repository_id = ? AND um.repository_id = ? 
                  AND i.updated_at IS NOT NULL
                GROUP BY um.id
            )
            INSERT INTO statistics 
                (user_mapping_id, metric_name, metric_value, calculated_at)
            SELECT * FROM done_issue_stats
            ON CONFLICT (user_mapping_id, metric_name) 
             DO UPDATE SET
                            metric_value = EXCLUDED.metric_value,
                            calculated_at = CASE
                                WHEN statistics.metric_value IS DISTINCT FROM EXCLUDED.metric_value
                                THEN NOW()
                                ELSE statistics.calculated_at::TIMESTAMP WITH TIME ZONE
                            END
            """;

        // Среднее время выполнения задач
        String avgTimeSql = """
            WITH avg_time_stats AS (
                SELECT 
                    um.id as user_mapping_id,
                    'AVG_ISSUE_TIME' as metric_name,
                    AVG(EXTRACT(EPOCH FROM (
                         TO_TIMESTAMP(i.updated_at, 'YYYY-MM-DD"T"HH24:MI:SS.MS"Z"') -
                         TO_TIMESTAMP(i.created_at, 'YYYY-MM-DD"T"HH24:MI:SS.MS"Z"')
                     )) / 86400) as metric_value,
                    NOW() as calculated_at 
                FROM user_mapping um
                JOIN issues i ON um.jira_username = i.assignee
                WHERE i.repository_id = ? AND um.repository_id = ?
                  AND i.updated_at IS NOT NULL
                GROUP BY um.id
            )
            INSERT INTO statistics 
                (user_mapping_id, metric_name, metric_value, calculated_at)
            SELECT * FROM avg_time_stats
            ON CONFLICT (user_mapping_id, metric_name) 
             DO UPDATE SET
                            metric_value = EXCLUDED.metric_value,
                            calculated_at = CASE
                                WHEN statistics.metric_value IS DISTINCT FROM EXCLUDED.metric_value
                                THEN NOW()
                                ELSE statistics.calculated_at::TIMESTAMP WITH TIME ZONE
                            END
            """;

        jdbcTemplate.update(issuesSql, repoId, repoId);
        jdbcTemplate.update(doneIssuesSql, repoId, repoId);
        jdbcTemplate.update(avgTimeSql, repoId, repoId);
    }
}

