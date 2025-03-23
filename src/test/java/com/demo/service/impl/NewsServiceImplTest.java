package com.demo.service.impl;

import com.demo.dao.NewsDao;
import com.demo.entity.News;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import javax.persistence.EntityNotFoundException;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class NewsServiceImplTest {

    @Mock
    private NewsDao newsDao;

    @InjectMocks
    private NewsServiceImpl newsService;

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void findAll() {
        findAll_ValidPageable_ReturnsPage();
    }

    void findAll_ValidPageable_ReturnsPage() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<News> expectedPage = new PageImpl<>(Collections.singletonList(new News()));
        when(newsDao.findAll(pageable)).thenReturn(expectedPage);

        Page<News> result = newsService.findAll(pageable);

        assertEquals(expectedPage, result);
    }

    @Test
    void findById() {
        findById_ValidId_ReturnsNews();
        findById_InvalidId_ThrowsException();
    }

    void findById_ValidId_ReturnsNews() {
        News expected = new News();
        expected.setNewsID(1);
        when(newsDao.getOne(1)).thenReturn(expected);

        News result = newsService.findById(1);

        assertEquals(expected, result);
    }

    void findById_InvalidId_ThrowsException() {
        when(newsDao.getOne(999)).thenThrow(EntityNotFoundException.class);

        assertThrows(EntityNotFoundException.class, () -> newsService.findById(999));
    }

    @Test
    void create() {
        create_ValidNews_ReturnsId();
        create_InvalidNews_ThrowsException();
        create_NullNews_ThrowsException();
    }

    void create_ValidNews_ReturnsId() {
        News news = new News();
        News savedNews = new News();
        savedNews.setNewsID(100);
        when(newsDao.save(news)).thenReturn(savedNews);

        int result = newsService.create(news);

        assertEquals(100, result);
    }

    void create_InvalidNews_ThrowsException() {
        News invalidNews = new News(); // Assume missing required fields
        when(newsDao.save(invalidNews)).thenThrow(DataIntegrityViolationException.class);

        assertThrows(DataIntegrityViolationException.class, () -> newsService.create(invalidNews));
    }

    void create_NullNews_ThrowsException() {
        when(newsDao.save(null)).thenThrow(IllegalArgumentException.class);

        assertThrows(IllegalArgumentException.class, () -> newsService.create(null));
    }

    @Test
    void delById() {
        delById_ValidId_DeletesNews();
        delById_InvalidId_NoException();
        delById_ZeroId_DeletesWithoutError();
    }

    void delById_ValidId_DeletesNews() {
        newsService.delById(100);

        verify(newsDao).deleteById(100);
    }

    void delById_InvalidId_NoException() {
        doNothing().when(newsDao).deleteById(999);

        assertDoesNotThrow(() -> newsService.delById(999));
    }

    void delById_ZeroId_DeletesWithoutError() {
        newsService.delById(0);

        verify(newsDao).deleteById(0);
    }

    @Test
    void update() {
        update_ValidNews_SavesChanges();
        update_NullNews_ThrowsException();
    }

    void update_ValidNews_SavesChanges() {
        News news = new News();
        newsService.update(news);

        verify(newsDao).save(news);
    }

    void update_NullNews_ThrowsException() {
        doThrow(IllegalArgumentException.class).when(newsDao).save(null);

        assertThrows(IllegalArgumentException.class, () -> newsService.update(null));
    }
}