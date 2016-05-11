package com.cskaoyan.birthday.whobirthday;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * To work on unit tests, switch the Test Artifact in the Build Variants view.
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() throws Exception {
        assertEquals(4, 2 + 2);
    }

    @Test
    public void regexTest(){

        String str = "666566";
        boolean isRight = str.matches("^\\[0-9 | a-z]{6,10}");

        assertTrue(isRight);

    }
}