package oc.mdd.dto;

import jakarta.validation.constraints.Min;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class PaginationQueryDto {

    private static final int DEFAULT_PAGE = 0;
    private static final int DEFAULT_SIZE = 50;
    private static final String SORT = "ASC";

    @Min(value = 0, message = "Page number must be greater than or equal to 0")
    private int page;

    @Min(value = 1, message = "Page size must be greater than 0")
    private int size;

    private String sort;


    public PaginationQueryDto(Integer page, Integer size, String sort) {
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
        if(sort == null || sort.isEmpty()) {
            this.sort = SORT;
        } else {
            this.sort = sort;
        }
    }

}

