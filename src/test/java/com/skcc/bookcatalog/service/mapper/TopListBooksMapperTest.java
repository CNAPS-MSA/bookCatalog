package com.skcc.bookcatalog.service.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class TopListBooksMapperTest {

    private TopListBooksMapper topListBooksMapper;

    @BeforeEach
    public void setUp() {
        topListBooksMapper = new TopListBooksMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        String id = "id1";
        assertThat(topListBooksMapper.fromId(id).getId()).isEqualTo(id);
        assertThat(topListBooksMapper.fromId(null)).isNull();
    }
}
