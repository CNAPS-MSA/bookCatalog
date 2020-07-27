package com.skcc.bookcatalog.service.impl;

import com.skcc.bookcatalog.domain.CatalogChanged;
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

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

/**
 * Service Implementation for managing {@link BookCatalog}.
 */
@Service
public class BookCatalogServiceImpl implements BookCatalogService {

    private final Logger log = LoggerFactory.getLogger(BookCatalogServiceImpl.class);

    private final BookCatalogRepository bookCatalogRepository;

    private final BookCatalogMapper bookCatalogMapper;
    private static final DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd");

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

    @Override
    public Page<BookCatalogDTO> findBookByTitle(String title, Pageable pageable)
    {
        return bookCatalogRepository.findByTitleContaining(title, pageable).map(bookCatalogMapper::toDto);
    }

    @Override
    public BookCatalog registerNewBook(CatalogChanged catalogChanged) {
        System.out.println("register new book");
        BookCatalog bookCatalog = new BookCatalog();
        bookCatalog.setBookId(catalogChanged.getBookId());
        bookCatalog.setAuthor(catalogChanged.getAuthor());
        bookCatalog.setClassification(catalogChanged.getClassification());
        bookCatalog.setDescription(catalogChanged.getDescription());
        bookCatalog.setPublicationDate(LocalDate.parse(catalogChanged.getPublicationDate(), fmt));
        bookCatalog.setRented(catalogChanged.getRented());
        bookCatalog.setTitle(catalogChanged.getTitle());
        bookCatalog.setRentCnt(catalogChanged.getRentCnt());
        bookCatalog= bookCatalogRepository.save(bookCatalog);
        return bookCatalog;
    }

    @Override
    public void deleteBook(CatalogChanged catalogChanged) {
        bookCatalogRepository.deleteByBookId(catalogChanged.getBookId());
    }

    @Override
    public BookCatalog updateBookStatus(CatalogChanged catalogChanged) {
        BookCatalog bookCatalog = bookCatalogRepository.findByBookId(catalogChanged.getBookId());
        if(catalogChanged.getEventType().equals("RENT_BOOK")) {
            Long newCnt = bookCatalog.getRentCnt() + (long) 1;
            bookCatalog.setRentCnt(newCnt);
            bookCatalog.setRented(true);
            bookCatalog= bookCatalogRepository.save(bookCatalog);
        }else if(catalogChanged.getEventType().equals("RETURN_BOOK")){
            bookCatalog.setRented(false);
            bookCatalog= bookCatalogRepository.save(bookCatalog);

        }
        return bookCatalog;

    }

    @Override
    public BookCatalog updateBookInfo(CatalogChanged catalogChanged) {
        BookCatalog bookCatalog = bookCatalogRepository.findByBookId(catalogChanged.getBookId());
        bookCatalog.setAuthor(catalogChanged.getAuthor());
        bookCatalog.setClassification(catalogChanged.getClassification());
        bookCatalog.setDescription(catalogChanged.getDescription());
        bookCatalog.setPublicationDate(LocalDate.parse(catalogChanged.getPublicationDate(), fmt));
        bookCatalog.setRented(catalogChanged.getRented());
        bookCatalog.setTitle(catalogChanged.getTitle());
        bookCatalog.setRentCnt(catalogChanged.getRentCnt());
        bookCatalogRepository.save(bookCatalog);
        return bookCatalog;
    }

    @Override
    public void processCatalogChanged(CatalogChanged catalogChanged) {
        String eventType  = catalogChanged.getEventType();
        switch (eventType) {
            case "NEW_BOOK":
               registerNewBook(catalogChanged);
                break;
            case "DELETE_BOOK":
                deleteBook(catalogChanged);
                break;
            case "RENT_BOOK":
            case "RETURN_BOOK":
                updateBookStatus(catalogChanged);
                break;
            case "UPDATE_BOOK":
                updateBookInfo(catalogChanged);
                break;
        }
    }
}
