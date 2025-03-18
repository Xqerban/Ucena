package com.demo.service;


import com.demo.entity.Order;
import com.demo.entity.vo.OrderVo;

import java.util.List;

public interface OrderVoService {
    OrderVo returnOrderVoByOrderID(int orderID);
    List<OrderVo> returnVo(List<Order> list);
}
