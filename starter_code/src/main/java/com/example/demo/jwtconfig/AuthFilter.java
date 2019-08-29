/**
 * 
 */
package com.example.demo.jwtconfig;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.example.demo.model.requests.CreateUserRequest;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

/**
 * The Class AuthFilter.
 *
 * @author Manmeet
 */
public class AuthFilter extends UsernamePasswordAuthenticationFilter {

	/** The authentication manager. */
	private AuthenticationManager authenticationManager;

	/**
	 * Instantiates a new auth filter.
	 *
	 * @param authenticationManager the authentication manager
	 */
	public AuthFilter(AuthenticationManager authenticationManager) {
		this.authenticationManager = authenticationManager;
	}

	/**
	 * Attempt authentication.
	 *
	 * @param req the req
	 * @param res the res
	 * @return the authentication
	 * @throws AuthenticationException the authentication exception
	 */
	@Override
	public Authentication attemptAuthentication(HttpServletRequest req, HttpServletResponse res)
			throws AuthenticationException {
		try {
			CreateUserRequest creds = new ObjectMapper().readValue(req.getInputStream(), CreateUserRequest.class);
			return authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(creds.getUsername(),
					creds.getPassword(), new ArrayList<>()));
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * Successful authentication.
	 *
	 * @param req the req
	 * @param res the res
	 * @param chain the chain
	 * @param auth the auth
	 * @throws IOException Signals that an I/O exception has occurred.
	 * @throws ServletException the servlet exception
	 */
	@Override
	protected void successfulAuthentication(HttpServletRequest req, HttpServletResponse res, FilterChain chain,
			Authentication auth) throws IOException, ServletException {
		String token = Jwts.builder().setSubject(((User) auth.getPrincipal()).getUsername())
				.setExpiration(new Date(System.currentTimeMillis() + AuthProperties.EXPIRATION_TIME))
				.signWith(SignatureAlgorithm.HS512, AuthProperties.SECRET).compact();
		res.addHeader(AuthProperties.HEADER_STRING, AuthProperties.TOKEN_PREFIX + token);
	}
}
