package com.skcc.bookcatalog.service;

import com.skcc.bookcatalog.domain.BookCatalog;
import com.skcc.bookcatalog.web.rest.dto.BookCatalogDTO;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing {@link com.skcc.bookcatalog.domain.BookCatalog}.
 */
public interface BookCatalogService {

    /**
     * Save a bookCatalog.
     *
     * @param bookCatalogDTO the entity to save.
     * @return the persisted entity.
     */
    BookCatalogDTO save(BookCatalogDTO bookCatalogDTO);

    /**
     * Get all the bookCatalogs.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<BookCatalogDTO> findAll(Pageable pageable);


    /**
     * Get the "id" bookCatalog.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<BookCatalogDTO> findOne(String id);

    /**
     * Delete the "id" bookCatalog.
     *
     * @param id the id of the entity.
     */
    void delete(String id);

    BookCatalog findBookByTitle(String title);
}
