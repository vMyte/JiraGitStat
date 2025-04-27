package org.example.statservice.repository;

import org.example.statservice.model.entity.UserMapping;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserMappingRepo extends JpaRepository<UserMapping,Long> {
}
