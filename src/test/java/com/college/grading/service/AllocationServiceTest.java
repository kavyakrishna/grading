package com.college.grading.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;

import com.college.grading.domain.Allocation;
import com.college.grading.domain.Grades;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class AllocationServiceTest {
private static final Logger logger = LogManager.getLogger(AllocationServiceTest.class);
	
	@Autowired
	AllocationService allocationService ;
	
	/**
	 * This test case sets teacher's allocation
	 * where all the required fields are valid
	 */
	@Test
	void testSetAllocation() {
		logger.info("In test method setAllocation");
		Allocation allocation = new Allocation();
		allocation.setTeacherName("Tom");
		allocation.setAssignmnetWeightage(89.1);
		allocation.setExtraCreditWeightage(2);
		allocation.setExameWeightage(10.9);
		
		try {
			allocationService.setAllocation(allocation);
			Optional<Allocation> matchedResultAlloc = allocationService.teacherAllocation.stream().filter(alloc->allocation.getTeacherName().equals("Tom")).findAny();
			Allocation existingAllocation = matchedResultAlloc.orElse(null);
			assertEquals(existingAllocation.getAssignmnetWeightage(),89.1);
			assertEquals(existingAllocation.getExtraCreditWeightage(),2);
			assertEquals(existingAllocation.getExameWeightage(),10.9);
			assertEquals(existingAllocation.getTeacherName(),"Tom");
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
	}

	/**
	 * This test case includes 4 test cases in total
	 * 1. where an assignment is added for student
	 * 2. where assignment 2 is added for same student
	 * 3. where extra credit is added for same student
	 * 4. where exam is added for same student
	 */
	@Test
	void testAddGrade() {
		testSetAllocation();
		logger.info("In test method addGrade of student");
		double percentage=0d;
		
		try {
			/**
			 * Please goto getGradeDataByCase for each case description
			 */
			percentage = allocationService.addGrade(getGradeDataByCase(1));
			logger.info("current percentage==="+percentage);
			assertEquals(percentage,80d);
			
			percentage = allocationService.addGrade(getGradeDataByCase(2));
			logger.info("current percentage==="+percentage);
			assertEquals(percentage,84.5d);
			
			percentage = allocationService.addGrade(getGradeDataByCase(3));
			logger.info("current percentage==="+percentage);
			assertEquals(percentage,86.5d);
			
			percentage = allocationService.addGrade(getGradeDataByCase(4));
			logger.info("current percentage==="+percentage);
			assertEquals(percentage,87.42649999999999);
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
	}

	/**
	 * Display a student's grades when exists in records
	 */
	@Test
	void testGetGradesWhenStudentExists() {
		logger.info("In test method getGrades when student exists");
		testAddGrade();
		double percentage = 0;
		try {
			percentage = allocationService.getCompleteAssessment("Sally", "Tom");
			assertEquals(percentage,87.42649999999999);
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
	}

	/**
	 *  Display a student's record when 
	 *  student doesn't exist
	 *  throws appropriate error message
	 */
	@Test
	void testGetGradesNoStudent() {
		logger.info("In test method getGrades when student doesnt exist");
		try {
			allocationService.getCompleteAssessment("Stuart", "Tom");
		} catch (Exception e) {
			logger.error(e.getMessage());
			assertEquals("Student/Teacher does not exist",e.getMessage());
		}
	}

	/**
	 * This test case tests add allocation 
	 * when teacher name is not provided in request
	 * throws error if not provided
	 */
	@Test
	void testAddAllocationWithNoTeacherName() {
		logger.info("In test method addAllocation where teacher name doesnt exist");
		try {
			Allocation allc = new Allocation();
			allc.setAssignmnetWeightage(40);
			allc.setExameWeightage(20);
			allc.setExtraCreditWeightage(1);
			allocationService.setAllocation(allc);
		} catch(Exception e) {
			logger.error(e.getMessage());
			assertEquals("Request doesn't contain teacherName",e.getMessage());
		}
	}
		
	public Grades getGradeDataByCase(int caseNo) {
		Grades grade = new Grades();

		switch(caseNo) {
		case 1:
			/**
			 * Case 1 - where an assignment is added
			 */
			grade.setTeacherName("Tom");
			grade.setStudentName("Sally");
			grade.setAssignmentType("ASSIGNMENT");
			grade.setScore(80);
			grade.setMaxScore(100);
			break;
		case 2:
			/**
			 * Case 2 - where assignment 2 is added for same student
			 */
			grade.setTeacherName("Tom");
			grade.setStudentName("Sally");
			grade.setAssignmentType("ASSIGNMENT");
			grade.setScore(89);
			grade.setMaxScore(100);
			break;
		case 3:
			/**
			 * Case 3 - where extra credit is added for same student
			 */
			grade.setTeacherName("Tom");
			grade.setStudentName("Sally");
			grade.setAssignmentType("EXTRA_CREDIT");
			break;
		case 4:
			/**
			 * Case 4 - where exam is added for same student
			 */
			grade.setTeacherName("Tom");
			grade.setStudentName("Sally");
			grade.setAssignmentType("EXAM");
			grade.setScore(95);
			grade.setMaxScore(100);
			break;
			default:
				grade.setTeacherName("Tom");
				grade.setStudentName("Sally");
				grade.setAssignmentType("ASSIGNMENT");
				grade.setScore(89);
				grade.setMaxScore(100);
		}
			
		return grade;
	}
}