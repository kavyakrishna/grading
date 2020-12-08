package com.college.grading.domain;

public class Grades {

	public String studentName;
	
	 public String teacherName;
	 
	 public String assignmentType;
	 
	 public double score;
	 
	 public double maxScore;
	 
	 public Grades() {
		 
	 }

	public Grades(String studentName, String teacherName, String assignmentType, double score, double maxScore) {
		super();
		this.studentName = studentName;
		this.teacherName = teacherName;
		this.assignmentType = assignmentType;
		this.score = score;
		this.maxScore = maxScore;
	}

	public String getStudentName() {
		return studentName;
	}

	public void setStudentName(String studentName) {
		this.studentName = studentName;
	}

	public String getTeacherName() {
		return teacherName;
	}

	public void setTeacherName(String teacherName) {
		this.teacherName = teacherName;
	}

	public String getAssignmentType() {
		return assignmentType;
	}

	public void setAssignmentType(String assignmentType) {
		this.assignmentType = assignmentType;
	}

	public double getScore() {
		return score;
	}

	public void setScore(double score) {
		this.score = score;
	}

	public double getMaxScore() {
		return maxScore;
	}

	public void setMaxScore(double maxScore) {
		this.maxScore = maxScore;
	}

	@Override
	public String toString() {
		return "Grades [studentName=" + studentName + ", teacherName=" + teacherName + ", assignmentType="
				+ assignmentType + ", score=" + score + ", maxScore=" + maxScore + "]";
	}	
}