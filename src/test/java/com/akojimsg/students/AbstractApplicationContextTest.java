package com.akojimsg.students;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;

@TestPropertySource(
    properties = {
        "application.settings.default.userinfo-secret=test-secret"
    },
    inheritProperties = true

)
@ContextConfiguration
@SpringBootTest(
    classes = StudentsManagementApp.class
)
@ActiveProfiles("test")
public abstract class AbstractApplicationContextTest {
  @TestConfiguration
  public static class ApplicationTestsConfiguration {
  }
}
