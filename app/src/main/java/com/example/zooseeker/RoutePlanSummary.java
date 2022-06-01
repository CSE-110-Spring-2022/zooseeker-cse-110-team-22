package com.example.zooseeker;

import android.location.Location;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class RoutePlanSummary {
    @NonNull
    public static List<String> getExhibitsDistances(Map<String, ZooData.VertexInfo> exhibit_info, List<String> id_list, List<String> pathOrder, double curr_lat, double curr_lng) {
        List<String> exhibits = new ArrayList<String>();
        for (int i = 0; i < pathOrder.size(); i++) {
            String s = pathOrder.get(i);
            if (id_list.contains(s)) {
                String name = exhibit_info.get(s).name;
                double ex_lat = exhibit_info.get(s).lat;
                double ex_lng = exhibit_info.get(s).lng;
                float[] distance = new float[1];
                Location.distanceBetween(curr_lat, curr_lng, ex_lat, ex_lng, distance);
                String info_string = String.format("%s - %.1f meters", name, distance[0]);

                exhibits.add(info_string);
            }
        }
        return exhibits;
    }
}
