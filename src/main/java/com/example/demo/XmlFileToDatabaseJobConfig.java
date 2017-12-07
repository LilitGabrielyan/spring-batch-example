package com.example.demo;

import com.example.demo.common.*;
import com.example.demo.helper.ListUnpackingItemWriter;
import com.example.demo.model.*;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.ItemPreparedStatementSetter;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.support.CompositeItemWriter;
import org.springframework.batch.item.xml.StaxEventItemReader;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ClassPathResource;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;

/**
 * Defines readers and writers for types in xml.
 */
@Configuration
public class XmlFileToDatabaseJobConfig {
	private static final String PROPERTY_XML_SOURCE_FILE_PATH = "xml.to.database.job.source.file.path";

	@Bean
	public ItemReader<Currency> itemCurrencyReader(Environment environment) {
		StaxEventItemReader<Currency> xmlFileReader = new StaxEventItemReader<>();
		xmlFileReader.setResource(new ClassPathResource(environment.getRequiredProperty(PROPERTY_XML_SOURCE_FILE_PATH)));
		xmlFileReader.setFragmentRootElementNames(new String[]{"currency"});

		Jaxb2Marshaller shopMarshaller = new Jaxb2Marshaller();
		shopMarshaller.setClassesToBeBound(Currency.class);
		xmlFileReader.setUnmarshaller(shopMarshaller);

		return xmlFileReader;
	}

	@Bean
	public ItemReader<Category> itemCategoryReader(Environment environment) {
		StaxEventItemReader<Category> xmlFileReader = new StaxEventItemReader<>();
		xmlFileReader.setResource(new ClassPathResource(environment.getRequiredProperty(PROPERTY_XML_SOURCE_FILE_PATH)));
		xmlFileReader.setFragmentRootElementNames(new String[]{"category"});

		Jaxb2Marshaller shopMarshaller = new Jaxb2Marshaller();
		shopMarshaller.setClassesToBeBound(Category.class);
		xmlFileReader.setUnmarshaller(shopMarshaller);

		return xmlFileReader;
	}

	@Bean
	public ItemReader<Offer> itemOfferReader(Environment environment) {
		StaxEventItemReader<Offer> xmlFileReader = new StaxEventItemReader<>();
		xmlFileReader.setResource(new ClassPathResource(environment.getRequiredProperty(PROPERTY_XML_SOURCE_FILE_PATH)));
		xmlFileReader.setFragmentRootElementNames(new String[]{"offer"});

		Jaxb2Marshaller shopMarshaller = new Jaxb2Marshaller();
		shopMarshaller.setClassesToBeBound(Offer.class);
		xmlFileReader.setUnmarshaller(shopMarshaller);

		return xmlFileReader;
	}


	@Bean
	ItemProcessor<Currency, Currency> xmlFileCurrencyItemProcessor() {
		return new LoggingCurrencyProcessor();
	}

	@Bean
	ItemProcessor<Category, Category> xmlFileCategoryItemProcessor() {
		return new LoggingCategoryProcessor();
	}

	@Bean
	ItemProcessor<Offer, Offer> xmlFileOfferItemProcessor() {
		return new LoggingOfferProcessor();
	}


	@Bean
	ItemProcessor<Offer, List<Param>> xmlFileOfferParamItemProcessor() {
		return new LoggingOfferParamProcessor();
	}

	@Bean
	ItemProcessor<Offer, List<Picture>> xmlFileOfferPictureItemProcessor() {
		return new LoggingOfferPictureProcessor();
	}


	@Bean
	@StepScope
	public JdbcBatchItemWriter<Category> jdbcCategoryWriter(DataSource dataSource) {
		JdbcBatchItemWriter<Category> jdbcBatchItemWriter = new JdbcBatchItemWriter<>();
		jdbcBatchItemWriter.setAssertUpdates(true);
		jdbcBatchItemWriter.setDataSource(dataSource);
		jdbcBatchItemWriter.setSql("MERGE INTO Category KEY(id) VALUES(?,?)");
		ItemPreparedStatementSetter<Category> studentPreparedStatementSetter = new CategoryPreparedStatementSetter();
		jdbcBatchItemWriter.setItemPreparedStatementSetter(studentPreparedStatementSetter);
		return jdbcBatchItemWriter;
	}

	@Bean
	@StepScope
	public JdbcBatchItemWriter<Currency> jdbcCurrencyWriter(DataSource dataSource) {
		JdbcBatchItemWriter<Currency> jdbcBatchItemWriter = new JdbcBatchItemWriter<>();
		jdbcBatchItemWriter.setDataSource(dataSource);
		jdbcBatchItemWriter.setSql("MERGE INTO Currency KEY(id) VALUES(?)");

		ItemPreparedStatementSetter<Currency> studentPreparedStatementSetter = new CurrencyPreparedStatementSetter();
		jdbcBatchItemWriter.setItemPreparedStatementSetter(studentPreparedStatementSetter);
		return jdbcBatchItemWriter;
	}

	@Bean
	@StepScope
	public JdbcBatchItemWriter<Offer> jdbcOfferWriter(DataSource dataSource) {
		JdbcBatchItemWriter<Offer> jdbcBatchItemWriter = new JdbcBatchItemWriter<>();
		jdbcBatchItemWriter.setDataSource(dataSource);
		jdbcBatchItemWriter.setSql("MERGE INTO Offer KEY(id) VALUES(?,?,?,?,?,?,?,?,?,?)");

		ItemPreparedStatementSetter<Offer> studentPreparedStatementSetter = new OfferPreparedStatementSetter();
		jdbcBatchItemWriter.setItemPreparedStatementSetter(studentPreparedStatementSetter);
		return jdbcBatchItemWriter;
	}

	@Bean
	@StepScope
	public JdbcBatchItemWriter<Offer> jdbcModelWriter(DataSource dataSource) {
		JdbcBatchItemWriter<Offer> jdbcBatchItemWriter = new JdbcBatchItemWriter<>();
		jdbcBatchItemWriter.setDataSource(dataSource);
		jdbcBatchItemWriter.setSql("MERGE INTO Model KEY(id) VALUES(?,?)");

		ItemPreparedStatementSetter<Offer> studentPreparedStatementSetter = new ModelPreparedStatementSetter();
		jdbcBatchItemWriter.setItemPreparedStatementSetter(studentPreparedStatementSetter);
		return jdbcBatchItemWriter;
	}

	@Bean
	@StepScope
	public ListUnpackingItemWriter<Param> jdbcParamWriter(DataSource dataSource) {
		ListUnpackingItemWriter<Param> writer = new ListUnpackingItemWriter<>();
		JdbcBatchItemWriter<Param> jdbcBatchItemWriter = new JdbcBatchItemWriter<>();
		jdbcBatchItemWriter.setDataSource(dataSource);
		jdbcBatchItemWriter.setSql("MERGE INTO Param KEY(id, offerId) VALUES(?,?,?)");

		ItemPreparedStatementSetter<Param> studentPreparedStatementSetter = new ParamPreparedStatementSetter();
		jdbcBatchItemWriter.setItemPreparedStatementSetter(studentPreparedStatementSetter);
		writer.setDelegate(jdbcBatchItemWriter);
		return writer;
	}

	@Bean
	@StepScope
	public ListUnpackingItemWriter<Picture> jdbcPictureWriter(DataSource dataSource) {
		ListUnpackingItemWriter<Picture> writer = new ListUnpackingItemWriter<>();
		JdbcBatchItemWriter<Picture> jdbcBatchItemWriter = new JdbcBatchItemWriter<>();
		jdbcBatchItemWriter.setDataSource(dataSource);
		jdbcBatchItemWriter.setSql("MERGE INTO Picture KEY(offerId, name) VALUES(?,?)");

		ItemPreparedStatementSetter<Picture> studentPreparedStatementSetter = new PicturePreparedStatementSetter();
		jdbcBatchItemWriter.setItemPreparedStatementSetter(studentPreparedStatementSetter);
		writer.setDelegate(jdbcBatchItemWriter);
		return writer;
	}

	@Bean
	public CompositeItemWriter<Offer> compositeItemOfferWriter( @Qualifier("jdbcModelWriter") JdbcBatchItemWriter<Offer> jdbcModelWriter,
	                                                            @Qualifier("jdbcOfferWriter") JdbcBatchItemWriter<Offer> jdbcOfferWriter ){
		CompositeItemWriter<Offer> writer = new CompositeItemWriter<>();
		List<ItemWriter<? super Offer>> writerList = new ArrayList<>();
		writerList.add(jdbcOfferWriter);
		writerList.add(jdbcModelWriter);
		writer.setDelegates(writerList);
		return writer;
	}

	@Bean
	Step xmlFileCurrencyToDatabaseStep(ItemReader<Currency> xmlFileItemReader,
	                                   ItemProcessor<Currency, Currency> xmlFileItemProcessor,
	                                   ItemWriter<Currency> xmlFileDatabaseItemWriter,
	                                   StepBuilderFactory stepBuilderFactory) {
		return stepBuilderFactory.get("xmlFileCurrencyToDatabaseStep")
				.<Currency, Currency>chunk(1)
				.reader(xmlFileItemReader)
				.processor(xmlFileItemProcessor)
				.writer(xmlFileDatabaseItemWriter)
				.build();
	}

	@Bean
	Step xmlFileCategoryToDatabaseStep(ItemReader<Category> xmlFileItemReader,
	                                   ItemProcessor<Category, Category> xmlFileItemProcessor,
	                                   ItemWriter<Category> xmlFileDatabaseItemWriter,
	                                   StepBuilderFactory stepBuilderFactory) {
		return stepBuilderFactory.get("xmlFileCategoryToDatabaseStep")
				.<Category, Category>chunk(1)
				.reader(xmlFileItemReader)
				.processor(xmlFileItemProcessor)
				.writer(xmlFileDatabaseItemWriter)
				.build();
	}

	@Bean
	Step xmlFileOfferToDatabaseStep(ItemReader<Offer> xmlFileItemReader,
	                                ItemProcessor<Offer, Offer> xmlFileItemProcessor,
	                                CompositeItemWriter<Offer> compositeItemOfferWriter,
	                                StepBuilderFactory stepBuilderFactory) {
		return stepBuilderFactory.get("xmlFileOfferToDatabaseStep")
				.<Offer, Offer>chunk(1)
				.reader(xmlFileItemReader)
				.processor(xmlFileItemProcessor)
				.writer(compositeItemOfferWriter)
				.build();
	}

	@Bean
	Step xmlFileOfferParamToDatabaseStep(ItemReader<Offer> xmlFileItemReader,
	                                ItemProcessor<Offer, List<Param>> xmlFileItemProcessor,
	                                ItemWriter<List<Param>> listItemWriter,
	                                StepBuilderFactory stepBuilderFactory) {
		return stepBuilderFactory.get("xmlFileOfferParamToDatabaseStep")
				.<Offer, List<Param>>chunk(1)
				.reader(xmlFileItemReader)
				.processor(xmlFileItemProcessor)
				.writer(listItemWriter)
				.build();
	}

	@Bean
	Step xmlFileOfferPictureToDatabaseStep(ItemReader<Offer> xmlFileItemReader,
	                                     ItemProcessor<Offer, List<Picture>> xmlFileItemProcessor,
	                                     ItemWriter<List<Picture>> listItemWriter,
	                                     StepBuilderFactory stepBuilderFactory) {
		return stepBuilderFactory.get("xmlFileOfferPictureToDatabaseStep")
				.<Offer, List<Picture>>chunk(1)
				.reader(xmlFileItemReader)
				.processor(xmlFileItemProcessor)
				.writer(listItemWriter)
				.build();
	}

	@Bean
	Job xmlFileToDatabaseJob(JobBuilderFactory jobBuilderFactory,
	                         @Qualifier("xmlFileCurrencyToDatabaseStep") Step xmlCurrencyStep,
	                         @Qualifier("xmlFileCategoryToDatabaseStep") Step xmlCategoryStep,
	                         @Qualifier("xmlFileOfferToDatabaseStep") Step xmlOfferStep,
	                         @Qualifier("xmlFileOfferParamToDatabaseStep") Step xmlOfferParamStep,
	                         @Qualifier("xmlFileOfferPictureToDatabaseStep") Step xmlOfferPictureStep
	) {
		return jobBuilderFactory.get("xmlFileToDatabaseJob")
				.incrementer(new RunIdIncrementer())
				.flow(xmlCurrencyStep)
				.next(xmlCategoryStep)
				.next(xmlOfferStep)
				.next(xmlOfferParamStep)
				.next(xmlOfferPictureStep)
				.end()
				.build();
	}
}
