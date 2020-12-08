package com.college.grading;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.college.grading.controller.AllocationController;
@SpringBootTest
class GradingApplicationTest {

	@Autowired
	AllocationController controller;
	
	@BeforeEach
	void setUp() throws Exception {
	}

	@AfterEach
	void tearDown() throws Exception {
	}
	
	
	@Test
	public void contextLoads() throws Exception {
		assertThat(controller).isNotNull();
	}
	

}
