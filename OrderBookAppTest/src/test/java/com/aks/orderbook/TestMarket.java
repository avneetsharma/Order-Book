package com.aks.orderbook;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import com.aks.orderbook.OrderBook;

public class TestMarket
{
    private OrderBook market;

    @Before
    public void initMarket()
    {
        market = new OrderBook("Test");
    }

    /*******************************************
     *
     *   addBid Tests
     *
     *******************************************/

    @Test
    public void addNewBidShouldCorrectlyAddNewBid()
    {
        initMarket();
        assertTrue(market.getOderMap().isEmpty());
        market.addOrder(12.0, 1);
        assertTrue(market.getOderMap().containsKey(12.0));
    }

    @Test
    public void addDuplicateBidPriceShouldCorrectlyAddNewBid()
    {
        initMarket();
        assertTrue(market.getOderMap().isEmpty());
        market.addOrder(12.0, 1);
        market.addOrder(12.0, 2);

        // Checks to see if corresponding elements have same quantities.
        assertEquals(1, market.getOderMap().get(12.0).get(0).getQuantity());
        assertEquals(2, market.getOderMap().get(12.0).get(1).getQuantity());
    }

    /*******************************************
     *
     *   addOffer Tests
     *
     *******************************************/

    @Test
    public void addNewOfferShouldCorrectlyAddNewOffer()
    {
        initMarket();
        assertTrue(market.getOfferMap().isEmpty());
        market.addOffer(12.0, 1);
        assertTrue(market.getOfferMap().containsKey(12.0));
    }

    @Test
    public void addDuplicateOfferPriceShouldCorrectlyAddNewOffer()
    {
        initMarket();
        assertTrue(market.getOfferMap().isEmpty());
        market.addOffer(12.0, 1);
        market.addOffer(12.0, 2);

        // Checks to see if corresponding elements have same quantities.
        assertEquals(1, market.getOfferMap().get(12.0).get(0).getQuantity());
        assertEquals(2, market.getOfferMap().get(12.0).get(1).getQuantity());
    }

    /*******************************************
     *
     *   getBucket Test
     *
     *******************************************/

    @Test
    public void getBucketShouldReturnCorrectList()
    {
        initMarket();
        assertTrue(market.getOfferMap().isEmpty());
        market.addOffer(12.0, 1);
        market.addOffer(12.0, 2);

        // Checks to see if corresponding bucket elements have same quantities.
        assertEquals(1, market.getBucket(market.getOfferMap(), 12.0).get(0).getQuantity());
        assertEquals(2, market.getBucket(market.getOfferMap(), 12.0).get(1).getQuantity());
    }

    /*******************************************
     *
     *   matchOrders Tests
     *
     *******************************************/

    @Test
    public void BidQuantityShouldCorrectlyDecrementWhenGreaterThanOfferQuantity()
    {
        initMarket();
        market.addOffer(12.0, 6);
        market.addOrder(12.0, 9);
        market.matchOrders();
        assertEquals(3, market.getOderMap().get(12.0).get(0).getQuantity());  // Bid correctly decremented
        assertTrue(market.getOfferMap().get(12.0).isEmpty());  // Offer correctly closed
    }

    @Test
    public void OfferQuantityShouldCorrectlyDecrementWhenGreaterThanBidQuantity()
    {
        initMarket();
        market.addOrder(12.0, 5);
        market.addOffer(12.0, 10);
        market.matchOrders();
        assertEquals(5, market.getOfferMap().get(12.0).get(0).getQuantity());  // Offer correctly decremented
        assertTrue(market.getOderMap().get(12.0).isEmpty());  // Bid correctly closed
    }

    @Test
    public void BothQuantitiesEqualShouldCorrectlyRemoveBoth()
    {
        initMarket();
        market.addOrder(12.0, 5);
        market.addOffer(12.0, 5);
        market.matchOrders();
        assertTrue(market.getOderMap().get(12.0).isEmpty());   // Bid correctly closed
        assertTrue(market.getOfferMap().get(12.0).isEmpty()); // Offer correctly closed
    }

    @Test
    public void BidWithValueAndNoOffersShouldStayTheSame()
    {
        initMarket();
        market.addOrder(12.0, 5);
        market.matchOrders();
        assertEquals(5, market.getOderMap().get(12.0).get(0).getQuantity());   // Bid still has same value
        assertTrue(market.getOfferMap().get(12.0) == null); // Offer still null
    }

    @Test
    public void OfferWithValueAndNoBidsShouldStayTheSame()
    {
        initMarket();
        market.addOffer(12.0, 5);
        market.matchOrders();
        assertEquals(5, market.getOfferMap().get(12.0).get(0).getQuantity());   // Offer still has same value
        assertTrue(market.getOderMap().get(12.0) == null); // Bid still null
    }

    @Test
    public void OfferPriceHigherThanBidPriceShouldStayTheSame()
    {
        initMarket();
        market.addOffer(12.0, 7);
        market.addOrder(6.0, 5);
        market.matchOrders();
        assertEquals(7, market.getOfferMap().get(12.0).get(0).getQuantity());   // Offer still has same value
        assertEquals(5, market.getOderMap().get(6.0).get(0).getQuantity());   // Bid still has same value
    }
}
