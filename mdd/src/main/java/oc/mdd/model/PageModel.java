package oc.mdd.model;

import lombok.Data;

import java.util.List;

@Data
public class PageModel<T> {
    private List<T> data;
    private Pagination pagination;

    public PageModel(List<T> data, Pagination pagination) {
        this.data = data;
        this.pagination = pagination;
    }


    @Data
    public static class Pagination {
        private long total;
        private int page;
        private int size;

        public Pagination(long totalElements, int number, int size) {
            this.total = totalElements;
            this.page = number;
            this.size = size;
        }
    }

}

