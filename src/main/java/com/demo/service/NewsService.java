package com.demo.service;

import com.demo.entity.News;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable
        ;

public interface NewsService {
    Page<News> findAll(Pageable pageable);

    News findById(int newsID);

    int create(News news);

    void delById(int newsID);

    void update(News news);
}
