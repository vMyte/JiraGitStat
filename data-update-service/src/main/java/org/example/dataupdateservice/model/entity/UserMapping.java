package org.example.dataupdateservice.model.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "user_mapping")
public class UserMapping {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "email")
    private String email;

    @Column(name = "jira_username")
    private String jiraUsername;

    @Column(name = "github_username")
    private String githubUsername;
}
