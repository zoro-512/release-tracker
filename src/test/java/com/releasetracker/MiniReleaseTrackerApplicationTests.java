package com.releasetracker;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

/**
 * Smoke test — verifies the Spring application context loads without errors.
 */
@SpringBootTest
@ActiveProfiles("test")
class MiniReleaseTrackerApplicationTests {

    @Test
    void contextLoads() {
        // Passes if the application context starts successfully
    }
}
