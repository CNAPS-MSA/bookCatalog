package com.skcc.bookcatalog.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.skcc.bookcatalog.web.rest.TestUtil;

public class TopListBooksTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(TopListBooks.class);
        TopListBooks topListBooks1 = new TopListBooks();
        topListBooks1.setId("id1");
        TopListBooks topListBooks2 = new TopListBooks();
        topListBooks2.setId(topListBooks1.getId());
        assertThat(topListBooks1).isEqualTo(topListBooks2);
        topListBooks2.setId("id2");
        assertThat(topListBooks1).isNotEqualTo(topListBooks2);
        topListBooks1.setId(null);
        assertThat(topListBooks1).isNotEqualTo(topListBooks2);
    }
}
