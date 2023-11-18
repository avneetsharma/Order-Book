package com.aks.orderbook;

import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.aks.orderbook.OrderBook;

@RunWith(SpringRunner.class)
@SpringBootTest
public class OrderbookApplicationTests {

	private OrderBook market;

	@Before
	public void initMarket()
	{
			market = new OrderBook("Test");
	}

	@Test
	public void contextLoads() {
	}

	@Test
	public void addNewBidShouldCorrectlyAddNewBid2()
	{
			initMarket();
			assertTrue(market.getOderMap().isEmpty());
			market.addOrder(12.0, 1);
			assertTrue(market.getOderMap().containsKey(12.0));
	}

}
