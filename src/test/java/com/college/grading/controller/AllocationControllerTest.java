package com.college.grading.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.HashMap;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.college.grading.domain.Allocation;
import com.college.grading.domain.Grades;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class AllocationControllerTest {
	private static final Logger logger = LogManager.getLogger(AllocationControllerTest.class);
	
	@Autowired
	AllocationController allocationController ;
	
	@LocalServerPort
	private int port;

	@Autowired
	private TestRestTemplate restTemplate;
	
	/**
	 * testing add allocation for teacher
	 */
	@Test
	void testAddAllocation() {
		String allocationJson =" {\r\n" + 
				"  \r\n" + 
				"    \"teacherName\": \"Tom\",\r\n" + 
				"    \"assignmentPercentage\": 80,\r\n" + 
				"    \"extraCreditPercentage\": 5,\r\n" + 
				"    \"examPercentage\": 10\r\n" + 
				"  \r\n" + 
				"}";
		
		logger.info(allocationJson);
		String url="http://localhost:"+port+"/grading/addAllocation";
		
		Allocation request = new Allocation("Tom", 80, 2, 20);
		
		ResponseEntity<String> response = restTemplate.postForEntity(url, request, String.class);
		
		logger.info(response.getBody());
		assertEquals(HttpStatus.OK, response.getStatusCode());
	}

	/**
	 * testing of add student grade
	 */
	@Test
	void testAddStudentGrade() {
		String url="http://localhost:"+port+"/grading/addStudentGrade";
		Grades grades = new Grades("Sally", "Tom", "ASSIGNMENT", 92d, 100d);
		ResponseEntity<String> response = restTemplate.postForEntity(url, grades, String.class);
		logger.info(response.getBody());
		assertEquals(HttpStatus.OK, response.getStatusCode());
	}
	
	/**
	 * testing get student score
	 */
	@Test
	void testGetStudentGrade() {
		testAddStudentGrade();
		String url="http://localhost:"+port+"/grading/getStudentGrade?studentName=Sally&teacherName=Tom";
		Map<String, String> urlVariables = new HashMap<>();
		urlVariables.put("studentName", "Sally");
		urlVariables.put("teacherName", "Tom");
		ResponseEntity<String> responseEntity = restTemplate.getForEntity(url, String.class, urlVariables);
		assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
		assertEquals(responseEntity.getBody(),"The current percentage of Sally for teacher Tom is 92.0");
	}
}