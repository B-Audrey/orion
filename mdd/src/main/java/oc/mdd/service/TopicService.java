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

    public Page<TopicEntity> getTopics(PaginationQueryDto pageDto) {
        Pageable pageable = PaginationUtil.createPageable(pageDto.getPage(), pageDto.getSize());
        return this.topicRepository.findAll(pageable);
    }
}
