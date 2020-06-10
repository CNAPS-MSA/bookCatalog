package com.skcc.bookcatalog.web.rest;

import com.skcc.bookcatalog.BookCatalogApp;
import com.skcc.bookcatalog.domain.TopListBooks;
import com.skcc.bookcatalog.repository.TopListBooksRepository;
import com.skcc.bookcatalog.service.TopListBooksService;
import com.skcc.bookcatalog.service.dto.TopListBooksDTO;
import com.skcc.bookcatalog.service.mapper.TopListBooksMapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link TopListBooksResource} REST controller.
 */
@SpringBootTest(classes = BookCatalogApp.class)
@AutoConfigureMockMvc
@WithMockUser
public class TopListBooksResourceIT {

    private static final String DEFAULT_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_TITLE = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String DEFAULT_AUTHOR = "AAAAAAAAAA";
    private static final String UPDATED_AUTHOR = "BBBBBBBBBB";

    private static final String DEFAULT_PUBLISHER = "AAAAAAAAAA";
    private static final String UPDATED_PUBLISHER = "BBBBBBBBBB";

    @Autowired
    private TopListBooksRepository topListBooksRepository;

    @Autowired
    private TopListBooksMapper topListBooksMapper;

    @Autowired
    private TopListBooksService topListBooksService;

    @Autowired
    private MockMvc restTopListBooksMockMvc;

    private TopListBooks topListBooks;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TopListBooks createEntity() {
        TopListBooks topListBooks = new TopListBooks()
            .title(DEFAULT_TITLE)
            .description(DEFAULT_DESCRIPTION)
            .author(DEFAULT_AUTHOR)
            .publisher(DEFAULT_PUBLISHER);
        return topListBooks;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TopListBooks createUpdatedEntity() {
        TopListBooks topListBooks = new TopListBooks()
            .title(UPDATED_TITLE)
            .description(UPDATED_DESCRIPTION)
            .author(UPDATED_AUTHOR)
            .publisher(UPDATED_PUBLISHER);
        return topListBooks;
    }

    @BeforeEach
    public void initTest() {
        topListBooksRepository.deleteAll();
        topListBooks = createEntity();
    }

    @Test
    public void createTopListBooks() throws Exception {
        int databaseSizeBeforeCreate = topListBooksRepository.findAll().size();
        // Create the TopListBooks
        TopListBooksDTO topListBooksDTO = topListBooksMapper.toDto(topListBooks);
        restTopListBooksMockMvc.perform(post("/api/top-list-books")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(topListBooksDTO)))
            .andExpect(status().isCreated());

        // Validate the TopListBooks in the database
        List<TopListBooks> topListBooksList = topListBooksRepository.findAll();
        assertThat(topListBooksList).hasSize(databaseSizeBeforeCreate + 1);
        TopListBooks testTopListBooks = topListBooksList.get(topListBooksList.size() - 1);
        assertThat(testTopListBooks.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testTopListBooks.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testTopListBooks.getAuthor()).isEqualTo(DEFAULT_AUTHOR);
        assertThat(testTopListBooks.getPublisher()).isEqualTo(DEFAULT_PUBLISHER);
    }

    @Test
    public void createTopListBooksWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = topListBooksRepository.findAll().size();

        // Create the TopListBooks with an existing ID
        topListBooks.setId("existing_id");
        TopListBooksDTO topListBooksDTO = topListBooksMapper.toDto(topListBooks);

        // An entity with an existing ID cannot be created, so this API call must fail
        restTopListBooksMockMvc.perform(post("/api/top-list-books")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(topListBooksDTO)))
            .andExpect(status().isBadRequest());

        // Validate the TopListBooks in the database
        List<TopListBooks> topListBooksList = topListBooksRepository.findAll();
        assertThat(topListBooksList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    public void getAllTopListBooks() throws Exception {
        // Initialize the database
        topListBooksRepository.save(topListBooks);

        // Get all the topListBooksList
        restTopListBooksMockMvc.perform(get("/api/top-list-books?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(topListBooks.getId())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].author").value(hasItem(DEFAULT_AUTHOR)))
            .andExpect(jsonPath("$.[*].publisher").value(hasItem(DEFAULT_PUBLISHER)));
    }
    
    @Test
    public void getTopListBooks() throws Exception {
        // Initialize the database
        topListBooksRepository.save(topListBooks);

        // Get the topListBooks
        restTopListBooksMockMvc.perform(get("/api/top-list-books/{id}", topListBooks.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(topListBooks.getId()))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.author").value(DEFAULT_AUTHOR))
            .andExpect(jsonPath("$.publisher").value(DEFAULT_PUBLISHER));
    }
    @Test
    public void getNonExistingTopListBooks() throws Exception {
        // Get the topListBooks
        restTopListBooksMockMvc.perform(get("/api/top-list-books/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    public void updateTopListBooks() throws Exception {
        // Initialize the database
        topListBooksRepository.save(topListBooks);

        int databaseSizeBeforeUpdate = topListBooksRepository.findAll().size();

        // Update the topListBooks
        TopListBooks updatedTopListBooks = topListBooksRepository.findById(topListBooks.getId()).get();
        updatedTopListBooks
            .title(UPDATED_TITLE)
            .description(UPDATED_DESCRIPTION)
            .author(UPDATED_AUTHOR)
            .publisher(UPDATED_PUBLISHER);
        TopListBooksDTO topListBooksDTO = topListBooksMapper.toDto(updatedTopListBooks);

        restTopListBooksMockMvc.perform(put("/api/top-list-books")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(topListBooksDTO)))
            .andExpect(status().isOk());

        // Validate the TopListBooks in the database
        List<TopListBooks> topListBooksList = topListBooksRepository.findAll();
        assertThat(topListBooksList).hasSize(databaseSizeBeforeUpdate);
        TopListBooks testTopListBooks = topListBooksList.get(topListBooksList.size() - 1);
        assertThat(testTopListBooks.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testTopListBooks.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testTopListBooks.getAuthor()).isEqualTo(UPDATED_AUTHOR);
        assertThat(testTopListBooks.getPublisher()).isEqualTo(UPDATED_PUBLISHER);
    }

    @Test
    public void updateNonExistingTopListBooks() throws Exception {
        int databaseSizeBeforeUpdate = topListBooksRepository.findAll().size();

        // Create the TopListBooks
        TopListBooksDTO topListBooksDTO = topListBooksMapper.toDto(topListBooks);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTopListBooksMockMvc.perform(put("/api/top-list-books")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(topListBooksDTO)))
            .andExpect(status().isBadRequest());

        // Validate the TopListBooks in the database
        List<TopListBooks> topListBooksList = topListBooksRepository.findAll();
        assertThat(topListBooksList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    public void deleteTopListBooks() throws Exception {
        // Initialize the database
        topListBooksRepository.save(topListBooks);

        int databaseSizeBeforeDelete = topListBooksRepository.findAll().size();

        // Delete the topListBooks
        restTopListBooksMockMvc.perform(delete("/api/top-list-books/{id}", topListBooks.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<TopListBooks> topListBooksList = topListBooksRepository.findAll();
        assertThat(topListBooksList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
