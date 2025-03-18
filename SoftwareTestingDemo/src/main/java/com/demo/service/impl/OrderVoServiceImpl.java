package com.demo.service.impl;


import com.demo.dao.OrderDao;
import com.demo.dao.VenueDao;
import com.demo.entity.Order;
import com.demo.entity.Venue;
import com.demo.entity.vo.OrderVo;
import com.demo.service.OrderVoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class OrderVoServiceImpl implements OrderVoService {
    @Autowired
    private OrderDao orderDao;
    @Autowired
    private VenueDao venueDao;


    @Override
    public OrderVo returnOrderVoByOrderID(int orderID) {
        Order order=orderDao.findByOrderID(orderID);
        Venue venue=venueDao.findByVenueID(order.getVenueID());
        OrderVo orderVo=new OrderVo(order.getOrderID(),order.getUserID(),order.getVenueID(),venue.getVenueName(),
                                    order.getState(),order.getOrderTime(),order.getStartTime(),order.getHours(),order.getTotal());

        return orderVo;
    }

    @Override
    public List<OrderVo> returnVo(List<Order> list) {
        List<OrderVo> list1=new ArrayList<>();
        for(int i=0;i<list.size();i++) {
            list1.add(returnOrderVoByOrderID(list.get(i).getOrderID()));
        }
        return list1;
    }
}
