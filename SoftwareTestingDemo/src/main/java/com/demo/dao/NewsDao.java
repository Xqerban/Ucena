package com.demo.dao;

import com.demo.entity.News;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NewsDao extends JpaRepository<News,Integer> {

}
