package com.paragon;
import com.paragon.stock.Offer;
import java.math.BigDecimal;
import java.util.List;

public class Main {

    public static void main(String[] args) throws Exception {
        OrderService orderSystem = new OrderSystem();
        String userAuthToken = "tom@example.com";
        List<Offer> searchResults = orderSystem.searchForProduct("Cabernet Sauvignon");
        if (searchResults.isEmpty()) {
            System.out.println("No search results found");
        } else {
            Offer offer = searchResults.get(0);
            // some time may pass...
            Thread.sleep(5 * 1000);
            if (priceAcceptable(offer.price)) {
                orderSystem.confirmOrder(offer.id, userAuthToken);
            }
        }
    }
    private static boolean priceAcceptable(BigDecimal price) {
        return true;
    }
}
