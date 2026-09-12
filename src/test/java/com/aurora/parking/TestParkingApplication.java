package com.aurora.parking;

import org.springframework.boot.SpringApplication;

public class TestParkingApplication {

	public static void main(String[] args) {
		SpringApplication.from(ParkingApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
