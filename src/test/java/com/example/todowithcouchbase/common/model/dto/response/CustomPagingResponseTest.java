package com.example.todowithcouchbase.common.model.dto.response;

import com.example.todowithcouchbase.common.model.CustomPage;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Test class for verifying the functionality of {@link CustomPagingResponse}.
 * Ensures proper pagination-related behavior.
 */
class CustomPagingResponseTest {

    @Test
    void testOfMethod() {

        List<String> content = Arrays.asList("Item1", "Item2", "Item3");
        CustomPage<String> customPage = new CustomPage<>(
                content,
                1,
                10, // pageSize
                3L, // totalElementCount
                1  // totalPageCount
        );

        CustomPagingResponse<String> response = CustomPagingResponse.<String>builder()
                .of(customPage)
                .content(content)
                .build();

        assertEquals(customPage.getPageNumber(), response.getPageNumber());
        assertEquals(customPage.getPageSize(), response.getPageSize());
        assertEquals(customPage.getTotalElementCount(), response.getTotalElementCount());
        assertEquals(customPage.getTotalPageCount(), response.getTotalPageCount());
        assertEquals(customPage.getContent(), response.getContent());

    }

}
