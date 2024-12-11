package com.example.flowchartmanager.repository;

import com.example.flowchartmanager.model.Flowchart;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FlowchartRepository extends JpaRepository<Flowchart, Long> {
}
