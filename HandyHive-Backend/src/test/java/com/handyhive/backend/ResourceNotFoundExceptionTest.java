package com.handyhive.backend;

import com.handyhive.backend.exception.ResourceNotFoundException;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class ResourceNotFoundExceptionTest {

    @Test
    public void testExceptionMessage() {
        // 1. Throw the exception
        ResourceNotFoundException ex = assertThrows(ResourceNotFoundException.class, () -> {
            throw new ResourceNotFoundException("User not found with ID 99");
        });

        // 2. Verify the message is exactly what we sent
        assertEquals("User not found with ID 99", ex.getMessage());
    }
}