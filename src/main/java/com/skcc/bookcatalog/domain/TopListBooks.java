package com.skcc.bookcatalog.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.io.Serializable;

/**
 * A TopListBooks.
 */
@Document(collection = "top_list_books")
@Data
@ToString
public class TopListBooks implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    @Field("title")
    private String title;

    @Field("description")
    private String description;

    @Field("author")
    private String author;

    @Field("publisher")
    private String publisher;

    // jhipster-needle-entity-add-field - JHipster will add fields here


    public TopListBooks title(String title) {
        this.title = title;
        return this;
    }


    public TopListBooks description(String description) {
        this.description = description;
        return this;
    }


    public TopListBooks author(String author) {
        this.author = author;
        return this;
    }


    public TopListBooks publisher(String publisher) {
        this.publisher = publisher;
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TopListBooks)) {
            return false;
        }
        return id != null && id.equals(((TopListBooks) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

}
