package com.demo.controller.user;

import com.demo.entity.Message;
import com.demo.entity.User;
import com.demo.entity.vo.MessageVo;
import com.demo.exception.LoginException;
import com.demo.service.MessageService;
import com.demo.service.MessageVoService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class MessageControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MessageService messageService;

    @MockBean
    private MessageVoService messageVoService;

    /**
     * 测试用例1：/message_manage页面加载
     * 输入：无输入
     * 操作：访问/message_manage
     * 期望输出：页面能够加载，并显示总页数
     * @throws Exception org.springframework.web.util.NestedServletException: Request processing failed; nested exception is java.lang.NullPointerException: Cannot invoke "org.springframework.data.domain.Page.getTotalPages()" because the return value of "com.demo.service.MessageService.findByUser(String, org.springframework.data.domain.Pageable)" is null
     */
    @Test
    public void messageList_shouldReturnMessageListView_whenUserIsLoggedIn() throws Exception {
        List<Message> messages = Collections.emptyList();
        List<MessageVo> messageVos = Collections.emptyList();
        Page<Message> emptyPage = new PageImpl<>(messages);
        Mockito.when(messageService.findPassState(Mockito.any())).thenReturn(emptyPage);
        Mockito.when(messageService.findByUser(Mockito.anyString(), Mockito.any(Pageable.class))).thenReturn(emptyPage);
        Mockito.when(messageVoService.returnVo(messages)).thenReturn(messageVos);

        mockMvc.perform(get("/message_list")
                        .sessionAttr("user", new User()))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("total"))
                .andExpect(model().attributeExists("user_total"))
                .andExpect(view().name("message_list"));
    }

    /**
     * 测试用例2：/message_list未登录
     * 输入：无输入
     * 操作：访问/message_list
     * 期望输出：抛出LoginException异常，提示请登录
     * @throws Exception org.springframework.web.util.NestedServletException: Request processing failed; nested exception is java.lang.NullPointerException: Cannot invoke "org.springframework.data.domain.Page.getContent()" because "messages" is null
     */
    @Test
    public void messageList_shouldThrowLoginException_whenUserIsNotLoggedIn() throws Exception {
        mockMvc.perform(get("/message_list"))
                .andExpect(status().is4xxClientError())
                .andExpect(result -> assertInstanceOf(LoginException.class, result.getResolvedException()))
                .andExpect(result -> assertEquals("请登录！", Objects.requireNonNull(result.getResolvedException()).getMessage()));
    }

    /**
     * 测试用例3：/message/getMessageList无消息
     * 输入：page=1
     * 操作：访问/message/getMessageList?page=1
     * 期望输出：返回空列表
     */
    @Test
    public void getMessageList_shouldReturnEmptyList_whenNoMessagesExist() throws Exception {
        List<Message> messages = Collections.emptyList();
        List<MessageVo> messageVos = Collections.emptyList();
        Page<Message> emptyPage = new PageImpl<>(messages);
        Mockito.when(messageService.findPassState(Mockito.any())).thenReturn(emptyPage);
        Mockito.when(messageVoService.returnVo(messages)).thenReturn(messageVos);

        mockMvc.perform(get("/message/getMessageList")
                        .param("page", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").isEmpty());
    }

    /**
     * 测试用例4：/message/findUserList无用户消息
     * 输入：page=1
     * 操作：访问/message/findUserList?page=1
     * 期望输出：返回空列表
     * @throws Exception org.springframework.web.util.NestedServletException: Request processing failed; nested exception is java.lang.NullPointerException: Cannot invoke "org.springframework.data.domain.Page.getContent()" because "messages" is null
     */
    @Test
    public void findUserList_shouldReturnEmptyList_whenNoUserMessagesExist() throws Exception {
        List<Message> messages = Collections.emptyList();
        List<MessageVo> messageVos = Collections.emptyList();
        Page<Message> emptyPage = new PageImpl<>(messages);
        Mockito.when(messageService.findByUser(Mockito.anyString(), Mockito.any(Pageable.class))).thenReturn(emptyPage);
        Mockito.when(messageVoService.returnVo(messages)).thenReturn(messageVos);

        mockMvc.perform(get("/message/findUserList")
                        .param("page", "1")
                        .sessionAttr("user", new User()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").isEmpty());
    }

    /**
     * 测试用例5：/sendMessage成功
     * 输入：userID=1，content=Test message
     * 操作：访问/sendMessage?userID=1&content=Test message
     * 期望输出：重定向到/message_list
     */
    @Test
    public void sendMessage_shouldRedirectToMessageList_whenMessageIsSent() throws Exception {
        mockMvc.perform(post("/sendMessage")
                        .param("userID", "1")
                        .param("content", "Test message")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/message_list"));
    }

    /**
     * 测试用例6：/modifyMessage.do成功
     * 输入：messageID=1，content=Updated content
     * 操作：访问/modifyMessage.do?messageID=1&content=Updated content
     * 期望输出：返回true表示消息已修改
     */
    @Test
    public void modifyMessage_shouldReturnTrue_whenMessageIsModified() throws Exception {
        Message message = new Message();
        Mockito.when(messageService.findById(Mockito.anyInt())).thenReturn(message);

        mockMvc.perform(post("/modifyMessage.do")
                        .param("messageID", "1")
                        .param("content", "Updated content")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));
    }

    /**
     * 测试用例7：/delMessage.do成功
     * 输入：messageID=1
     * 操作：访问/delMessage.do?messageID=1
     * 期望输出：返回true表示消息已删除
     */
    @Test
    public void delMessage_shouldReturnTrue_whenMessageIsDeleted() throws Exception {
        mockMvc.perform(post("/delMessage.do")
                        .param("messageID", "1")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));
    }
}