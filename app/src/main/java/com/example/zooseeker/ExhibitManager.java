package com.example.zooseeker;

import android.location.Location;

import java.io.Reader;
import java.util.List;
import java.util.Map;

/* Stores and gets information relevant to all exhibits*/
public class ExhibitManager {

    //Mapping names to node ids for graph algorithm
    public static Map<String, Exhibit> nameToExhibit;

    public ExhibitManager(Reader exhibitsReader, List mylist) {
        List<Exhibit> exhibits = Exhibit.fromJson(exhibitsReader);

        for(int i = 0; i < exhibits.size(); i++){
            //only attain exhibits
            if (exhibits.get(i).isExhibit()){
                mylist.add(exhibits.get(i).name);
            }
            nameToExhibit.put(exhibits.get(i).name, exhibits.get(i));
        }
    }

    Exhibit getClosest(Location loc){
        double lat = loc.getLatitude();
        double lng = loc.getLongitude();
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
                }
            }

        }
        return  closest_exhibit;
    }
}
