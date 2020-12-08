package com.college.grading.domain;

public class Allocation {
	public String teacherName;
	
	public double assignmnetWeightage;
	
	public double extraCreditWeightage;
	
	public double exameWeightage;
	
	public Allocation() {
	}

	public Allocation(String teacherName, double assignmnetWeightage, double extraCreditWeightage,
			double exameWeightage) {
		super();
		this.teacherName = teacherName;
		this.assignmnetWeightage = assignmnetWeightage;
		this.extraCreditWeightage = extraCreditWeightage;
		this.exameWeightage = exameWeightage;
	}

	public String getTeacherName() {
		return teacherName;
	}

	public void setTeacherName(String teacherName) {
		this.teacherName = teacherName;
	}

	public double getAssignmnetWeightage() {
		return assignmnetWeightage;
	}

	public void setAssignmnetWeightage(double assignmnetWeightage) {
		this.assignmnetWeightage = assignmnetWeightage;
	}

	public double getExtraCreditWeightage() {
		return extraCreditWeightage;
	}

	public void setExtraCreditWeightage(double extraCreditWeightage) {
		this.extraCreditWeightage = extraCreditWeightage;
	}

	public double getExameWeightage() {
		return exameWeightage;
	}

	public void setExameWeightage(double exameWeightage) {
		this.exameWeightage = exameWeightage;
	}

	@Override
	public String toString() {
		return "Allocation [teacherName=" + teacherName + ", assignmnetWeightage=" + assignmnetWeightage
				+ ", extraCreditWeightage=" + extraCreditWeightage + ", exameWeightage=" + exameWeightage + "]";
	}
}