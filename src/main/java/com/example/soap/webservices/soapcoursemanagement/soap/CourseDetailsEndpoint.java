package com.example.soap.webservices.soapcoursemanagement.soap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

import com.example.soap.webservices.soap.bean.Course;
import com.example.soap.webservices.soapcoursemanagement.jaxb.CourseDetails;
import com.example.soap.webservices.soapcoursemanagement.jaxb.GetCourseDetailsRequest;
import com.example.soap.webservices.soapcoursemanagement.jaxb.GetCourseDetailsResponse;
import com.example.soap.webservices.soapcoursemanagement.soap.service.CourseDetailsService;

@Endpoint
public class CourseDetailsEndpoint {
	
	@Autowired
	CourseDetailsService service;
	
	// Supports the Request from 
	// http://carlallanvela.com/courses
	// and GetCourseDetailsRequest
	@PayloadRoot(namespace = "http://carlallanvela.com/courses",
			localPart="GetCourseDetailsRequest")
	@ResponsePayload
	public GetCourseDetailsResponse processCourseDetailsRequest
		(@RequestPayload GetCourseDetailsRequest request) {
		
		Course course = service.findById(request.getId());
		
		return mapCourse(course);
	}

	private GetCourseDetailsResponse mapCourse(Course course) {
		GetCourseDetailsResponse response = new GetCourseDetailsResponse();
		
		CourseDetails courseDetails = new CourseDetails();
		courseDetails.setId(course.getId());
		courseDetails.setName(course.getName());
		courseDetails.setDescription(course.getDescription());
		
		response.setCourseDetails(courseDetails);
		return response;
	}
}
