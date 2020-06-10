package com.skcc.bookcatalog.web.rest;

import com.skcc.bookcatalog.service.BookCatalogService;
import com.skcc.bookcatalog.web.rest.errors.BadRequestAlertException;
import com.skcc.bookcatalog.service.dto.BookCatalogDTO;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link com.skcc.bookcatalog.domain.BookCatalog}.
 */
@RestController
@RequestMapping("/api")
public class BookCatalogResource {

    private final Logger log = LoggerFactory.getLogger(BookCatalogResource.class);

    private static final String ENTITY_NAME = "bookCatalogBookCatalog";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final BookCatalogService bookCatalogService;

    public BookCatalogResource(BookCatalogService bookCatalogService) {
        this.bookCatalogService = bookCatalogService;
    }

    /**
     * {@code POST  /book-catalogs} : Create a new bookCatalog.
     *
     * @param bookCatalogDTO the bookCatalogDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new bookCatalogDTO, or with status {@code 400 (Bad Request)} if the bookCatalog has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/book-catalogs")
    public ResponseEntity<BookCatalogDTO> createBookCatalog(@RequestBody BookCatalogDTO bookCatalogDTO) throws URISyntaxException {
        log.debug("REST request to save BookCatalog : {}", bookCatalogDTO);
        if (bookCatalogDTO.getId() != null) {
            throw new BadRequestAlertException("A new bookCatalog cannot already have an ID", ENTITY_NAME, "idexists");
        }
        BookCatalogDTO result = bookCatalogService.save(bookCatalogDTO);
        return ResponseEntity.created(new URI("/api/book-catalogs/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId()))
            .body(result);
    }

    /**
     * {@code PUT  /book-catalogs} : Updates an existing bookCatalog.
     *
     * @param bookCatalogDTO the bookCatalogDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated bookCatalogDTO,
     * or with status {@code 400 (Bad Request)} if the bookCatalogDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the bookCatalogDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/book-catalogs")
    public ResponseEntity<BookCatalogDTO> updateBookCatalog(@RequestBody BookCatalogDTO bookCatalogDTO) throws URISyntaxException {
        log.debug("REST request to update BookCatalog : {}", bookCatalogDTO);
        if (bookCatalogDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        BookCatalogDTO result = bookCatalogService.save(bookCatalogDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, bookCatalogDTO.getId()))
            .body(result);
    }

    /**
     * {@code GET  /book-catalogs} : get all the bookCatalogs.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of bookCatalogs in body.
     */
    @GetMapping("/book-catalogs")
    public ResponseEntity<List<BookCatalogDTO>> getAllBookCatalogs(Pageable pageable) {
        log.debug("REST request to get a page of BookCatalogs");
        Page<BookCatalogDTO> page = bookCatalogService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /book-catalogs/:id} : get the "id" bookCatalog.
     *
     * @param id the id of the bookCatalogDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the bookCatalogDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/book-catalogs/{id}")
    public ResponseEntity<BookCatalogDTO> getBookCatalog(@PathVariable String id) {
        log.debug("REST request to get BookCatalog : {}", id);
        Optional<BookCatalogDTO> bookCatalogDTO = bookCatalogService.findOne(id);
        return ResponseUtil.wrapOrNotFound(bookCatalogDTO);
    }

    /**
     * {@code DELETE  /book-catalogs/:id} : delete the "id" bookCatalog.
     *
     * @param id the id of the bookCatalogDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/book-catalogs/{id}")
    public ResponseEntity<Void> deleteBookCatalog(@PathVariable String id) {
        log.debug("REST request to delete BookCatalog : {}", id);
        bookCatalogService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id)).build();
    }
}
