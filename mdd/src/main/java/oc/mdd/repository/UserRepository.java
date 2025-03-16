package oc.mdd.repository;

import oc.mdd.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, String> {
    UserEntity findByEmail(String email);

    UserEntity findByName(String name);

    UserEntity findByUuid(String uuid);
}