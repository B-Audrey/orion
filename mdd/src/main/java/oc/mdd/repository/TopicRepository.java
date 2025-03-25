package oc.mdd.repository;

import oc.mdd.entity.TopicEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface TopicRepository extends JpaRepository<TopicEntity, String> {

    TopicEntity findByUuid(String topicUuid);

    TopicEntity findByLabel(String label);

    /**
     * Find topics with filters
     * if search is passed it will be parsed in lower case
     * if search is null or empty it will return all topics
     *
     * @param search   a string to search for in labels
     * @param pageable the page params
     * @return a page of topics
     */
    @Query("""
             SELECT t
             FROM TopicEntity t
             WHERE (:search IS NULL\s
                    OR :search = ''\s
                    OR LOWER(t.label) LIKE LOWER(CONCAT('%', :search, '%')))
            \s""")
    Page<TopicEntity> findWithFilters(@Param("search") String search, Pageable pageable);

}
