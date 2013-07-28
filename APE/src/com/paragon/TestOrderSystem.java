package com.paragon;
import com.paragon.stock.Offer;
import com.paragon.stock.Warehouse;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.Test;
import org.junit.runner.RunWith;
import java.lang.String;

import java.util.ArrayList;
import java.util.List;

@RunWith(JMock.class)
public class TestOrderSystem {

    final String searchString = "C";
    Mockery context = new JUnit4Mockery();

    OrderService orderService = context.mock(OrderService.class);
    OrderSystem orderSystem = new OrderSystem();
    List<Offer> offers = new ArrayList<Offer>();


    @Test
    public void testSearchForProduct() {

        context.checking(new Expectations() {{
            atLeast(1).of(orderService).searchForProduct(searchString);
  //          oneOf(inventory).searchFor(searchString);
        }});

        List<Offer> offers = orderSystem.searchForProduct(searchString);
    }

}
