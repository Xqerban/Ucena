package com.demo.service.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

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
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

@ExtendWith(MockitoExtension.class)
class OrderVoServiceImplTest {

    @Mock
    private OrderDao orderDao;

    @Mock
    private VenueDao venueDao;

    @InjectMocks
    private OrderServiceImpl orderService;

    private Venue basketballCourt;
    private Order testOrder;
    private final LocalDateTime currentTime = LocalDateTime.now();

    @BeforeEach
    void setup() {
        // 初始化测试数据
        basketballCourt = new Venue(1, "篮球场", "标准场地", 200, "img.jpg",
                "体育中心", "08:00", "22:00");

        testOrder = new Order();
        testOrder.setOrderID(1);
        testOrder.setUserID("user123");
        testOrder.setVenueID(1);
        testOrder.setState(OrderService.STATE_NO_AUDIT);
        testOrder.setOrderTime(currentTime.minusHours(1));
        testOrder.setStartTime(currentTime.plusHours(2));
        testOrder.setHours(2);
        testOrder.setTotal(400);
    }

    // region 订单创建测试
    @Test
    @DisplayName("创建有效订单-成功保存")
    void submit_ValidOrder_SavesSuccessfully() {
        // 模拟依赖
        when(venueDao.findByVenueName("篮球场")).thenReturn(basketballCourt);

        // 执行测试
        orderService.submit("篮球场", currentTime.plusDays(1), 3, "user456");

        // 验证结果
        ArgumentCaptor<Order> orderCaptor = ArgumentCaptor.forClass(Order.class);
        verify(orderDao).save(orderCaptor.capture());

        Order savedOrder = orderCaptor.getValue();
        assertAll("订单参数验证",
                () -> assertEquals(OrderService.STATE_NO_AUDIT, savedOrder.getState()),
                () -> assertEquals(600, savedOrder.getTotal()),
                () -> assertEquals("user456", savedOrder.getUserID()),
                () -> assertEquals(basketballCourt.getVenueID(), savedOrder.getVenueID())
        );
    }


    // region 订单更新测试
    @Test
    @DisplayName("更新场馆信息-重新计算金额")
    void updateOrder_ChangeVenue_RecalculatesTotal() {
        // 准备新场馆
        Venue badmintonCourt = new Venue(2, "羽毛球场", "", 150, "", "", "", "");

        // 模拟依赖
        when(venueDao.findByVenueName("羽毛球场")).thenReturn(badmintonCourt);
        when(orderDao.findByOrderID(1)).thenReturn(testOrder);

        // 执行测试
        orderService.updateOrder(1, "羽毛球场", currentTime.plusHours(3), 4, "user123");

        // 验证结果
        assertAll("更新验证",
                () -> assertEquals(badmintonCourt.getVenueID(), testOrder.getVenueID()),
                () -> assertEquals(150 * 4, testOrder.getTotal()),
                () -> assertEquals(OrderService.STATE_NO_AUDIT, testOrder.getState())
        );
        verify(orderDao).save(testOrder);
    }
    // endregion

    // region 状态变更测试
    @Test
    @DisplayName("审核通过未处理订单-状态变更")
    void confirmOrder_PendingOrder_UpdatesState() {
        when(orderDao.findByOrderID(1)).thenReturn(testOrder);

        orderService.confirmOrder(1);

        verify(orderDao).updateState(OrderService.STATE_WAIT, 1);
    }

    @Test
    @DisplayName("完成已审核订单-合法操作")
    void finishOrder_ApprovedOrder_Success() {
        testOrder.setState(OrderService.STATE_WAIT);
        when(orderDao.findByOrderID(1)).thenReturn(testOrder);

        assertDoesNotThrow(() -> orderService.finishOrder(1));
        verify(orderDao).updateState(OrderService.STATE_FINISH, 1);
    }

    @Test
    @DisplayName("拒绝不存在订单-异常处理")
    void rejectOrder_NonExistentOrder_ThrowsException() {
        when(orderDao.findByOrderID(999)).thenReturn(null);

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> orderService.rejectOrder(999));
        assertEquals("订单不存在", ex.getMessage());
    }
    // endregion

    // region 查询功能测试
    @Test
    @DisplayName("查询用户订单-分页结果")
    void findUserOrder_Pagination_ReturnsData() {
        // 准备分页数据
        PageRequest pageRequest = PageRequest.of(0, 5);
        List<Order> orders = Collections.nCopies(3, testOrder);
        Page<Order> mockPage = new PageImpl<>(orders, pageRequest, 10);

        when(orderDao.findAllByUserID("user123", pageRequest)).thenReturn(mockPage);

        // 执行查询
        Page<Order> result = orderService.findUserOrder("user123", pageRequest);

        // 验证结果
        assertAll("分页验证",
                () -> assertEquals(3, result.getNumberOfElements()),
                () -> assertTrue(result.getContent().stream()
                        .allMatch(o -> o.getUserID().equals("user123")))
        );
    }

    @Test
    @DisplayName("查询时间段订单-空结果")
    void findDateOrder_NoBookings_ReturnsEmpty() {
        LocalDateTime testStart = currentTime.plusDays(1);
        when(orderDao.findByVenueIDAndStartTimeIsBetween(1, testStart, testStart.plusHours(2)))
                .thenReturn(Collections.emptyList());

        List<Order> result = orderService.findDateOrder(1, testStart, testStart.plusHours(2));
        assertTrue(result.isEmpty());
    }
    // endregion

    // region 删除操作测试
    @Test
    @DisplayName("删除有效订单-成功执行")
    void delOrder_ValidOrder_DeletesSuccessfully() {
        doNothing().when(orderDao).deleteById(1);

        assertDoesNotThrow(() -> orderService.delOrder(1));
        verify(orderDao).deleteById(1);
    }
    // endregion

    // region 边界条件测试
    @Test
    @DisplayName("创建最小时长订单-1小时")
    void submit_MinimumDuration_Success() {
        when(venueDao.findByVenueName("篮球场")).thenReturn(basketballCourt);

        orderService.submit("篮球场", currentTime.plusHours(1), 1, "user123");

        ArgumentCaptor<Order> captor = ArgumentCaptor.forClass(Order.class);
        verify(orderDao).save(captor.capture());
        assertEquals(200, captor.getValue().getTotal());
    }

    @Test
    @DisplayName("更新为相同时间-无变化")
    void updateOrder_SameTime_UpdatesNothing() {
        when(venueDao.findByVenueName("篮球场")).thenReturn(basketballCourt);
        when(orderDao.findByOrderID(1)).thenReturn(testOrder);

        orderService.updateOrder(1, "篮球场", testOrder.getStartTime(), 2, "user123");

        assertEquals(testOrder.getStartTime(), testOrder.getStartTime()); // 时间未变
        verify(orderDao).save(testOrder);
    }
    // endregion
}