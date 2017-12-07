package com.example.demo;

import org.junit.After;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.JobOperator;
import org.springframework.batch.core.launch.JobParametersNotFoundException;
import org.springframework.batch.core.launch.NoSuchJobException;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

@RunWith(SpringRunner.class)
@SpringBootTest
public class DemoApplicationTests {

	@Autowired
	private DataSource dataSource;

	@Autowired
	JobLauncher jobLauncher;

	@Autowired
	Job job;


	@Test
	public void contextLoads() throws SQLException, JobParametersInvalidException, JobExecutionAlreadyRunningException, JobRestartException, JobInstanceAlreadyCompleteException, NoSuchJobException, JobParametersNotFoundException {

		//check that all currencies, categories and offers are saved

		Connection connection = dataSource.getConnection();
		Statement statement = connection.createStatement();
		ResultSet resultSet = statement.executeQuery("Select count(*) from Currency");
		resultSet.next();
		int currencyCount = resultSet.getInt(1);
		Assert.assertTrue("Must be 3 currencies", currencyCount == 3);

		resultSet = statement.executeQuery("Select count(*) from Category");
		resultSet.next();
		int categoryCount = resultSet.getInt(1);
		Assert.assertTrue("Must be 8 categories", categoryCount == 8);

		resultSet = statement.executeQuery("Select count(*) from Offer");
		resultSet.next();
		int offerCount = resultSet.getInt(1);
		Assert.assertTrue("Must be 6 offers", offerCount == 6);

		resultSet = statement.executeQuery("Select count(*) from Model");
		resultSet.next();
		int modelCount = resultSet.getInt(1);
		Assert.assertTrue("Must be 5 models", modelCount == 5);

		resultSet = statement.executeQuery("Select count(*) from Param");
		resultSet.next();
		int paramCount = resultSet.getInt(1);
		Assert.assertTrue("Must be 35 params", paramCount == 35);

		resultSet = statement.executeQuery("Select count(*) from Picture");
		resultSet.next();
		int pictureCount = resultSet.getInt(1);
		Assert.assertTrue("Must be 13 pictures", pictureCount == 13);

		jobLauncher.run(job, new JobParameters());
		resultSet = statement.executeQuery("Select count(*) from Offer");
		resultSet.next();
		offerCount = resultSet.getInt(1);
		Assert.assertTrue("Must be 6 offers after running job twice.", offerCount == 6);

		resultSet = statement.executeQuery("Select count(*) from Model");
		resultSet.next();
		modelCount = resultSet.getInt(1);
		Assert.assertTrue("Must be 5 models after running job twice.", modelCount == 5);
	}


}
