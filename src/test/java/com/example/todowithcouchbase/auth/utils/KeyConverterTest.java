package com.example.todowithcouchbase.auth.utils;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

import static org.junit.jupiter.api.Assertions.*;

class KeyConverterTest {

    @Test
    void utilityClass_ShouldNotBeInstantiated() {
        assertThrows(InvocationTargetException.class, () -> {
            // Attempt to use reflection to create an instance of the utility class
            java.lang.reflect.Constructor<KeyConverter> constructor = KeyConverter.class.getDeclaredConstructor();
            constructor.setAccessible(true);
            constructor.newInstance();
        });
    }

    @Test
    void testConvertPublicKeyRuntimeException() {
        String invalidPublicKey = "InvalidPublicKey";

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            KeyConverter.convertPublicKey(invalidPublicKey);
        });

        assertNotNull(exception);
        assertTrue(exception.getCause() instanceof IOException);
    }

    @Test
    void testConvertPrivateKeyRuntimeException() {
        String invalidPrivateKey = "InvalidPrivateKey";

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            KeyConverter.convertPrivateKey(invalidPrivateKey);
        });

        assertNotNull(exception);
        assertTrue(exception.getCause() instanceof IOException);
    }

}