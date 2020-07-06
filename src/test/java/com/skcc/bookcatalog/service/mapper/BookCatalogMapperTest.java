package com.skcc.bookcatalog.service.mapper;

import com.skcc.bookcatalog.web.rest.mapper.BookCatalogMapper;
import com.skcc.bookcatalog.web.rest.mapper.BookCatalogMapperImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class BookCatalogMapperTest {

    private BookCatalogMapper bookCatalogMapper;

    @BeforeEach
    public void setUp() {
        bookCatalogMapper = new BookCatalogMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        String id = "id1";
        assertThat(bookCatalogMapper.fromId(id).getId()).isEqualTo(id);
        assertThat(bookCatalogMapper.fromId(null)).isNull();
    }
}
