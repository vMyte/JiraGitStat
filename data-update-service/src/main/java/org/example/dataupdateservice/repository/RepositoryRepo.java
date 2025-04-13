package org.example.dataupdateservice.repository;

import org.example.dataupdateservice.model.entity.Repos;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface RepositoryRepo extends JpaRepository<Repos,Long> {
  boolean existsByName(String nameRepository);

  Optional<Repos> findByOwnerAndName(String owner, String repos);
}
