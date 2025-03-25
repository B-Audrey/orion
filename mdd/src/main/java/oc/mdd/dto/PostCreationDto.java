package oc.mdd.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PostCreationDto {
    @NotEmpty
    private String title;

    @NotEmpty
    private String content;

    @NotEmpty
    private String topicUuid;
}
