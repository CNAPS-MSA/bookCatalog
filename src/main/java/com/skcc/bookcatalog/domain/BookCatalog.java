package com.skcc.bookcatalog.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * A BookCatalog.
 */
@Document(collection = "book_catalog")
@Data
@ToString
public class BookCatalog implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    @Field("title")
    private String title;

    @Field("description")
    private String description;

    @Field("author")
    private String author;

    @Field("publication_date")
    private LocalDate publicationDate;

    @Field("classification")
    private String classification;

    @Field("rented")
    private Boolean rented;

    @Field("rent_cnt")
    private Long rentCnt;

    @Field("book_id")
    private Long bookId;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public BookCatalog title(String title) {
        this.title = title;
        return this;
    }


    public BookCatalog description(String description) {
        this.description = description;
        return this;
    }


    public BookCatalog author(String author) {
        this.author = author;
        return this;
    }


    public BookCatalog publicationDate(LocalDate publicationDate) {
        this.publicationDate = publicationDate;
        return this;
    }


    public BookCatalog classification(String classification) {
        this.classification = classification;
        return this;
    }



    public Boolean isRented() {
        return rented;
    }

    public BookCatalog rented(Boolean rented) {
        this.rented = rented;
        return this;
    }


    public BookCatalog rentCnt(Long rentCnt) {
        this.rentCnt = rentCnt;
        return this;
    }


    public BookCatalog bookId(Long bookId) {
        this.bookId = bookId;
        return this;
    }


    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof BookCatalog)) {
            return false;
        }
        return id != null && id.equals(((BookCatalog) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    public static BookCatalog registerNewBookCatalog(BookChanged bookChanged){
        BookCatalog bookCatalog = new BookCatalog();
        bookCatalog.setBookId(bookChanged.getBookId());
        bookCatalog.setAuthor(bookChanged.getAuthor());
        bookCatalog.setClassification(bookChanged.getClassification());
        bookCatalog.setDescription(bookChanged.getDescription());
        bookCatalog.setPublicationDate(LocalDate.parse(bookChanged.getPublicationDate(), DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        bookCatalog.setRented(bookChanged.getRented());
        bookCatalog.setTitle(bookChanged.getTitle());
        bookCatalog.setRentCnt(bookChanged.getRentCnt());
        return bookCatalog;
    }

    public BookCatalog rentBook(){
        this.setRentCnt(this.getRentCnt()+(long)1);
        this.setRented(true);
        return this;
    }

    public BookCatalog returnBook(){
        this.setRented(false);
        return this;
    }

    public BookCatalog updateBookCatalogInfo(BookChanged bookChanged){
        this.setAuthor(bookChanged.getAuthor());
        this.setClassification(bookChanged.getClassification());
        this.setDescription(bookChanged.getDescription());
        this.setPublicationDate(LocalDate.parse(bookChanged.getPublicationDate(), DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        this.setRented(bookChanged.getRented());
        this.setTitle(bookChanged.getTitle());
        this.setRentCnt(bookChanged.getRentCnt());
        return this;
    }

}
