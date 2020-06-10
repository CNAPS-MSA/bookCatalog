package com.skcc.bookcatalog.repository;

import com.skcc.bookcatalog.domain.TopListBooks;

import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data MongoDB repository for the TopListBooks entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TopListBooksRepository extends MongoRepository<TopListBooks, String> {
}
