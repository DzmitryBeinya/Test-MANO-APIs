package com.netcracker.config;

import javax.servlet.DispatcherType;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;

import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.aop.support.annotation.AnnotationMatchingPointcut;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletContextInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RestController;

import net.bull.javamelody.MonitoredWithAnnotationPointcut;
import net.bull.javamelody.MonitoringFilter;
import net.bull.javamelody.MonitoringSpringAdvisor;
import net.bull.javamelody.SessionListener;
import net.bull.javamelody.SpringDataSourceBeanPostProcessor;

@Configuration
public class PortConfiguration implements ServletContextInitializer {

	@Override
	public void onStartup(ServletContext servletContext) throws ServletException {
		servletContext.addListener(new SessionListener());
	}

	@Bean(name = "javaMelodyFilter")
	public FilterRegistrationBean javaMelodyFilter() {
		final FilterRegistrationBean javaMelody = new FilterRegistrationBean();
		javaMelody.setFilter(new MonitoringFilter());
		javaMelody.setOrder(1);
		javaMelody.setAsyncSupported(true);
		javaMelody.setName("javaMelody");
		javaMelody.setDispatcherTypes(DispatcherType.REQUEST, DispatcherType.ASYNC);
		javaMelody.addUrlPatterns("/*");
		javaMelody.addInitParameter("monitoring-path", "/monitoring");
		return javaMelody;
	}

	@Bean(name = "openstackAuthFilter")
	public FilterRegistrationBean openstackAuthFilter() {
		final FilterRegistrationBean openstackAuth = new FilterRegistrationBean();
		openstackAuth.setFilter(new MonitoringFilter());
		openstackAuth.setOrder(1);
		openstackAuth.setAsyncSupported(true);
		openstackAuth.setName("openstackAuth");
		openstackAuth.setDispatcherTypes(DispatcherType.REQUEST, DispatcherType.ASYNC);
		openstackAuth.addUrlPatterns("/*");
		openstackAuth.addInitParameter("openstackAuth-path", "/v3");
		return openstackAuth;
	}

	@Bean(name = "openstackKeyPairsFilter")
	public FilterRegistrationBean openstackKeyPairsFilter() {
		final FilterRegistrationBean openstackKeyPairsFilter = new FilterRegistrationBean();
		openstackKeyPairsFilter.setFilter(new MonitoringFilter());
		openstackKeyPairsFilter.setOrder(1);
		openstackKeyPairsFilter.setAsyncSupported(true);
		openstackKeyPairsFilter.setName("openstackKeyPairsFilter");
		openstackKeyPairsFilter.setDispatcherTypes(DispatcherType.REQUEST, DispatcherType.ASYNC);
		openstackKeyPairsFilter.addUrlPatterns("/*");
		openstackKeyPairsFilter.addInitParameter("openstackKeyPairsFilter-path", "/v2.1");
		openstackKeyPairsFilter.addInitParameter("openstackKeyPairsFilter-path2", "/flavors");
		return openstackKeyPairsFilter;
	}

	@Bean
	public DefaultAdvisorAutoProxyCreator getDefaultAdvisorAutoProxyCreator() {
		return new DefaultAdvisorAutoProxyCreator();
	}

	// Monitoring JDBC datasources
	@Bean
	public SpringDataSourceBeanPostProcessor monitoringDataSourceBeanPostProcessor() {
		final SpringDataSourceBeanPostProcessor processor = new SpringDataSourceBeanPostProcessor();
		processor.setExcludedDatasources(null);
		return processor;
	}

	// Monitoring of beans or methods annotated with @MonitoredWithSpring
	@Bean
	public MonitoringSpringAdvisor monitoringAdvisor() {
		final MonitoringSpringAdvisor interceptor = new MonitoringSpringAdvisor();
		interceptor.setPointcut(new MonitoredWithAnnotationPointcut());
		return interceptor;
	}

	// Monitoring of all services and controllers (even without having
	// @MonitoredWithSpring annotation)
	@Bean
	public MonitoringSpringAdvisor springServiceMonitoringAdvisor() {
		final MonitoringSpringAdvisor interceptor = new MonitoringSpringAdvisor();
		interceptor.setPointcut(new AnnotationMatchingPointcut(Service.class));
		return interceptor;
	}

	@Bean
	public MonitoringSpringAdvisor springControllerMonitoringAdvisor() {
		final MonitoringSpringAdvisor interceptor = new MonitoringSpringAdvisor();
		interceptor.setPointcut(new AnnotationMatchingPointcut(Controller.class));
		return interceptor;
	}

	@Bean
	public MonitoringSpringAdvisor springRestControllerMonitoringAdvisor() {
		final MonitoringSpringAdvisor interceptor = new MonitoringSpringAdvisor();
		interceptor.setPointcut(new AnnotationMatchingPointcut(RestController.class));
		return interceptor;
	}
}