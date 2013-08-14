package com.paragon;
import com.paragon.orders.Order;
import com.paragon.stock.Quote;
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
        Thread.sleep(19 * 60 * 10);
        orderSystem.add(OrderLedger.getInstance());
        Assert.assertNotNull(orderSystem.confirmOrder(orderSystem.validQuote(offers.get(0).id), userAuthToken, System.currentTimeMillis()));

    }

    @Test
    public void testConfirmOrderExpired() throws Exception {
        List<Offer> offers = orderSystem.searchForProduct(searchString);
        Thread.sleep(21 * 60 * 10);
        orderSystem.add(OrderLedger.getInstance());
        Assert.assertNull(orderSystem.confirmOrder(orderSystem.validQuote(offers.get(0).id), userAuthToken, System.currentTimeMillis()));

    }

    @Test
    public void testConfirmOrderCheckPrice() throws Exception {
        List<Offer> offers = orderSystem.searchForProduct(searchString);
        long timeNow = System.currentTimeMillis();
        Thread.sleep(1 * 60 * 10);
        orderSystem.add(OrderLedger.getInstance());
        Quote quote = orderSystem.validQuote(offers.get(0).id);
        orderSystem.confirmOrder(orderSystem.validQuote(offers.get(0).id), userAuthToken, timeNow);
        Assert.assertEquals(true, orderSystem.confirmOrder(quote, userAuthToken, timeNow).totalPrice.equals(orderSystem.totalPrice(quote.offer.price, quote.timestamp, timeNow)));

    }
    @Test
    public void testConfirmOrderMock() throws Exception {

        List<Offer> offers = new ArrayList<Offer>(orderSystem.searchForProduct(searchString));
        final Order completeOrder = orderSystem.confirmOrder(orderSystem.validQuote(offers.get(0).id), userAuthToken, System.currentTimeMillis());
        orderSystem.add(fulfillmentService);

        mockingContext.checking(new Expectations() {{
            exactly(1).of(fulfillmentService).placeOrder(completeOrder);
        }});

        orderSystem.updateOrderLedger(completeOrder);
    }
}
