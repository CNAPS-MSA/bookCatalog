package com.skcc.bookcatalog.web.rest;

import com.skcc.bookcatalog.BookCatalogApp;
import com.skcc.bookcatalog.domain.BookCatalog;
import com.skcc.bookcatalog.repository.BookCatalogRepository;
import com.skcc.bookcatalog.service.BookCatalogService;
import com.skcc.bookcatalog.web.rest.dto.BookCatalogDTO;
import com.skcc.bookcatalog.web.rest.mapper.BookCatalogMapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link BookCatalogResource} REST controller.
 */
@SpringBootTest(classes = BookCatalogApp.class)
@AutoConfigureMockMvc
@WithMockUser
public class BookCatalogResourceIT {

    private static final String DEFAULT_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_TITLE = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String DEFAULT_AUTHOR = "AAAAAAAAAA";
    private static final String UPDATED_AUTHOR = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_PUBLICATION_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_PUBLICATION_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final String DEFAULT_CLASSIFICATION = "AAAAAAAAAA";
    private static final String UPDATED_CLASSIFICATION = "BBBBBBBBBB";

    private static final Boolean DEFAULT_RENTED = false;
    private static final Boolean UPDATED_RENTED = true;

    private static final Long DEFAULT_RENT_CNT = 1L;
    private static final Long UPDATED_RENT_CNT = 2L;

    private static final Long DEFAULT_BOOK_ID = 1L;
    private static final Long UPDATED_BOOK_ID = 2L;

    @Autowired
    private BookCatalogRepository bookCatalogRepository;

    @Autowired
    private BookCatalogMapper bookCatalogMapper;

    @Autowired
    private BookCatalogService bookCatalogService;

    @Autowired
    private MockMvc restBookCatalogMockMvc;

    private BookCatalog bookCatalog;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static BookCatalog createEntity() {
        BookCatalog bookCatalog = new BookCatalog()
            .title(DEFAULT_TITLE)
            .description(DEFAULT_DESCRIPTION)
            .author(DEFAULT_AUTHOR)
            .publicationDate(DEFAULT_PUBLICATION_DATE)
            .classification(DEFAULT_CLASSIFICATION)
            .rented(DEFAULT_RENTED)
            .rentCnt(DEFAULT_RENT_CNT)
            .bookId(DEFAULT_BOOK_ID);
        return bookCatalog;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static BookCatalog createUpdatedEntity() {
        BookCatalog bookCatalog = new BookCatalog()
            .title(UPDATED_TITLE)
            .description(UPDATED_DESCRIPTION)
            .author(UPDATED_AUTHOR)
            .publicationDate(UPDATED_PUBLICATION_DATE)
            .classification(UPDATED_CLASSIFICATION)
            .rented(UPDATED_RENTED)
            .rentCnt(UPDATED_RENT_CNT)
            .bookId(UPDATED_BOOK_ID);
        return bookCatalog;
    }

    @BeforeEach
    public void initTest() {
        bookCatalogRepository.deleteAll();
        bookCatalog = createEntity();
    }

    @Test
    public void createBookCatalog() throws Exception {
        int databaseSizeBeforeCreate = bookCatalogRepository.findAll().size();
        // Create the BookCatalog
        BookCatalogDTO bookCatalogDTO = bookCatalogMapper.toDto(bookCatalog);
        restBookCatalogMockMvc.perform(post("/api/book-catalogs")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(bookCatalogDTO)))
            .andExpect(status().isCreated());

        // Validate the BookCatalog in the database
        List<BookCatalog> bookCatalogList = bookCatalogRepository.findAll();
        assertThat(bookCatalogList).hasSize(databaseSizeBeforeCreate + 1);
        BookCatalog testBookCatalog = bookCatalogList.get(bookCatalogList.size() - 1);
        assertThat(testBookCatalog.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testBookCatalog.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testBookCatalog.getAuthor()).isEqualTo(DEFAULT_AUTHOR);
        assertThat(testBookCatalog.getPublicationDate()).isEqualTo(DEFAULT_PUBLICATION_DATE);
        assertThat(testBookCatalog.getClassification()).isEqualTo(DEFAULT_CLASSIFICATION);
        assertThat(testBookCatalog.isRented()).isEqualTo(DEFAULT_RENTED);
        assertThat(testBookCatalog.getRentCnt()).isEqualTo(DEFAULT_RENT_CNT);
        assertThat(testBookCatalog.getBookId()).isEqualTo(DEFAULT_BOOK_ID);
    }

    @Test
    public void createBookCatalogWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = bookCatalogRepository.findAll().size();

        // Create the BookCatalog with an existing ID
        bookCatalog.setId("existing_id");
        BookCatalogDTO bookCatalogDTO = bookCatalogMapper.toDto(bookCatalog);

        // An entity with an existing ID cannot be created, so this API call must fail
        restBookCatalogMockMvc.perform(post("/api/book-catalogs")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(bookCatalogDTO)))
            .andExpect(status().isBadRequest());

        // Validate the BookCatalog in the database
        List<BookCatalog> bookCatalogList = bookCatalogRepository.findAll();
        assertThat(bookCatalogList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    public void getAllBookCatalogs() throws Exception {
        // Initialize the database
        bookCatalogRepository.save(bookCatalog);

        // Get all the bookCatalogList
        restBookCatalogMockMvc.perform(get("/api/book-catalogs?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(bookCatalog.getId())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].author").value(hasItem(DEFAULT_AUTHOR)))
            .andExpect(jsonPath("$.[*].publicationDate").value(hasItem(DEFAULT_PUBLICATION_DATE.toString())))
            .andExpect(jsonPath("$.[*].classification").value(hasItem(DEFAULT_CLASSIFICATION)))
            .andExpect(jsonPath("$.[*].rented").value(hasItem(DEFAULT_RENTED.booleanValue())))
            .andExpect(jsonPath("$.[*].rentCnt").value(hasItem(DEFAULT_RENT_CNT.intValue())))
            .andExpect(jsonPath("$.[*].bookId").value(hasItem(DEFAULT_BOOK_ID.intValue())));
    }

    @Test
    public void getBookCatalog() throws Exception {
        // Initialize the database
        bookCatalogRepository.save(bookCatalog);

        // Get the bookCatalog
        restBookCatalogMockMvc.perform(get("/api/book-catalogs/{id}", bookCatalog.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(bookCatalog.getId()))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.author").value(DEFAULT_AUTHOR))
            .andExpect(jsonPath("$.publicationDate").value(DEFAULT_PUBLICATION_DATE.toString()))
            .andExpect(jsonPath("$.classification").value(DEFAULT_CLASSIFICATION))
            .andExpect(jsonPath("$.rented").value(DEFAULT_RENTED.booleanValue()))
            .andExpect(jsonPath("$.rentCnt").value(DEFAULT_RENT_CNT.intValue()))
            .andExpect(jsonPath("$.bookId").value(DEFAULT_BOOK_ID.intValue()));
    }
    @Test
    public void getNonExistingBookCatalog() throws Exception {
        // Get the bookCatalog
        restBookCatalogMockMvc.perform(get("/api/book-catalogs/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    public void updateBookCatalog() throws Exception {
        // Initialize the database
        bookCatalogRepository.save(bookCatalog);

        int databaseSizeBeforeUpdate = bookCatalogRepository.findAll().size();

        // Update the bookCatalog
        BookCatalog updatedBookCatalog = bookCatalogRepository.findById(bookCatalog.getId()).get();
        updatedBookCatalog
            .title(UPDATED_TITLE)
            .description(UPDATED_DESCRIPTION)
            .author(UPDATED_AUTHOR)
            .publicationDate(UPDATED_PUBLICATION_DATE)
            .classification(UPDATED_CLASSIFICATION)
            .rented(UPDATED_RENTED)
            .rentCnt(UPDATED_RENT_CNT)
            .bookId(UPDATED_BOOK_ID);
        BookCatalogDTO bookCatalogDTO = bookCatalogMapper.toDto(updatedBookCatalog);

        restBookCatalogMockMvc.perform(put("/api/book-catalogs")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(bookCatalogDTO)))
            .andExpect(status().isOk());

        // Validate the BookCatalog in the database
        List<BookCatalog> bookCatalogList = bookCatalogRepository.findAll();
        assertThat(bookCatalogList).hasSize(databaseSizeBeforeUpdate);
        BookCatalog testBookCatalog = bookCatalogList.get(bookCatalogList.size() - 1);
        assertThat(testBookCatalog.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testBookCatalog.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testBookCatalog.getAuthor()).isEqualTo(UPDATED_AUTHOR);
        assertThat(testBookCatalog.getPublicationDate()).isEqualTo(UPDATED_PUBLICATION_DATE);
        assertThat(testBookCatalog.getClassification()).isEqualTo(UPDATED_CLASSIFICATION);
        assertThat(testBookCatalog.isRented()).isEqualTo(UPDATED_RENTED);
        assertThat(testBookCatalog.getRentCnt()).isEqualTo(UPDATED_RENT_CNT);
        assertThat(testBookCatalog.getBookId()).isEqualTo(UPDATED_BOOK_ID);
    }

    @Test
    public void updateNonExistingBookCatalog() throws Exception {
        int databaseSizeBeforeUpdate = bookCatalogRepository.findAll().size();

        // Create the BookCatalog
        BookCatalogDTO bookCatalogDTO = bookCatalogMapper.toDto(bookCatalog);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBookCatalogMockMvc.perform(put("/api/book-catalogs")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(bookCatalogDTO)))
            .andExpect(status().isBadRequest());

        // Validate the BookCatalog in the database
        List<BookCatalog> bookCatalogList = bookCatalogRepository.findAll();
        assertThat(bookCatalogList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    public void deleteBookCatalog() throws Exception {
        // Initialize the database
        bookCatalogRepository.save(bookCatalog);

        int databaseSizeBeforeDelete = bookCatalogRepository.findAll().size();

        // Delete the bookCatalog
        restBookCatalogMockMvc.perform(delete("/api/book-catalogs/{id}", bookCatalog.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<BookCatalog> bookCatalogList = bookCatalogRepository.findAll();
        assertThat(bookCatalogList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
