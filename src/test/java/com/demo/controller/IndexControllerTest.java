package com.demo.controller;

import com.demo.entity.Message;
import com.demo.entity.News;
import com.demo.entity.Venue;
import com.demo.entity.vo.MessageVo;
import com.demo.service.MessageService;
import com.demo.service.MessageVoService;
import com.demo.service.NewsService;
import com.demo.service.VenueService;
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
import org.springframework.data.domain.Sort;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.util.NestedServletException;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(SpringExtension.class)
@WebMvcTest(IndexController.class)
public class IndexControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private NewsService newsService;

    @MockBean
    private VenueService venueService;

    @MockBean
    private MessageService messageService;

    @MockBean
    private MessageVoService messageVoService;

    // --------------------- GET /index ---------------------

    /**
     * 测试用例：/index 首页（正常情况）
     * 输入：各个 service 均正常返回数据
     * 操作：GET /index
     * 期望输出：返回视图 "index"，并在模型中包含 "news_list"、"venue_list"、"message_list" 以及 "user" 属性
     */
    @Test
    public void testIndexSuccess() throws Exception {
        // 模拟分页参数
        Pageable newsPageable = PageRequest.of(0, 5, Sort.by("time").descending());
        Pageable venuePageable = PageRequest.of(0, 5, Sort.by("venueID").ascending());
        Pageable messagePageable = PageRequest.of(0, 5, Sort.by("time").descending());

        // 模拟返回的分页数据
        List<News> newsList = Collections.singletonList(new News());
        Page<News> newsPage = new PageImpl<>(newsList, newsPageable, newsList.size());
        Mockito.when(newsService.findAll(any(Pageable.class))).thenReturn(newsPage);

        List<Venue> venueList = Collections.singletonList(new Venue());
        Page<Venue> venuePage = new PageImpl<>(venueList, venuePageable, venueList.size());
        Mockito.when(venueService.findAll(any(Pageable.class))).thenReturn(venuePage);

        List<Message> messageData = Collections.singletonList(new Message());
        Page<Message> messagePage = new PageImpl<>(messageData, messagePageable, messageData.size());
        Mockito.when(messageService.findPassState(any(Pageable.class))).thenReturn(messagePage);

        List<MessageVo> messageVoList = Collections.singletonList(new MessageVo());
        Mockito.when(messageVoService.returnVo(messagePage.getContent())).thenReturn(messageVoList);

        mockMvc.perform(MockMvcRequestBuilders.get("/index"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("index"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("news_list"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("venue_list"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("message_list"))
                .andExpect(MockMvcResultMatchers.model().attribute("user", org.hamcrest.Matchers.nullValue()));
    }

    // --------------------- GET /admin_index ---------------------

    /**
     * 测试用例：/admin_index 管理员首页（正常情况）
     * 输入：无
     * 操作：GET /admin_index
     * 期望输出：返回视图 "admin/admin_index"
     */
    @Test
    public void testAdminIndexSuccess() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/admin_index"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("admin/admin_index"));
    }

    /**
     * 测试用例：/admin_index 管理员首页（异常情况）
     * 输入：模拟异常（例如 service 异常，此处直接模拟 Controller 内部异常）
     * 操作：GET /admin_index
     * 期望输出：抛出 RuntimeException
     */
    @Test
    public void testAdminIndexThrowsException() {
        // 为了模拟异常，可以构造一个 IndexController 的匿名子类，重写 admin_index 抛异常
        IndexController controller = new IndexController() {
            @Override
            public String admin_index(org.springframework.ui.Model model) {
                throw new RuntimeException("管理员首页异常");
            }
        };

        // 使用 MockMvcBuilders standaloneSetup 测试该 Controller 方法
        Exception exception = assertThrows(NestedServletException.class, () -> {
            org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup(controller)
                    .build()
                    .perform(MockMvcRequestBuilders.get("/admin_index"));
        });
        assertTrue(exception.getMessage().contains("管理员首页异常"));
    }
}
