package com.skcc.bookcatalog.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.skcc.bookcatalog.web.rest.TestUtil;

public class BookCatalogTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(BookCatalog.class);
        BookCatalog bookCatalog1 = new BookCatalog();
        bookCatalog1.setId("id1");
        BookCatalog bookCatalog2 = new BookCatalog();
        bookCatalog2.setId(bookCatalog1.getId());
        assertThat(bookCatalog1).isEqualTo(bookCatalog2);
        bookCatalog2.setId("id2");
        assertThat(bookCatalog1).isNotEqualTo(bookCatalog2);
        bookCatalog1.setId(null);
        assertThat(bookCatalog1).isNotEqualTo(bookCatalog2);
    }
}
