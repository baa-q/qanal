package com.silqin.qanal;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.silqin.qanal.core.mapper") 
public class QanalApplication {

	public static void main(String[] args) {
		SpringApplication.run(QanalApplication.class, args);
	}

}
