package com.demo.controller.admin;

import com.demo.entity.Venue;
import com.demo.service.VenueService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.ui.Model;
import org.springframework.web.multipart.MultipartFile;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class AdminVenueControllerTest {
    
    @Mock
    private VenueService venueService;
    
    @Mock
    private Model model;
    
    @InjectMocks
    private AdminVenueController adminVenueController;
    
    private MockMvc mockMvc;
    
    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(adminVenueController).build();
    }
    
    // 工具方法：通过反射设置私有字段
    private void setField(Object obj, String fieldName, Object value) throws Exception {
        Field field = obj.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(obj, value);
    }
    
    // 创建测试用的场馆对象
    private Venue createTestVenue() throws Exception {
        Venue venue = new Venue();
        setField(venue, "venueID", 1);
        setField(venue, "venueName", "测试场馆");
        setField(venue, "address", "测试地址");
        setField(venue, "description", "测试描述");
        setField(venue, "price", 100);
        setField(venue, "picture", "test.jpg");
        setField(venue, "open_time", "09:00");
        setField(venue, "close_time", "18:00");
        return venue;
    }
    
    // 测试场馆管理页面（有数据）
    @Test
    public void testVenueManagePageWithValidData() throws Exception {
        List<Venue> venues = new ArrayList<>();
        venues.add(createTestVenue());
        Page<Venue> venuePage = new PageImpl<>(venues);
        
        when(venueService.findAll(any(Pageable.class))).thenReturn(venuePage);
        
        mockMvc.perform(get("/venue_manage"))
               .andExpect(status().isOk())
               .andExpect(model().attribute("total", 1))
               .andExpect(view().name("admin/venue_manage"));
        
        verify(venueService, times(1)).findAll(any(Pageable.class));
    }
    
    // 测试场馆管理页面（空数据）
    @Test
    public void testVenueManagePageWithEmptyData() throws Exception {
        List<Venue> venues = new ArrayList<>();
        Page<Venue> venuePage = new PageImpl<>(venues);
        
        when(venueService.findAll(any(Pageable.class))).thenReturn(venuePage);
        
        mockMvc.perform(get("/venue_manage"))
               .andExpect(status().isOk())
               .andExpect(model().attribute("total", 0))
               .andExpect(view().name("admin/venue_manage"));
        
        verify(venueService, times(1)).findAll(any(Pageable.class));
    }
    
    // 测试场馆编辑页面（有效ID）
    @Test
    public void testVenueEditWithValidId() throws Exception {
        Venue venue = createTestVenue();
        when(venueService.findByVenueID(1)).thenReturn(venue);
        
        mockMvc.perform(get("/venue_edit").param("venueID", "1"))
               .andExpect(status().isOk())
               .andExpect(model().attributeExists("venue"))
               .andExpect(view().name("/admin/venue_edit"));
        
        verify(venueService, times(1)).findByVenueID(1);
    }
    
    // 测试场馆编辑页面（无效ID）
    @Test
    public void testVenueEditWithInvalidValues() throws Exception {
        when(venueService.findByVenueID(999)).thenReturn(null);
        
        mockMvc.perform(get("/venue_edit").param("venueID", "999"))
               .andExpect(status().isOk())
               .andExpect(model().attributeExists("venue"))
               .andExpect(view().name("/admin/venue_edit"));
        
        verify(venueService, times(1)).findByVenueID(999);
    }
    
    // 测试场馆添加页面
    @Test
    public void testVenueAddPage() throws Exception {
        mockMvc.perform(get("/venue_add"))
               .andExpect(status().isOk())
               .andExpect(view().name("/admin/venue_add"));
    }
    
    // 测试修改场馆（有效数据）
    @Test
    public void testModifyVenueWithValidData() throws Exception {
        Venue venue = createTestVenue();
        when(venueService.findByVenueID(1)).thenReturn(venue);
        
        MockMultipartFile picture = new MockMultipartFile("picture", "test.jpg", "image/jpeg", "test image content".getBytes());
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();
        
        adminVenueController.modifyVenue(1, "更新场馆", "更新地址", "更新描述", 
                                         200, picture, "10:00", "20:00", request, response);
        
        verify(venueService, times(1)).findByVenueID(1);
        verify(venueService, times(1)).update(any(Venue.class));
        assertEquals("venue_manage", response.getRedirectedUrl());
    }
    
    // 测试修改场馆（无效数据）
    @Test
    public void testModifyVenueWithInvalidData() throws Exception {
        Venue venue = createTestVenue();
        when(venueService.findByVenueID(1)).thenReturn(venue);
        
        MockMultipartFile emptyPicture = new MockMultipartFile("picture", "", "image/jpeg", new byte[0]);
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();
        
        adminVenueController.modifyVenue(1, "更新场馆", "更新地址", "更新描述", 
                                         200, emptyPicture, "10:00", "20:00", request, response);
        
        verify(venueService, times(1)).findByVenueID(1);
        verify(venueService, times(1)).update(any(Venue.class));
        assertEquals("venue_manage", response.getRedirectedUrl());
    }
    
    // 测试获取场馆列表
    @Test
    public void testGetVenueList() throws Exception {
        List<Venue> venues = new ArrayList<>();
        venues.add(createTestVenue());
        Page<Venue> venuePage = new PageImpl<>(venues);
        
        when(venueService.findAll(any(Pageable.class))).thenReturn(venuePage);
        
        mockMvc.perform(get("/venueList.do").param("page", "1"))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$[0].venueName").value("测试场馆"));
        
        verify(venueService, times(1)).findAll(any(Pageable.class));
    }
    
    // 测试添加场馆（有效数据）
    @Test
    public void testAddVenueWithValidData() throws Exception {
        MockMultipartFile picture = new MockMultipartFile("picture", "test.jpg", "image/jpeg", "test image content".getBytes());
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();
        
        when(venueService.create(any(Venue.class))).thenReturn(1);
        
        adminVenueController.addVenue("新场馆", "新地址", "新描述", 
                                     150, picture, "08:00", "22:00", request, response);
        
        verify(venueService, times(1)).create(any(Venue.class));
        assertEquals("venue_manage", response.getRedirectedUrl());
    }
    
    // 测试添加场馆（无效数据）
    @Test
    public void testAddVenueWithInvalidData() throws Exception {
        MockMultipartFile picture = new MockMultipartFile("picture", "test.jpg", "image/jpeg", "test image content".getBytes());
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();
        
        when(venueService.create(any(Venue.class))).thenReturn(0);
        
        adminVenueController.addVenue("", "新地址", "新描述", 
                                     150, picture, "08:00", "22:00", request, response);
        
        verify(venueService, times(1)).create(any(Venue.class));
        assertEquals("venue_add", response.getRedirectedUrl());
    }
    
    // 测试检查场馆名称
    @Test
    public void testCheckVenueName() throws Exception {
        when(venueService.countVenueName("已存在")).thenReturn(1);
        when(venueService.countVenueName("新名称")).thenReturn(0);
        
        mockMvc.perform(post("/checkVenueName.do").param("venueName", "已存在"))
               .andExpect(status().isOk())
               .andExpect(content().string("false"));
        
        mockMvc.perform(post("/checkVenueName.do").param("venueName", "新名称"))
               .andExpect(status().isOk())
               .andExpect(content().string("true"));
        
        verify(venueService, times(1)).countVenueName("已存在");
        verify(venueService, times(1)).countVenueName("新名称");
    }
    
    // 测试删除场馆（有效ID和无效ID）
    @Test
    public void testDelVenueWithValidAndInvalidIds() throws Exception {
        doNothing().when(venueService).delById(1);
        doThrow(new RuntimeException("Invalid ID")).when(venueService).delById(999);
        
        mockMvc.perform(post("/delVenue.do").param("venueID", "1"))
               .andExpect(status().isOk())
               .andExpect(content().string("true"));
        
        try {
            mockMvc.perform(post("/delVenue.do").param("venueID", "999"));
            fail("应该抛出异常");
        } catch (Exception e) {
            assertTrue(e.getCause() instanceof RuntimeException);
            assertEquals("Invalid ID", e.getCause().getMessage());
        }
        
        verify(venueService, times(1)).delById(1);
        verify(venueService, times(1)).delById(999);
    }
}
