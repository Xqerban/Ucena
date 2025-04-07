package com.demo.service.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import org.mockito.ArgumentCaptor;



import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import com.demo.dao.OrderDao;
import com.demo.dao.VenueDao;
import com.demo.entity.Order;
import com.demo.entity.Venue;
import com.demo.service.OrderService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@ExtendWith(MockitoExtension.class)
class OrderServiceImplTest {

    @Mock
    private OrderDao orderDao;

    @Mock
    private VenueDao venueDao;

    @InjectMocks
    private OrderServiceImpl orderService;

    private Venue validVenue;
    private Order sampleOrder;
    private final LocalDateTime now = LocalDateTime.now();

    @BeforeEach
    void setUp() {
        validVenue = new Venue(1, "篮球场", "标准篮球场地", 200, "court.jpg",
                "体育中心A座", "08:00", "22:00");

        sampleOrder = new Order();
        sampleOrder.setOrderID(1);
        sampleOrder.setUserID("user123");
        sampleOrder.setVenueID(1);
        sampleOrder.setState(OrderService.STATE_NO_AUDIT);
        sampleOrder.setOrderTime(now.minusDays(1));
        sampleOrder.setStartTime(now.plusDays(1));
        sampleOrder.setHours(2);
        sampleOrder.setTotal(400);
    }

    // region 查询相关方法测试
    @Test
    @DisplayName("通过有效订单ID查询订单-正常情况")
    void findById_ValidOrderId_ReturnsOrder() {
        when(orderDao.getOne(1)).thenReturn(sampleOrder);

        Order result = orderService.findById(1);
        assertEquals(1, result.getOrderID());
        assertEquals("user123", result.getUserID());
    }

    @Test
    @DisplayName("查询时间段内的场馆订单-边界情况（空结果）")
    void findDateOrder_NoResults_ReturnsEmptyList() {
        when(orderDao.findByVenueIDAndStartTimeIsBetween(1, now, now.plusHours(1)))
                .thenReturn(Collections.emptyList());

        List<Order> result = orderService.findDateOrder(1, now, now.plusHours(1));
        assertTrue(result.isEmpty());
    }
    // endregion

    // region 状态变更方法测试
    @Test
    @DisplayName("确认审核通过订单-正常流程")
    void confirmOrder_ValidOrder_ChangesState() {
        when(orderDao.findByOrderID(1)).thenReturn(sampleOrder);

        orderService.confirmOrder(1);
        verify(orderDao).updateState(OrderService.STATE_WAIT, 1);
    }

    @Test
    @DisplayName("完成不存在的订单-异常情况")
    void finishOrder_NonExistOrder_ThrowsException() {
        when(orderDao.findByOrderID(999)).thenReturn(null);

        assertThrows(RuntimeException.class, () ->
                orderService.finishOrder(999));
    }
    // endregion

    // region 复杂业务方法测试
    @Test
    @DisplayName("新建订单-正常参数")
    void submit_ValidParameters_CreatesOrder() {
        when(venueDao.findByVenueName("篮球场")).thenReturn(validVenue);

        orderService.submit("篮球场", now.plusDays(1), 3, "newUser");

        ArgumentCaptor<Order> orderCaptor = ArgumentCaptor.forClass(Order.class);
        verify(orderDao).save(orderCaptor.capture());

        Order savedOrder = orderCaptor.getValue();
        assertAll("验证订单参数",
                () -> assertEquals(OrderService.STATE_NO_AUDIT, savedOrder.getState()),
                () -> assertEquals(3, savedOrder.getHours()),
                () -> assertEquals(600, savedOrder.getTotal()),
                () -> assertEquals("newUser", savedOrder.getUserID())
        );
    }

    @Test
    @DisplayName("更新订单-无效场馆名称")
    void updateOrder_InvalidVenueName_ThrowsException() {
        when(venueDao.findByVenueName("无效场馆")).thenReturn(null);
        when(orderDao.findByOrderID(1)).thenReturn(sampleOrder);

        assertThrows(NullPointerException.class, () ->
                orderService.updateOrder(1, "无效场馆", now.plusDays(2), 3, "user123"));
    }
    // endregion

    // region 分页查询测试
    @Test
    @DisplayName("分页查询用户订单-多页数据")
    void findUserOrder_PaginatedResults_ReturnsCorrectPage() {
        // 准备分页数据
        Pageable pageable = PageRequest.of(0, 5);
        List<Order> orders = List.of(
                new Order(), new Order(), new Order()
        );
        Page<Order> mockPage = new PageImpl<>(orders, pageable, 10);

        when(orderDao.findAllByUserID("user123", pageable)).thenReturn(mockPage);

        Page<Order> result = orderService.findUserOrder("user123", pageable);
        assertEquals(3, result.getNumberOfElements());
        assertEquals(10, result.getTotalElements());
    }
    // endregion

    // region 边界值测试
    @Test
    @DisplayName("创建订单-最短租用时间（1小时）")
    void submit_MinimumHours_CorrectCalculation() {
        when(venueDao.findByVenueName("篮球场")).thenReturn(validVenue);

        orderService.submit("篮球场", now.plusHours(2), 1, "user1");

        ArgumentCaptor<Order> captor = ArgumentCaptor.forClass(Order.class);
        verify(orderDao).save(captor.capture());
        assertEquals(200, captor.getValue().getTotal());
    }


    // region 状态流转验证
    @Test
    @DisplayName("拒绝订单-状态变更验证")
    void rejectOrder_ValidTransition_UpdatesState() {
        Order pendingOrder = new Order();
        pendingOrder.setOrderID(2);
        pendingOrder.setState(OrderService.STATE_NO_AUDIT);

        when(orderDao.findByOrderID(2)).thenReturn(pendingOrder);

        orderService.rejectOrder(2);
        verify(orderDao).updateState(OrderService.STATE_REJECT, 2);
    }

    // endregion
}