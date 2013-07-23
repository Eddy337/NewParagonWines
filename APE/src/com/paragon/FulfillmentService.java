package com.paragon;
import com.paragon.orders.Order;

/**
 * Created with IntelliJ IDEA.
 * User: Jon
 * Date: 23/07/13
 * Time: 19:47
 * To change this template use File | Settings | File Templates.
 */
public interface FulfillmentService {
    void placeOrder(Order order);
}
