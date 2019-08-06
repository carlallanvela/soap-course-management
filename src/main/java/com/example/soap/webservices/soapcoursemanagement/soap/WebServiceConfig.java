package com.example.soap.webservices.soapcoursemanagement.soap;

import java.util.Collections;
import java.util.List;

import javax.security.auth.callback.CallbackHandler;

import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.ws.config.annotation.EnableWs;
import org.springframework.ws.config.annotation.WsConfigurerAdapter;
import org.springframework.ws.server.EndpointInterceptor;
import org.springframework.ws.soap.security.wss4j2.callback.SimplePasswordValidationCallbackHandler;
import org.springframework.ws.soap.security.xwss.XwsSecurityInterceptor;
import org.springframework.ws.transport.http.MessageDispatcherServlet;
import org.springframework.ws.wsdl.wsdl11.DefaultWsdl11Definition;
import org.springframework.xml.xsd.SimpleXsdSchema;
import org.springframework.xml.xsd.XsdSchema;

// Enable Web Services
// Spring Configuration

@EnableWs
@Configuration
public class WebServiceConfig extends WsConfigurerAdapter {
	// Message Dispatcher Servlet 
	// Servlet for simplified dispatching
	// of messages. It handles all SOAP request and identifes endpoints.
	
	// ApplicationContext
	// URL to this servlet -> /ws/*
	@Bean
	public ServletRegistrationBean  messageDispatcherServlet(ApplicationContext context) {
		// helps us map a servlet to URI
		// servlet to handle request
		MessageDispatcherServlet messageDispatcherServlet = new MessageDispatcherServlet();
		messageDispatcherServlet.setApplicationContext(context);
		messageDispatcherServlet.setTransformWsdlLocations(true);
		return new ServletRegistrationBean(messageDispatcherServlet, "/ws/*");
	}
	
	// course.wsdl
	// Expose it in url /ws/course.wsdl
	// course-details.xsd is the schema
	// use the schema and generate the wsdl
	// to generate we need port type CoursePort
	// we also need to define the Namespace
	
	@Bean(name="courses")
	public DefaultWsdl11Definition defaultWsdl11Definition
		(XsdSchema courseSchema) {
		DefaultWsdl11Definition definition = new DefaultWsdl11Definition();
		// PortType - CoursePort
		definition.setPortTypeName("CoursePort");
		// Namespace - http://carlallanvela/courses
		definition.setTargetNamespace("http://carlallanvela.com/courses");
		// ws
		definition.setLocationUri("/ws");
		// schema
		definition.setSchema(courseSchema);
		return definition;
	}
	
	@Bean
	public XsdSchema coursesSchema() {
		return new SimpleXsdSchema(new ClassPathResource("course-details.xsd"));
	}
	
	//XwsSecurityInterceptor - Intercepts the requests that our coming in and checks whether it's secure.
		//1. Define Callback Handler  -> Check the user id and password --> SimplePasswordValidationCallbackHandler
		//2. Security Policy -> securityPolicy.xml
	// Add it to the list of interceptor
	// Spring has a mechanism where it adds to the list of interceptors
	@Bean
	public XwsSecurityInterceptor securityInterceptor() {
		XwsSecurityInterceptor securityInterceptor = new XwsSecurityInterceptor();
		securityInterceptor.setCallbackHandler(callbackHandler());
		securityInterceptor.setPolicyConfiguration(new ClassPathResource("securityPolicy.xml"));
		return securityInterceptor;
	}

	@Bean
	public SimplePasswordValidationCallbackHandler callbackHandler() {
		SimplePasswordValidationCallbackHandler handler = new SimplePasswordValidationCallbackHandler();
		handler.setUsersMap(Collections.singletonMap("user", "password"));
		return handler;
	}
	
	@Override
	public void addInterceptors(List<EndpointInterceptor> interceptors) {
		interceptors.add(securityInterceptor());
	}
	
	
}
