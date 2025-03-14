package com.backend.domicare.dto.paging;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ResultPagingDTO {
    private Meta meta;
    private Object data;

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Meta {
        private int page;
        private int size;
        private long total;
        private int totalPages;
    }

}
