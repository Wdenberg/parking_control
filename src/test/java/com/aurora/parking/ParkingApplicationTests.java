package com.aurora.parking;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

@Import(TestcontainersConfiguration.class)
@SpringBootTest(properties = {
		"jwt.access-secret=test-access-secret-that-is-long-enough-for-hs256",
		"jwt.refresh-secret=test-refresh-secret-that-is-long-enough-for-hs256"
})
class ParkingApplicationTests {

	@Test
	void contextLoads() {
	}

}
