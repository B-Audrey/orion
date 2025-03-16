package oc.mdd.utils;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

public class PaginationUtil {

    private static final int DEFAULT_PAGE = 0;
    private static final int DEFAULT_SIZE = 500;
    private static final String DEFAULT_SORT = "ASC";
    private static final String SORT_PROPERTY = "createdAt";

    public static Pageable createPageable(Integer page, Integer size, String sort) {
        if (page == null || page < 0) {
            page = DEFAULT_PAGE;
        }
        if (size == null || size <= 0) {
            size = DEFAULT_SIZE;
        }
        if (sort == null || sort.isEmpty()) {
            sort = DEFAULT_SORT;
        }

        Sort.Direction direction = Sort.Direction.valueOf(sort.toUpperCase());

        return PageRequest.of(page, size, Sort.by(direction, SORT_PROPERTY));
    }
}
