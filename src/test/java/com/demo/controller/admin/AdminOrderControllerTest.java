package com.demo.controller.admin;

import com.demo.entity.Order;
import com.demo.entity.vo.OrderVo;
import com.demo.service.OrderService;
import com.demo.service.OrderVoService;
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

@WebMvcTest(AdminOrderController.class)
class AdminOrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private OrderService orderService;

    @MockBean
    private OrderVoService orderVoService;

    private void setField(Object obj, String fieldName, Object value) throws Exception {
        Field field = obj.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(obj, value);
    }

    /**
     * 测试用例1：/reservation_manage页面正常加载
     * 输入：无输入
     * 操作：访问/reservation_manage
     * 期望输出：页面能够加载，并显示订单列表和总页数
     */
    @Test
    void testReservationManagePageWithOrders() throws Exception {
        // 准备测试数据
        List<Order> orders = new ArrayList<>();
        Order order = new Order();
        setField(order, "orderID", 1);
        setField(order, "orderTime", LocalDateTime.now());
        orders.add(order);

        List<OrderVo> orderVos = new ArrayList<>();
        OrderVo orderVo = new OrderVo();
        setField(orderVo, "orderID", 1);
        orderVos.add(orderVo);

        // 模拟服务层行为
        when(orderService.findAuditOrder()).thenReturn(orders);
        when(orderVoService.returnVo(orders)).thenReturn(orderVos);
        when(orderService.findNoAuditOrder(any(Pageable.class))).thenReturn(new PageImpl<>(orders));

        // 执行测试
        mockMvc.perform(get("/reservation_manage"))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/reservation_manage"))
                .andExpect(model().attributeExists("order_list"))
                .andExpect(model().attributeExists("total"))
                .andExpect(model().attribute("order_list", orderVos));
    }

    /**
     * 测试用例2：/reservation_manage页面无订单数据
     * 输入：无输入
     * 操作：访问/reservation_manage
     * 期望输出：页面能够加载，但order_list为空列表
     */
    @Test
    void testReservationManagePageWithoutOrders() throws Exception {
        // 模拟空数据
        when(orderService.findAuditOrder()).thenReturn(new ArrayList<>());
        when(orderVoService.returnVo(anyList())).thenReturn(new ArrayList<>());
        when(orderService.findNoAuditOrder(any(Pageable.class))).thenReturn(new PageImpl<>(new ArrayList<>()));

        // 执行测试
        mockMvc.perform(get("/reservation_manage"))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/reservation_manage"))
                .andExpect(model().attributeExists("order_list"))
                .andExpect(model().attribute("order_list", hasSize(0)));
    }

    /**
     * 测试用例3：/admin/getOrderList.do第一页数据
     * 输入：page=1
     * 操作：访问/admin/getOrderList.do?page=1
     * 期望输出：返回第一页的订单数据
     */
    @Test
    void testGetNoAuditOrderFirstPage() throws Exception {
        // 准备测试数据
        List<Order> orders = new ArrayList<>();
        Order order = new Order();
        setField(order, "orderID", 1);
        setField(order, "orderTime", LocalDateTime.now());
        orders.add(order);

        List<OrderVo> orderVos = new ArrayList<>();
        OrderVo orderVo = new OrderVo();
        setField(orderVo, "orderID", 1);
        orderVos.add(orderVo);

        // 模拟服务层行为
        when(orderService.findNoAuditOrder(any(Pageable.class))).thenReturn(new PageImpl<>(orders));
        when(orderVoService.returnVo(orders)).thenReturn(orderVos);

        // 执行测试
        mockMvc.perform(get("/admin/getOrderList.do").param("page", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].orderID").value(1));
    }

    /**
     * 测试用例4：/admin/getOrderList.do第二页数据
     * 输入：page=2
     * 操作：访问/admin/getOrderList.do?page=2
     * 期望输出：返回第二页的订单数据
     */
    @Test
    void testGetNoAuditOrderSecondPage() throws Exception {
        // 准备测试数据
        List<Order> orders = new ArrayList<>();
        Order order = new Order();
        setField(order, "orderID", 2);
        setField(order, "orderTime", LocalDateTime.now());
        orders.add(order);

        List<OrderVo> orderVos = new ArrayList<>();
        OrderVo orderVo = new OrderVo();
        setField(orderVo, "orderID", 2);
        orderVos.add(orderVo);

        // 模拟服务层行为
        when(orderService.findNoAuditOrder(any(Pageable.class))).thenReturn(new PageImpl<>(orders));
        when(orderVoService.returnVo(orders)).thenReturn(orderVos);

        // 执行测试
        mockMvc.perform(get("/admin/getOrderList.do").param("page", "2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].orderID").value(2));
    }

    /**
     * 测试用例5：/admin/getOrderList.do无效页码（负数）
     * 输入：page=-1
     * 操作：访问/admin/getOrderList.do?page=-1
     * 期望输出：返回空列表
     */
    @Test
    void testGetNoAuditOrderWithNegativePage() throws Exception {
        // 模拟空数据
        when(orderService.findNoAuditOrder(any(Pageable.class))).thenReturn(new PageImpl<>(new ArrayList<>()));
        when(orderVoService.returnVo(anyList())).thenReturn(new ArrayList<>());

        // 执行测试
        mockMvc.perform(get("/admin/getOrderList.do").param("page", "-1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }

    /**
     * 测试用例6：/admin/getOrderList.do超出范围的页码
     * 输入：page=1000
     * 操作：访问/admin/getOrderList.do?page=1000
     * 期望输出：返回空列表
     */
    @Test
    void testGetNoAuditOrderWithOutOfRangePage() throws Exception {
        // 模拟空数据
        when(orderService.findNoAuditOrder(any(Pageable.class))).thenReturn(new PageImpl<>(new ArrayList<>()));
        when(orderVoService.returnVo(anyList())).thenReturn(new ArrayList<>());

        // 执行测试
        mockMvc.perform(get("/admin/getOrderList.do").param("page", "1000"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }

    /**
     * 测试用例7：/passOrder.do成功通过订单
     * 输入：orderID=1
     * 操作：访问/passOrder.do?orderID=1
     * 期望输出：返回true表示订单已通过
     */
    @Test
    void testPassOrderSuccess() throws Exception {
        // 模拟服务层行为
        doNothing().when(orderService).confirmOrder(1);

        // 执行测试
        mockMvc.perform(post("/passOrder.do").param("orderID", "1"))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));
    }

    /**
     * 测试用例8：/passOrder.do无效订单ID
     * 输入：orderID=-1
     * 操作：访问/passOrder.do?orderID=-1
     * 期望输出：返回true（即使ID无效，接口仍然返回成功）
     */
    @Test
    void testPassOrderWithInvalidId() throws Exception {
        // 模拟服务层行为
        doNothing().when(orderService).confirmOrder(-1);

        // 执行测试
        mockMvc.perform(post("/passOrder.do").param("orderID", "-1"))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));
    }

    /**
     * 测试用例9：/rejectOrder.do成功拒绝订单
     * 输入：orderID=1
     * 操作：访问/rejectOrder.do?orderID=1
     * 期望输出：返回true表示订单已拒绝
     */
    @Test
    void testRejectOrderSuccess() throws Exception {
        // 模拟服务层行为
        doNothing().when(orderService).rejectOrder(1);

        // 执行测试
        mockMvc.perform(post("/rejectOrder.do").param("orderID", "1"))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));
    }

    /**
     * 测试用例10：/rejectOrder.do无效订单ID
     * 输入：orderID=-1
     * 操作：访问/rejectOrder.do?orderID=-1
     * 期望输出：返回true（即使ID无效，接口仍然返回成功）
     */
    @Test
    void testRejectOrderWithInvalidId() throws Exception {
        // 模拟服务层行为
        doNothing().when(orderService).rejectOrder(-1);

        // 执行测试
        mockMvc.perform(post("/rejectOrder.do").param("orderID", "-1"))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));
    }
} 