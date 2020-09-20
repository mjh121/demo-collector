package com.ssu.ISCOM;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySource;

import com.ssu.ISCOM.service.*;

@SpringBootApplication
//@EnableAutoConfiguration
////@ComponentScan(basePackageClasses = InputController.class)
////@ComponentScan(basePackageClasses = CollectorManager.class)
////@ComponentScan(basePackageClasses = Loader.class)
////@ComponentScan(basePackageClasses = Provider.class)
//@PropertySource("application.properties")
public class SilverCollectorApplication {

	public static void main(String[] args) {
		SpringApplication.run(SilverCollectorApplication.class, args);
	}
}
