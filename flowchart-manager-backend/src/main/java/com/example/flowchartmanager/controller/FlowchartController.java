package com.example.flowchartmanager.controller;

import com.example.flowchartmanager.model.Flowchart;
import com.example.flowchartmanager.service.FlowchartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/flowcharts")
public class FlowchartController {

    @Autowired
    private FlowchartService flowchartService;

    // Create a new flowchart
    @PostMapping
    public ResponseEntity<Flowchart> createFlowchart(@RequestBody Flowchart flowchart) {
        Flowchart createdFlowchart = flowchartService.createFlowchart(flowchart);
        return new ResponseEntity<>(createdFlowchart, HttpStatus.CREATED);
    }

    // Get all flowcharts
    @GetMapping
    public ResponseEntity<List<Flowchart>> getAllFlowcharts() {
        List<Flowchart> flowcharts = flowchartService.getAllFlowcharts();
        return new ResponseEntity<>(flowcharts, HttpStatus.OK);
    }

    // Get a specific flowchart by ID
    @GetMapping("/{id}")
    public ResponseEntity<Flowchart> getFlowchartById(@PathVariable Long id) {
        Flowchart flowchart = flowchartService.getFlowchartById(id);
        if (flowchart != null) {
            return new ResponseEntity<>(flowchart, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // Update an existing flowchart
    @PutMapping("/{id}")
    public ResponseEntity<Flowchart> updateFlowchart(@PathVariable Long id, @RequestBody Flowchart flowchart) {
        Flowchart updatedFlowchart = flowchartService.updateFlowchart(id, flowchart);
        if (updatedFlowchart != null) {
            return new ResponseEntity<>(updatedFlowchart, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // Delete a flowchart by ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFlowchart(@PathVariable Long id) {
        flowchartService.deleteFlowchart(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    // Get outgoing edges from a specific node in a flowchart
    @GetMapping("/{id}/edges/{nodeId}")
    public ResponseEntity<List<String>> getOutgoingEdges(@PathVariable Long id, @PathVariable String nodeId) {
        Flowchart flowchart = flowchartService.getFlowchartById(id);
        if (flowchart != null && flowchart.getNodes().contains(nodeId)) {
            List<String> outgoingEdges = flowchart.getEdges().stream()
                    .filter(edge -> edge.startsWith(nodeId + "->"))
                    .toList();
            return new ResponseEntity<>(outgoingEdges, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // Get connected nodes for a specific node in a flowchart
    @GetMapping("/{id}/connected-nodes/{nodeId}")
    public ResponseEntity<Set<String>> getConnectedNodes(@PathVariable Long id, @PathVariable String nodeId) {
        Set<String> connectedNodes = flowchartService.findConnectedNodes(id, nodeId);
        return new ResponseEntity<>(connectedNodes, HttpStatus.OK);
    }
}
