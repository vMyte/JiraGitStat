package org.example.dataupdateservice.repository;

import org.example.dataupdateservice.model.entity.UserMapping;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserMappingRepo extends JpaRepository<UserMapping,Long> {
    boolean existsByJiraUsername (String name);

    boolean existsByEmail(String email);

    UserMapping findByEmail(String email);
    UserMapping findByJiraUsername(String jiraUsername);
    UserMapping findByGithubUsername(String gitHubUsername);

    boolean existsByGithubUsername(String name);
}
