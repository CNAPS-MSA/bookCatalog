package com.skcc.bookcatalog.web.rest.dto;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

/**
 * A DTO for the {@link com.skcc.bookcatalog.domain.TopListBooks} entity.
 */
@Getter
@Setter
@EqualsAndHashCode
@ToString
public class TopListBooksDTO implements Serializable {

    private String id;

    private String title;

    private String description;

    private String author;

    private String publisher;



}
