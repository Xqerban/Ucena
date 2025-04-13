package com.demo.controller.user;

import com.demo.entity.Order;
import com.demo.entity.User;
import com.demo.entity.Venue;
import com.demo.entity.vo.OrderVo;
import com.demo.exception.LoginException;
import com.demo.service.OrderService;
import com.demo.service.OrderVoService;
import com.demo.service.VenueService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.util.NestedServletException;

import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@WebMvcTest(OrderController.class)
public class OrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private OrderService orderService;

    @MockBean
    private OrderVoService orderVoService;

    @MockBean
    private VenueService venueService;

    // --------------------- order_manage ---------------------

    /**
     * 测试用例：/order_manage 访问订单管理页面（正常情况）
     * 输入：用户已登录，订单数据正常返回
     * 操作：访问 /order_manage，并在 session 中传入 user 对象
     * 期望输出：返回视图 "order_manage"，模型中包含 total 属性
     */
    @Test
    public void testOrderManageSuccess() throws Exception {
        User mockUser = new User();
        mockUser.setUserID(String.valueOf(1));
        // 模拟返回空分页数据
        Page<Order> emptyPage = new PageImpl<>(Collections.emptyList(), PageRequest.of(0, 5, Sort.by("orderTime").descending()), 0);
        when(orderService.findUserOrder(eq(mockUser.getUserID()), any())).thenReturn(emptyPage);

        mockMvc.perform(MockMvcRequestBuilders.get("/order_manage").sessionAttr("user", mockUser))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("order_manage"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("total"));
    }

    /**
     * 测试用例：/order_manage 访问订单管理页面（未登录）
     * 输入：用户未登录
     * 操作：访问 /order_manage
     * 期望输出：抛出 LoginException
     */
    @Test
    public void testOrderManageThrowsLoginException() {
        Exception exception = assertThrows(NestedServletException.class, () -> {
            mockMvc.perform(MockMvcRequestBuilders.get("/order_manage"));
        });
        assertInstanceOf(LoginException.class, exception.getCause());
        assertEquals("请登录！", exception.getCause().getMessage());
    }

    // --------------------- order_place.do ---------------------

    /**
     * 测试用例：/order_place.do 访问场馆订单页面（正常情况）
     * 输入：venueID=1，场馆数据正常返回
     * 操作：访问 /order_place.do?venueID=1
     * 期望输出：返回视图 "order_place"，模型中包含 venue 属性
     */
    @Test
    public void testOrderPlaceDoSuccess() throws Exception {
        Venue venue = new Venue();
        when(venueService.findByVenueID(1)).thenReturn(venue);

        mockMvc.perform(MockMvcRequestBuilders.get("/order_place.do").param("venueID", "1"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("order_place"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("venue"));
    }

    /**
     * 测试用例：/order_place.do 访问场馆订单页面（异常情况）
     * 输入：venueID=1，但 venueService 抛出异常
     * 操作：访问 /order_place.do?venueID=1
     * 期望输出：抛出 RuntimeException
     */
    @Test
    public void testOrderPlaceDoThrowsException() {
        when(venueService.findByVenueID(anyInt())).thenThrow(new RuntimeException("场馆服务异常"));

        Exception exception = assertThrows(NestedServletException.class, () -> {
            mockMvc.perform(MockMvcRequestBuilders.get("/order_place.do").param("venueID", "1"));
        });
        assertInstanceOf(RuntimeException.class, exception.getCause());
        assertEquals("场馆服务异常", exception.getCause().getMessage());
    }

    // --------------------- order_place (无参数) ---------------------

    /**
     * 测试用例：/order_place 访问订单下单页面（正常情况）
     * 输入：无参数
     * 操作：访问 /order_place
     * 期望输出：返回视图 "order_place"
     */
    @Test
    public void testOrderPlaceWithoutParamSuccess() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/order_place"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("order_place"));
    }

    // --------------------- getOrderList.do ---------------------

    /**
     * 测试用例：/getOrderList.do 获取订单列表（正常情况）
     * 输入：用户已登录，page 参数正常
     * 操作：访问 /getOrderList.do?page=1
     * 期望输出：返回 JSON 格式的订单列表
     */
    @Test
    public void testGetOrderListSuccess() throws Exception {
        User mockUser = new User();
        mockUser.setUserID(String.valueOf(1));
        // 模拟订单分页数据
        Page<Order> orderPage = new PageImpl<>(Collections.emptyList(), PageRequest.of(0, 5, Sort.by("orderTime").descending()), 0);
        when(orderService.findUserOrder(eq(mockUser.getUserID()), any())).thenReturn(orderPage);
        // 模拟转换为 OrderVo
        when(orderVoService.returnVo(any())).thenReturn(Collections.singletonList(new OrderVo()));

        mockMvc.perform(MockMvcRequestBuilders.get("/getOrderList.do").param("page", "1").sessionAttr("user", mockUser))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    /**
     * 测试用例：/getOrderList.do 获取订单列表（未登录）
     * 输入：用户未登录
     * 操作：访问 /getOrderList.do
     * 期望输出：抛出 LoginException
     */
    @Test
    public void testGetOrderListThrowsLoginException() {
        when(orderService.findUserOrder(String.valueOf(anyInt()), any())).thenThrow(new LoginException("请登录！"));

        Exception exception = assertThrows(NestedServletException.class, () -> {
            mockMvc.perform(MockMvcRequestBuilders.get("/getOrderList.do"));
        });
        assertInstanceOf(LoginException.class, exception.getCause());
        assertEquals("请登录！", exception.getCause().getMessage());
    }

    // --------------------- addOrder.do ---------------------

    /**
     * 测试用例：/addOrder.do 提交订单（正常情况）
     * 输入：用户已登录，参数正确
     * 操作：POST /addOrder.do，传入 venueName、date、startTime、hours 参数
     * 期望输出：重定向至 order_manage 页面
     */
    @Test
    public void testAddOrderSuccess() throws Exception {
        User mockUser = new User();
        mockUser.setUserID(String.valueOf(1));
        // 模拟 submit 方法正常执行，不抛异常
        Mockito.doNothing().when(orderService).submit(anyString(), any(LocalDateTime.class), anyInt(), eq(mockUser.getUserID()));

        mockMvc.perform(MockMvcRequestBuilders.post("/addOrder.do")
                        .sessionAttr("user", mockUser)
                        .param("venueName", "Test Venue")
                        .param("date", "2025-03-25")
                        .param("startTime", "2025-03-25 10:00")
                        .param("hours", "2"))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.redirectedUrl("order_manage"));
    }

    /**
     * 测试用例：/addOrder.do 提交订单（未登录）
     * 输入：用户未登录
     * 操作：POST /addOrder.do
     * 期望输出：抛出 LoginException
     * Unexpected type ==> expected: <com.demo.exception.LoginException> but was: <java.time.format.DateTimeParseException>
     * 没有处理未登录情况
     */
    @Test
    public void testAddOrderThrowsLoginException() {
        doThrow(new LoginException("请登录！")).when(orderService)
                .submit(anyString(), any(LocalDateTime.class), anyInt(), String.valueOf(anyInt()));

        Exception exception = assertThrows(NestedServletException.class, () -> {
            mockMvc.perform(MockMvcRequestBuilders.post("/addOrder.do")
                    .param("venueName", "Test Venue")
                    .param("date", "2025-03-25")
                    .param("startTime", "2025-03-25 10:00:00")
                    .param("hours", "2"));
        });
        assertInstanceOf(LoginException.class, exception.getCause());
        assertEquals("请登录！", exception.getCause().getMessage());
    }


    // --------------------- finishOrder.do ---------------------

    /**
     * 测试用例：/finishOrder.do 完成订单（正常情况）
     * 输入：orderID=1
     * 操作：POST /finishOrder.do
     * 期望输出：返回 HTTP 200
     */
    @Test
    public void testFinishOrderSuccess() throws Exception {
        Mockito.doNothing().when(orderService).finishOrder(anyInt());

        mockMvc.perform(MockMvcRequestBuilders.post("/finishOrder.do")
                        .param("orderID", "1"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    /**
     * 测试用例：/finishOrder.do 完成订单（异常情况）
     * 输入：orderID=1
     * 操作：POST /finishOrder.do，模拟 finishOrder 抛异常
     * 期望输出：抛出 RuntimeException
     */
    @Test
    public void testFinishOrderThrowsException() {
        doThrow(new RuntimeException("完成订单失败")).when(orderService).finishOrder(anyInt());

        Exception exception = assertThrows(NestedServletException.class, () -> {
            mockMvc.perform(MockMvcRequestBuilders.post("/finishOrder.do")
                    .param("orderID", "1"));
        });
        assertInstanceOf(RuntimeException.class, exception.getCause());
        assertEquals("完成订单失败", exception.getCause().getMessage());
    }

    // --------------------- modifyOrder.do (GET) ---------------------

    /**
     * 测试用例：/modifyOrder.do 编辑订单页面（正常情况）
     * 输入：orderID=1，订单数据正常返回
     * 操作：访问 /modifyOrder.do?orderID=1
     * 期望输出：返回视图 "order_edit"，模型中包含 order 和 venue 属性
     */
    @Test
    public void testEditOrderSuccess() throws Exception {
        Order order = new Order();
        order.setVenueID(10);
        Venue venue = new Venue();
        when(orderService.findById(1)).thenReturn(order);
        when(venueService.findByVenueID(10)).thenReturn(venue);

        mockMvc.perform(MockMvcRequestBuilders.get("/modifyOrder.do")
                        .param("orderID", "1"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("order_edit"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("order"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("venue"));
    }

    /**
     * 测试用例：/modifyOrder.do 编辑订单页面（异常情况）
     * 输入：orderID=999，订单不存在
     * 操作：访问 /modifyOrder.do?orderID=999
     * 期望输出：抛出 NullPointerException
     */
    @Test
    public void testEditOrderThrowsException() {
        when(orderService.findById(anyInt())).thenReturn(null);

        Exception exception = assertThrows(NestedServletException.class, () -> {
            mockMvc.perform(MockMvcRequestBuilders.get("/modifyOrder.do")
                    .param("orderID", "999"));
        });
        assertInstanceOf(NullPointerException.class, exception.getCause());
    }

    // --------------------- modifyOrder (POST) ---------------------

    /**
     * 测试用例：/modifyOrder 修改订单（正常情况）
     * 输入：venueName、date、startTime、hours、orderID 参数正常，用户已登录
     * 操作：POST /modifyOrder
     * 期望输出：返回 true 并重定向至 order_manage
     */
    @Test
    public void testModifyOrderSuccess() throws Exception {
        User mockUser = new User();
        mockUser.setUserID(String.valueOf(1));
        Mockito.doNothing().when(orderService).updateOrder(anyInt(), anyString(), any(LocalDateTime.class), anyInt(), eq(mockUser.getUserID()));

        mockMvc.perform(MockMvcRequestBuilders.post("/modifyOrder")
                        .sessionAttr("user", mockUser)
                        .param("venueName", "Test Venue")
                        .param("date", "2025-03-25")
                        .param("startTime", "2025-03-25 10:00")
                        .param("hours", "2")
                        .param("orderID", "1"))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.redirectedUrl("order_manage"));
    }

    /**
     * 测试用例：/modifyOrder 修改订单（未登录）
     * 输入：用户未登录
     * 操作：POST /modifyOrder
     * 期望输出：抛出 LoginException
     * Unexpected type ==> expected: <com.demo.exception.LoginException> but was: <java.time.format.DateTimeParseException>
     * 没有处理未登录情况
     */
    @Test
    public void testModifyOrderThrowsLoginException() {
        Exception exception = assertThrows(NestedServletException.class, () -> {
            mockMvc.perform(MockMvcRequestBuilders.post("/modifyOrder")
                    .param("venueName", "Test Venue")
                    .param("date", "2025-03-25")
                    .param("startTime", "2025-03-25 10:00:00")
                    .param("hours", "2")
                    .param("orderID", "1"));
        });
        assertInstanceOf(LoginException.class, exception.getCause());
        assertEquals("请登录！", exception.getCause().getMessage());
    }

    // --------------------- delOrder.do ---------------------

    /**
     * 测试用例：/delOrder.do 删除订单（正常情况）
     * 输入：orderID=1
     * 操作：POST /delOrder.do
     * 期望输出：返回 "true"
     */
    @Test
    public void testDelOrderSuccess() throws Exception {
        Mockito.doNothing().when(orderService).delOrder(anyInt());

        mockMvc.perform(MockMvcRequestBuilders.post("/delOrder.do")
                        .param("orderID", "1"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("true"));
    }

    /**
     * 测试用例：/delOrder.do 删除订单（异常情况）
     * 输入：orderID=1
     * 操作：POST /delOrder.do，模拟 delOrder 抛异常
     * 期望输出：抛出 RuntimeException
     */
    @Test
    public void testDelOrderThrowsException() {
        doThrow(new RuntimeException("删除订单失败")).when(orderService).delOrder(anyInt());

        Exception exception = assertThrows(NestedServletException.class, () -> {
            mockMvc.perform(MockMvcRequestBuilders.post("/delOrder.do")
                    .param("orderID", "1"));
        });
        assertInstanceOf(RuntimeException.class, exception.getCause());
        assertEquals("删除订单失败", exception.getCause().getMessage());
    }

    // --------------------- order/getOrderList.do ---------------------

    /**
     * 测试用例：/order/getOrderList.do 获取场馆订单信息（正常情况）
     * 输入：venueName=Test Venue, date=2025-03-25
     * 操作：访问 /order/getOrderList.do
     * 期望输出：返回 VenueOrder 对象（JSON 格式）
     */
    @Test
    public void testGetVenueOrderSuccess() throws Exception {
        Venue venue = new Venue();
        venue.setVenueID(100);
        when(venueService.findByVenueName("Test Venue")).thenReturn(venue);
        when(orderService.findDateOrder(eq(100), any(LocalDateTime.class), any(LocalDateTime.class)))
                .thenReturn(Collections.singletonList(new Order()));

        mockMvc.perform(MockMvcRequestBuilders.get("/order/getOrderList.do")
                        .param("venueName", "Test Venue")
                        .param("date", "2025-03-25"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    /**
     * 测试用例：/order/getOrderList.do 获取场馆订单信息（异常情况）
     * 输入：venueName=Unknown Venue, date=2025-03-25
     * 操作：访问 /order/getOrderList.do，模拟 venueService.findByVenueName 返回 null
     * 期望输出：抛出 NullPointerException
     */
    @Test
    public void testGetVenueOrderThrowsException() {
        when(venueService.findByVenueName(anyString())).thenReturn(null);

        Exception exception = assertThrows(NestedServletException.class, () -> {
            mockMvc.perform(MockMvcRequestBuilders.get("/order/getOrderList.do")
                    .param("venueName", "Unknown Venue")
                    .param("date", "2025-03-25"));
        });
        assertInstanceOf(NullPointerException.class, exception.getCause());
    }
}
