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

import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
//import static org.hamcrest.Matchers.*;

@WebMvcTest(AdminMessageController.class)
class AdminMessageControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MessageService messageService;

    @MockBean
    private MessageVoService messageVoService;

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

    @Test
    void testMessageList() throws Exception {
        Message msg1 = new Message(); msg1.setMessageID(1); // 可补充其他属性
        Page<Message> page = new PageImpl<>(List.of(msg1));
        when(messageService.findWaitState(any(Pageable.class))).thenReturn(page);

        MessageVo vo1 = new MessageVo(); vo1.setMessageID(1); // 可补充其他属性
        when(messageVoService.returnVo(anyList())).thenReturn(List.of(vo1));

        mockMvc.perform(get("/messageList.do").param("page", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].messageID").value(1));
    }

    @Test
    void testPassMessage() throws Exception {
        doNothing().when(messageService).confirmMessage(1);

        mockMvc.perform(post("/passMessage.do").param("messageID", "1"))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));
    }

    @Test
    void testRejectMessage() throws Exception {
        doNothing().when(messageService).rejectMessage(1);

        mockMvc.perform(post("/rejectMessage.do").param("messageID", "1"))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));
    }

    @Test
    void testDelMessage() throws Exception {
        doNothing().when(messageService).delById(1);

        mockMvc.perform(post("/delMessage.do").param("messageID", "1"))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));
    }
}