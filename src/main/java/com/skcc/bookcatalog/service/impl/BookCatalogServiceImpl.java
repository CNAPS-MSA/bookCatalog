package com.skcc.bookcatalog.service.impl;

import com.skcc.bookcatalog.service.BookCatalogService;
import com.skcc.bookcatalog.domain.BookCatalog;
import com.skcc.bookcatalog.repository.BookCatalogRepository;
import com.skcc.bookcatalog.web.rest.dto.BookCatalogDTO;
import com.skcc.bookcatalog.web.rest.mapper.BookCatalogMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Service Implementation for managing {@link BookCatalog}.
 */
@Service
public class BookCatalogServiceImpl implements BookCatalogService {

    private final Logger log = LoggerFactory.getLogger(BookCatalogServiceImpl.class);

    private final BookCatalogRepository bookCatalogRepository;

    private final BookCatalogMapper bookCatalogMapper;

    public BookCatalogServiceImpl(BookCatalogRepository bookCatalogRepository, BookCatalogMapper bookCatalogMapper) {
        this.bookCatalogRepository = bookCatalogRepository;
        this.bookCatalogMapper = bookCatalogMapper;
    }

    /**
     * Save a bookCatalog.
     *
     * @param bookCatalogDTO the entity to save.
     * @return the persisted entity.
     */
    @Override
    public BookCatalogDTO save(BookCatalogDTO bookCatalogDTO) {
        log.debug("Request to save BookCatalog : {}", bookCatalogDTO);
        BookCatalog bookCatalog = bookCatalogMapper.toEntity(bookCatalogDTO);
        bookCatalog = bookCatalogRepository.save(bookCatalog);
        return bookCatalogMapper.toDto(bookCatalog);
    }

    /**
     * Get all the bookCatalogs.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Override
    public Page<BookCatalogDTO> findAll(Pageable pageable) {
        log.debug("Request to get all BookCatalogs");
        return bookCatalogRepository.findAll(pageable)
            .map(bookCatalogMapper::toDto);
    }


    /**
     * Get one bookCatalog by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    public Optional<BookCatalogDTO> findOne(String id) {
        log.debug("Request to get BookCatalog : {}", id);
        return bookCatalogRepository.findById(id)
            .map(bookCatalogMapper::toDto);
    }

    /**
     * Delete the bookCatalog by id.
     *
     * @param id the id of the entity.
     */
    @Override
    public void delete(String id) {
        log.debug("Request to delete BookCatalog : {}", id);
        bookCatalogRepository.deleteById(id);
    }
}
