package com.netcracker.config;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.web.filter.OncePerRequestFilter;

public class FiltersConfiguration {

	@Value("${javaMelodyPort:${server.port}}")
	private Integer javaMelodyPort;

	@Value("${javaMelodyPortOnly:true}")
	private Boolean javaMelodyPortOnly;

	@Value("${authTokenPort:${server.port}}")
	private Integer authTokenPort;

	@Value("${authTokenPortOnly:true}")
	private Boolean authTokenPortOnly;

	@Bean(name = "javaMelodyRestrictingFilter")
	public FilterRegistrationBean javaMelodyRestrictingFilter(FilterRegistrationBean javaMelodyFilter) {
		Filter filter = new OncePerRequestFilter() {

			@Override
			protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
					FilterChain filterChain) throws ServletException, IOException {

				System.out.println("javaMelodyPort : " + javaMelodyPort);
				System.out.println("javaMelodyPortOnly : " + javaMelodyPortOnly);
				System.out.println("authTokenPort : " + authTokenPort);
				System.out.println("authTokenPortOnly : " + authTokenPortOnly);
				if (!javaMelodyPortOnly || request.getLocalPort() == javaMelodyPort) {
					filterChain.doFilter(request, response);
				} else {
					response.sendError(404);
				}
			}
		};
		FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean();
		filterRegistrationBean.setFilter(filter);
		filterRegistrationBean.setOrder(-100);
		filterRegistrationBean.setName("javaMelodyPortRestriction");
		filterRegistrationBean.addUrlPatterns(javaMelodyFilter.getInitParameters().get("monitoring-path"));
		return filterRegistrationBean;
	}

	@Bean(name = "openstackAuthRestrictingFilter")
	public FilterRegistrationBean openstackAuthRestrictingFilter(FilterRegistrationBean openstackAuthFilter) {
		Filter filter = new OncePerRequestFilter() {

			@Override
			protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
											FilterChain filterChain) throws ServletException, IOException {

				System.out.println("authTokenPort : " + authTokenPort);
				System.out.println("authTokenPortOnly : " + authTokenPortOnly);
				System.out.println("javaMelodyPort : " + javaMelodyPort);
				System.out.println("javaMelodyPortOnly : " + javaMelodyPortOnly);
				if (!authTokenPortOnly || request.getLocalPort() == authTokenPort) {
					filterChain.doFilter(request, response);
				} else {
					response.sendError(404);
				}
			}
		};
		FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean();
		filterRegistrationBean.setFilter(filter);
		filterRegistrationBean.setOrder(-100);
		filterRegistrationBean.setName("openstackAuthRestriction");
		filterRegistrationBean.addUrlPatterns(openstackAuthFilter.getInitParameters().get("openstackAuth-path"));
		return filterRegistrationBean;
	}
}
