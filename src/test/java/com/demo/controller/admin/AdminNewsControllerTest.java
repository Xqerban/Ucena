package com.demo.controller.admin;

import com.demo.entity.News;
import com.demo.service.NewsService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;

import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AdminNewsController.class)
class AdminNewsControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private NewsService newsService;

    private void setField(Object obj, String fieldName, Object value) throws Exception {
        Field field = obj.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(obj, value);
    }

    /**
     * 测试用例1：/news_manage页面加载
     * 输入：无输入
     * 操作：访问/news_manage
     * 期望输出：页面能够加载，并显示总页数
     */
    @Test
    void testNewsManagePage() throws Exception {
        Pageable pageable = PageRequest.of(0, 10);
        Page<News> page = new PageImpl<>(List.of(), pageable, 0);
        when(newsService.findAll(any(Pageable.class))).thenReturn(page);

        mockMvc.perform(get("/news_manage"))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/news_manage"))
                .andExpect(model().attributeExists("total"));
    }

    /**
     * 测试用例2：/newsList.do正常页码
     * 输入：page=1
     * 操作：访问/newsList.do?page=1
     * 期望输出：返回一个News对象列表
     */
    @Test
    void testNewsListWithValidPage() throws Exception {
        News news1 = new News();
        setField(news1, "newsID", 1);
        setField(news1, "title", "Test News");
        setField(news1, "content", "Test Content");
        setField(news1, "time", LocalDateTime.now());
        
        Page<News> page = new PageImpl<>(List.of(news1));
        when(newsService.findAll(any(Pageable.class))).thenReturn(page);

        mockMvc.perform(get("/newsList.do").param("page", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].newsID").value(1))
                .andExpect(jsonPath("$[0].title").value("Test News"));
    }

    /**
     * 测试用例3：/newsList.do无效页码（负值）
     * 输入：page=-1
     * 操作：访问/newsList.do?page=-1
     * 期望输出：返回空列表
     */
    @Test
    void testNewsListWithNegativePage() throws Exception {
        Pageable pageable = PageRequest.of(0, 10);
        Page<News> page = new PageImpl<>(new ArrayList<>(), pageable, 0);
        when(newsService.findAll(any(Pageable.class))).thenReturn(page);

        mockMvc.perform(get("/newsList.do").param("page", "-1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }

    /**
     * 测试用例4：/newsList.do页码超出范围
     * 输入：page=1000
     * 操作：访问/newsList.do?page=1000
     * 期望输出：返回空列表
     */
    @Test
    void testNewsListWithOutOfRangePage() throws Exception {
        Pageable pageable = PageRequest.of(0, 10);
        Page<News> page = new PageImpl<>(new ArrayList<>(), pageable, 0);
        when(newsService.findAll(any(Pageable.class))).thenReturn(page);

        mockMvc.perform(get("/newsList.do").param("page", "1000"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }

    /**
     * 测试用例5：/news_add页面加载
     * 输入：无输入
     * 操作：访问/news_add
     * 期望输出：跳转至新闻添加页面
     */
    @Test
    void testNewsAddPage() throws Exception {
        mockMvc.perform(get("/news_add"))
                .andExpect(status().isOk())
                .andExpect(view().name("/admin/news_add"));
    }

    /**
     * 测试用例6：/news_edit成功
     * 输入：newsID=1（已存在的新闻ID）
     * 操作：访问/news_edit?newsID=1
     * 期望输出：返回对应的News对象并显示编辑页面
     */
    @Test
    void testNewsEditWithValidId() throws Exception {
        News news = new News();
        setField(news, "newsID", 1);
        setField(news, "title", "Test News");
        setField(news, "content", "Test Content");
        setField(news, "time", LocalDateTime.now());
        
        when(newsService.findById(1)).thenReturn(news);

        mockMvc.perform(get("/news_edit").param("newsID", "1"))
                .andExpect(status().isOk())
                .andExpect(view().name("/admin/news_edit"))
                .andExpect(model().attributeExists("news"))
                .andExpect(model().attribute("news", news));
    }

    /**
     * 测试用例7：/news_edit失败（无效ID）
     * 输入：newsID=-1（不存在的ID）
     * 操作：访问/news_edit?newsID=-1
     * 期望输出：返回错误或空页面
     */
    @Test
    void testNewsEditWithInvalidId() throws Exception {
        when(newsService.findById(-1)).thenReturn(null);

        mockMvc.perform(get("/news_edit").param("newsID", "-1"))
                .andExpect(status().isOk())
                .andExpect(view().name("/admin/news_edit"));
    }

    /**
     * 测试用例8：/delNews.do成功
     * 输入：newsID=1（有效ID）
     * 操作：访问/delNews.do?newsID=1
     * 期望输出：返回true表示成功删除
     */
    @Test
    void testDelNewsSuccess() throws Exception {
        doNothing().when(newsService).delById(1);

        mockMvc.perform(post("/delNews.do").param("newsID", "1"))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));
    }

    /**
     * 测试用例9：/modifyNews.do成功
     * 输入：newsID=1，title="New Title"，content="New Content"
     * 操作：访问/modifyNews.do?newsID=1&title=New Title&content=New Content
     * 期望输出：新闻被更新并跳转到news_manage页面
     */
    @Test
    void testModifyNewsSuccess() throws Exception {
        News news = new News();
        setField(news, "newsID", 1);
        setField(news, "title", "New Title");
        setField(news, "content", "New Content");
        setField(news, "time", LocalDateTime.now());
        
        when(newsService.findById(1)).thenReturn(news);
        doNothing().when(newsService).update(any(News.class));

        mockMvc.perform(post("/modifyNews.do")
                .param("newsID", "1")
                .param("title", "New Title")
                .param("content", "New Content"))
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("news_manage"));
    }

    /**
     * 测试用例10：/addNews.do成功
     * 输入：title="New News"，content="This is new content"
     * 操作：访问/addNews.do?title=New News&content=This is new content
     * 期望输出：新新闻被创建并跳转到news_manage页面
     */
    @Test
    void testAddNewsSuccess() throws Exception {
        when(newsService.create(any(News.class))).thenReturn(1);

        mockMvc.perform(post("/addNews.do")
                .param("title", "New News")
                .param("content", "This is new content"))
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("news_manage"));
    }

    /**
     * 测试用例11：/addNews.do失败（空标题）
     * 输入：title=""，content="Some content"
     * 操作：访问/addNews.do?title=&content=Some content
     * 期望输出：提示错误或返回新闻添加页面
     */
    @Test
    void testAddNewsWithEmptyTitle() throws Exception {
        mockMvc.perform(post("/addNews.do")
                .param("title", "")
                .param("content", "Some content"))
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("news_manage"));
    }
}
