package com.demo.service.impl;

import com.demo.dao.NewsDao;
import com.demo.entity.News;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import javax.persistence.EntityNotFoundException;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class NewsServiceImplTest {

    @Mock
    private NewsDao newsDao;

    @InjectMocks
    private NewsServiceImpl newsService;

    private News validNews;

    @BeforeEach
    void setUp() {
        validNews = new News();
        validNews.setTitle("Breaking News");
        validNews.setContent("Important content");
    }

    @Test
    void findAll_ValidPageable_ReturnsPage() {
        // Arrange
        Pageable pageable = PageRequest.of(0, 10);
        Page<News> expectedPage = new PageImpl<>(Collections.singletonList(validNews));
        when(newsDao.findAll(pageable)).thenReturn(expectedPage);

        // Act
        Page<News> result = newsService.findAll(pageable);

        // Assert
        assertEquals(expectedPage, result);
        verify(newsDao).findAll(pageable);
    }

    @Test
    void findById_ValidId_ReturnsNews() {
        // Arrange
        when(newsDao.getOne(1)).thenReturn(validNews);

        // Act
        News result = newsService.findById(1);

        // Assert
        assertEquals(validNews, result);
        verify(newsDao).getOne(1);
    }

    @Test
    void findById_InvalidId_ThrowsException() {
        // Arrange
        when(newsDao.getOne(999)).thenThrow(EntityNotFoundException.class);

        // Act/Assert
        assertThrows(EntityNotFoundException.class, () -> newsService.findById(999));
    }

    @Test
    void create_ValidNews_ReturnsId() {
        // Arrange
        News savedNews = new News();
        savedNews.setNewsID(100);
        when(newsDao.save(validNews)).thenReturn(savedNews);

        // Act
        int result = newsService.create(validNews);

        // Assert
        assertEquals(100, result);
        verify(newsDao).save(validNews);
    }

    @Test
    void create_InvalidNews_ThrowsException() {
        // Arrange
        News invalidNews = new News(); // Missing required fields
        when(newsDao.save(invalidNews)).thenThrow(DataIntegrityViolationException.class);

        // Act/Assert
        assertThrows(DataIntegrityViolationException.class, () -> newsService.create(invalidNews));
    }

    @Test
    void create_NullNews_ThrowsServiceException() {
        // Act/Assert
        assertThrows(IllegalArgumentException.class, () -> newsService.create(null));
        verifyNoInteractions(newsDao);
    }

    @Test
    void delById_ValidId_DeletesNews() {
        // Act
        newsService.delById(100);

        // Assert
        verify(newsDao).deleteById(100);
    }

    @Test
    void delById_InvalidId_NoException() {
        // Act/Assert
        assertDoesNotThrow(() -> newsService.delById(999));
        verify(newsDao).deleteById(999);
    }

    @Test
    void update_ValidNews_SavesChanges() {
        // Act
        newsService.update(validNews);

        // Assert
        verify(newsDao).save(validNews);
    }

    @Test
    void update_NullNews_ThrowsServiceException() {
        // Act/Assert
        assertThrows(IllegalArgumentException.class, () -> newsService.update(null));
        verifyNoInteractions(newsDao);
    }
}