package com.assignment.phonebook;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan(basePackages = {
		"com.assignment.phonebook"
})
@SpringBootApplication
public class PhonebookApplication {

	public static void main(String[] args) {

		ApplicationContext ctx =SpringApplication.run(PhonebookApplication.class, args);
	}

}
