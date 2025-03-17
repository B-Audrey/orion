package oc.mdd.utils.mappers;

import lombok.RequiredArgsConstructor;
import oc.mdd.entity.UserEntity;
import oc.mdd.model.UserModel;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserMapper {
    public UserModel convertToUserModel(UserEntity user) {
        return new UserModel(user.getUuid(),
                             user.getEmail(),
                             user.getName(),
                             user.getTopics(),
                             user.getCreatedAt(),
                             user.getUpdatedAt());
    }
}
