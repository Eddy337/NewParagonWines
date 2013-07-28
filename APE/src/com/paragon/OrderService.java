package com.paragon;

import com.paragon.orders.Order;
import com.paragon.stock.Offer;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public interface OrderService {
    List<Offer> searchForProduct(String query);

    Order confirmOrder(UUID id, String userAuthToken);

    BigDecimal TotalPrice(BigDecimal bottlePrice);
}
