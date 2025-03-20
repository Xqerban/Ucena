package com.demo.service.impl;

import com.demo.dao.NewsDao;
import com.demo.entity.News;
import com.demo.service.NewsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Pageable;

@Service
public class NewsServiceImpl implements NewsService {
    @Autowired
    private NewsDao newsDao;

    @Override
    public Page<News> findAll(Pageable pageable) {
        return newsDao.findAll(pageable);
    }

    @Override
    public News findById(int newsID) {
        return newsDao.getOne(newsID);
    }

    @Override
    public int create(News news) {
        return newsDao.save(news).getNewsID();
    }

    @Override
    public void delById(int newsID) {
        newsDao.deleteById(newsID);
    }

    @Override
    public void update(News news) {
        newsDao.save(news);
    }
}
