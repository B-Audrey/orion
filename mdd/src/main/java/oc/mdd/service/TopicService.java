package oc.mdd.service;

import lombok.RequiredArgsConstructor;
import oc.mdd.dto.PaginationQueryDto;
import oc.mdd.entity.TopicEntity;
import oc.mdd.repository.TopicRepository;
import oc.mdd.utils.PaginationUtil;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TopicService {

    private final TopicRepository topicRepository;

    /**
     * Get topics with pagination and search
     *
     * @param pageDto PaginationQueryDto
     * @param search  String to search on labels
     * @return Page<TopicEntity>
     */
    public Page<TopicEntity> getTopics(PaginationQueryDto pageDto, String search) {
        Pageable pageable = PaginationUtil.createPageable(pageDto.getPage(), pageDto.getSize(), pageDto.getSort());
        return this.topicRepository.findWithFilters(search, pageable);
    }

    /**
     * Get a topic by uuid
     *
     * @param topicUuid String
     * @return TopicEntity
     */
    public TopicEntity getTopicByUuid(String topicUuid) {
        return this.topicRepository.findByUuid(topicUuid);
    }
}
