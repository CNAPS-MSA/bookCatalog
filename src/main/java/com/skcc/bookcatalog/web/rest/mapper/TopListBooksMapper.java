package com.skcc.bookcatalog.web.rest.mapper;


import com.skcc.bookcatalog.domain.*;
import com.skcc.bookcatalog.web.rest.dto.TopListBooksDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link TopListBooks} and its DTO {@link TopListBooksDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface TopListBooksMapper extends EntityMapper<TopListBooksDTO, TopListBooks> {



    default TopListBooks fromId(String id) {
        if (id == null) {
            return null;
        }
        TopListBooks topListBooks = new TopListBooks();
        topListBooks.setId(id);
        return topListBooks;
    }
}
