package com.demo.controller.user;

import com.demo.controller.user.NewsController;
import com.demo.entity.News;
import com.demo.service.NewsService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@WebMvcTest(NewsController.class)
public class NewsControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private NewsService newsService;

    /**
     * 测试用例：/news 成功
     * 输入：newsID=1
     * 操作：访问 /news?newsID=1
     * 期望输出：返回视图 "news" 并包含新闻数据
     */
    @Test
    public void testNews() throws Exception {
        when(newsService.findById(anyInt())).thenReturn(new News());

        mockMvc.perform(MockMvcRequestBuilders.get("/news").param("newsID", "1"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("news"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("news"));
    }

    /**
     * 测试用例：/news/getNewsList 成功
     * 输入：默认请求
     * 操作：访问 /news/getNewsList
     * 期望输出：返回 JSON 对象，包含新闻分页数据
     */
    @Test
    public void testNewsList() throws Exception {
        Page<News> mockPage = new PageImpl<>(Collections.emptyList(), PageRequest.of(0, 5), 0);
        when(newsService.findAll(any(Pageable.class))).thenReturn(mockPage);

        mockMvc.perform(MockMvcRequestBuilders.get("/news/getNewsList"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.content").isArray());
    }

    /**
     * 测试用例：/news_list 成功
     * 输入：默认请求
     * 操作：访问 /news_list
     * 期望输出：返回视图 "news_list" 并包含新闻列表和总页数
     */
    @Test
    public void testNewsListView() throws Exception {
        Page<News> mockPage = new PageImpl<>(Collections.emptyList(), PageRequest.of(0, 5), 0);
        when(newsService.findAll(any(Pageable.class))).thenReturn(mockPage);

        mockMvc.perform(MockMvcRequestBuilders.get("/news_list"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("news_list"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("news_list"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("total"));
    }

    /**
     * 测试用例：/news 访问不存在的新闻
     * 输入：newsID=999
     * 操作：访问 /news
     * 期望输出：模型中 news 为空
     * TemplateInputException: An error happened during template parsing (template: "class path resource [templates/news.html]")
     * 没有处理 news 为空的情况，导致 Thymeleaf 试图解析 news 为空时的模板
     */
    @Test
    public void testNewsNotFound() throws Exception {
        when(newsService.findById(anyInt())).thenReturn(null);

        mockMvc.perform(MockMvcRequestBuilders.get("/news")
                        .param("newsID", "999"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.model().attributeDoesNotExist("news"));
    }

}
