package org.example.dataupdateservice.model.entity;


import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "commits")
public class Commit {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "repository_id")
    private Long repositoryId;

    @Column(name = "author")
    private String author;

    @Column(name = "message")
    private String massage;

    @Column(name = "created_at")
    private String  createdAt;

    @Column(name = "hash")
    private String sha;
}
