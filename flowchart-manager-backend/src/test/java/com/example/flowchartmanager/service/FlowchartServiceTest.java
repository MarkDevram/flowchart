package com.example.flowchartmanager.service;

import com.example.flowchartmanager.model.Flowchart;
import com.example.flowchartmanager.repository.FlowchartRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class FlowchartServiceTest {

    @InjectMocks
    private FlowchartService flowchartService;

    @Mock
    private FlowchartRepository flowchartRepository;

    private Flowchart flowchart;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        flowchart = new Flowchart();
        flowchart.setNodes(new HashSet<>(Set.of("A", "B", "C")));
        flowchart.setEdges(new HashSet<>(Set.of("A->B", "B->C")));
    }

    @Test
    void testCreateFlowchart() {
        when(flowchartRepository.save(any(Flowchart.class))).thenReturn(flowchart);
        Flowchart createdFlowchart = flowchartService.createFlowchart(flowchart);
        assertNotNull(createdFlowchart);
        assertEquals(flowchart.getNodes(), createdFlowchart.getNodes());
    }

    @Test
    void testFindConnectedNodes() {
        when(flowchartRepository.findById(1L)).thenReturn(java.util.Optional.of(flowchart));
        var connectedNodes = flowchartService.findConnectedNodes(1L, "A");
        assertTrue(connectedNodes.contains("B"));
        assertTrue(connectedNodes.contains("C"));
    }
}
