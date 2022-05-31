package com.example.zooseeker;

import android.util.Log;
import android.util.Pair;

import java.io.Reader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/* Stores and gets information relevant to exhibits*/
public class ExhibitManager {

    //Mapping names to node ids for graph algorithm
    public static Map<String, Exhibit> nameToExhibit = new HashMap<>();
    public static Map<String, Exhibit> idToExhibit = new HashMap<>();


    public ExhibitManager(Reader exhibitsReader) {
        List<Exhibit> exhibits = Exhibit.fromJson(exhibitsReader);

        for(int i = 0; i < exhibits.size(); i++){
            //only attain exhibits
            nameToExhibit.put(exhibits.get(i).name, exhibits.get(i));
            idToExhibit.put(exhibits.get(i).id, exhibits.get(i));
        }
    }

    public ExhibitManager(Reader exhibitsReader, List mylist) {
        List<Exhibit> exhibits = Exhibit.fromJson(exhibitsReader);

        for(int i = 0; i < exhibits.size(); i++){
            //only attain exhibits
            if (exhibits.get(i).isExhibit()){
                mylist.add(exhibits.get(i).name);
            }
            nameToExhibit.put(exhibits.get(i).name, exhibits.get(i));
            idToExhibit.put(exhibits.get(i).id, exhibits.get(i));
        }
    }

    Exhibit getClosest(double lat, double lng){
//        double lat = loc.getLatitude();
//        double lng = loc.getLongitude();
        double smallest_dist = Double.MAX_VALUE;
        Exhibit closest_exhibit = null;

        for (Map.Entry exhibit_ent: nameToExhibit.entrySet()){
            var exhibit = (Exhibit) exhibit_ent.getValue();
            if (! exhibit.hasGroup()){
                var curr_lat = exhibit.lat;
                var curr_lng = exhibit.lng;

                var d_lat = curr_lat - lat;
                var d_lng = curr_lng - lng;

                var curr_dist = Math.sqrt(Math.pow(d_lat, 2) + Math.pow(d_lng, 2));

                if (curr_dist < smallest_dist){
                    closest_exhibit = exhibit;
                    smallest_dist = curr_dist;
//                    Log.d("Closest", String.format("exhibit: %s dist: %f", exhibit.name, curr_dist));
                }
            }

        }
        return  closest_exhibit;
    }

    Pair<Double, Double> getCords(Exhibit ext){
        if (! ext.hasGroup()){
            return new Pair<>(ext.lat, ext.lng);
        }
        Exhibit groupExt = idToExhibit.get(ext.groupId);
        return new Pair<>(groupExt.lat, groupExt.lng);
    }

    double getDistanceBetween(Exhibit ext, double curr_lat, double curr_lng){
        var extCords = getCords(ext);
        var dLat = extCords.first - curr_lat;
        var dLng = extCords.second - curr_lng;

        return Math.sqrt(Math.pow(dLat, 2) + Math.pow(dLng, 2));

    }
}
