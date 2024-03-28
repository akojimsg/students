package com.akojimsg.students;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan
public class StudentsManagementApp {

	public static void main(String[] args) {
		SpringApplication.run(StudentsManagementApp.class, args);
	}

}
