package com.handyhive.backend;

import com.handyhive.backend.model.JobStatus;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class JobStatusTest {

    @Test
    public void testEnumValues() {
        // Verify the Enum has the expected constants
        assertNotNull(JobStatus.valueOf("PENDING"));
        assertNotNull(JobStatus.valueOf("COMPLETED"));
        assertNotNull(JobStatus.valueOf("CANCELLED"));

        // Verify toString or name behavior
        assertEquals("PENDING", JobStatus.PENDING.name());
    }
}