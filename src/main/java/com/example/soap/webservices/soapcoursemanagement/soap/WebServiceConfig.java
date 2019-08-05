package com.example.soap.webservices.soapcoursemanagement.soap;

import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.ws.config.annotation.EnableWs;
import org.springframework.ws.transport.http.MessageDispatcherServlet;
import org.springframework.ws.wsdl.wsdl11.DefaultWsdl11Definition;
import org.springframework.xml.xsd.SimpleXsdSchema;
import org.springframework.xml.xsd.XsdSchema;

// Enable Web Services
// Spring Configuration

@EnableWs
@Configuration
public class WebServiceConfig {
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
}
