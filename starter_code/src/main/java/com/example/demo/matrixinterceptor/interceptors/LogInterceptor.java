/**
 * 
 */
package com.example.demo.matrixinterceptor.interceptors;

import javax.servlet.DispatcherType;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.example.demo.matrixinterceptor.interceptorservice.LoggingService;

/**
 * The Class LogInterceptor.
 */
@Component
public class LogInterceptor implements HandlerInterceptor {

    @Autowired
    LoggingService loggingService;

    private static final Logger log = Logger.getLogger("LogInterceptor.class");

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {

	if (DispatcherType.REQUEST.name().equals(request.getDispatcherType().name())
		&& request.getMethod().equals(HttpMethod.GET.name())) {
	    loggingService.logRequest(request, null);
	}
	request.setAttribute("startTime", System.currentTimeMillis());
	return true;
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o,
	    ModelAndView modelAndView) throws Exception {
	// empty method
    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse,
	    Object o, Exception e) throws Exception {

	long executeTime = System.currentTimeMillis() - (long) httpServletRequest.getAttribute("startTime");
	log.info((String) httpServletRequest.getAttribute("request") + "\n"
		+ (String) httpServletRequest.getAttribute("response") + "\n" + "RESPONSETIME: " + executeTime + "\n");

    }
}