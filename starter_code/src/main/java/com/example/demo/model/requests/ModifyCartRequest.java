package com.example.demo.model.requests;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * The Class ModifyCartRequest.
 *
 * @author Manmeet
 */
public class ModifyCartRequest {
	
	/** The username. */
	@JsonProperty
	private String username;
	
	/** The item id. */
	@JsonProperty
	private long itemId;
	
	/** The quantity. */
	@JsonProperty
	private int quantity;

	/**
	 * Gets the username.
	 *
	 * @return the username
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * Sets the username.
	 *
	 * @param username the new username
	 */
	public void setUsername(String username) {
		this.username = username;
	}

	/**
	 * Gets the item id.
	 *
	 * @return the item id
	 */
	public long getItemId() {
		return itemId;
	}

	/**
	 * Sets the item id.
	 *
	 * @param itemId the new item id
	 */
	public void setItemId(long itemId) {
		this.itemId = itemId;
	}

	/**
	 * Gets the quantity.
	 *
	 * @return the quantity
	 */
	public int getQuantity() {
		return quantity;
	}

	/**
	 * Sets the quantity.
	 *
	 * @param quantity the new quantity
	 */
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
}
