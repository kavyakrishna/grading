package com.college.grading.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.college.grading.domain.Allocation;
import com.college.grading.domain.Grades;
import com.college.grading.service.AllocationService;

@RestController
@RequestMapping(path = "/grading")
public class AllocationController {
	private static final Logger logger = LogManager.getLogger(AllocationController.class);

	@Autowired
	AllocationService allocationService;

	@PostMapping(path = "/addAllocation", consumes = "application/json")
	public Allocation addAllocation(@RequestBody Allocation allocation){
		logger.info("Request received for adding allocation for teacher");
		
		try {
			allocationService.setAllocation(allocation);
		} catch (Exception e) {
			 throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Request is incomplete/null");
		}
		
		logger.info("Request processed successfully");

		return allocation;
	}

	@PostMapping(path = "/addStudentGrade", consumes = "application/json")
	public Grades addStudentGrade(@RequestBody Grades grade) {
		logger.info("Request received to add student grade");
		logger.debug("Request body for addStudentGrade Post method :" + grade);//TODO:
		
		try {
			allocationService.addGrade(grade);
		} catch (Exception e) { //can add custom Exception class later.
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST,e.getMessage());
		}
		logger.info("Request processed successfully, added student grade");

		return grade;
	}

	@GetMapping("/getStudentGrade")
	public String getStudentGrade(@RequestParam(name = "studentName") String studentName,
			@RequestParam(name = "teacherName") String teacherName) {

		double percentage;
		try {
			percentage = allocationService.getCompleteAssessment(studentName, teacherName);
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST,e.getMessage());
		}

		logger.info("The current percentage of " + studentName + " for teacher " + teacherName + " is " + percentage);
		return "The current percentage of " + studentName + " for teacher " + teacherName + " is " + percentage;
	}
}