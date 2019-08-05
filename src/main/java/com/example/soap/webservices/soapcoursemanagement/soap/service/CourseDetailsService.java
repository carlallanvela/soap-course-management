package com.example.soap.webservices.soapcoursemanagement.soap.service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.springframework.stereotype.Component;

import com.example.soap.webservices.soap.bean.Course;

@Component
public class CourseDetailsService {
	
	private static List<Course> courses = new ArrayList<>();
	
	static {
		Course course1 = new Course(1, "Spring", "10 Lessons");
		courses.add(course1);
		
		Course course2 = new Course(1, "Spring MVC", "15 Lessons");
		courses.add(course2);
		
		Course course3 = new Course(1, "Spring Boot", "15 Lessons");
		courses.add(course3);
		
		Course course4 = new Course(1, "Maven", "5 Lessons");
		courses.add(course4);
	}
	
	public Course findById(int id) {
		for (Course course : courses) {
			if (course.getId() == id) {
				return course;
			}
		}
		return null;
	}
	
	public List<Course> findAll() {
		return courses;
	}
	
	public int deleteById(int id) {
		Iterator<Course> iterator = courses.iterator();
		while (iterator.hasNext()) {
			Course course = iterator.next();
			if (course.getId() == id) {
				iterator.remove();
				return 1;
			}
		}
		return 0;
	}
}
