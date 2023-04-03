package com.kentisthebest.demospringsecurity;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest
@TestPropertySource(locations = { "classpath:application.yaml" })
class DemoSpringSecurityApplicationTests {

    @Test
    void contextLoads() {
    }

}
