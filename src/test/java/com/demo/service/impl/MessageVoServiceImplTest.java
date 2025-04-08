package com.demo.service.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import com.demo.dao.MessageDao;
import com.demo.dao.UserDao;
import com.demo.entity.Message;
import com.demo.entity.User;
import com.demo.entity.vo.MessageVo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class MessageVoServiceImplTest {

    @Mock
    private MessageDao messageDao;

    @Mock
    private UserDao userDao;

    @InjectMocks
    private MessageVoServiceImpl messageVoService;

    private Message
            validMessage;
    private User validUser;
    private final LocalDateTime testTime = LocalDateTime.of(2024, 1, 1, 12, 0);

    @BeforeEach
    void setUp() {
        // 初始化测试数据
        validMessage = new Message(1, "user123", "测试内容", testTime, 2);
        validUser = new User(1, "user123", "张三", "pass", "a@b.com", "13800000000", 0, "pic.jpg");
    }

    @Test
    @DisplayName("正常情况-有效messageID和有效userID")
    void returnMessageVoByMessageID_ValidInput_ReturnsCorrectVo() {
        // Arrange
        when(messageDao.findByMessageID(1)).thenReturn(validMessage);
        when(userDao.findByUserID("user123")).thenReturn(validUser);

        // Act
        MessageVo result = messageVoService.returnMessageVoByMessageID(1);

        // Assert
        assertAll("验证所有字段映射",
                () -> assertEquals(1, result.getMessageID()),
                () -> assertEquals("user123", result.getUserID()),
                () -> assertEquals("测试内容", result.getContent()),
                () -> assertEquals(testTime, result.getTime()),
                () -> assertEquals("张三", result.getUserName()),
                () -> assertEquals("pic.jpg", result.getPicture()),
                () -> assertEquals(2, result.getState())
        );

        verify(messageDao, times(1)).findByMessageID(1);
        verify(userDao, times(1)).findByUserID("user123");
    }

    @Test
    @DisplayName("边界情况-最小有效messageID(0)")
    void returnMessageVoByMessageID_MinValidId_ThrowsException() {
        // Arrange
        when(messageDao.findByMessageID(0)).thenReturn(null);

        // Act & Assert
        assertThrows(NullPointerException.class, () ->
                messageVoService.returnMessageVoByMessageID(0));
    }

    @Test
    @DisplayName("异常情况-存在message但关联用户不存在")
    void returnMessageVoByMessageID_MissingUser_ThrowsException() {
        // Arrange
        Message message = new Message(2, "nonExist", "内容", testTime, 1);
        when(messageDao.findByMessageID(2)).thenReturn(message);
        when(userDao.findByUserID("nonExist")).thenReturn(null);

        // Act & Assert
        assertThrows(NullPointerException.class, () ->
                messageVoService.returnMessageVoByMessageID(2));
    }

    @Test
    @DisplayName("异常情况-输入不存在messageID")
    void returnMessageVoByMessageID_InvalidId_ThrowsException() {
        // Arrange
        when(messageDao.findByMessageID(999)).thenReturn(null);

        // Act & Assert
        assertThrows(NullPointerException.class, () ->
                messageVoService.returnMessageVoByMessageID(999));
    }

    @Test
    @DisplayName("正常情况-空列表输入")
    void returnVo_EmptyList_ReturnsEmptyList() {
        // Act
        List<MessageVo> result = messageVoService.returnVo(Collections.emptyList());

        // Assert
        assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("边界情况-单个元素列表")
    void returnVo_SingleElementList_ReturnsValidVo() {
        // Arrange
        Message message = new Message(3, "user1", "单一内容", testTime, 1);
        User user = new User(2, "user1", "李四", "pwd", "b@c.com", "13900000000", 0, "img.png");

        when(messageDao.findByMessageID(3)).thenReturn(message);
        when(userDao.findByUserID("user1")).thenReturn(user);

        // Act
        List<MessageVo> result = messageVoService.returnVo(List.of(message));

        // Assert
        assertEquals(1, result.size());
        MessageVo vo = result.get(0);
        assertAll("验证转换结果",
                () -> assertEquals(3, vo.getMessageID()),
                () -> assertEquals("李四", vo.getUserName()),
                () -> assertEquals("img.png", vo.getPicture())
        );
    }

    @Test
    @DisplayName("混合有效和无效元素的列表")
    void returnVo_MixedElements_ThrowsException() {
        // Arrange
        Message validMessage = new Message(4, "validUser", "有效内容", testTime, 2);
        Message invalidMessage = new Message(5, "invalidUser", "无效内容", testTime, 1);

        when(messageDao.findByMessageID(4)).thenReturn(validMessage);
        when(messageDao.findByMessageID(5)).thenReturn(null);
        when(userDao.findByUserID("validUser")).thenReturn(
                new User(3, "validUser", "王五", "pw", "c@d.com", "13500000000", 0, "photo.jpg"));

        // Act & Assert
        assertThrows(NullPointerException.class, () ->
                messageVoService.returnVo(List.of(validMessage, invalidMessage)));
    }

    @Test
    @DisplayName("重复元素列表处理")
    void returnVo_DuplicateElements_ReturnsCorrectCount() {
        // Arrange
        Message message1 = new Message(6, "userA", "内容1", testTime, 1);
        Message message2 = new Message(6, "userA", "内容1", testTime, 1);

        User user = new User(4, "userA", "赵六", "pwd", "d@e.com", "13600000000", 0, "avatar.png");

        when(messageDao.findByMessageID(6)).thenReturn(message1);
        when(userDao.findByUserID("userA")).thenReturn(user);

        // Act
        List<MessageVo> result = messageVoService.returnVo(List.of(message1, message2));

        // Assert
        assertEquals(2, result.size());
        verify(messageDao, times(2)).findByMessageID(6);
    }
}