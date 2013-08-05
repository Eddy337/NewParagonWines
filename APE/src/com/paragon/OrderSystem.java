package com.paragon;

import com.paragon.orders.Order;
import com.paragon.orders.OrderLedger;
import com.paragon.stock.Offer;
import com.paragon.stock.Quote;
import com.paragon.stock.Warehouse;

import java.math.BigDecimal;
import java.util.*;

public class OrderSystem implements OrderService {

    private static final long MAX_QUOTE_AGE_MILLIS = 20 * 60 * 10;

    public static final BigDecimal STANDARD_PROCESSING_CHARGE = new BigDecimal(5);
    public static final BigDecimal CASE_SIZE = new BigDecimal(12);

    private Map<UUID, Quote> quotes = new HashMap<UUID, Quote>();
    private FulfillmentService fulfillmentService;

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
    public Order confirmOrder(UUID id, String userAuthToken) {

        try {
        if (!quotes.containsKey(id)) {
            throw new NoSuchElementException("Offer ID is invalid");
        }

        Quote quote = quotes.get(id);

        long timeNow = System.currentTimeMillis();

        if (timeNow - quote.timestamp > MAX_QUOTE_AGE_MILLIS) {
            throw new IllegalStateException("Quote expired, please get a new price");
        }

        Order completeOrder = new Order(totalPrice(quote.offer.price), quote, timeNow, userAuthToken);

        updateOrderLedger(completeOrder);

            return completeOrder;
        }
        catch (Exception ex) {
            return null;
        }
     }

    @Override
    public void updateOrderLedger(Order order) {
        this.fulfillmentService.placeOrder(order);
    }

    @Override
    public BigDecimal totalPrice(BigDecimal bottlePrice) {
        return bottlePrice.multiply(CASE_SIZE).add(STANDARD_PROCESSING_CHARGE);
    }

    private BigDecimal totalPrice(BigDecimal bottlePrice, int orderConfirmedTime) {

        BigDecimal casePrice = bottlePrice.multiply(CASE_SIZE);

        if (orderConfirmedTime > 2 * 60 * 10) {
            if (orderConfirmedTime > 10 * 60 * 10) {
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
}