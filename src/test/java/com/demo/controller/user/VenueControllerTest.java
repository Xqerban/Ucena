package com.demo.controller.user;

import com.demo.entity.Venue;
import com.demo.service.VenueService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
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
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@WebMvcTest(VenueController.class)
public class VenueControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private VenueService venueService;

    // --------------------- GET /venue ---------------------
    /**
     * 测试用例：/venue 场馆详情页面（正常情况）
     * 输入：venueID=1，场馆数据正常返回
     * 操作：GET /venue?venueID=1
     * 期望输出：返回视图 "venue"，并在模型中包含 venue 属性
     */
    @Test
    public void testToGymPageSuccess() throws Exception {
        Venue venue = new Venue();
        venue.setVenueID(1);
        venue.setVenueName("Test Venue");
        when(venueService.findByVenueID(1)).thenReturn(venue);

        mockMvc.perform(MockMvcRequestBuilders.get("/venue")
                        .param("venueID", "1"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("venue"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("venue"));
    }

    /**
     * 测试用例：/venue 场馆详情页面（异常情况）
     * 输入：venueID=1，venueService 抛出异常
     * 操作：GET /venue?venueID=1
     * 期望输出：抛出 RuntimeException
     */
    @Test
    public void testToGymPageThrowsException() {
        when(venueService.findByVenueID(anyInt())).thenThrow(new RuntimeException("场馆查询异常"));

        Exception exception = assertThrows(NestedServletException.class, () -> {
            mockMvc.perform(MockMvcRequestBuilders.get("/venue").param("venueID", "1"));
        });
        assertInstanceOf(RuntimeException.class, exception.getCause());
        assertEquals("场馆查询异常", exception.getCause().getMessage());
    }

    // --------------------- GET /venuelist/getVenueList ---------------------
    /**
     * 测试用例：/venuelist/getVenueList 分页查看场馆（正常情况）
     * 输入：page=1
     * 操作：GET /venuelist/getVenueList?page=1
     * 期望输出：返回 JSON 格式的场馆分页数据
     */
    @Test
    public void testVenueListSuccess() throws Exception {
        Pageable pageable = PageRequest.of(0, 5, Sort.by("venueID").ascending());
        Page<Venue> page = new PageImpl<>(Collections.emptyList(), pageable, 0);
        when(venueService.findAll(any(Pageable.class))).thenReturn(page);

        mockMvc.perform(MockMvcRequestBuilders.get("/venuelist/getVenueList")
                        .param("page", "1"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                // 验证 JSON 返回数据中包含 "content" 字段（即分页数据内容）
                .andExpect(MockMvcResultMatchers.jsonPath("$.content").isArray());
    }

    /**
     * 测试用例：/venuelist/getVenueList 分页查看场馆（异常情况）
     * 输入：page=1，venueService.findAll 抛出异常
     * 操作：GET /venuelist/getVenueList?page=1
     * 期望输出：抛出 RuntimeException
     */
    @Test
    public void testVenueListThrowsException() {
        when(venueService.findAll(any(Pageable.class))).thenThrow(new RuntimeException("分页查询异常"));

        Exception exception = assertThrows(NestedServletException.class, () -> {
            mockMvc.perform(MockMvcRequestBuilders.get("/venuelist/getVenueList")
                    .param("page", "1"));
        });
        assertInstanceOf(RuntimeException.class, exception.getCause());
        assertEquals("分页查询异常", exception.getCause().getMessage());
    }

    // --------------------- GET /venue_list ---------------------
    /**
     * 测试用例：/venue_list 查看场馆列表页面（正常情况）
     * 输入：无
     * 操作：GET /venue_list
     * 期望输出：返回视图 "venue_list"，并在模型中包含 venue_list 与 total 属性
     */
    @Test
    public void testVenueListViewSuccess() throws Exception {
        Pageable pageable = PageRequest.of(0, 5, Sort.by("venueID").ascending());
        List<Venue> venueList = Collections.singletonList(new Venue());
        Page<Venue> page = new PageImpl<>(venueList, pageable, 1);
        when(venueService.findAll(any(Pageable.class))).thenReturn(page);

        mockMvc.perform(MockMvcRequestBuilders.get("/venue_list"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("venue_list"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("venue_list"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("total"));
    }

    /**
     * 测试用例：/venue_list 查看场馆列表页面（异常情况）
     * 输入：无，venueService.findAll 抛出异常
     * 操作：GET /venue_list
     * 期望输出：抛出 RuntimeException
     */
    @Test
    public void testVenueListViewThrowsException() {
        when(venueService.findAll(any(Pageable.class))).thenThrow(new RuntimeException("场馆列表查询异常"));

        Exception exception = assertThrows(NestedServletException.class, () -> {
            mockMvc.perform(MockMvcRequestBuilders.get("/venue_list"));
        });
        assertInstanceOf(RuntimeException.class, exception.getCause());
        assertEquals("场馆列表查询异常", exception.getCause().getMessage());
    }
}
