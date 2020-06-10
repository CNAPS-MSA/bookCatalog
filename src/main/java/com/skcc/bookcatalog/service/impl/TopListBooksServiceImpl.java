package com.skcc.bookcatalog.service.impl;

import com.skcc.bookcatalog.service.TopListBooksService;
import com.skcc.bookcatalog.domain.TopListBooks;
import com.skcc.bookcatalog.repository.TopListBooksRepository;
import com.skcc.bookcatalog.service.dto.TopListBooksDTO;
import com.skcc.bookcatalog.service.mapper.TopListBooksMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Service Implementation for managing {@link TopListBooks}.
 */
@Service
public class TopListBooksServiceImpl implements TopListBooksService {

    private final Logger log = LoggerFactory.getLogger(TopListBooksServiceImpl.class);

    private final TopListBooksRepository topListBooksRepository;

    private final TopListBooksMapper topListBooksMapper;

    public TopListBooksServiceImpl(TopListBooksRepository topListBooksRepository, TopListBooksMapper topListBooksMapper) {
        this.topListBooksRepository = topListBooksRepository;
        this.topListBooksMapper = topListBooksMapper;
    }

    /**
     * Save a topListBooks.
     *
     * @param topListBooksDTO the entity to save.
     * @return the persisted entity.
     */
    @Override
    public TopListBooksDTO save(TopListBooksDTO topListBooksDTO) {
        log.debug("Request to save TopListBooks : {}", topListBooksDTO);
        TopListBooks topListBooks = topListBooksMapper.toEntity(topListBooksDTO);
        topListBooks = topListBooksRepository.save(topListBooks);
        return topListBooksMapper.toDto(topListBooks);
    }

    /**
     * Get all the topListBooks.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Override
    public Page<TopListBooksDTO> findAll(Pageable pageable) {
        log.debug("Request to get all TopListBooks");
        return topListBooksRepository.findAll(pageable)
            .map(topListBooksMapper::toDto);
    }


    /**
     * Get one topListBooks by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    public Optional<TopListBooksDTO> findOne(String id) {
        log.debug("Request to get TopListBooks : {}", id);
        return topListBooksRepository.findById(id)
            .map(topListBooksMapper::toDto);
    }

    /**
     * Delete the topListBooks by id.
     *
     * @param id the id of the entity.
     */
    @Override
    public void delete(String id) {
        log.debug("Request to delete TopListBooks : {}", id);
        topListBooksRepository.deleteById(id);
    }
}
