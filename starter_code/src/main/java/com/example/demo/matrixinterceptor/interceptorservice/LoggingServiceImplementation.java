/**
 * 
 */
package com.example.demo.matrixinterceptor.interceptorservice;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Service;

/**
 * The Class LoggingServiceImplementation.
 *
 * @author Manmeet
 */
@Service
public class LoggingServiceImplementation implements LoggingService {

	@Override
	public void logRequest(HttpServletRequest httpServletRequest, Object body) {
		StringBuilder stringBuilder = new StringBuilder();
		Map<String, String> parameters = buildParametersMap(httpServletRequest);

		stringBuilder.append("REQUEST ");
		stringBuilder.append("path=[").append(httpServletRequest.getRequestURI()).append("] ");
		stringBuilder.append("method=[").append(httpServletRequest.getMethod()).append("] ");
		stringBuilder.append("headers=[").append(buildHeadersMap(httpServletRequest)).append("] ");

		if (!parameters.isEmpty()) {
			stringBuilder.append("parameters=[").append(parameters).append("] ");
		}

		if (body != null) {
			stringBuilder.append("body=[" + body + "]");
		}

		httpServletRequest.setAttribute("request", stringBuilder.toString());
	}

	@Override
	public void logResponse(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse,
			Object body) {
		StringBuilder stringBuilder = new StringBuilder();

		stringBuilder.append("RESPONSE ");
		stringBuilder.append("path=[").append(httpServletRequest.getRequestURI()).append("] ");
		stringBuilder.append("method=[").append(httpServletRequest.getMethod()).append("] ");
		stringBuilder.append("status=[ ").append(httpServletResponse.getStatus()).append(" ] ");
		
		if (body != null)
			stringBuilder.append("responseBody=[").append(body.toString()).append("] ");

		httpServletRequest.setAttribute("response", stringBuilder.toString());
	}

	private Map<String, String> buildParametersMap(HttpServletRequest httpServletRequest) {
		Map<String, String> resultMap = new HashMap<>();
		Enumeration<String> parameterNames = httpServletRequest.getParameterNames();

		while (parameterNames.hasMoreElements()) {
			String key = parameterNames.nextElement();
			String value = httpServletRequest.getParameter(key);
			resultMap.put(key, value);
		}

		return resultMap;
	}

	private Map<String, String> buildHeadersMap(HttpServletRequest request) {
		Map<String, String> map = new HashMap<>();

		Enumeration<String> headerNames = request.getHeaderNames();
		while (headerNames.hasMoreElements()) {
			String key = (String) headerNames.nextElement();
			String value = request.getHeader(key);
			map.put(key, value);
		}

		return map;
	}
}
