package com.demo.controller.admin;

import com.demo.entity.Message;
import com.demo.entity.vo.MessageVo;
import com.demo.service.MessageService;
import com.demo.service.MessageVoService;
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

@WebMvcTest(AdminMessageController.class)
class AdminMessageControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MessageService messageService;

    @MockBean
    private MessageVoService messageVoService;

    private void setField(Object obj, String fieldName, Object value) throws Exception {
        Field field = obj.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(obj, value);
    }

    /**
     * 测试用例1：/message_manage页面加载
     * 输入：无输入
     * 操作：访问/message_manage
     * 期望输出：页面能够加载，并显示总页数
     */
    @Test
    void testMessageManagePage() throws Exception {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Message> page = new PageImpl<>(List.of(), pageable, 0);
        when(messageService.findWaitState(any(Pageable.class))).thenReturn(page);

        mockMvc.perform(get("/message_manage"))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/message_manage"))
                .andExpect(model().attributeExists("total"));
    }

    /**
     * 测试用例2：/messageList.do正常页码
     * 输入：page=1
     * 操作：访问/messageList.do?page=1
     * 期望输出：返回一个MessageVo对象列表
     */
    @Test
    void testMessageListWithValidPage() throws Exception {
        Message msg1 = new Message();
        setField(msg1, "messageID", 1);
        Page<Message> page = new PageImpl<>(List.of(msg1));
        when(messageService.findWaitState(any(Pageable.class))).thenReturn(page);

        MessageVo vo1 = new MessageVo();
        setField(vo1, "messageID", 1);
        when(messageVoService.returnVo(anyList())).thenReturn(List.of(vo1));

        mockMvc.perform(get("/messageList.do").param("page", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].messageID").value(1));
    }

    /**
     * 测试用例3：/messageList.do无效页码（负值）
     * 输入：page=-1
     * 操作：访问/messageList.do?page=-1
     * 期望输出：返回空列表
     */
    @Test
    void testMessageListWithNegativePage() throws Exception {
        when(messageService.findWaitState(any(Pageable.class))).thenReturn(new PageImpl<>(new ArrayList<>()));
        when(messageVoService.returnVo(anyList())).thenReturn(new ArrayList<>());

        mockMvc.perform(get("/messageList.do").param("page", "-1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }

    /**
     * 测试用例4：/messageList.do页码超出范围
     * 输入：page=1000
     * 操作：访问/messageList.do?page=1000
     * 期望输出：返回空列表
     */
    @Test
    void testMessageListWithOutOfRangePage() throws Exception {
        when(messageService.findWaitState(any(Pageable.class))).thenReturn(new PageImpl<>(new ArrayList<>()));
        when(messageVoService.returnVo(anyList())).thenReturn(new ArrayList<>());

        mockMvc.perform(get("/messageList.do").param("page", "1000"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }

    /**
     * 测试用例5：/passMessage.do成功
     * 输入：messageID=1（有效ID）
     * 操作：访问/passMessage.do?messageID=1
     * 期望输出：返回true，表示成功通过消息
     */
    @Test
    void testPassMessageSuccess() throws Exception {
        doNothing().when(messageService).confirmMessage(1);

        mockMvc.perform(post("/passMessage.do").param("messageID", "1"))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));
    }

    /**
     * 测试用例6：/rejectMessage.do失败（无效ID）
     * 输入：messageID=-1（无效ID）
     * 操作：访问/rejectMessage.do?messageID=-1
     * 期望输出：返回true（即使ID无效，接口仍然返回成功）
     */
    @Test
    void testRejectMessageWithInvalidId() throws Exception {
        doNothing().when(messageService).rejectMessage(-1);

        mockMvc.perform(post("/rejectMessage.do").param("messageID", "-1"))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));
    }

    /**
     * 测试用例7：/delMessage.do成功
     * 输入：messageID=1
     * 操作：访问/delMessage.do?messageID=1
     * 期望输出：返回true，表示删除操作成功
     */
    @Test
    void testDelMessageSuccess() throws Exception {
        doNothing().when(messageService).delById(1);

        mockMvc.perform(post("/delMessage.do").param("messageID", "1"))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));
    }
}