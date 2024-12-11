package com.example.flowchartmanager.model;


import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

@Entity
public class Flowchart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ElementCollection
    private Set<String> nodes = new HashSet<>();

    @ElementCollection
    private Set<String> edges = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Set<String> getNodes() {
        return nodes;
    }

    public void setNodes(Set<String> nodes) {
        this.nodes = nodes;
    }

    public Set<String> getEdges() {
        return edges;
    }

    public void setEdges(Set<String> edges) {
        this.edges = edges;
    }
}
