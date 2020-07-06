package com.skcc.bookcatalog.web.rest;

import com.skcc.bookcatalog.service.TopListBooksService;
import com.skcc.bookcatalog.web.rest.errors.BadRequestAlertException;
import com.skcc.bookcatalog.web.rest.dto.TopListBooksDTO;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link com.skcc.bookcatalog.domain.TopListBooks}.
 */
@RestController
@RequestMapping("/api")
public class TopListBooksResource {

    private final Logger log = LoggerFactory.getLogger(TopListBooksResource.class);

    private static final String ENTITY_NAME = "bookCatalogTopListBooks";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TopListBooksService topListBooksService;

    public TopListBooksResource(TopListBooksService topListBooksService) {
        this.topListBooksService = topListBooksService;
    }

    /**
     * {@code POST  /top-list-books} : Create a new topListBooks.
     *
     * @param topListBooksDTO the topListBooksDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new topListBooksDTO, or with status {@code 400 (Bad Request)} if the topListBooks has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/top-list-books")
    public ResponseEntity<TopListBooksDTO> createTopListBooks(@RequestBody TopListBooksDTO topListBooksDTO) throws URISyntaxException {
        log.debug("REST request to save TopListBooks : {}", topListBooksDTO);
        if (topListBooksDTO.getId() != null) {
            throw new BadRequestAlertException("A new topListBooks cannot already have an ID", ENTITY_NAME, "idexists");
        }
        TopListBooksDTO result = topListBooksService.save(topListBooksDTO);
        return ResponseEntity.created(new URI("/api/top-list-books/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId()))
            .body(result);
    }

    /**
     * {@code PUT  /top-list-books} : Updates an existing topListBooks.
     *
     * @param topListBooksDTO the topListBooksDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated topListBooksDTO,
     * or with status {@code 400 (Bad Request)} if the topListBooksDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the topListBooksDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/top-list-books")
    public ResponseEntity<TopListBooksDTO> updateTopListBooks(@RequestBody TopListBooksDTO topListBooksDTO) throws URISyntaxException {
        log.debug("REST request to update TopListBooks : {}", topListBooksDTO);
        if (topListBooksDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        TopListBooksDTO result = topListBooksService.save(topListBooksDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, topListBooksDTO.getId()))
            .body(result);
    }

    /**
     * {@code GET  /top-list-books} : get all the topListBooks.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of topListBooks in body.
     */
    @GetMapping("/top-list-books")
    public ResponseEntity<List<TopListBooksDTO>> getAllTopListBooks(Pageable pageable) {
        log.debug("REST request to get a page of TopListBooks");
        Page<TopListBooksDTO> page = topListBooksService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /top-list-books/:id} : get the "id" topListBooks.
     *
     * @param id the id of the topListBooksDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the topListBooksDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/top-list-books/{id}")
    public ResponseEntity<TopListBooksDTO> getTopListBooks(@PathVariable String id) {
        log.debug("REST request to get TopListBooks : {}", id);
        Optional<TopListBooksDTO> topListBooksDTO = topListBooksService.findOne(id);
        return ResponseUtil.wrapOrNotFound(topListBooksDTO);
    }

    /**
     * {@code DELETE  /top-list-books/:id} : delete the "id" topListBooks.
     *
     * @param id the id of the topListBooksDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/top-list-books/{id}")
    public ResponseEntity<Void> deleteTopListBooks(@PathVariable String id) {
        log.debug("REST request to delete TopListBooks : {}", id);
        topListBooksService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id)).build();
    }
}
