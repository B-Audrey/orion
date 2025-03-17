package oc.mdd.repository;

import oc.mdd.entity.PostEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

@Repository
public interface PostRepository extends JpaRepository<PostEntity, String> {
    PostEntity findByUuid(String uuid);

    @Query("""
                SELECT p FROM PostEntity p\s
                WHERE p.topic.uuid IN (
                    SELECT t.uuid FROM UserEntity u\s
                    LEFT JOIN u.topics t\s
                    WHERE u.uuid = :userUuid
                )
            """)
    Page<PostEntity> findUserFeed(@Param("userUuid") String userUuid, Pageable pageable);

    @Query("""
                SELECT p FROM PostEntity p
                LEFT JOIN FETCH p.comments c
                WHERE p.uuid = :postUuid
            """)
    PostEntity findByUuidWithComments(@Param("postUuid") String postUuid);


    PostEntity findByTitle(String s);
}