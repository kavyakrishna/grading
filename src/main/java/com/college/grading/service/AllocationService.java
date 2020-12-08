package com.college.grading.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.college.grading.domain.Allocation;
import com.college.grading.domain.Grades;
import com.college.grading.domain.GradesType;

@Component
@Scope(value = "singleton")
public class AllocationService {
	private static final Logger logger = LogManager.getLogger(AllocationService.class);

	List<Grades> studentGrades;

	List<Allocation> teacherAllocation;

	@PostConstruct
	public void postConstruct() {
		studentGrades = new ArrayList<Grades>();
		teacherAllocation = new ArrayList<Allocation>();
	}

	/**
	 * Method which allocates teacher's allocations
	 * @param allc
	 * @throws Exception
	 */
	public void setAllocation(Allocation allc) throws Exception {
		logger.debug("Setting allocation for the teacher");

		checkValidityOfAllocation(allc);
		logger.debug("Allocation object valid => "+ allc.toString());

		Optional<Allocation> matchedTeacherAlloc = teacherAllocation.stream().filter(teacherAllocation ->
		allc.getTeacherName().equals(teacherAllocation.getTeacherName())).findAny();
		Allocation existingAllocation = matchedTeacherAlloc.orElse(null);

		if(null== existingAllocation){
			teacherAllocation.add(allc);
			logger.debug("New allocation is set");
		}else {
			//Removing the existing teacher assessment allocation and updating new one.
			teacherAllocation.remove(teacherAllocation.indexOf(existingAllocation));
			teacherAllocation.add(allc);
			logger.debug("Teacher allocation is removed and updated with new one");
		}
	}

	/**
	 * To check if the request sent for adding allocation is valid
	 * @param allc
	 * @throws Exception
	 */
	public void checkValidityOfAllocation(Allocation allc) throws Exception {
		logger.debug("Checking validity of the request..");
		if(null == allc) {
			throw new Exception("The request is not valid.");
		}
		else if(null == allc.getTeacherName() || allc.getTeacherName().isEmpty()) {
			throw new Exception("Request doesn't contain teacherName");
		}
		logger.debug("Request is VALID");
	}

	/**
	 * Adding grades for given student
	 * @param grade
	 * @return
	 * @throws Exception
	 */
	public double addGrade(Grades grade) throws Exception {
		logger.debug("In method adding student grade");
		validateStudentGrade(grade);
		studentGrades.add(grade);

		logger.info(studentGrades);
		return getGrades(grade.getStudentName(), grade.getTeacherName());
	}

	/**
	 * This method checks for validity of Grades object request before processing 
	 * @param grade
	 * @throws Exception
	 */
	private void validateStudentGrade(Grades grade) throws Exception {
		logger.debug("Checking validity of the request..");
		if(null == grade) {
			throw new Exception("The request is not valid.");
		}
		else if(null == grade.getTeacherName() || grade.getTeacherName().isEmpty()) {
			throw new Exception("Request doesn't contain teacherName");
		}
		else if(null == grade.getStudentName() || grade.getStudentName().isEmpty()) {
			throw new Exception("Request doesn't contain studentName");
		}

		boolean exam =validateIfExamGiven(grade.getStudentName(),grade.getTeacherName());

		if(exam) {
			throw new Exception("Exam/Term for this student is done. No more assessments is accepted.");
		}
		logger.debug("Request is VALID");
	}

	/**
	 * Method to check if student has already given exam
	 * for particular teacher's course
	 * @param studentName
	 * @param teacherName
	 * @return
	 */
	private boolean validateIfExamGiven(String studentName, String teacherName) {
		Grades st = studentGrades.stream().filter(grade -> grade.getStudentName().equals(studentName) && grade.getTeacherName().equals(teacherName)&& grade.getAssignmentType().equals("EXAM")).findFirst().orElse(null);
		return null==st?false:true;
	}

	/**
	 * Get grades of a particular student for given teachername
	 * @param studentName
	 * @param teacherName
	 * @return
	 */
	public double getGrades(String studentName , String teacherName) {
		logger.info(">?>?> " + studentName + "<><<" + teacherName);
		Optional<Allocation> matching = teacherAllocation.stream().filter(alloc -> alloc.getTeacherName().equals(teacherName)).findFirst();

		Allocation allc = matching.orElse(null);

		if(allc == null) {
			allc = new Allocation("default" ,10.1d,2,89.1d);
		}

		List<Grades> studentGradingList = studentGrades.stream().filter(grades -> grades.getStudentName().equals(studentName) && grades.getTeacherName().equals(teacherName)).collect(Collectors.toList());

		logger.debug(">>>>>"+studentGradingList);
		boolean exam=false;
		boolean extraCredit =false;

		double assignmentTotal=0;
		double assignmentMaxTotal=0;

		double examTotal=0;
		double examMaxTotal=0;

		for (Grades grades : studentGradingList) {
			GradesType gradeType =GradesType.decode(grades.getAssignmentType());

			logger.debug(":::>>." +gradeType);

			if(gradeType.equals(GradesType.ASSIGNMENT)) {
				assignmentTotal = assignmentTotal+ grades.getScore();
				assignmentMaxTotal = assignmentMaxTotal +grades.getMaxScore();
			}else if(gradeType.equals(GradesType.EXTRA_CREDIT)) {
				extraCredit = true;
			} else if(gradeType.equals(GradesType.EXAM) ) {
				exam = true;
				examTotal = grades.getScore();
				examMaxTotal= grades.getMaxScore();
			} 
		}

		double finalPercentage =0;

		Double assignmentPercentage = (assignmentTotal/assignmentMaxTotal)*100;

		if(extraCredit) {
			assignmentPercentage +=allc.getExtraCreditWeightage();
		}
		finalPercentage += assignmentPercentage;

		/** if the assessment type is exam then 
		 * applying the allocation percentage by the teacher associated **/
		if(exam) {
			double examPercentage = (examTotal/examMaxTotal) *100;

			if(assignmentPercentage.isNaN()) {
				logger.debug(">>Inside NaN ");
				assignmentPercentage=0d;
			}

			finalPercentage = (assignmentPercentage * (allc.getAssignmnetWeightage()/100)) + (examPercentage * (allc.getExameWeightage()/100));

		}

		/** Assuming if the student get perfect score in both exam and assignments, 
		 * also gets extra credit added 2% if the total goes more than 100% 
		 * then sending maximum of 100% as result for particular student. **/
		if(finalPercentage > 100d) {
			finalPercentage=100d;
		}
		return finalPercentage;
	}

	/**
	 * Display complete assessment details
	 * @param studentName
	 * @param teacherName
	 * @return
	 * @throws Exception
	 */
	public double getCompleteAssessment(String studentName, String teacherName) throws Exception {
		logger.info("Getting complete assessment for given student");
		Grades st = studentGrades.stream().filter(grade -> grade.getStudentName().equals(studentName) && grade.getTeacherName().equals(teacherName)).findFirst().orElse(null);

		if(st==null) {
			//can further be enhanced to customised exception class
			throw new Exception("Student/Teacher does not exist"); 
		}

		return getGrades(studentName, teacherName);
	}
}