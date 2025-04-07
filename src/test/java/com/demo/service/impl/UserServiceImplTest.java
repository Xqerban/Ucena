package com.demo.service.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import com.demo.dao.UserDao;
import com.demo.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserDao userDao;

    @InjectMocks
    private UserServiceImpl userService;

    private User sampleUser;
    private final Pageable pageable = PageRequest.of(0, 10);

    @BeforeEach
    void setUp() {
        sampleUser = new User(1, "testUser", "张三", "password123",
                "test@demo.com", "13800138000", 0, "avatar.jpg");
    }

    // region 基础查询测试
    @Test
    @DisplayName("通过有效用户ID查询-返回用户对象")
    void findByUserID_ValidID_ReturnsUser() {
        when(userDao.findByUserID("testUser")).thenReturn(sampleUser);

        User result = userService.findByUserID("testUser");
        assertAll("用户字段验证",
                () -> assertEquals("testUser", result.getUserID()),
                () -> assertEquals("张三", result.getUserName()),
                () -> assertEquals("test@demo.com", result.getEmail())
        );
    }

    @Test
    @DisplayName("查询不存在的用户ID-返回null")
    void findByUserID_InvalidID_ReturnsNull() {
        when(userDao.findByUserID("nonExist")).thenReturn(null);

        assertNull(userService.findByUserID("nonExist"));
    }
    // endregion

    // region 分页查询测试
    @Test
    @DisplayName("分页查询普通用户-返回正确分页数据")
    void findByUserID_PageQuery_ReturnsPagedResults() {
        Page<User> mockPage = new PageImpl<>(Collections.singletonList(sampleUser));
        when(userDao.findAllByIsadmin(0, pageable)).thenReturn(mockPage);

        Page<User> result = userService.findByUserID(pageable);
        assertAll("分页结果验证",
                () -> assertEquals(1, result.getTotalElements()),
                () -> assertFalse(result.getContent().isEmpty()),
                () -> assertEquals("testUser", result.getContent().get(0).getUserID())
        );
    }

    @Test
    @DisplayName("分页查询空结果-返回空页")
    void findByUserID_EmptyResult_ReturnsEmptyPage() {
        when(userDao.findAllByIsadmin(0, pageable)).thenReturn(Page.empty());

        Page<User> result = userService.findByUserID(pageable);
        assertTrue(result.isEmpty());
    }
    // endregion

    // region 登录验证测试
    @Test
    @DisplayName("验证正确凭证-返回用户对象")
    void checkLogin_ValidCredentials_ReturnsUser() {
        when(userDao.findByUserIDAndPassword("testUser", "password123"))
                .thenReturn(sampleUser);

        User result = userService.checkLogin("testUser", "password123");
        assertEquals(sampleUser, result);
    }

    @Test
    @DisplayName("验证错误密码-返回null")
    void checkLogin_WrongPassword_ReturnsNull() {
        when(userDao.findByUserIDAndPassword("testUser", "wrongPass")).thenReturn(null);

        assertNull(userService.checkLogin("testUser", "wrongPass"));
    }
    // endregion

    // region 用户管理操作测试
    @Test
    @DisplayName("创建新用户-返回当前用户总数")
    void create_NewUser_ReturnsUserCount() {
        User newUser = new User(2, "newUser", "李四", "pass",
                "li@demo.com", "13900139000", 0, "img.jpg");

        // 模拟数据库保存和查询
        when(userDao.save(any(User.class))).thenReturn(newUser);
        when(userDao.findAll()).thenReturn(List.of(sampleUser, newUser));

        int count = userService.create(newUser);
        assertEquals(2, count);

        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        verify(userDao).save(userCaptor.capture());
        assertEquals("李四", userCaptor.getValue().getUserName());
    }

    @Test
    @DisplayName("删除存在的用户-无异常抛出")
    void delByID_ExistingUser_DeletesSuccessfully() {
        doNothing().when(userDao).deleteById(1);

        assertDoesNotThrow(() -> userService.delByID(1));
        verify(userDao).deleteById(1);
    }
    // endregion

    // region 更新操作测试
    @Test
    @DisplayName("更新用户信息-保存修改后的数据")
    void updateUser_ValidData_SavesUpdatedUser() {
        User updatedUser = new User(1, "testUser", "张伟", "newPass",
                "new@demo.com", "13800000000", 0, "new.jpg");

        userService.updateUser(updatedUser);

        ArgumentCaptor<User> captor = ArgumentCaptor.forClass(User.class);
        verify(userDao).save(captor.capture());
        assertAll("更新字段验证",
                () -> assertEquals("张伟", captor.getValue().getUserName()),
                () -> assertEquals("new@demo.com", captor.getValue().getEmail()),
                () -> assertEquals("newPass", captor.getValue().getPassword())
        );
    }
    // endregion

    // region 统计功能测试
    @Test
    @DisplayName("统计存在的用户ID-返回正确计数")
    void countUserID_ExistingID_ReturnsOne() {
        when(userDao.countByUserID("testUser")).thenReturn(1);

        assertEquals(1, userService.countUserID("testUser"));
    }

    @Test
    @DisplayName("统计不存在的用户ID-返回零")
    void countUserID_NonExistingID_ReturnsZero() {
        when(userDao.countByUserID("unknown")).thenReturn(0);

        assertEquals(0, userService.countUserID("unknown"));
    }
    // endregion


    @Test
    @DisplayName("更新不存在用户-静默保存")
    void updateUser_NonExistingUser_SavesAnyway() {
        User newUser = new User(999, "new", "王五", "pwd",
                "wang@demo.com", "13500135000", 0, "pic.jpg");

        assertDoesNotThrow(() -> userService.updateUser(newUser));
        verify(userDao).save(newUser);
    }
    // endregion
}