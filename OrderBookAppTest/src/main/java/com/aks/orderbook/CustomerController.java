package com.aks.orderbook;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CustomerController {

	private final Logger logger = LoggerFactory.getLogger(getClass());
    private MarketList marketList;

    public CustomerController()
    {
        marketList = new MarketList();
        marketList.addItem("Test");
    }

	
    @PostMapping(value = "/marketItem",consumes="application/json")
  	public String addMarketItem(@RequestBody MarketItem marketItem) {
        marketList.addItem(marketItem.getName());
        return "success";
  	}

    @GetMapping(value = "/marketItem", produces ="application/json")
    @ResponseBody
  	public List<String>  GetMarketList() {
    	List<String> list = marketList.GetList();
        return list;
  	}

    @PostMapping(value = "/order/add",consumes="application/json")
  	public ResponseEntity<String> addMarketOder(@RequestBody OrderItemDao order) {
    	logger.info("MarketController order name : {}", order.getName());
    	marketList.addOrder(order);
  		return new ResponseEntity<String>("success: order added.", HttpStatus.OK);
  	}

    @PostMapping(value = "/offer/add",consumes="application/json")
  	public ResponseEntity<String> addMarketOffer(@RequestBody OrderItemDao offer) {
    	logger.info("MarketController offer name : {}", offer.getName());
    	marketList.addOffer(offer);
  		return new ResponseEntity<String>("success: offer added.", HttpStatus.OK);
  	}

    @PostMapping(value = "/offer/get",consumes="application/json")
  	public ResponseEntity<Map<Double, List<Order>>> getMarketOffer(@RequestBody OrderItemDao offer) {
    	logger.info("MarketController GetMarketOffer name : {}", offer.getName());
    	Map<Double, List<Order>> list = marketList.getOfferMap(offer);
    	return new ResponseEntity<Map<Double, List<Order>>>(list, HttpStatus.OK);
  	}

    @PostMapping(value = "/order/get",consumes="application/json")
  	public ResponseEntity<Map<Double, List<Order>>> GetMarketBid(@RequestBody OrderItemDao order) {
    	logger.info("MarketController GetMarketBid name : {}", order.getName());
    	Map<Double, List<Order>> list = marketList.getOrderMap(order);
    	return new ResponseEntity<Map<Double, List<Order>>>(list, HttpStatus.OK);
  	}
	
}
