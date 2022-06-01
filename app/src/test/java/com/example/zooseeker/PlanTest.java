package com.example.zooseeker;

import static org.junit.Assert.*;

import android.content.Context;
import android.util.Log;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.internal.bytecode.ClassHandler;

import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RunWith(AndroidJUnit4.class)
public class PlanTest {

    private ZooGraph zGraph;
    private RoutePlanSummary rps;

    @Before
    public void initializeGraph(){
        Context context = ApplicationProvider.getApplicationContext();
        this.zGraph = new ZooGraph(context);
    }

    // sanity check for plan list
    @Test
    public void getExhibitsDistancesTest() {
        // crocodile coordinates
        double curr_lat = 32.745293428608484;
        double curr_lng = -117.16976102878033;

        Map<String,ZooData.VertexInfo> exhibit_info = zGraph.vInfo;

        List<String> pathOrder = new ArrayList<>();
        pathOrder.add("crocodile");
        List<String> id_list = new ArrayList<>();
        id_list.add("crocodile");
        List<String> route_plan = rps.getExhibitsDistances(exhibit_info, id_list, pathOrder, curr_lat, curr_lng);
        String route = route_plan.get(0);
        assertEquals(route, "Crocodiles - 0.0 meters");
    }

}
