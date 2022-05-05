package com.example.zooseeker;

import android.content.Context;

import org.jgrapht.Graph;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ZooGraph {
    public Map<String, ZooData.VertexInfo> vInfo;
    public Map<String, ZooData.EdgeInfo> eInfo;
    public Graph<String, IdentifiedWeightedEdge> ZooG;

    public ZooGraph(Context context){
        // 1. Load the graph...
         this.ZooG = ZooData.loadZooGraphJSON(context,"sample_zoo_graph.json");
        // 2. Load the information about our nodes and edges...
        this.vInfo = ZooData.loadVertexInfoJSON(context,"sample_node_info.json");
        this.eInfo = ZooData.loadEdgeInfoJSON(context,"sample_edge_info.json");
    }

    /**
     * Get path between start and goal nodes
     * @param start
     * @param goal
     * @return path
     */
    public GraphPath<String, IdentifiedWeightedEdge> getPath2(String start, String goal){
        return DijkstraShortestPath.findPathBetween(this.ZooG, start, goal);
    }


    /**
     * Returns list of directions given path between two nodes
     * @param path path between two nodes
     * @return List of directions
     */
    public List<String> getDirectionsFromPath2(GraphPath<String, IdentifiedWeightedEdge> path){
        List<String> directions = new ArrayList<>();
//        for (IdentifiedWeightedEdge e : path.getEdgeList()){
//            String direction = String.format("Walk %.0f meters along %s from '%s' to '%s'.\n",
//                    this.ZooG.getEdgeWeight(e),
//                    eInfo.get(e.getId()).street,
//                    vInfo.get(this.ZooG.getEdgeSource(e).toString()).name,
//                    vInfo.get(this.ZooG.getEdgeTarget(e).toString()).name);
//            directions.add(direction);
//        }
        List<String> nodes = path.getVertexList();
        for(int i=0;i<nodes.size()-1;i++) {
            String curr = nodes.get(i);
            String next = nodes.get(i+1);
            IdentifiedWeightedEdge e = this.ZooG.getEdge(curr, next);
            String direction = String.format("Walk %.0f meters along %s from '%s' to '%s'.\n",
                    this.ZooG.getEdgeWeight(e),
                    eInfo.get(e.getId()).street,
                    curr,
                    next);
            directions.add(direction);
        }
        return directions;
    }

    /**
     * Use this function to get optimal path given list of exhibits to visit
     * @param vertexList List of exhibits to visit
     * @return Directions with shortest path between exhibits
     */
    public List<String> getShortestPath(List<String> vertexList){
        List<String> copy = new ArrayList<>(vertexList);
        List<String> directions_list = new ArrayList<>();
        String start = "entrance_exit_gate";
        String curr_vertex = start;
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
            directions_list.addAll(getDirectionsFromPath2(path_leg));
        }
        return  directions_list;
    }

}
