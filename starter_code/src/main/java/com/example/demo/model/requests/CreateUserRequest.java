package com.example.demo.model.requests;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * The Class CreateUserRequest.
 *
 * @author Manmeet
 */
public class CreateUserRequest {

	/** The username. */
	@JsonProperty
	private String username;

	/** The password. */
	@JsonProperty
	private String password;

	/** The confirm password. */
	@JsonProperty
	private String confirmPassword;

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
	 * Gets the password.
	 *
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * Sets the password.
	 *
	 * @param password the new password
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * Gets the confirm password.
	 *
	 * @return the confirmPassword
	 */
	public String getConfirmPassword() {
		return confirmPassword;
	}

	/**
	 * Sets the confirm password.
	 *
	 * @param confirmPassword the confirmPassword to set
	 */
	public void setConfirmPassword(String confirmPassword) {
		this.confirmPassword = confirmPassword;
	}
}