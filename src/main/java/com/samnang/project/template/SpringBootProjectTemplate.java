package com.samnang.project.template;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.TimeZone;
@Slf4j
@SpringBootApplication
public class SpringBootProjectTemplate {

	public static void main(String[] args) {
		TimeZone.setDefault(TimeZone.getTimeZone("Asia/Bangkok"));
		SpringApplication.run(SpringBootProjectTemplate.class, args);
	}

}
