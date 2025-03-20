package com.demo.service.impl;

import com.demo.dao.VenueDao;
import com.demo.entity.Venue;
import com.demo.service.VenueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VenueServiceImpl implements VenueService {
    @Autowired
    private VenueDao venueDao;

    @Override
    public Venue findByVenueID(int id) {
        return venueDao.getOne(id);
    }

    @Override
    public Venue findByVenueName(String venueName) {
        return venueDao.findByVenueName(venueName);
    }

    @Override
    public Page<Venue> findAll(Pageable pageable) {
        return venueDao.findAll(pageable);
    }

    @Override
    public List<Venue> findAll() {
        return venueDao.findAll();
    }

    @Override
    public int create(Venue venue) {
        return venueDao.save(venue).getVenueID();
    }

    @Override
    public void update(Venue venue) {
        venueDao.save(venue);
    }

    @Override
    public void delById(int id) {
        venueDao.deleteById(id);
    }

    @Override
    public int countVenueName(String venueName) {
        return venueDao.countByVenueName(venueName);
    }
}
