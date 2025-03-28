package oc.mdd.repository;

import oc.mdd.entity.CommentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentRepository extends JpaRepository<CommentEntity, String> {
    CommentEntity findByUuid(String uuid);
}
