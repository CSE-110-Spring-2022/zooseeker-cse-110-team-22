package com.example.zooseeker;

import android.content.Context;
import android.util.Log;

import org.jgrapht.Graph;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ZooGraph {
    public Map<String, ZooData.VertexInfo> vInfo;
    public static Map<String, ZooData.EdgeInfo> eInfo;
    public static Graph<String, IdentifiedWeightedEdge> ZooG;

    public ZooGraph(Context context){
        // 1. Load the graph... using new data
        this.ZooG = ZooData.loadZooGraphJSON(context,"zoo_graph.json");
        // 2. Load the information about our nodes and edges... using new data
        //problem here
        this.vInfo = ZooData.loadVertexInfoJSON(context,"exhibit_info.json");
        this.eInfo = ZooData.loadEdgeInfoJSON(context,"trail_info.json");
    }

    /**
     * Get path between start and goal nodes
     * @param start beginning node
     * @param goal ending node
     * @return path between the two
     */
    public static GraphPath<String, IdentifiedWeightedEdge> getPath2(String start, String goal){
        return DijkstraShortestPath.findPathBetween(ZooG, start, goal);
    }


    /**
     * Returns list of directions given path between two nodes
     *
     * @return List of directions
     */
    public static List<String> getDirectionsFromPath2(GraphPath<String, IdentifiedWeightedEdge> path){
        List<String> directions = new ArrayList<>();
        List<String> nodes = path.getVertexList();
        for(int i=0;i<nodes.size()-1;i++) {
            String curr = nodes.get(i);
            String next = nodes.get(i+1);
            IdentifiedWeightedEdge e = ZooG.getEdge(curr, next);
            String direction = String.format("Walk %.0f meters along %s from %s to %s.\n",
                    ZooG.getEdgeWeight(e),
                    eInfo.get(e.getId()).street,
                    curr,
                    next);
            directions.add(direction);
        }
        directions.add(String.format("You have arrived at %s.\n", path.getEndVertex()));
        return directions;
    }
//    public List<String> getExhibitOrders(GraphPath<String, IdentifiedWeightedEdge> path) {
//        List<String> order = new ArrayList<>();
//        List<String> nodes = path.getVertexList();
//        for(int i = 0; i < nodes.size()- 1; i++){
//            String next = nodes.get(i + 1);
//            order.add(next);
//        }
//        return order;
//    }
        //updated version of GetDirectionsFromPath2
        public static List<String> getDirectionsToExhibit(double lat, double lng, String end){
            var start = MainActivity.exhibitManager.getClosest(lat, lng).id;
            var cur_path = getPath2(start, end);
            return getDirectionsFromPath2(cur_path);
        }

    /**
     * Gets the order of the exhibits you need to visit.
     * @param path - the graph containing the path
     * @return simple List with the order the exhibits should be visited in
     */
    public List<String> getExhibitOrders(GraphPath<String, IdentifiedWeightedEdge> path) {
        List<String> order = new ArrayList<>();
        List<String> nodes = path.getVertexList();
        for(int i=0;i<nodes.size()-1;i++) {
            String next = nodes.get(i+1);
            order.add(next);
        }
        return order;
    }

    public List<String> getShortestPathOrder(List<String> exhibits, Exhibit start_ext) {
        List<String> copy = new ArrayList<>(exhibits);
        List<String> directions_list = new ArrayList<>();
        String start = start_ext.id;
        String curr_vertex = start;
        String next_vertex = null;

        while (! copy.isEmpty()){
            double min_weight = Double.MAX_VALUE;
            GraphPath<String, IdentifiedWeightedEdge> path_leg = null;
            for (String s: copy){
                Log.d("Animal", s);
                GraphPath<String, IdentifiedWeightedEdge> curr_leg = getPath2(curr_vertex, s);
                double curr_weight = curr_leg.getWeight();
                if (curr_weight < min_weight){
                    next_vertex = s;
                    min_weight = curr_weight;
                    path_leg = curr_leg;
                }
            }
            directions_list.add(next_vertex);
            curr_vertex = next_vertex;
            copy.remove(curr_vertex);
        }
        Log.d("Directions List", directions_list.toString());
        return directions_list;
    }

    /**
     * Use this function to get optimal path given list of exhibits to visit
     * @param vertexList List of exhibits to visit
     * @param start
     * @return Directions with shortest path between exhibits
     */
    public List<String> getShortestPath(List<String> vertexList, Exhibit start){
        List<String> copy = new ArrayList<>(vertexList);
        List<String> directions_list = new ArrayList<>();
        Log.d("getShortestPath start", start.name);
        String start_str = start.id;
        String curr_vertex = start_str;
        String next_vertex = null;

        while (! copy.isEmpty()){
            double min_weight = Double.MAX_VALUE;
            GraphPath<String, IdentifiedWeightedEdge> path_leg = null;
            for (String s: copy){
                GraphPath<String, IdentifiedWeightedEdge> curr_leg = getPath2(curr_vertex, s);
                double curr_weight = curr_leg.getWeight();
                if (curr_weight < min_weight){
                    next_vertex = s;
                    min_weight = curr_weight;
                    path_leg = curr_leg;
                }
            }
            curr_vertex = next_vertex;
            copy.remove(curr_vertex);
            directions_list.add(path_leg.getEndVertex());

        }
        return directions_list;
    }
}
