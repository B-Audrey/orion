package oc.mdd.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommentCreationDto {
    @NotEmpty
    private String content;
}
