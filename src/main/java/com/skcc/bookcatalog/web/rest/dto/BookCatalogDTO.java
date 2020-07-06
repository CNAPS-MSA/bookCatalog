package com.skcc.bookcatalog.web.rest.dto;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;
import java.io.Serializable;

/**
 * A DTO for the {@link com.skcc.bookcatalog.domain.BookCatalog} entity.
 */
@Getter
@Setter
@EqualsAndHashCode
@ToString
public class BookCatalogDTO implements Serializable {

    private String id;

    private String title;

    private String description;

    private String author;

    private LocalDate publicationDate;

    private String classification;

    private Boolean rented;

    private Long rentCnt;

    private Long bookId;



    public Boolean isRented() {
        return rented;
    }


}
