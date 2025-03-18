package com.demo.entity.vo;

import com.demo.entity.Order;
import com.demo.entity.Venue;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VenueOrder {
    Venue venue;
    List<Order> orders;
}
