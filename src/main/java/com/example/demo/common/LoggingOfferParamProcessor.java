package com.example.demo.common;

import com.example.demo.model.Offer;
import com.example.demo.model.Param;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;

import java.util.List;
import java.util.stream.Collectors;

public class LoggingOfferParamProcessor implements ItemProcessor<Offer, List<Param>> {

	private static final Logger LOGGER = LoggerFactory.getLogger(LoggingOfferParamProcessor.class);

	@Override
	public List<Param> process(Offer item) throws Exception {
		LOGGER.info("Processing offer information: {}", item);
		return item.getParams().stream().peek(param -> param.setOfferId(item.getId())).collect(Collectors.toList());
	}
}