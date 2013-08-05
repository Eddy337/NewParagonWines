package com.paragon;
import com.paragon.orders.Order;
import com.paragon.orders.OrderLedger;
import com.paragon.stock.Offer;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.Assert;
import org.junit.Test;
import java.lang.String;

import java.util.*;

public class TestOrderSystem {

    private String searchString = "C";
    private String userAuthToken = "ted@test.com";

    Mockery mockingContext = new JUnit4Mockery();
    FulfillmentService fulfillmentService = mockingContext.mock(FulfillmentService.class);
    OrderSystem orderSystem = new OrderSystem();


    @Test
    public void testSearchForProduct() throws Exception {
        List<Offer> offers = orderSystem.searchForProduct(searchString);
        Assert.assertTrue(!offers.isEmpty());
    }

    @Test
    public void testSearchForProductNotThere() throws Exception {
        List<Offer> offers = orderSystem.searchForProduct("Orange Fanta");
        Assert.assertTrue(offers.isEmpty());
    }

    @Test
    public void testConfirmOrder() throws Exception {
        List<Offer> offers = orderSystem.searchForProduct(searchString);
        Offer offer = offers.get(0);
        Thread.sleep(19 * 60 * 10);
        orderSystem.add(OrderLedger.getInstance());
        Assert.assertNotNull(orderSystem.confirmOrder(offer.id, userAuthToken));

    }

    @Test
    public void testConfirmOrderExpired() throws Exception {
        List<Offer> offers = orderSystem.searchForProduct(searchString);
        Offer offer = offers.get(0);
        Thread.sleep(21 * 60 * 10);
        orderSystem.add(OrderLedger.getInstance());
        Assert.assertNull(orderSystem.confirmOrder(offer.id, userAuthToken));

    }

    @Test
    public void testConfirmOrderCheckPrice() throws Exception {
        List<Offer> offers = orderSystem.searchForProduct(searchString);
        Offer offer = offers.get(0);
        Thread.sleep(1 * 60 * 10);
        orderSystem.add(OrderLedger.getInstance());
        Order order = orderSystem.confirmOrder(offer.id, userAuthToken);
        Assert.assertEquals(true, orderSystem.confirmOrder(offer.id, userAuthToken).totalPrice.equals(orderSystem.totalPrice(offer.price)));

    }
    @Test
    public void testConfirmOrderMock() throws Exception {

        List<Offer> offers = new ArrayList<Offer>(orderSystem.searchForProduct(searchString));
        final Order completeOrder = orderSystem.confirmOrder(offers.get(0).id, userAuthToken);
        orderSystem.add(fulfillmentService);

        mockingContext.checking(new Expectations() {{
            exactly(1).of(fulfillmentService).placeOrder(completeOrder);
        }});

        orderSystem.updateOrderLedger(completeOrder);
    }
}
