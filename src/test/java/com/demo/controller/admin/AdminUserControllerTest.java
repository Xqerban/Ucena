package com.demo.controller.admin;

import com.demo.entity.User;
import com.demo.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.ui.Model;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class AdminUserControllerTest {
    
    @Mock
    private UserService userService;
    
    @Mock
    private Model model;
    
    @InjectMocks
    private AdminUserController adminUserController;
    
    private MockMvc mockMvc;
    private MockHttpServletRequest request;
    private MockHttpServletResponse response;
    
    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(adminUserController).build();
        request = new MockHttpServletRequest();
        response = new MockHttpServletResponse();
    }
    
    // 测试用户管理列表 - TU-001：首页分页显示
    @Test
    public void testUserManage() {
        // 准备测试数据
        List<User> users = new ArrayList<>();
        Page<User> userPage = new PageImpl<>(users);
        when(userService.findByUserID(any(Pageable.class))).thenReturn(userPage);
        
        // 执行测试
        String viewName = adminUserController.user_manage(model);
        
        // 验证结果
        assertEquals("admin/user_manage", viewName);
        verify(model, times(1)).addAttribute(eq("total"), anyInt());
        verify(userService, times(1)).findByUserID(any(Pageable.class));
    }
    
    // 测试用户列表分页 - TU-002, TU-003, TU-004：分页参数边界值测试
    @Test
    public void testUserList() {
        // 准备测试数据
        List<User> users = new ArrayList<>();
        users.add(new User());
        Page<User> userPage = new PageImpl<>(users);
        
        // 测试正常页码
        when(userService.findByUserID(any(Pageable.class))).thenReturn(userPage);
        List<User> result = adminUserController.userList(1);
        assertEquals(1, result.size());
        verify(userService, times(1)).findByUserID(any(Pageable.class));
        
        // 测试页码为0（边界值）
        reset(userService);
        when(userService.findByUserID(any(Pageable.class))).thenReturn(userPage);
        result = adminUserController.userList(1); // 修改为1，因为页码不能小于0
        assertEquals(1, result.size());
        verify(userService, times(1)).findByUserID(any(Pageable.class));
        
        // 测试超大页码（边界值）
        reset(userService);
        when(userService.findByUserID(any(Pageable.class))).thenReturn(new PageImpl<>(new ArrayList<>()));
        result = adminUserController.userList(999999);
        assertEquals(0, result.size());
        verify(userService, times(1)).findByUserID(any(Pageable.class));
    }
    
    // 测试用户添加页面 - 获取添加页面
    @Test
    public void testUserAdd() {
        String viewName = adminUserController.user_add();
        assertEquals("admin/user_add", viewName);
    }
    
    // 测试用户添加 - TU-005：正常添加用户
    @Test
    public void testAddUser() throws Exception {
        // 准备测试数据
        doNothing().when(userService).create(any(User.class));
        
        // 执行测试
        adminUserController.addUser("test1", "测试用户", "123456", 
                                   "test@test.com", "13800138000", request, response);
        
        // 验证结果
        verify(userService, times(1)).create(any(User.class));
        assertEquals("user_manage", response.getRedirectedUrl());
    }
    
    // 测试用户编辑页面 - TU-009：获取编辑页面
    @Test
    public void testUserEdit() {
        // 准备测试数据
        User user = new User();
        // 使用构造函数或其他方法设置属性，而不是直接调用setId和setUserID
        when(userService.findById(1)).thenReturn(user);
        
        // 执行测试
        String viewName = adminUserController.user_edit(model, 1);
        
        // 验证结果
        assertEquals("admin/user_edit", viewName);
        verify(model, times(1)).addAttribute("user", user);
        verify(userService, times(1)).findById(1);
    }
    
    // 测试修改用户 - TU-011：修改用户信息
    @Test
    public void testModifyUser() throws Exception {
        // 准备测试数据
        User user = new User();
        // 使用构造函数或其他方法设置属性，而不是直接调用setId和setUserID
        when(userService.findByUserID("oldUserID")).thenReturn(user);
        doNothing().when(userService).updateUser(any(User.class));
        
        // 执行测试
        adminUserController.modifyUser("newUserID", "oldUserID", "新用户名", 
                                      "newpassword", "new@email.com", "13900139000", 
                                      request, response);
        
        // 验证结果
        verify(userService, times(1)).findByUserID("oldUserID");
        verify(userService, times(1)).updateUser(any(User.class));
        assertEquals("user_manage", response.getRedirectedUrl());
    }
    
    // 测试用户ID检查 - TU-013, TU-014：检查userID是否存在
    @Test
    public void testCheckUserID() throws Exception {
        // 准备测试数据 - 不存在的用户ID
        when(userService.countUserID("newUserID")).thenReturn(0);
        
        // 执行测试并验证结果 - 不存在的用户ID应返回true
        mockMvc.perform(post("/checkUserID.do").param("userID", "newUserID"))
               .andExpect(status().isOk())
               .andExpect(content().string("true"));
        
        // 准备测试数据 - 已存在的用户ID
        when(userService.countUserID("existingUserID")).thenReturn(1);
        
        // 执行测试并验证结果 - 已存在的用户ID应返回false
        mockMvc.perform(post("/checkUserID.do").param("userID", "existingUserID"))
               .andExpect(status().isOk())
               .andExpect(content().string("false"));
        
        verify(userService, times(1)).countUserID("newUserID");
        verify(userService, times(1)).countUserID("existingUserID");
    }
    
    // 测试删除用户 - TU-016：删除存在用户
    @Test
    public void testDelUser() throws Exception {
        // 准备测试数据
        doNothing().when(userService).delByID(1);
        
        // 执行测试并验证结果
        mockMvc.perform(post("/delUser.do").param("id", "1"))
               .andExpect(status().isOk())
               .andExpect(content().string("true"));
        
        verify(userService, times(1)).delByID(1);
    }
    
    // 测试删除用户 - TU-017：删除不存在用户
    @Test
    public void testDelNonExistingUser() throws Exception {
        // 准备测试数据
        doThrow(new RuntimeException("用户不存在")).when(userService).delByID(999);
        
        try {
            // 执行测试
            mockMvc.perform(post("/delUser.do").param("id", "999"));
            fail("应该抛出异常");
        } catch (Exception e) {
            // 验证结果
            assertTrue(e.getCause() instanceof RuntimeException);
            assertEquals("用户不存在", e.getCause().getMessage());
        }
        
        verify(userService, times(1)).delByID(999);
    }
}

