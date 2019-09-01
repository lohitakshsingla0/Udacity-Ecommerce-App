/**
 * 
 */
package com.example.demo.jwtconfig;

/**
 * The Class AuthProperties.
 *
 * @author Lohitaksh
 */
public class AuthProperties {

	private AuthProperties() {

	}

	public static final String SECRET = "GenerateToken";
	
	public static final long EXPIRATION_TIME = 864_000_000; 
	
	public static final String TOKEN_PREFIX = "Bearer ";
	
	public static final String HEADER_STRING = "Authorization";

}
