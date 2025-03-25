package oc.mdd.utils.mappers;

import lombok.RequiredArgsConstructor;
import oc.mdd.entity.CommentEntity;
import oc.mdd.model.CommentModel;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class CommentMapper {
    private final PartialUserMapper userMapper;

    public List<CommentModel> convertToCommentModelsList(List<CommentEntity> comments) {
        List<CommentModel> commentModels = new ArrayList<>();
        if (comments != null) {
            for (CommentEntity commentEntity : comments) {
                CommentModel commentModel = new CommentModel(
                        commentEntity.getUuid(),
                        commentEntity.getContent(),
                        this.userMapper.convertToPartialUserModel(commentEntity.getUser()),
                        commentEntity.getCreatedAt(),
                        commentEntity.getUpdatedAt(),
                        commentEntity.getDeletedAt()
                );
                commentModels.add(commentModel);
            }
        }
        return commentModels;
    }
}
