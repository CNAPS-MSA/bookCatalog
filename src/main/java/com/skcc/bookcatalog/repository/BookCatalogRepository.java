package com.skcc.bookcatalog.repository;

import com.skcc.bookcatalog.domain.BookCatalog;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data MongoDB repository for the BookCatalog entity.
 */
@SuppressWarnings("unused")
@Repository
public interface BookCatalogRepository extends MongoRepository<BookCatalog, String> {

    Page<BookCatalog> findByTitleContaining(String title, Pageable pageable);

    BookCatalog findByBookId(Long bookId);

    void deleteByBookId(Long bookId);

    List<BookCatalog> findTop10ByOrderByRentCntDesc();
}
