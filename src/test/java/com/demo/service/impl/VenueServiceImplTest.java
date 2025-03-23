package com.demo.service.impl;

import com.demo.dao.VenueDao;
import com.demo.entity.Venue;
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
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class VenueServiceImplTest {

    @Mock
    private VenueDao venueDao;

    @InjectMocks
    private VenueServiceImpl venueService;

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void findByVenueID() {
        findByVenueID_ValidId_ReturnsVenue();
        findByVenueID_InvalidId_ThrowsException();
    }

    void findByVenueID_ValidId_ReturnsVenue() {
        Venue expected = new Venue();
        expected.setVenueID(1);
        when(venueDao.getOne(1)).thenReturn(expected);

        Venue result = venueService.findByVenueID(1);

        assertEquals(expected, result);
    }

    void findByVenueID_InvalidId_ThrowsException() {
        when(venueDao.getOne(999)).thenThrow(EntityNotFoundException.class);

        assertThrows(EntityNotFoundException.class, () -> venueService.findByVenueID(999));
    }

    @Test
    void findByVenueName() {
        findByVenueName_ExactMatch_ReturnsVenue();
        findByVenueName_NoMatch_ReturnsNull();
    }

    void findByVenueName_ExactMatch_ReturnsVenue() {
        Venue expected = new Venue();
        when(venueDao.findByVenueName("Arena")).thenReturn(expected);

        Venue result = venueService.findByVenueName("Arena");

        assertEquals(expected, result);
    }

    void findByVenueName_NoMatch_ReturnsNull() {
        when(venueDao.findByVenueName("Stadium")).thenReturn(null);

        Venue result = venueService.findByVenueName("Stadium");

        assertNull(result);
    }

    @Test
    void findAllPaged() {
        findAll_Paged_ReturnsPage();
    }

    void findAll_Paged_ReturnsPage() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Venue> expectedPage = new PageImpl<>(Collections.singletonList(new Venue()));
        when(venueDao.findAll(pageable)).thenReturn(expectedPage);

        Page<Venue> result = venueService.findAll(pageable);

        assertEquals(expectedPage, result);
    }

    @Test
    void findAllUnpaged() {
        findAll_Unpaged_ReturnsList();
    }

    void findAll_Unpaged_ReturnsList() {
        List<Venue> expectedList = Collections.singletonList(new Venue());
        when(venueDao.findAll()).thenReturn(expectedList);

        List<Venue> result = venueService.findAll();

        assertEquals(expectedList, result);
    }

    @Test
    void create() {
        create_ValidVenue_ReturnsId();
        create_DuplicateVenueName_ThrowsException();
        create_NullVenue_ThrowsException();
    }

    void create_ValidVenue_ReturnsId() {
        Venue venue = new Venue();
        Venue savedVenue = new Venue();
        savedVenue.setVenueID(100);
        when(venueDao.save(venue)).thenReturn(savedVenue);

        int result = venueService.create(venue);

        assertEquals(100, result);
    }

    void create_DuplicateVenueName_ThrowsException() {
        Venue venue = new Venue();
        venue.setVenueName("Arena");
        when(venueDao.save(venue)).thenThrow(DataIntegrityViolationException.class);

        assertThrows(DataIntegrityViolationException.class, () -> venueService.create(venue));
    }

    void create_NullVenue_ThrowsException() {
        when(venueDao.save(null)).thenThrow(IllegalArgumentException.class);

        assertThrows(IllegalArgumentException.class, () -> venueService.create(null));
    }

    @Test
    void update() {
        update_ValidVenue_SavesChanges();
        update_NullVenue_ThrowsException();
    }

    void update_ValidVenue_SavesChanges() {
        Venue venue = new Venue();
        venueService.update(venue);

        verify(venueDao).save(venue);
    }

    void update_NullVenue_ThrowsException() {
        doThrow(IllegalArgumentException.class).when(venueDao).save(null);

        assertThrows(IllegalArgumentException.class, () -> venueService.update(null));
    }

    @Test
    void delById() {
        delById_ValidId_DeletesVenue();
        delById_InvalidId_NoException();
    }

    void delById_ValidId_DeletesVenue() {
        venueService.delById(100);

        verify(venueDao).deleteById(100);
    }

    void delById_InvalidId_NoException() {
        doNothing().when(venueDao).deleteById(999);

        assertDoesNotThrow(() -> venueService.delById(999));
    }

    @Test
    void countVenueName() {
        countVenueName_ExactMatch_ReturnsCount();
        countVenueName_NoMatch_ReturnsZero();
        countVenueName_EmptyName_ReturnsZero();
    }

    void countVenueName_ExactMatch_ReturnsCount() {
        when(venueDao.countByVenueName("Stadium")).thenReturn(2);

        int result = venueService.countVenueName("Stadium");

        assertEquals(2, result);
    }

    void countVenueName_NoMatch_ReturnsZero() {
        when(venueDao.countByVenueName("Cinema")).thenReturn(0);

        int result = venueService.countVenueName("Cinema");

        assertEquals(0, result);
    }

    void countVenueName_EmptyName_ReturnsZero() {
        when(venueDao.countByVenueName("")).thenReturn(0);

        int result = venueService.countVenueName("");

        assertEquals(0, result);
    }
}