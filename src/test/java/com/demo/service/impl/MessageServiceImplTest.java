package com.demo.service.impl;

import com.demo.dao.MessageDao;
import com.demo.entity.Message;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Collections;

import static com.demo.service.MessageService.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MessageServiceImplTest {
    // ======================== constants ========================
    private static final int TEST_MESSAGE_ID = 1;
    private static final String TEST_USER_ID = "user123";
    // ======================== set up mock data fields ========================
    // target
    @InjectMocks
    private MessageServiceImpl messageService;
    // target class data fields
    @Mock
    private MessageDao messageDao;
    // ======================== set up mock data fields ========================
    // interact with the following
    private Message testMessage;
    private Pageable pageable;
    // ======================== constants ========================

    // ======================== preprocessing and postprocessing ========================
    @BeforeEach
    void setUp() {
        testMessage = new Message();
        testMessage.setMessageID(TEST_MESSAGE_ID);
        testMessage.setState(STATE_NO_AUDIT);
        pageable = PageRequest.of(0, 10);
    }

    @AfterEach
    void tearDown() {
    }
    // ======================== preprocessing and postprocessing ========================

    // ==================================================================================
    @Test
    void findById() {
        shouldReturnMessageWhenExistId();
        shouldThrowWhenInvalidId();
    }

    void shouldReturnMessageWhenExistId() {
        when(messageDao.getOne(TEST_MESSAGE_ID)).thenReturn(testMessage);

        Message message = messageService.findById(TEST_MESSAGE_ID);

        assertEquals(TEST_MESSAGE_ID, message.getMessageID());
        verify(messageDao).getOne(TEST_MESSAGE_ID);
    }

    void shouldThrowWhenInvalidId() {
        when(messageDao.getOne(-1)).thenThrow(new IllegalArgumentException());

        assertThrows(IllegalArgumentException.class, () ->
                messageService.findById(-1));
    }

    // ==================================================================================

    @Test
    void findByUser() {
        shouldReturnPagedMessagesWhenExistUserAndValidPageable();
    }

    void shouldReturnPagedMessagesWhenExistUserAndValidPageable() {
        Page<Message> mockPage = new PageImpl<>(Collections.singletonList(testMessage));
        when(messageDao.findAllByUserID(TEST_USER_ID, pageable)).thenReturn(mockPage);

        Page<Message> result = messageService.findByUser(TEST_USER_ID, pageable);

        assertEquals(1, result.getTotalElements());
        verify(messageDao).findAllByUserID(TEST_USER_ID, pageable);
    }

    void shouldThrowWhenInvalidPageable() {
    }

    void shouldThrowWhenInvalidUser() {
    }

    // ==================================================================================

    @Test
    void create() {
        shouldReturnGeneratedIdWhenSuccess();
        shouldThrowWhenMessageIsNull();
    }

    void shouldReturnGeneratedIdWhenSuccess() {
        when(messageDao.save(any(Message.class))).thenReturn(testMessage);

        int generatedId = messageService.create(new Message());

        assertEquals(TEST_MESSAGE_ID, generatedId);
        verify(messageDao).save(any(Message.class));
    }

    void shouldThrowWhenMessageIsNull() {
        assertThrows(NullPointerException.class, () ->
                messageService.create(null));
    }

    // ==================================================================================

    @Test
    void delById() {
        shouldCallDelete();
    }

    void shouldCallDelete() {
        doNothing().when(messageDao).deleteById(TEST_MESSAGE_ID);

        messageService.delById(TEST_MESSAGE_ID);

        verify(messageDao).deleteById(TEST_MESSAGE_ID);
    }

    // ==================================================================================

    @Test
    void update() {
        shouldCallSave();
    }

    void shouldCallSave() {
        when(messageDao.save(any(Message.class))).thenReturn(testMessage);

        messageService.update(new Message());

        verify(messageDao).save(any(Message.class));
    }

    // ==================================================================================

    @Test
    void confirmMessage() {
        shouldUpdateStateToPassWhenMessageExists();
        shouldThrowWhenMessageNotFound();
    }

    void shouldUpdateStateToPassWhenMessageExists() {
        when(messageDao.findByMessageID(TEST_MESSAGE_ID)).thenReturn(testMessage);
        doNothing().when(messageDao).updateState(eq(STATE_PASS), eq(TEST_MESSAGE_ID));

        messageService.confirmMessage(TEST_MESSAGE_ID);

        verify(messageDao).updateState(STATE_PASS, TEST_MESSAGE_ID);
    }

    void shouldThrowWhenMessageNotFound() {
        when(messageDao.findByMessageID(TEST_MESSAGE_ID)).thenReturn(null);

        assertThrows(RuntimeException.class, () ->
                messageService.confirmMessage(TEST_MESSAGE_ID));
    }

    // ==================================================================================

    @Test
    void rejectMessage() {
        shouldUpdateStateToRejectWhenMessageExists();
    }

    void shouldUpdateStateToRejectWhenMessageExists() {
        when(messageDao.findByMessageID(TEST_MESSAGE_ID)).thenReturn(testMessage);
        doNothing().when(messageDao).updateState(eq(STATE_REJECT), eq(TEST_MESSAGE_ID));

        messageService.rejectMessage(TEST_MESSAGE_ID);

        verify(messageDao).updateState(STATE_REJECT, TEST_MESSAGE_ID);
    }

    // ==================================================================================

    @Test
    void findWaitState() {
        shouldReturnUnapprovedMessages();
    }

    void shouldReturnUnapprovedMessages() {
        Page<Message> mockPage = new PageImpl<>(Collections.singletonList(testMessage));
        when(messageDao.findAllByState(STATE_NO_AUDIT, pageable)).thenReturn(mockPage);

        Page<Message> result = messageService.findWaitState(pageable);

        assertEquals(1, result.getTotalElements());
        verify(messageDao).findAllByState(STATE_NO_AUDIT, pageable);
    }

    // ==================================================================================

    @Test
    void findPassState() {
        shouldReturnApprovedMessages();
    }

    void shouldReturnApprovedMessages() {
        Page<Message> mockPage = new PageImpl<>(Collections.singletonList(testMessage));
        when(messageDao.findAllByState(STATE_PASS, pageable)).thenReturn(mockPage);

        Page<Message> result = messageService.findPassState(pageable);

        assertEquals(1, result.getTotalElements());
        verify(messageDao).findAllByState(STATE_PASS, pageable);
    }
}