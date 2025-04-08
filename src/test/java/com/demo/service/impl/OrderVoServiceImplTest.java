package com.demo.service.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import com.demo.dao.OrderDao;
import com.demo.dao.VenueDao;
import com.demo.entity.Order;
import com.demo.entity.Venue;
import com.demo.entity.vo.OrderVo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@ExtendWith(MockitoExtension.class)
class OrderVoServiceImplTest {

    @Mock
    private OrderDao orderDao;

    @Mock
    private VenueDao venueDao;

    @InjectMocks
    private OrderVoServiceImpl orderVoService; // 确保注入的是 OrderVoServiceImpl

    private Order testOrder;
    private Venue testVenue;

    @BeforeEach
    void setup() {
        testVenue = new Venue(1, "篮球场", "标准场地", 200, "img.jpg", "体育中心", "08:00", "22:00");
        testOrder = new Order();
        testOrder.setOrderID(1);
        testOrder.setVenueID(1);
        testOrder.setUserID("user123");
    }

    // region 测试 returnOrderVoByOrderID()
    @Test
    @DisplayName("通过订单ID返回OrderVo-成功")
    void returnOrderVoByOrderID_ValidOrderID_ReturnsOrderVo() {
        // 模拟依赖
        when(orderDao.findByOrderID(1)).thenReturn(testOrder);
        when(venueDao.findByVenueID(1)).thenReturn(testVenue);

        // 执行测试
        OrderVo result = orderVoService.returnOrderVoByOrderID(1);

        // 验证结果
        assertNotNull(result);
        assertEquals("篮球场", result.getVenueName());
        verify(orderDao).findByOrderID(1);
        verify(venueDao).findByVenueID(1);
    }

    @Test
    @DisplayName("查询不存在的订单ID-抛出异常")
    void returnOrderVoByOrderID_InvalidOrderID_ThrowsException() {
        when(orderDao.findByOrderID(999)).thenReturn(null);

        assertThrows(RuntimeException.class,
                () -> orderVoService.returnOrderVoByOrderID(999));
    }
    // endregion

    // region 测试 returnVo()
    @Test
    @DisplayName("转换Order列表为OrderVo列表-成功")
    void returnVo_ValidOrders_ReturnsConvertedList() {
        // 准备数据
        Order order1 = new Order();
        order1.setOrderID(2);
        order1.setVenueID(1);
        List<Order> orders = Arrays.asList(testOrder, order1);

        // 模拟依赖：为每个 orderID 返回对应的 Order 对象
        when(orderDao.findByOrderID(1)).thenReturn(testOrder);
        when(orderDao.findByOrderID(2)).thenReturn(order1); // 新增此行
        when(venueDao.findByVenueID(1)).thenReturn(testVenue);

        // 执行测试
        List<OrderVo> result = orderVoService.returnVo(orders);

        // 验证结果
        assertEquals(2, result.size());
        assertTrue(result.stream().allMatch(vo -> vo.getVenueName().equals("篮球场")));
    }
    // endregion
}