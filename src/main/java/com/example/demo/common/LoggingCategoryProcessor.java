package com.example.demo.common;

import com.example.demo.model.Category;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;

public class LoggingCategoryProcessor implements ItemProcessor<Category, Category> {

	private static final Logger LOGGER = LoggerFactory.getLogger(LoggingCategoryProcessor.class);

	@Override
	public Category process(Category item) throws Exception {
		LOGGER.info("Processing Category information: {}", item);
		return item;
	}
}