package com.example.demo;

import com.example.demo.model.Offer;
import org.springframework.batch.item.database.ItemPreparedStatementSetter;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class ModelPreparedStatementSetter implements ItemPreparedStatementSetter<Offer> {

	@Override
	public void setValues(Offer offer, PreparedStatement preparedStatement) throws SQLException {
		String groupId = offer.getId();
		if(offer.getGroup_id() == null) {
			offer.setGroup_id(groupId);
		}
		preparedStatement.setString(1, offer.getGroup_id());
		preparedStatement.setString(2, offer.getName());
		System.out.println("Saving model");
	}
}