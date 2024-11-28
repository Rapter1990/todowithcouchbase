package com.example.todowithcouchbase.common.util;

import com.example.todowithcouchbase.base.AbstractBaseServiceTest;
import org.junit.jupiter.api.Test;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ListUtilTest extends AbstractBaseServiceTest {


    @Test
    void toMethod_ShouldCastObjectToList() {
        List<String> expectedList = List.of("one", "two", "three");

        List<String> result = ListUtil.to(expectedList, String.class);

        assertNotNull(result, "Result should not be null");
        assertEquals(expectedList, result, "Result should match the expected list");
    }

    @Test
    void toMethod_ShouldThrowExceptionWhenCastFails() {
        Object invalidInput = "not a list";

        assertThrows(ClassCastException.class, () -> {
            List<Integer> result = ListUtil.to(invalidInput, Integer.class);
        });
    }

}