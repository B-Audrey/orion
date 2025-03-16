package oc.mdd.dto;

import lombok.Data;

@Data
public class PostCreationDto {

    private String title;
    private String content;
    private String topicUuid;

}
