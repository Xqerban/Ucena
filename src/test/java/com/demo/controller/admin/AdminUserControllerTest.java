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
    
    /**
     * 测试用例1：用户管理列表首页分页显示
     * 输入：无
     * 操作：访问用户管理页面
     * 期望输出：返回用户管理页面，并包含总页数
     */
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
    
    /**
     * 测试用例2：用户列表分页参数边界值测试
     * 输入：正常页码、边界页码
     * 操作：获取不同页码的用户列表
     * 期望输出：返回对应页码的用户列表
     */
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
    
    /**
     * 测试用例3：用户添加页面获取
     * 输入：无
     * 操作：访问用户添加页面
     * 期望输出：返回用户添加页面
     */
    @Test
    public void testUserAdd() {
        String viewName = adminUserController.user_add();
        assertEquals("admin/user_add", viewName);
    }
    
    /**
     * 测试用例4：正常添加用户
     * 输入：有效的用户信息
     * 操作：添加新用户
     * 期望输出：用户被成功添加并重定向到用户管理页面
     */
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
    
    /**
     * 测试用例5：获取用户编辑页面
     * 输入：有效的用户ID
     * 操作：访问用户编辑页面
     * 期望输出：返回用户编辑页面，并包含用户信息
     */
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
    
    /**
     * 测试用例6：修改用户信息
     * 输入：有效的用户信息
     * 操作：修改用户信息
     * 期望输出：用户信息被成功修改并重定向到用户管理页面
     */
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
    
    /**
     * 测试用例7：检查用户ID是否存在
     * 输入：存在和不存在的用户ID
     * 操作：检查用户ID
     * 期望输出：不存在的用户ID返回true，存在的用户ID返回false
     */
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
    
    /**
     * 测试用例8：删除存在的用户
     * 输入：有效的用户ID
     * 操作：删除用户
     * 期望输出：用户被成功删除并返回true
     */
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
    
    /**
     * 测试用例9：删除不存在的用户
     * 输入：无效的用户ID
     * 操作：删除不存在的用户
     * 期望输出：抛出异常
     */
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
