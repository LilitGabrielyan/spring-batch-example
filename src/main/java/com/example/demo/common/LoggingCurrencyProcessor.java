package com.example.demo.common;

import com.example.demo.model.Currency;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;

public class LoggingCurrencyProcessor implements ItemProcessor<Currency, Currency> {

	private static final Logger LOGGER = LoggerFactory.getLogger(LoggingCurrencyProcessor.class);

	@Override
	public Currency process(Currency item) throws Exception {
		LOGGER.info("Processing Currency information: {}", item);
		return item;
	}
}