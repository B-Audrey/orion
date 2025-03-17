package oc.mdd.repository;

import oc.mdd.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, String> {
    @Query("""
                SELECT u
                FROM UserEntity u
                LEFT JOIN FETCH u.topics
                WHERE u.email = :email
            """)
    UserEntity findByEmail(String email);

    UserEntity findByName(String name);

    UserEntity findByUuid(String uuid);


}