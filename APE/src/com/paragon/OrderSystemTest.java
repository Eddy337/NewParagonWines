package com.paragon;

import com.paragon.stock.Offer;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

public class OrderSystemTest {
    @Test
    public void testSearchForProduct() throws Exception {
        OrderSystem orderSystem = new OrderSystem();
        List<Offer> offers = orderSystem.searchForProduct("C");
             Assert.assertTrue(!offers.isEmpty());
    }

    @Test
    public void testConfirmOrder() throws Exception {
        OrderSystem orderSystem = new OrderSystem();
        List<Offer> offers = orderSystem.searchForProduct("C");
        Offer offer = offers.get(0);
        Thread.sleep(55000);
        Assert.assertTrue(orderSystem.confirmOrder(offer.id, "tom@example.com"));

    }
}
