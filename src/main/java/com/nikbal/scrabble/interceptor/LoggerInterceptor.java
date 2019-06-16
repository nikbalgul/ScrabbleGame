package com.nikbal.scrabble.interceptor;

import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

public class LoggerInterceptor extends HandlerInterceptorAdapter {

	private static final Logger logger = Logger.getLogger(LoggerInterceptor.class);

	/**
	 * Executed before actual handler is executed
	 **/
	@Override
	public boolean preHandle(final HttpServletRequest request, final HttpServletResponse response, final Object handler)
			throws Exception {
		logger.info("[preHandle][" + request + "]" + "[" + request.getMethod() + "]" + request.getRequestURI()
				+ getParameters(request));
		return true;
	}

	/**
	 * Executed before after handler is executed
	 **/
	@Override
	public void postHandle(final HttpServletRequest request, final HttpServletResponse response, final Object handler,
			final ModelAndView modelAndView) throws Exception {
		logger.info("[postHandle][" + request + "]");
	}

	/**
	 * Executed after complete request is finished
	 **/
	@Override
	public void afterCompletion(final HttpServletRequest request, final HttpServletResponse response,
			final Object handler, final Exception ex) throws Exception {
		if (ex != null)
			logger.error(ex.getMessage());
		logger.info("[afterCompletion][" + request + "][exception: " + ex + "]");
	}

	private String getParameters(final HttpServletRequest request) {
		final StringBuilder posted = new StringBuilder();
		final Enumeration<?> e = request.getParameterNames();
		if (e != null)
			posted.append("?");
		while (e != null && e.hasMoreElements()) {
			if (posted.length() > 1)
				posted.append("&");
			final String curr = (String) e.nextElement();
			posted.append(curr).append("=");
			if (curr.contains("password") || curr.contains("answer") || curr.contains("pwd")) {
				posted.append("*****");
			} else {
				posted.append(request.getParameter(curr));
			}
		}

		final String ip = request.getHeader("X-FORWARDED-FOR");
		final String ipAddr = (ip == null) ? getRemoteAddr(request) : ip;
		if (!StringUtils.isEmpty(ipAddr))
			posted.append("&_psip=" + ipAddr);
		return posted.toString();
	}

	private String getRemoteAddr(final HttpServletRequest request) {
		final String ipFromHeader = request.getHeader("X-FORWARDED-FOR");
		if (ipFromHeader != null && ipFromHeader.length() > 0) {
			logger.debug("ip from proxy - X-FORWARDED-FOR : " + ipFromHeader);
			return ipFromHeader;
		}
		return request.getRemoteAddr();
	}
}