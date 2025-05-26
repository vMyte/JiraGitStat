package org.example.statservice.repository;

import java.util.List;
import org.example.statservice.model.entity.UserMapping;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.Query;

public interface UserMappingRepo extends JpaRepository<UserMapping, Long> {
     Optional<UserMapping> findByEmail(String email);
     
     // Метод для получения всех email
    @Query("SELECT u.email FROM UserMapping u")
    List<String> findAllEmails();
}