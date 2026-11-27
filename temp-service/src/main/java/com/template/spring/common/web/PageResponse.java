package com.template.spring.common.web;

import java.util.List;
import org.springframework.data.domain.Page;

// Spring Data 내부 타입을 외부 API 계약과 분리한다.
public record PageResponse<T>(List<T> content, int page, int size, long totalElements, int totalPages) {

    public static <T> PageResponse<T> from(Page<T> page) {
        return new PageResponse<>(page.getContent(), page.getNumber(), page.getSize(),
                page.getTotalElements(), page.getTotalPages());
    }
}
