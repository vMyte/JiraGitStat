package org.example.dataupdateservice.repository;

import org.example.dataupdateservice.model.entity.IssueEntity;
import org.springframework.data.domain.Example;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IssueRepo extends JpaRepository<IssueEntity,Long> {
    boolean existsByKey(String key);
    IssueEntity findByKey(String key);
}
