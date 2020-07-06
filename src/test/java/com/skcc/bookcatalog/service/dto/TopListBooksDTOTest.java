package com.skcc.bookcatalog.service.dto;

import com.skcc.bookcatalog.web.rest.dto.TopListBooksDTO;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.skcc.bookcatalog.web.rest.TestUtil;

public class TopListBooksDTOTest {

    @Test
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(TopListBooksDTO.class);
        TopListBooksDTO topListBooksDTO1 = new TopListBooksDTO();
        topListBooksDTO1.setId("id1");
        TopListBooksDTO topListBooksDTO2 = new TopListBooksDTO();
        assertThat(topListBooksDTO1).isNotEqualTo(topListBooksDTO2);
        topListBooksDTO2.setId(topListBooksDTO1.getId());
        assertThat(topListBooksDTO1).isEqualTo(topListBooksDTO2);
        topListBooksDTO2.setId("id2");
        assertThat(topListBooksDTO1).isNotEqualTo(topListBooksDTO2);
        topListBooksDTO1.setId(null);
        assertThat(topListBooksDTO1).isNotEqualTo(topListBooksDTO2);
    }
}
