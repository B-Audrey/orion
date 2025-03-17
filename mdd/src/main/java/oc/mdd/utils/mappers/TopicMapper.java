package oc.mdd.utils.mappers;

import lombok.RequiredArgsConstructor;
import oc.mdd.entity.TopicEntity;
import oc.mdd.model.TopicModel;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TopicMapper {

    public TopicModel convertToTopicModel(TopicEntity topicEntity) {
        return new TopicModel(
                topicEntity.getUuid(),
                topicEntity.getLabel(),
                topicEntity.getDescription(),
                topicEntity.getCreatedAt(),
                topicEntity.getUpdatedAt(),
                topicEntity.getDeletedAt()
        );
    }

}
