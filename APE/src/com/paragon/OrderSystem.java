package com.paragon;

import com.paragon.orders.Order;
import com.paragon.stock.Offer;
import com.paragon.stock.Quote;
import com.paragon.stock.Warehouse;

import java.math.BigDecimal;
import java.util.*;

public class OrderSystem implements OrderService {

    private static final long MAX_QUOTE_AGE_MILLIS = 20 * 60 * 10;

    public static final BigDecimal CASE_SIZE = new BigDecimal(12);

    private Map<UUID, Quote> quotes = new HashMap<UUID, Quote>();
    private FulfillmentService fulfillmentService;

    @Override
    public void add(FulfillmentService fulfillmentService) {
        this.fulfillmentService = fulfillmentService;
    }

    @Override
    public List<Offer> searchForProduct(String query) {

        List<Offer> searchResults = Warehouse.getInstance().searchFor(query);
        for (Offer offer : searchResults) {
            quotes.put(offer.id, new Quote(offer, System.currentTimeMillis()));
        }
        return searchResults;
    }

    @Override
    public Order confirmOrder(Quote quote, String userAuthToken, long timeNow) {

        if (quote != null) {
        Order completeOrder = new Order(totalPrice(quote.offer.price, quote.timestamp, timeNow), quote, timeNow, userAuthToken);

        updateOrderLedger(completeOrder);

            return completeOrder;
        }
        else {
            return null;
        }
     }

    @Override
    public void updateOrderLedger(Order order) {
        this.fulfillmentService.placeOrder(order);
    }

    @Override
    public BigDecimal totalPrice(BigDecimal bottlePrice, long orderTime, long confirmedOrderTime) {

        BigDecimal casePrice = bottlePrice.multiply(CASE_SIZE);

        if (timeOutOccurred(orderTime, confirmedOrderTime, 2 * 60 * 10)) {
            if (timeOutOccurred(orderTime, confirmedOrderTime, 10 * 60 * 10)) {
                return casePrice.add(new BigDecimal(20));
            }
            else {
                return casePrice.divide(new BigDecimal(20)).min(new BigDecimal(10));
            }
        }
        else {
            return casePrice;
        }
    }

    @Override
    public Quote validQuote(UUID uuid)  {

        Quote quote = quotes.get(uuid);

        if (quote == null || timeOutOccurred(quote.timestamp, System.currentTimeMillis(), MAX_QUOTE_AGE_MILLIS)) {
             return null;
        }
        else {
            return quote;
        }
    }

    private boolean timeOutOccurred(long startTime, long endTime, long timeOutValue) {
        return timeOutValue < endTime - startTime;
    }
}