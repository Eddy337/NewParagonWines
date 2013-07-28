package com.paragon;

import com.paragon.orders.Order;
import com.paragon.stock.Offer;
import org.junit.Assert;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.List;

public class OrderSystemTest {
    @Test
    public void testSearchForProduct() throws Exception {
        OrderSystem orderSystem = new OrderSystem();
        List<Offer> offers = orderSystem.searchForProduct("C");
             Assert.assertTrue(!offers.isEmpty());
    }

    @Test
    public void testSearchForProductNotThere() throws Exception {
        OrderSystem orderSystem = new OrderSystem();
        List<Offer> offers = orderSystem.searchForProduct("Orange Fanta");
        Assert.assertTrue(offers.isEmpty());
    }

    @Test
    public void testConfirmOrder() throws Exception {
        OrderSystem orderSystem = new OrderSystem();
        List<Offer> offers = orderSystem.searchForProduct("C");
        Offer offer = offers.get(0);
        Thread.sleep(19 * 60 * 10);
        Assert.assertNotNull(orderSystem.confirmOrder(offer.id, "tom@example.com"));

    }

    @Test
    public void testConfirmOrderExpired() throws Exception {
        OrderSystem orderSystem = new OrderSystem();
        List<Offer> offers = orderSystem.searchForProduct("C");
        Offer offer = offers.get(0);
        Thread.sleep(21 * 60 * 10);
        Assert.assertNull(orderSystem.confirmOrder(offer.id, "tom@example.com"));

    }

    @Test
    public void testConfirmOrderCheckPrice() throws Exception {
        OrderSystem orderSystem = new OrderSystem();
        List<Offer> offers = orderSystem.searchForProduct("C");
        Offer offer = offers.get(0);
        Thread.sleep(1 * 60 * 10);
        Order order = orderSystem.confirmOrder(offer.id, "tom@example.com");
        Assert.assertEquals(true, orderSystem.confirmOrder(offer.id, "tom@example.com").totalPrice.equals(orderSystem.TotalPrice(offer.price)));

    }

}
