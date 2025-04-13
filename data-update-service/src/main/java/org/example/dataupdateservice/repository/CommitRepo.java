package org.example.dataupdateservice.repository;

import org.example.dataupdateservice.model.entity.Commit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommitRepo extends JpaRepository<Commit,Long> {

}
