package com.paragon;
import com.paragon.orders.OrderLedger;
import com.paragon.stock.Offer;
import java.io.*;
import java.math.BigDecimal;
import java.util.List;

public class Main {

    public static void main(String[] args) throws Exception {
        OrderService orderSystem = new OrderSystem();
        orderSystem.add(OrderLedger.getInstance());
        String userAuthToken = "tom@example.com";
        String searchString = "";
        int timeDelay = 0;
        DataInputStream dataInputStream = new DataInputStream(System.in);

        InputStreamReader inputStreamReader = new InputStreamReader(System.in);
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

        System.out.println("Enter a product to search for:");

        try {
            searchString = bufferedReader.readLine();
        }
        catch (IOException ex) {
            System.out.println("Exception detected in keyboard input");
        }

        System.out.println("Enter a delay in minutes:");

        try {
            timeDelay = Integer.parseInt(bufferedReader.readLine());
        }
        catch (NumberFormatException ex) {
            System.out.println("Not a number!");
        }

        catch (IOException ex) {
            System.out.println("Exception detected in keyboard input");
        }

        List<Offer> searchResults = orderSystem.searchForProduct(searchString);
        if (searchResults.isEmpty()) {
            System.out.println("No search results found");
        } else {
            Offer offer = searchResults.get(0);
            // some time may pass...
            Thread.sleep(timeDelay * 1000);
            if (priceAcceptable(offer.price)) {
                System.out.println(orderSystem.updateOrderLedger(orderSystem.confirmOrder(orderSystem.validQuote(offer.id), userAuthToken)));
            }
        }
    }
    private static boolean priceAcceptable(BigDecimal price) {
        return true;
    }
}
