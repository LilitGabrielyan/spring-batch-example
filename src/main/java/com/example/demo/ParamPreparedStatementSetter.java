package com.example.demo;

import com.example.demo.model.Param;
import org.springframework.batch.item.database.ItemPreparedStatementSetter;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class ParamPreparedStatementSetter implements ItemPreparedStatementSetter<Param> {

	@Override
	public void setValues(Param param, PreparedStatement preparedStatement) throws SQLException {
		preparedStatement.setString(1, param.getName());
		preparedStatement.setString(2, param.getOfferId());
		preparedStatement.setString(3, param.getValue());
		System.out.println("Saving params");
	}
}