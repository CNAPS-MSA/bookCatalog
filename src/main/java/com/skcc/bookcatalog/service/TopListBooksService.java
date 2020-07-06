package com.skcc.bookcatalog.service;

import com.skcc.bookcatalog.web.rest.dto.TopListBooksDTO;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing {@link com.skcc.bookcatalog.domain.TopListBooks}.
 */
public interface TopListBooksService {

    /**
     * Save a topListBooks.
     *
     * @param topListBooksDTO the entity to save.
     * @return the persisted entity.
     */
    TopListBooksDTO save(TopListBooksDTO topListBooksDTO);

    /**
     * Get all the topListBooks.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<TopListBooksDTO> findAll(Pageable pageable);


    /**
     * Get the "id" topListBooks.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<TopListBooksDTO> findOne(String id);

    /**
     * Delete the "id" topListBooks.
     *
     * @param id the id of the entity.
     */
    void delete(String id);
}
