package com.example.demo;

import com.example.demo.model.Param;
import com.example.demo.model.Picture;
import org.springframework.batch.item.database.ItemPreparedStatementSetter;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class PicturePreparedStatementSetter implements ItemPreparedStatementSetter<Picture> {

	@Override
	public void setValues(Picture param, PreparedStatement preparedStatement) throws SQLException {
		preparedStatement.setString(2, param.getName());
		preparedStatement.setString(1, param.getOfferId());
		System.out.println("Saving pictures");
	}
}