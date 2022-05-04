package com.example.zooseeker;

import static org.junit.Assert.assertNotNull;

import android.content.Context;

import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.jgrapht.GraphPath;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class ZooGraphUnitTest {

    private ZooGraph testG;

    @Before
    public void initializeGraph(){
        Context context = ApplicationProvider.getApplicationContext();
        this.testG = new ZooGraph(context);
    }

    @Test
    public void assertGraphExists(){
        assertNotNull(this.testG);
    }

    @Test
    public void assertGraphComponentsExist(){
        assertNotNull(this.testG.ZooG);
        assertNotNull(this.testG.eInfo);
        assertNotNull(this.testG.vInfo);
    }

    @Test
    public void testGetPath2(){
        String start = "entrance_exit_gate";
        String goal = "elephant_odyssey";
        GraphPath<String, IdentifiedWeightedEdge> path = this.testG.getPath2(start, goal);
        assertNotNull(path);
    }
}
