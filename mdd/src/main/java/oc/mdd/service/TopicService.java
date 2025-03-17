package oc.mdd.service;

import lombok.RequiredArgsConstructor;
import oc.mdd.dto.PaginationQueryDto;
import oc.mdd.entity.TopicEntity;
import oc.mdd.model.TopicModel;
import oc.mdd.repository.TopicRepository;
import oc.mdd.utils.PaginationUtil;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TopicService {

    private final TopicRepository topicRepository;

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

    public Page<TopicEntity> getTopics(PaginationQueryDto pageDto) {
        Pageable pageable = PaginationUtil.createPageable(pageDto.getPage(), pageDto.getSize(), pageDto.getSort());
        return this.topicRepository.findAll(pageable);
    }

    public TopicEntity getTopicByUuid(String topicUuid) {
        return this.topicRepository.findByUuid(topicUuid);
    }
}
