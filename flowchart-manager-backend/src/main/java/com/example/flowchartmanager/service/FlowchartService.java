package com.example.flowchartmanager.service;

import com.example.flowchartmanager.model.Flowchart;
import com.example.flowchartmanager.repository.FlowchartRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class FlowchartService {

    @Autowired
    private FlowchartRepository flowchartRepository;

//    private boolean isValidGraph(Set<String> nodes, Set<String> edges) {
//        // Check if nodes and edges are not null or empty
//        if (nodes == null || edges == null || nodes.isEmpty() || edges.isEmpty()) {
//            return false; // Invalid graph structure
//        }
//
//        // Check if all edges reference existing nodes
//        Set<String> uniqueEdges = new HashSet<>();
//        for (String edge : edges) {
//            String[] parts = edge.split("->");
//            if (parts.length != 2 || !nodes.contains(parts[0]) || !nodes.contains(parts[1])) {
//                return false; // Invalid edge
//            }
//            // Check for duplicate edges
//            if (!uniqueEdges.add(edge)) {
//                return false; // Duplicate edge found
//            }
//        }
//        return true; // Valid graph
//    }
private boolean isValidGraph(Set<String> nodes, Set<String> edges) {
    // Check if nodes and edges are not null or empty
    if (nodes == null || edges == null || nodes.isEmpty() || edges.isEmpty()) {
        return false; // Invalid graph structure
    }

    // Create a set to track unique edges
    Set<String> uniqueEdges = new HashSet<>();

    for (String edge : edges) {
        String[] parts = edge.split("->");
        if (parts.length != 2 || !nodes.contains(parts[0]) || !nodes.contains(parts[1])) {
            return false; // Invalid edge
        }
        // Check for duplicate edges
        if (!uniqueEdges.add(edge)) {
            return false; // Duplicate edge found
        }
    }
    return true; // Valid graph
}

    public Flowchart createFlowchart(Flowchart flowchart) {
        if (!isValidGraph(flowchart.getNodes(), flowchart.getEdges())) {
            throw new IllegalArgumentException("Invalid graph structure.");
        }
        return flowchartRepository.save(flowchart);
    }

    public List<Flowchart> getAllFlowcharts() {
        return flowchartRepository.findAll();
    }

    public Flowchart getFlowchartById(Long id) {
        return flowchartRepository.findById(id).orElse(null);
    }

    public Flowchart updateFlowchart(Long id, Flowchart flowchart) {
        // Check if the flowchart exists before updating
        Flowchart existingFlowchart = getFlowchartById(id);
        if (existingFlowchart == null) {
            throw new IllegalArgumentException("Flowchart with ID " + id + " does not exist.");
        }

        if (!isValidGraph(flowchart.getNodes(), flowchart.getEdges())) {
            throw new IllegalArgumentException("Invalid graph structure.");
        }

        // Set the ID for the updated flowchart
        flowchart.setId(id);

        // Save updated flowchart to the repository
        return flowchartRepository.save(flowchart);
    }

    public void deleteFlowchart(Long id) {
        Flowchart existingFlowchart = getFlowchartById(id);
        if (existingFlowchart != null) {
            flowchartRepository.deleteById(id);
        } else {
            throw new IllegalArgumentException("Flowchart with ID " + id + " does not exist.");
        }
    }

    public Set<String> findConnectedNodes(Long id, String nodeId) {
        Flowchart flowchart = getFlowchartById(id);
        Set<String> connectedNodes = new HashSet<>();

        if (flowchart != null && flowchart.getNodes().contains(nodeId)) {
            findConnectedNodesRecursively(flowchart.getEdges(), nodeId, connectedNodes);
        }

        return connectedNodes;
    }

    private void findConnectedNodesRecursively(Set<String> edges, String currentNode, Set<String> visited) {
        visited.add(currentNode);

        for (String edge : edges) {
            String[] parts = edge.split("->");
            if (parts[0].equals(currentNode) && !visited.contains(parts[1])) {
                findConnectedNodesRecursively(edges, parts[1], visited);
            }
        }
    }
}
