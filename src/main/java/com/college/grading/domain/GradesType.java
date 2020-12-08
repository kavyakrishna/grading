package com.college.grading.domain;

public enum GradesType {

	ASSIGNMENT("ASSIGNMENT"), EXTRA_CREDIT("EXTRA_CREDIT"), EXAM("EXAM");

	private String code;

	private GradesType(String code) {
		this.code = code;
	}

	public String getCode() {
		return code;
	}

	public static GradesType decode(final String code) {
		return GradesType.valueOf(code);
	}
}