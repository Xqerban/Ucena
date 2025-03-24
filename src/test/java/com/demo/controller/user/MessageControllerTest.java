package com.demo.controller.user;

import com.demo.controller.user.MessageController;
import com.demo.entity.Message;
import com.demo.entity.vo.MessageVo;
import com.demo.service.MessageService;
import com.demo.service.MessageVoService;
import com.demo.exception.LoginException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.util.NestedServletException;

import javax.servlet.http.HttpServletRequest;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(MessageController.class)
public class MessageControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MessageService messageService;

    @MockBean
    private MessageVoService messageVoService;

    /**
     * 测试用例：/message/getMessageList 成功
     * 输入：默认请求
     * 操作：访问 /message/getMessageList
     * 期望输出：返回 JSON 数组
     */
    @Test
    public void testMessageList() throws Exception {
        when(messageService.findPassState(any())).thenReturn(Mockito.mock(org.springframework.data.domain.Page.class));
        when(messageVoService.returnVo(any())).thenReturn(Collections.emptyList());

        mockMvc.perform(MockMvcRequestBuilders.get("/message/getMessageList"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$").isArray());
    }

    /**
     * 测试用例：/message/findUserList 未登录
     * 输入：用户未登录
     * 操作：访问 /message/findUserList
     * 期望输出：返回 4xx 错误
     */
    @Test
    public void testFindUserListWithoutLogin() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/message/findUserList"))
                .andExpect(status().isUnauthorized())  // 期望返回 401
                .andExpect(content().string("请登录！"));  // 期望返回的错误消息
    }


    /**
     * 测试用例：/sendMessage 成功
     * 输入：userID=1, content=Test message
     * 操作：发送 POST 请求到 /sendMessage
     * 期望输出：重定向到 /message_list
     */
    @Test
    public void testSendMessage() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/sendMessage")
                        .param("userID", "1")
                        .param("content", "Test message"))
                .andExpect(status().is3xxRedirection());
    }

    /**
     * 测试用例：/modifyMessage.do 成功
     * 输入：messageID=1, content=Updated content
     * 操作：发送 POST 请求到 /modifyMessage.do
     * 期望输出：返回 true
     */
    @Test
    public void testModifyMessage() throws Exception {
        when(messageService.findById(anyInt())).thenReturn(new Message());
        mockMvc.perform(MockMvcRequestBuilders.post("/modifyMessage.do")
                        .param("messageID", "1")
                        .param("content", "Updated content"))
                .andExpect(status().isOk());
    }

    /**
     * 测试用例：/delMessage.do 成功
     * 输入：messageID=1
     * 操作：发送 POST 请求到 /delMessage.do
     * 期望输出：返回 true
     */
    @Test
    public void testDelMessage() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/delMessage.do")
                        .param("messageID", "1"))
                .andExpect(status().isOk());
    }

    /**
     * 测试用例：/modifyMessage.do 消息不存在
     * 输入：messageID=999, content=Non-existent message
     * 操作：发送 POST 请求到 /modifyMessage.do
     * 期望输出：返回 4xx 错误
     * NullPointerException: Cannot invoke "com.demo.entity.Message.setContent(String)" because "message" is null
     */
    @Test
    public void testModifyMessageNotFound() throws Exception {
        when(messageService.findById(anyInt())).thenReturn(null);
        mockMvc.perform(MockMvcRequestBuilders.post("/modifyMessage.do")
                        .param("messageID", "999")
                        .param("content", "Non-existent message"))
                .andExpect(status().is4xxClientError());
    }

    /**
     * 测试用例：/message/findUserList 触发异常
     * 输入：模拟 service 抛出异常
     * 操作：访问 /message/findUserList
     * 期望输出：返回 5xx 错误
     */
    @Test
    public void testFindUserListThrowsException() throws Exception {
        when(messageVoService.returnVo(any())).thenThrow(new RuntimeException("Database error"));
        mockMvc.perform(MockMvcRequestBuilders.get("/message/findUserList"))
                .andExpect(status().is5xxServerError());
    }
}
