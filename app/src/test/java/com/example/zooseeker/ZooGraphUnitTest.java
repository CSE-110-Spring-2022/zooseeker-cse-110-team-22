package com.example.zooseeker;

import static org.junit.Assert.assertNotNull;

import android.content.Context;
import android.util.Log;

import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.jgrapht.GraphPath;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.List;

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

    @Test
    public void testGetShortestPath(){
        List<String> animal_list = new ArrayList<>();
        animal_list.add("elephant_odyssey");
        animal_list.add("gorillas");
        animal_list.add("gators");

        List<String> output = this.testG.getShortestPath(animal_list);
        Log.d("out1", output.get(0));
        assertNotNull(output);
    }
}
