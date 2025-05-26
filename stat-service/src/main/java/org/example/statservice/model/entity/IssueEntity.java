package org.example.statservice.model.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "issues")
public class IssueEntity {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "key")
    private String key;

    @Column(name = "summary")
    private String summary;

    @Column(name = "status")
    private String status;

    @Column(name = "assignee")
    private String assignee;

    @Column(name = "created_at")
    private String createdAt;

    @Column(name = "updated_at")
    private String updatedAt;

    @Column(name = "repository_id")
    private Long repositoryId;
}