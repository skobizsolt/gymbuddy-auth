package com.gymbuddy.auth;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class AuthApplicationTests {

	private final ApplicationContext context;

	AuthApplicationTests(ApplicationContext context) {
		this.context = context;
	}

	@Test
	void contextLoads() {
		assertEquals("application", context.getId());
	}
}
