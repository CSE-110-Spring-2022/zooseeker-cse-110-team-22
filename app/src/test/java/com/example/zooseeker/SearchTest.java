package com.example.zooseeker;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import androidx.lifecycle.Lifecycle;
import androidx.test.core.app.ActivityScenario;
import org.junit.Test;

public class SearchTest {

    @Test
    public void TestTextIsText() {
        int text = 7;
        assertEquals(7, text);
    }

    @Test
    public void assertString() {
        String str = "application";
        assertEquals("application", str);
    }

    @Test
    public void basicStateTest() {
        Lifecycle.State state = Lifecycle.State.CREATED;
        assertNotEquals(state, Lifecycle.State.DESTROYED);
    }

    @Test
    public void newStateTest() {
        Lifecycle.State state = Lifecycle.State.CREATED;
        assertEquals(state, Lifecycle.State.CREATED);
    }


}


