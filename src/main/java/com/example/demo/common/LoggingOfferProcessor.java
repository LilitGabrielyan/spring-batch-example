package com.example.demo.common;

import com.example.demo.model.Category;
import com.example.demo.model.Offer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;

public class LoggingOfferProcessor implements ItemProcessor<Offer, Offer> {

	private static final Logger LOGGER = LoggerFactory.getLogger(LoggingOfferProcessor.class);

	@Override
	public Offer process(Offer item) throws Exception {
		LOGGER.info("Processing offer information: {}", item);
		return item;
	}
}