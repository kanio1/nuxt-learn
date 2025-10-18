package com.eventmaster.backend;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.oauth2.jwt.JwtDecoder;

import javax.sql.DataSource;

@SpringBootTest
class EventmasterBackendApplicationTests {

	@MockBean
	private DataSource dataSource;

	@MockBean
	private JwtDecoder jwtDecoder;

	@Test
	void contextLoads() {
	}

}
