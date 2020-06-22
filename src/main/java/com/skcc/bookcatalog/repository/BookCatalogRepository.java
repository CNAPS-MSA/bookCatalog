package com.skcc.bookcatalog.repository;

import com.skcc.bookcatalog.domain.BookCatalog;

import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import javax.swing.text.html.Option;
import java.util.Optional;

/**
 * Spring Data MongoDB repository for the BookCatalog entity.
 */
@SuppressWarnings("unused")
@Repository
public interface BookCatalogRepository extends MongoRepository<BookCatalog, String> {
    BookCatalog findByTitle(String title);
    void deleteByTitle(String title);
    void removeByTitle(String title);
}
