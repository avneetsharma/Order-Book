package com.aks.orderbook;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class MarketController
{
	private final Logger logger = LoggerFactory.getLogger(MarketController.class);
    private MarketList marketList;

    // Initializes marketplace
    public MarketController()
    {
        marketList = new MarketList();
        marketList.addItem("Test");
    }


    @PostMapping("/market/add/item/")
  	public String AddMarketItem(@RequestBody MarketItem marketItem) {
        marketList.addItem(marketItem.getName());
        return "success";
  	}

    @GetMapping("/market/list/get")
    @ResponseBody
  	public List<String>  GetMarketList() {
    	List<String> list = marketList.GetList();
        return list;
  	}

    @PostMapping(value = "/market/order/add",consumes="application/json")
  	public ResponseEntity<String> addMarketBid(@RequestBody OrderItemDao order) {
    	logger.info("MarketController order name : {}", order.getName());
    	marketList.addOrder(order);
  		return new ResponseEntity<String>("success: order added.", HttpStatus.OK);
  	}



    @PostMapping(value = "/market/offer/add",consumes="application/json")
  	public ResponseEntity<String> AddMarketOffer(@RequestBody OrderItemDao offer) {
    	logger.info("MarketController offer name : {}", offer.getName());
    	marketList.addOffer(offer);
  		return new ResponseEntity<String>("success: offer added.", HttpStatus.OK);
  	}

    @PostMapping(value = "/market/offer/get",consumes="application/json")
  	public ResponseEntity<Map<Double, List<Order>>> GetMarketOffer(@RequestBody OrderItemDao offer) {
    	logger.info("MarketController GetMarketOffer name : {}", offer.getName());
    	Map<Double, List<Order>> list = marketList.getOfferMap(offer);
    	return new ResponseEntity<Map<Double, List<Order>>>(list, HttpStatus.OK);
  	}


    @PostMapping(value = "/market/oder/get",consumes="application/json")
  	public ResponseEntity<Map<Double, List<Order>>> getMarketOrder(@RequestBody OrderItemDao order) {
    	logger.info("MarketController GetMarketOrder name : {}", order.getName());
    	Map<Double, List<Order>> list = marketList.getOrderMap(order);
    	return new ResponseEntity<Map<Double, List<Order>>>(list, HttpStatus.OK);
  	}
    
 

  }
