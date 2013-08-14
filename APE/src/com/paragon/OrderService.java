package com.paragon;

import com.paragon.orders.Order;
import com.paragon.stock.Offer;
import com.paragon.stock.Quote;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public interface OrderService {
    List<Offer> searchForProduct(String query);

    Order confirmOrder(Quote quote, String userAuthToken, long timeNow);

    BigDecimal totalPrice(BigDecimal bottlePrice, long orderTime, long confirmedOrderTime);

    void updateOrderLedger(Order order);

    void add(FulfillmentService fulfillmentService);

    Quote validQuote(UUID uuid);
}
