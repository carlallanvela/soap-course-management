package com.example.soap.webservices.soapcoursemanagement.soap;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

import com.example.soap.webservices.soap.bean.Course;
import com.example.soap.webservices.soapcoursemanagement.jaxb.CourseDetails;
import com.example.soap.webservices.soapcoursemanagement.jaxb.DeleteCourseDetailsRequest;
import com.example.soap.webservices.soapcoursemanagement.jaxb.DeleteCourseDetailsResponse;
import com.example.soap.webservices.soapcoursemanagement.jaxb.GetAllCourseDetailsRequest;
import com.example.soap.webservices.soapcoursemanagement.jaxb.GetAllCourseDetailsResponse;
import com.example.soap.webservices.soapcoursemanagement.jaxb.GetCourseDetailsRequest;
import com.example.soap.webservices.soapcoursemanagement.jaxb.GetCourseDetailsResponse;
import com.example.soap.webservices.soapcoursemanagement.soap.exception.CourseNotFoundException;
import com.example.soap.webservices.soapcoursemanagement.soap.service.CourseDetailsService;
import com.example.soap.webservices.soapcoursemanagement.soap.service.CourseDetailsService.Status;

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
		
		if (course == null) {
			throw new CourseNotFoundException("Invalid Course Id: " + request.getId());
		}
		
		return mapCourseDetails(course);
	}
	
	@PayloadRoot(namespace = "http://carlallanvela.com/courses",
			localPart="GetAllCourseDetailsRequest")
	@ResponsePayload
	public GetAllCourseDetailsResponse processAllCourseDetailsRequest
		(@RequestPayload GetAllCourseDetailsRequest request) {
		
		List<Course> allCourses = service.findAll();
		
		return mapAllCourseDetails(allCourses);
	}
	
	@PayloadRoot(namespace = "http://carlallanvela.com/courses",
			localPart="DeleteCourseDetailsRequest")
	@ResponsePayload
	public DeleteCourseDetailsResponse deleteCourseDetailsRequest
		(@RequestPayload DeleteCourseDetailsRequest request) {
	
		DeleteCourseDetailsResponse response = new DeleteCourseDetailsResponse();
		
		Status responseCode = service.deleteById(request.getId());
		response.setStatus(mapStatus(responseCode));
		
		return response;
	}

	private CourseDetails mapCourse(Course course) {
		CourseDetails courseDetails = new CourseDetails();
		
		courseDetails.setId(course.getId());
		courseDetails.setName(course.getName());
		courseDetails.setDescription(course.getDescription());
		
		return courseDetails;
	}
	
	private GetCourseDetailsResponse mapCourseDetails(Course course) {
		GetCourseDetailsResponse response = new GetCourseDetailsResponse();
		response.setCourseDetails(mapCourse(course));
		
		return response;
	}
	
	private GetAllCourseDetailsResponse mapAllCourseDetails(List<Course> courses) {
		GetAllCourseDetailsResponse response = new GetAllCourseDetailsResponse();
		for(Course course : courses) {
			CourseDetails mapCourse = mapCourse(course);
			response.getCourseDetails().add(mapCourse);
		}
		
		return response;
	}
	
	private com.example.soap.webservices.soapcoursemanagement.jaxb.Status mapStatus(Status status) {
		if (status == Status.FAILURE) {
			return com.example.soap.webservices.soapcoursemanagement.jaxb.Status.FAILURE;
		}
		return com.example.soap.webservices.soapcoursemanagement.jaxb.Status.SUCCESS;
	}
}
