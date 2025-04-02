package com.demo.service.impl;

import com.demo.dao.VenueDao;
import com.demo.entity.Venue;
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
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class VenueServiceImplTest {

    @Mock
    private VenueDao venueDao;

    @InjectMocks
    private VenueServiceImpl venueService;

    private Venue validVenue;

    @BeforeEach
    void setUp() {
        validVenue = new Venue();
        validVenue.setVenueName("Central Arena");
    }

    @Test
    void findByVenueID_ValidId_ReturnsVenue() {
        // Arrange
        when(venueDao.getOne(1)).thenReturn(validVenue);

        // Act
        Venue result = venueService.findByVenueID(1);

        // Assert
        assertEquals(validVenue, result);
        verify(venueDao).getOne(1);
    }

    @Test
    void findByVenueID_InvalidId_ThrowsException() {
        // Arrange
        when(venueDao.getOne(999)).thenThrow(EntityNotFoundException.class);

        // Act & Assert
        assertThrows(EntityNotFoundException.class, () -> venueService.findByVenueID(999));
    }

    @Test
    void findByVenueName_ExactMatch_ReturnsVenue() {
        // Arrange
        when(venueDao.findByVenueName("Arena")).thenReturn(validVenue);

        // Act
        Venue result = venueService.findByVenueName("Arena");

        // Assert
        assertEquals(validVenue, result);
        verify(venueDao).findByVenueName("Arena");
    }

    @Test
    void findAll_Paged_ReturnsPage() {
        // Arrange
        Pageable pageable = PageRequest.of(0, 10);
        Page<Venue> expectedPage = new PageImpl<>(List.of(validVenue));
        when(venueDao.findAll(pageable)).thenReturn(expectedPage);

        // Act
        Page<Venue> result = venueService.findAll(pageable);

        // Assert
        assertEquals(expectedPage, result);
        verify(venueDao).findAll(pageable);
    }

    @Test
    void create_ValidVenue_ReturnsId() {
        // Arrange
        Venue savedVenue = new Venue();
        savedVenue.setVenueID(100);
        when(venueDao.save(validVenue)).thenReturn(savedVenue);

        // Act
        int resultId = venueService.create(validVenue);

        // Assert
        assertEquals(100, resultId);
        verify(venueDao).save(validVenue);
    }

    @Test
    void create_DuplicateVenueName_ThrowsException() {
        // Arrange
        Venue duplicateVenue = new Venue();
        duplicateVenue.setVenueName("Central Arena");
        when(venueDao.save(duplicateVenue)).thenThrow(DataIntegrityViolationException.class);

        // Act & Assert
        assertThrows(DataIntegrityViolationException.class,
                () -> venueService.create(duplicateVenue));
    }

    @Test
    void create_NullVenue_ThrowsException() {
        // Act & Assert
        assertThrows(IllegalArgumentException.class,
                () -> venueService.create(null));
        verifyNoInteractions(venueDao);
    }

    @Test
    void update_ValidVenue_SavesChanges() {
        // Act
        venueService.update(validVenue);

        // Assert
        verify(venueDao).save(validVenue);
    }

    @Test
    void update_NullVenue_ThrowsException() {
        // Act & Assert
        assertThrows(IllegalArgumentException.class,
                () -> venueService.update(null));
        verifyNoInteractions(venueDao);
    }

    @Test
    void delById_ValidId_DeletesVenue() {
        // Act
        venueService.delById(100);

        // Assert
        verify(venueDao).deleteById(100);
    }

    @Test
    void countVenueName_ExactMatch_ReturnsCount() {
        // Arrange
        when(venueDao.countByVenueName("Stadium")).thenReturn(2);

        // Act
        int count = venueService.countVenueName("Stadium");

        // Assert
        assertEquals(2, count);
        verify(venueDao).countByVenueName("Stadium");
    }
}