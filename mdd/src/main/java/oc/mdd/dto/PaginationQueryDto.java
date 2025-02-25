package oc.mdd.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class PaginationQueryDto {

    private static final int DEFAULT_PAGE = 0;
    private static final int DEFAULT_SIZE = 50;

    @NotNull(message = "Page number cannot be null")
    @Min(value = 0, message = "Page number must be greater than or equal to 0")
    private int page;

    @NotNull(message = "Page size cannot be null")
    @Min(value = 1, message = "Page size must be greater than 0")
    private int size;

    public PaginationQueryDto(Integer page, Integer size) {
        if (page == null || page < 0) {
            this.page = DEFAULT_PAGE;
        } else {
            this.page = page;
        }
        if (size == null || size <= 0) {
            this.size = DEFAULT_SIZE;
        } else {
            this.size = size;
        }
    }

}

