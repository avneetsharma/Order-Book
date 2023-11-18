package com.aks.orderbook;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class MarketList
{
	private final Logger logger = LoggerFactory.getLogger(MarketList.class);
    private OrderBook market;
    private Map<String, OrderBook> orderBooks = null;

    public MarketList()
    {
        orderBooks = new HashMap<String, OrderBook>();
        market = new OrderBook("Test");
        orderBooks.put("Test", market);
    }

    public List<String> GetList()
    {
      List<String> list = new ArrayList<String>(orderBooks.keySet());  
      return list;
    }

    public void addItem(String name)
    {
      if (!orderBooks.containsKey(name)) {
          market = new OrderBook(name);
          orderBooks.put(name, market);
      }
    }

    public void addOrder(OrderItemDao order)
    {
    	logger.info("MarketList AddOrder name : {}", order.getName());
    	if (orderBooks.containsKey(order.getName())) {
    		OrderBook book = orderBooks.get(order.getName());
    		book.addOrder(order.getPrice(), order.getQty());
    	}
    }

    public void addOffer(OrderItemDao offer)
    {
    	logger.info("MarketList AddOffer name : {}", offer.getName());
    	if (orderBooks.containsKey(offer.getName())) {
    		OrderBook book = orderBooks.get(offer.getName());
    		book.addOffer(offer.getPrice(), offer.getQty());
    	}
    }

    public Map<Double, List<Order>> getOrderMap(OrderItemDao order)
    {
    	logger.info("MarketList GetOrderMap name : {}", order.getName());
    	
    	if (orderBooks.containsKey(order.getName())) {
    		OrderBook book = orderBooks.get(order.getName());
        	Map<Double, List<Order>> orderMap = book.getOderMap();
            return orderMap;
    	}
    	return null;
    }

    public Map<Double, List<Order>> getOfferMap(OrderItemDao offer)
    {
    	logger.info("MarketList GetOfferMap name : {}", offer.getName());

    	if (orderBooks.containsKey(offer.getName())) {
    		OrderBook book = orderBooks.get(offer.getName());
        	Map<Double, List<Order>> offerMap = book.getOfferMap();
            return offerMap;
    	}
    	return null;
    }

  }
