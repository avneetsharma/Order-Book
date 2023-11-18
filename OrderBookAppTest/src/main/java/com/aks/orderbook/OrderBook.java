package com.aks.orderbook;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class OrderBook
{
	private final Logger logger = LoggerFactory.getLogger(OrderBook.class);
	
    private String itemName;
    // Maps use List as value to hold multiple values with same hash
    private Map<Double, List<Order>> oderMap = null;
    private Map<Double, List<Order>> offerMap = null;

    private Queue<Double> orderMaxPriceList = null;
    private Queue<Double> offerMinPriceList = null;

    // Initializes marketplace
    public OrderBook(String name)
    {
        oderMap = new HashMap<Double, List<Order>>();
        offerMap = new HashMap<Double, List<Order>>();

        orderMaxPriceList = new PriorityQueue<Double>(30, Collections.reverseOrder()); // top is maximum order price
        offerMinPriceList = new PriorityQueue<Double>();  // top is minimum offer price

        this.itemName = name;
    }

    public void setName(String name)
    {
      this.itemName = name;
    }

    /*  Adds Order to map by hashing the price, then
     *  adding order to list located in that hash bucket
     */
    public void addOrder(double price, int quantity)
    {
    	logger.info("addOrder qty : {}", quantity);
    	logger.info("addOrder price : {}", price);
        List<Order> bucket = getBucket(oderMap, price);
        Order newOrder = new Order(price, quantity);
        bucket.add(newOrder);
        oderMap.put(newOrder.getPrice(), bucket);
        orderMaxPriceList.add(price);
        matchOrders();
    }

    /*  Adds offer to map by hashing the price, then
     *  adding offer to list located in that hash bucket
     */
    public void addOffer(double price, int quantity)
    {
        List<Order> bucket = getBucket(offerMap, price);
        Order newOffer = new Order(price, quantity);
    	logger.info("addOffer qty : {}", newOffer.getQuantity());
        bucket.add(newOffer);
    	logger.info("offerMap after insert size : {}", bucket.size());
        offerMap.put(newOffer.getPrice(), bucket);
        offerMinPriceList.add(price);
        matchOrders();
    }

    // Returns bucket list if price match, otherwise returns new list
    public List<Order> getBucket(Map<Double, List<Order>> hashmap, Double price)
    {
        List<Order> bucket;
        if(hashmap.containsKey(price))
        {
            bucket = hashmap.get(price);
        }
        else
        {
            bucket = new LinkedList<Order>();
        }
        return bucket;
    }

    // Matches offers and order based on pricetime priority
    public void matchOrders()
    {
        List<Order> orderBucket = null;
        List<Order> offerBucket = null;
        Double lowestOffer = null;
        Double highestOrder = null;
        boolean finished = false;

        while(!finished)
        {
            // Peek because we don't want to remove the top element until the order is closed
            highestOrder = orderMaxPriceList.peek();
            lowestOffer = offerMinPriceList.peek();

            // No possible trade if either list is empty or no order higher than an offer
            if(lowestOffer == null || highestOrder == null || lowestOffer > highestOrder)
            {
                finished = true;
            	logger.info("OrderBook matchOrders finished = true");
            }
            else
            {
                // Gets buckets for both maps
                orderBucket = oderMap.get(orderMaxPriceList.peek());
                offerBucket = offerMap.get(offerMinPriceList.peek());

                // Gets first element from each bucket since they're the oldest
                int orderQuantity = orderBucket.get(0).getQuantity();
                int offerQuantity = offerBucket.get(0).getQuantity();

                if(orderQuantity > offerQuantity)
                {
                	logger.info("orderQuantity > offerQuantity");
                    System.out.println(successfulTrade(offerQuantity, lowestOffer));

                    // Decrements quantity in order
                    orderQuantity -= offerQuantity;
                    orderBucket.get(0).setQuantity(orderQuantity);
                	logger.info("OorderQuantity remaining qty : {}", orderQuantity);

                    // Closes previous offer
                    offerBucket.remove(0);
                    offerMinPriceList.remove();
                }
                else if(offerQuantity > orderQuantity)
                {
                	logger.info("orderQuantity < offerQuantity");
                    System.out.println(successfulTrade(orderQuantity, lowestOffer));

                    // Decrements quantity in offer
                    offerQuantity -= orderQuantity;
                    offerBucket.get(0).setQuantity(offerQuantity);
                	logger.info("offerQuantity remaining qty : {}", offerQuantity);

                    //  Closes previous order
                    orderBucket.remove(0);
                    orderMaxPriceList.remove();
                }
                else
                {
                    // orderQuantity is an arbitrary choice because both quantities are equal.
                    // lowestOffer is chosen because it's the price at which the trade is made.
                    System.out.println(successfulTrade(orderQuantity, lowestOffer));

                    // Removes order and offer because they're both closed
                    orderBucket.remove(0);
                    orderMaxPriceList.remove();
                    offerBucket.remove(0);
                    offerMinPriceList.remove();
                }
            }
        }
    }

    // Returns the string printed for a successful trade.
    public String successfulTrade(int quantity, double price)
    {
    	logger.info("successfulTrade orderQuantity : {}", quantity);
    	logger.info("successfulTrade lowestOffer : {}", price);
        return quantity + " shares traded for $" + price + " per share.";
    }

    // Prints the remaining trades from input map after close of market.
    private void printFailedTrades(Map<Double, List<Order>> hashmap, String type)
    {
        for (Double key : hashmap.keySet())
        {
            List<Order> bucket = hashmap.get(key);

            for(Order order : bucket)
            {
                System.out.println(type + order.getQuantity() + " shares for $" + order.getPrice() + " per share failed to trade.");
            }
        }
    }

    // Signifies that the market is open.
    public void openMarket()
    {
        System.out.println("Market opens.");
    }

    public void closeMarket()
    {
        System.out.println("Market closes.");
        printFailedTrades(oderMap, "order for ");
        printFailedTrades(offerMap, "Offer of ");
    }

    public Map<Double, List<Order>> getOderMap()
    {
        return oderMap;
    }

    public Map<Double, List<Order>> getOfferMap()
    {
        return offerMap;
    }
}
