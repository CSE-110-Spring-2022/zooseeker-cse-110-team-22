package com.example.zooseeker;

import static org.junit.Assert.assertEquals;
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

    //tests that graph exists
    @Test
    public void assertGraphExists(){
        assertNotNull(this.testG);
    }

    //testing that graph components are properly initialized
    @Test
    public void assertGraphComponentsExist(){
        assertNotNull(this.testG.ZooG);
        assertNotNull(this.testG.eInfo);
        assertNotNull(this.testG.vInfo);
    }

    //testing that getPath2 returns an appropriate GraphPath
    @Test
    public void testGetPath2(){
        String start = "entrance_exit_gate";
        String goal = "flamingo";
        GraphPath<String, IdentifiedWeightedEdge> path = this.testG.getPath2(start, goal);
        assertNotNull(path);
    }

    //testing that getPath2's returned GraphPath has correct edge weight
    @Test
    public void testGetPath2EdgeWeight(){
        String start = "entrance_exit_gate";
        String goal = "intxn_front_treetops";
        GraphPath<String, IdentifiedWeightedEdge> path = this.testG.getPath2(start, goal);
        assertEquals(1100.0, path.getWeight(), 0.05);
    }

    //testing that getDirectionsFromPath2 returns a proper String list
    @Test
    public void testGetDirectionsFromPath2(){
        String start = "entrance_exit_gate";
        String goal = "hippo";
        GraphPath<String, IdentifiedWeightedEdge> path = this.testG.getPath2(start, goal);
        List<String> twoNodesDirection = this.testG.getDetailedDirections(path);
        assertNotNull(path);
    }

    //testing that getShortestPath on an arbitrary list will return some non-null output
    @Test
    public void testGetShortestPath(){
        List<String> animal_list = new ArrayList<>();
        animal_list.add("koi");
        animal_list.add("gorilla");
        animal_list.add("capuchin");

        List<String> output = this.testG.getShortestPath(animal_list,null);
        Log.d("out1", output.get(0));
        assertNotNull(output);
    }
}
