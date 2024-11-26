package com.example.todowithcouchbase.auth.model.mapper;

import com.example.todowithcouchbase.auth.model.dto.request.RegisterRequest;
import com.example.todowithcouchbase.auth.model.entity.UserEntity;
import com.example.todowithcouchbase.auth.model.enums.UserType;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class RegisterRequestToUserEntityMapperTest {

    private final RegisterRequestToUserEntityMapper mapper = RegisterRequestToUserEntityMapper.initialize();

    @Test
    void testMapRegisterRequestNull() {
        // Test mapping a null RegisterRequest
        UserEntity result = mapper.map((RegisterRequest) null);
        assertNull(result);
    }

    @Test
    void testMapRegisterRequestCollectionNull() {
        // Test mapping a null collection of RegisterRequest
        List<UserEntity> result = mapper.map((Collection<RegisterRequest>) null);
        assertNull(result);
    }

    @Test
    void testMapRegisterRequestListEmpty() {
        // Test mapping an empty list of RegisterRequest
        List<UserEntity> result = mapper.map(Collections.emptyList());
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void testMapRegisterRequestListWithNullElements() {
        // Test mapping a list of RegisterRequest with a null element
        List<RegisterRequest> requests = Arrays.asList(
                new RegisterRequest("test1@example.com",
                        "password1",
                        "John",
                        "Doe",
                        "1234567890",
                        UserType.USER),
                null
        );

        List<UserEntity> result = mapper.map(requests);

        assertNotNull(result);
        assertEquals(2, result.size());
        assertNotNull(result.get(0));
        assertNull(result.get(1));
    }

    @Test
    void testMapSingleRegisterRequest() {
        // Test mapping a single RegisterRequest
        RegisterRequest request = new RegisterRequest(
                "test@example.com", "password", "John", "Doe", "1234567890", UserType.ADMIN
        );

        UserEntity result = mapper.map(request);

        assertNotNull(result);
        assertEquals(request.getEmail(), result.getEmail());
        assertEquals(request.getPassword(), result.getPassword());
        assertEquals(request.getFirstName(), result.getFirstName());
        assertEquals(request.getLastName(), result.getLastName());
        assertEquals(request.getPhoneNumber(), result.getPhoneNumber());
        assertEquals(request.getUserType(), result.getUserType());

    }

}