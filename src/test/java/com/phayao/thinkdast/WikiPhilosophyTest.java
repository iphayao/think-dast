package com.phayao.thinkdast;

import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.*;

public class WikiPhilosophyTest {

    /**
     * Test method for {@link WikiPhilosophy#main(String[])}.
     */
    @Test
    public void testMain() {
        // because this lab is more open-ended than others, we can't provide unit test.
        // Instead, we just check that you're modified WikiPhilosopy.java so it doesn't
        // throw a exception
        String[] args = {};
        try {
            WikiPhilosophy.main(args);
        }
        catch (IOException e) {
            e.printStackTrace();
            fail();
        }
    }

}