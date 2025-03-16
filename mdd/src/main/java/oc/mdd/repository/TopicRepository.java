package oc.mdd.repository;

import oc.mdd.entity.TopicEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TopicRepository extends JpaRepository<TopicEntity, String> {

    TopicEntity findByUuid(String topicUuid);

    TopicEntity findByLabel(String label);
}
