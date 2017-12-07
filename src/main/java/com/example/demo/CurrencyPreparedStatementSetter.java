package com.example.demo;

import com.example.demo.model.Currency;
import org.springframework.batch.item.database.ItemPreparedStatementSetter;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class CurrencyPreparedStatementSetter implements ItemPreparedStatementSetter<Currency> {

	@Override
	public void setValues(Currency currency, PreparedStatement preparedStatement) throws SQLException {
		preparedStatement.setString(1, currency.getId());
		System.out.println("Saving currency");
	}

}