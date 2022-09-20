package com.kentisthebest.demospringsecurity;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest
@TestPropertySource(properties = {"spring.datasource.url=jdbc-test",
        "spring.datasource.driverClassName=org.postgresql.Driver",
        "spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.PostgreSQLDialect",
        "spring.security.key=secret"})
class DemoSpringSecurityApplicationTests {

    @Test
    void contextLoads() {
    }

}
