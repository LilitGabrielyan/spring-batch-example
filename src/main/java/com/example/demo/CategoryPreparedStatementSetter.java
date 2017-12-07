package com.example.demo;

import com.example.demo.model.Category;
import org.springframework.batch.item.database.ItemPreparedStatementSetter;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class CategoryPreparedStatementSetter implements ItemPreparedStatementSetter<Category> {

	@Override
	public void setValues(Category category, PreparedStatement preparedStatement) throws SQLException {
		preparedStatement.setString(1, category.getId());
		preparedStatement.setString(2, category.getDescription());
		System.out.println("Saving category");
	}
}