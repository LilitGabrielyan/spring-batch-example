package com.example.demo.common;

import com.example.demo.model.Offer;
import com.example.demo.model.Picture;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;

import java.util.List;
import java.util.stream.Collectors;

public class LoggingOfferPictureProcessor implements ItemProcessor<Offer, List<Picture>> {

	private static final Logger LOGGER = LoggerFactory.getLogger(LoggingOfferPictureProcessor.class);

	@Override
	public List<Picture> process(Offer item) throws Exception {
		LOGGER.info("Processing offer information: {}", item);
		return item.getPicture().stream().map(picture -> new Picture(picture, item.getId())).collect(Collectors.toList());
	}
}