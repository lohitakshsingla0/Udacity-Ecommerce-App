/**
 * 
 */
package com.example.demo.jwtconfig;

import io.jsonwebtoken.Jwts;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;

/**
 * The Class AuthorizeFilter.
 *
 * @author Manmeet
 */
public class AuthorizeFilter extends BasicAuthenticationFilter {

	/**
	 * Instantiates a new authorize filter.
	 *
	 * @param authManager the auth manager
	 */
	public AuthorizeFilter(AuthenticationManager authManager) {
		super(authManager);
	}
	
	/**
	 * Do filter internal.
	 *
	 * @param req the req
	 * @param res the res
	 * @param chain the chain
	 * @throws IOException Signals that an I/O exception has occurred.
	 * @throws ServletException the servlet exception
	 */
	@Override
	protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain chain)
			throws IOException, ServletException {
		String header = req.getHeader(AuthProperties.HEADER_STRING);
		if (header == null || !header.startsWith(AuthProperties.TOKEN_PREFIX)) {
			chain.doFilter(req, res);
			return;
		}
		UsernamePasswordAuthenticationToken authentication = getAuthentication(req);
		SecurityContextHolder.getContext().setAuthentication(authentication);
		chain.doFilter(req, res);
	}

	/**
	 * Gets the authentication.
	 *
	 * @param request the request
	 * @return the authentication
	 */
	private UsernamePasswordAuthenticationToken getAuthentication(HttpServletRequest request) {
		String token = request.getHeader(AuthProperties.HEADER_STRING);
		if (token != null) {
			String user = Jwts.parser().setSigningKey(AuthProperties.SECRET)
					.parseClaimsJws(token.replace(AuthProperties.TOKEN_PREFIX, "")).getBody().getSubject();
			if (user != null) {
				return new UsernamePasswordAuthenticationToken(user, null, new ArrayList<>());
			}
			return null;
		}
		return null;
	}
}
