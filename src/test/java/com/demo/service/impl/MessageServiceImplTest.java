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
import static org.junit.jupiter.api.Assertions.*;
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
        findById_shouldReturnMessageWhenExistId();
        findById_shouldThrowWhenInvalidId();
    }

    void findById_shouldReturnMessageWhenExistId() {
        when(messageDao.getOne(TEST_MESSAGE_ID)).thenReturn(testMessage);

        Message message = messageService.findById(TEST_MESSAGE_ID);

        assertEquals(TEST_MESSAGE_ID, message.getMessageID());
        verify(messageDao).getOne(TEST_MESSAGE_ID);
    }

    void findById_shouldThrowWhenInvalidId() {
//        when(messageDao.getOne(-1)).thenThrow(new IllegalArgumentException());

        assertThrows(IllegalArgumentException.class, () ->
                messageService.findById(-1));
    }

    // ==================================================================================

    @Test
    void findByUser() {
        findByUser_shouldReturnPagedMessagesWhenExistUserAndValidPageable();
        findByUser_shouldThrowWhenInvalidPageable();
        findByUser_shouldThrowWhenInvalidUser();
    }

    void findByUser_shouldReturnPagedMessagesWhenExistUserAndValidPageable() {
        Page<Message> mockPage = new PageImpl<>(Collections.singletonList(testMessage));
        when(messageDao.findAllByUserID(TEST_USER_ID, pageable)).thenReturn(mockPage);

        Page<Message> result = messageService.findByUser(TEST_USER_ID, pageable);

        assertEquals(1, result.getTotalElements());
        verify(messageDao).findAllByUserID(TEST_USER_ID, pageable);
    }

    void findByUser_shouldThrowWhenInvalidPageable() {
        // test with null pageable
        assertDoesNotThrow(() ->
                messageService.findByUser(TEST_USER_ID, null));
    }

    void findByUser_shouldThrowWhenInvalidUser() {
        // Null userID
        assertThrows(IllegalArgumentException.class, () ->
                messageService.findByUser(null, pageable));

        // Empty userID
        assertThrows(IllegalArgumentException.class, () ->
                messageService.findByUser("", pageable));
    }

    // ==================================================================================

    @Test
    void create() {
        create_shouldReturnGeneratedIdWhenSuccess();
        create_shouldThrowWhenMessageIsNull();
    }

    void create_shouldReturnGeneratedIdWhenSuccess() {
        when(messageDao.save(any(Message.class))).thenReturn(testMessage);

        int generatedId = messageService.create(new Message());

        assertEquals(TEST_MESSAGE_ID, generatedId);
        verify(messageDao).save(any(Message.class));
    }

    void create_shouldThrowWhenMessageIsNull() {
        assertThrows(NullPointerException.class, () ->
                messageService.create(null));
    }

    // ==================================================================================

    @Test
    void delById() {
        delById_shouldCallDeleteWhenValidId();
        delById_shouldThrowWhenInvalidId();
    }

    void delById_shouldCallDeleteWhenValidId() {
        doNothing().when(messageDao).deleteById(TEST_MESSAGE_ID);

        messageService.delById(TEST_MESSAGE_ID);

        verify(messageDao).deleteById(TEST_MESSAGE_ID);
    }

    void delById_shouldThrowWhenInvalidId() {
        // Negative ID
        assertThrows(IllegalArgumentException.class, () ->
                messageService.delById(-1));

        // Zero ID (if your system disallows it)
        assertThrows(IllegalArgumentException.class, () ->
                messageService.delById(0));
    }

    // ==================================================================================

    @Test
    void update() {
        update_shouldCallSaveWhenValidMessage();
        update_shouldThrowWhenNullMessage();
    }

    void update_shouldCallSaveWhenValidMessage() {
        when(messageDao.save(any(Message.class))).thenReturn(testMessage);

        messageService.update(new Message());

        verify(messageDao).save(any(Message.class));
    }

    void update_shouldThrowWhenNullMessage() {
        assertThrows(IllegalArgumentException.class, () ->
                messageService.update(null));
    }

    // ==================================================================================

    @Test
    void confirmMessage() {
        confirmMessage_shouldUpdateStateToPassWhenMessageExists();
        confirmMessage_shouldThrowWhenMessageNotFound();
    }

    void confirmMessage_shouldUpdateStateToPassWhenMessageExists() {
        when(messageDao.findByMessageID(TEST_MESSAGE_ID)).thenReturn(testMessage);
        doNothing().when(messageDao).updateState(eq(STATE_PASS), eq(TEST_MESSAGE_ID));

        messageService.confirmMessage(TEST_MESSAGE_ID);

        verify(messageDao).updateState(STATE_PASS, TEST_MESSAGE_ID);
    }

    void confirmMessage_shouldThrowWhenMessageNotFound() {
        when(messageDao.findByMessageID(TEST_MESSAGE_ID)).thenReturn(null);

        assertThrows(RuntimeException.class, () ->
                messageService.confirmMessage(TEST_MESSAGE_ID));
    }

    // ==================================================================================

    @Test
    void rejectMessage() {
        rejectMessage_shouldUpdateStateToRejectWhenMessageExists();
        rejectMessage_shouldThrowWhenMessageNotFound();
    }

    void rejectMessage_shouldUpdateStateToRejectWhenMessageExists() {
        when(messageDao.findByMessageID(TEST_MESSAGE_ID)).thenReturn(testMessage);
        doNothing().when(messageDao).updateState(eq(STATE_REJECT), eq(TEST_MESSAGE_ID));

        messageService.rejectMessage(TEST_MESSAGE_ID);

        verify(messageDao).updateState(STATE_REJECT, TEST_MESSAGE_ID);
    }

    void rejectMessage_shouldThrowWhenMessageNotFound() {
        when(messageDao.findByMessageID(999)).thenReturn(null);

        assertThrows(RuntimeException.class, () ->
                messageService.rejectMessage(999));
    }

    // ==================================================================================

    @Test
    void findWaitState() {
        findWaitState_shouldReturnUnapprovedMessagesWhenValidPageable();
        findWaitState_shouldThrowWhenInvalidPageable();
    }

    void findWaitState_shouldReturnUnapprovedMessagesWhenValidPageable() {
        Page<Message> mockPage = new PageImpl<>(Collections.singletonList(testMessage));
        when(messageDao.findAllByState(STATE_NO_AUDIT, pageable)).thenReturn(mockPage);

        Page<Message> result = messageService.findWaitState(pageable);

        assertEquals(1, result.getTotalElements());
        verify(messageDao).findAllByState(STATE_NO_AUDIT, pageable);
    }

    void findWaitState_shouldThrowWhenInvalidPageable() {
        assertThrows(IllegalArgumentException.class, () ->
                messageService.findWaitState(null));
    }

    // ==================================================================================

    @Test
    void findPassState() {
        findPassState_shouldReturnApprovedMessagesWhenValidPageable();
        findPassState_shouldThrowWhenInvalidPageable();
    }

    void findPassState_shouldReturnApprovedMessagesWhenValidPageable() {
        Page<Message> mockPage = new PageImpl<>(Collections.singletonList(testMessage));
        when(messageDao.findAllByState(STATE_PASS, pageable)).thenReturn(mockPage);

        Page<Message> result = messageService.findPassState(pageable);

        assertEquals(1, result.getTotalElements());
        verify(messageDao).findAllByState(STATE_PASS, pageable);
    }

    void findPassState_shouldThrowWhenInvalidPageable() {
        assertThrows(IllegalArgumentException.class, () ->
                messageService.findPassState(null));
    }
}