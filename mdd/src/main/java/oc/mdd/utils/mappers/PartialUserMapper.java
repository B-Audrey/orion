package oc.mdd.utils.mappers;

import lombok.RequiredArgsConstructor;
import oc.mdd.entity.UserEntity;
import oc.mdd.model.PartialUserModel;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PartialUserMapper {
    public PartialUserModel convertToPartialUserModel(UserEntity user) {
        return new PartialUserModel(
                user.getUuid(),
                user.getName()
        );
    }
}
