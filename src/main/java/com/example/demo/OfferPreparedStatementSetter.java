package com.example.demo;

import com.example.demo.model.Offer;
import org.springframework.batch.item.database.ItemPreparedStatementSetter;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class OfferPreparedStatementSetter implements ItemPreparedStatementSetter<Offer> {

	@Override
	public void setValues(Offer offer, PreparedStatement preparedStatement) throws SQLException {
		String groupId = offer.getId();
		if (offer.getGroup_id() == null) {
			offer.setGroup_id(groupId);
		}
		preparedStatement.setString(1, offer.getId());
		preparedStatement.setBoolean(2, offer.isAvailable());
		preparedStatement.setString(3, offer.getGroup_id());
		preparedStatement.setInt(4, offer.getPrice() == null ? -1 : offer.getPrice());
		preparedStatement.setString(5, offer.getCurrencyId());
		preparedStatement.setString(6, offer.getCategoryId());
		preparedStatement.setInt(7, offer.getStock() == null ? -1 : offer.getStock());
		preparedStatement.setString(8, offer.getDescription());
		preparedStatement.setString(9, offer.getVendor());
		preparedStatement.setString(10, offer.getVendorCode());
		System.out.println("Saving offer");
	}
}