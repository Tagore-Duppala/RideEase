package com.project.rideEase;

import com.project.rideEase.services.EmailSenderService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class RideEaseTests {

	@Autowired
	private EmailSenderService emailSenderService;

	@Test
	void contextLoads() {

	}

}
