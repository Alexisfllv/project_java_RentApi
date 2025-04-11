package edu.com.rentapi.Pagination;

import org.springframework.data.domain.Page;

import java.util.List;

public record PageResponseDTO<T>(
        List<T> content,
        int page,
        int size,
        long totalElements,
        int totalPages,
        boolean isLast,
        boolean isFirst,
        int numberOfElements
) {public PageResponseDTO(Page<T> page) {
    this(page.getContent(),
            page.getNumber(),
            page.getSize(),
            page.getTotalElements(),
            page.getTotalPages(),
            page.isLast(),
            page.isFirst(),
            page.getNumberOfElements());
}
}