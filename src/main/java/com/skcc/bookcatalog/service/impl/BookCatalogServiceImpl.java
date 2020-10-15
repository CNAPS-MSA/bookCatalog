package com.skcc.bookcatalog.service.impl;

import com.skcc.bookcatalog.domain.BookChanged;
import com.skcc.bookcatalog.service.BookCatalogService;
import com.skcc.bookcatalog.domain.BookCatalog;
import com.skcc.bookcatalog.repository.BookCatalogRepository;
import com.skcc.bookcatalog.web.rest.mapper.BookCatalogMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

/**
 * Service Implementation for managing {@link BookCatalog}.
 */
@Service
public class BookCatalogServiceImpl implements BookCatalogService {

    private final Logger log = LoggerFactory.getLogger(BookCatalogServiceImpl.class);

    private final BookCatalogRepository bookCatalogRepository;

    private final BookCatalogMapper bookCatalogMapper;
    //private static final DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public BookCatalogServiceImpl(BookCatalogRepository bookCatalogRepository, BookCatalogMapper bookCatalogMapper) {
        this.bookCatalogRepository = bookCatalogRepository;
        this.bookCatalogMapper = bookCatalogMapper;
    }

    /**
     * Save a bookCatalog.
     *
     * @param bookCatalog the entity to save.
     * @return the persisted entity.
     */
    @Override
    public BookCatalog save(BookCatalog bookCatalog) {
        log.debug("Request to save BookCatalog : {}", bookCatalog);
        return bookCatalogRepository.save(bookCatalog);

    }

    /**
     * Get all the bookCatalogs.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Override
    public Page<BookCatalog> findAll(Pageable pageable) {
        log.debug("Request to get all BookCatalogs");
        return bookCatalogRepository.findAll(pageable);
    }


    /**
     * Get one bookCatalog by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    public Optional<BookCatalog> findOne(String id) {
        log.debug("Request to get BookCatalog : {}", id);
        return bookCatalogRepository.findById(id);
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

    @Override
    public Page<BookCatalog> findBookByTitle(String title, Pageable pageable)
    {
        return bookCatalogRepository.findByTitleContaining(title, pageable);
    }


    private BookCatalog registerNewBook(BookChanged bookChanged) {
        BookCatalog bookCatalog = BookCatalog.registerNewBookCatalog(bookChanged);
        bookCatalog= bookCatalogRepository.save(bookCatalog);
        return bookCatalog;
    }


    private void deleteBook(BookChanged bookChanged) {
        bookCatalogRepository.deleteByBookId(bookChanged.getBookId());
    }


    private BookCatalog updateBookStatus(BookChanged bookChanged) {
        BookCatalog bookCatalog = bookCatalogRepository.findByBookId(bookChanged.getBookId());
        if(bookChanged.getEventType().equals("RENT_BOOK")) {
            bookCatalog= bookCatalog.rentBook();

        }else if(bookChanged.getEventType().equals("RETURN_BOOK")){
            bookCatalog= bookCatalog.returnBook();

        }
        bookCatalog= bookCatalogRepository.save(bookCatalog);
        return bookCatalog;

    }


    private BookCatalog updateBookInfo(BookChanged bookChanged) {
        BookCatalog bookCatalog = bookCatalogRepository.findByBookId(bookChanged.getBookId());
        bookCatalog = bookCatalog.updateBookCatalogInfo(bookChanged);
        bookCatalog= bookCatalogRepository.save(bookCatalog);
        return bookCatalog;
    }

    @Override
    public void processCatalogChanged(BookChanged bookChanged) {
        String eventType  = bookChanged.getEventType();
        switch (eventType) {
            case "NEW_BOOK":
               registerNewBook(bookChanged);
                break;
            case "DELETE_BOOK":
                deleteBook(bookChanged);
                break;
            case "RENT_BOOK":
            case "RETURN_BOOK":
                updateBookStatus(bookChanged);
                break;
            case "UPDATE_BOOK":
                updateBookInfo(bookChanged);
                break;
        }
    }

    @Override
    public List<BookCatalog> loadTop10() {
        return bookCatalogRepository.findTop10ByOrderByRentCntDesc();
    }
}
