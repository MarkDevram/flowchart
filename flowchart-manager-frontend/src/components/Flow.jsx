import { useEffect, useState, useCallback } from "react"
import ReactFlow, {
  Controls,
  MiniMap,
  Background,
  useNodesState,
  useEdgesState,
  addEdge,
} from "reactflow"
import axios from "axios"
import FlowchartForm from "./FlowchartForm"
import DagreLayout from "./DagreLayout"
import "reactflow/dist/style.css"

const Flow = () => {
  const [flowcharts, setFlowcharts] = useState([])
  const [currentChartId, setCurrentChartId] = useState(null)
  const [nodes, setNodes, onNodesChange] = useNodesState([])
  const [edges, setEdges, onEdgesChange] = useEdgesState([])

  useEffect(() => {
    fetchFlowcharts()
  }, [])

  const fetchFlowcharts = async () => {
    try {
      const response = await axios.get("http://localhost:8080/api/flowcharts")
      setFlowcharts(response.data)
    } catch (error) {
      console.error("Error fetching flowcharts:", error)
    }
  }

  const handleFlowchartCreated = async (newFlowchart) => {
    try {
      const response = await axios.post(
        "http://localhost:8080/api/flowcharts",
        newFlowchart
      )
      setFlowcharts((prevCharts) => [...prevCharts, response.data])
    } catch (error) {
      console.error("Error creating flowchart:", error)
    }
  }

  const handleEditClick = (flowchart) => {
    setCurrentChartId(flowchart.id)

    const updatedNodes = flowchart.nodes.map((node, index) => ({
      id: `${index + 1}`, // Ensure unique IDs for nodes
      data: { label: node },
      position: { x: Math.random() * 400, y: Math.random() * 400 },
    }))

    const updatedEdges = flowchart.edges.map((edge) => ({
      id: edge,
      source: edge.split("->")[0],
      target: edge.split("->")[1],
      animated: true,
    }))

    setNodes(updatedNodes)
    setEdges(updatedEdges)
  }

  const handleUpdateChart = async () => {
    if (currentChartId) {
      const updatedChartData = {
        id: currentChartId,
        nodes: nodes.map((node) => node.data.label),
        edges: edges.map((edge) => `${edge.source}->${edge.target}`),
      }

      console.log("Updating chart with data:", updatedChartData)

      try {
        await axios.put(
          `http://localhost:8080/api/flowcharts/${currentChartId}`,
          updatedChartData
        )
        alert("Flowchart updated successfully!")
        fetchFlowcharts()
      } catch (error) {
        console.error(
          "Error updating flowchart:",
          error.response ? error.response.data : error.message
        )
        alert("Failed to update flowchart. Please check console for details.")
      }
    }
  }

  const handleDeleteClick = async (flowchartId) => {
    try {
      await axios.delete(`http://localhost:8080/api/flowcharts/${flowchartId}`)
      setFlowcharts((prevCharts) =>
        prevCharts.filter((chart) => chart.id !== flowchartId)
      )
    } catch (error) {
      console.error("Error deleting flowchart:", error)
    }
  }

  const onConnect = useCallback(
    (params) => {
      const existingEdge = edges.find(
        (edge) => edge.source === params.source && edge.target === params.target
      )

      if (!existingEdge) {
        setEdges((eds) => addEdge(params, eds))
      } else {
        alert("This connection already exists!")
      }
    },
    [edges]
  )

  return (
    <div style={{ height: "100vh" }}>
      <h2>Manage Flowcharts</h2>
      <FlowchartForm
        onFlowchartCreated={handleFlowchartCreated}
        onFlowchartUpdated={handleUpdateChart}
        currentFlowchart={flowcharts.find(
          (chart) => chart.id === currentChartId
        )}
      />

      <div
        style={{
          display: "flex",
          flexDirection: "column",
          marginBottom: "20px",
        }}
      >
        {flowcharts.map((flowchart) => (
          <div key={flowchart.id}>
            <button onClick={() => handleEditClick(flowchart)}>
              Edit {flowchart.nodes.join(", ")}
            </button>
            <button
              onClick={() => handleDeleteClick(flowchart.id)}
              style={{ marginLeft: "10px" }}
            >
              Delete
            </button>
          </div>
        ))}
      </div>

      {currentChartId && (
        <ReactFlow
          nodes={nodes}
          edges={edges}
          onNodesChange={onNodesChange}
          onEdgesChange={onEdgesChange}
          onConnect={onConnect}
          style={{ border: "1px solid #ddd", height: "400px" }}
        >
          <DagreLayout nodes={nodes} edges={edges} />
          <Controls />
          <MiniMap />
          <Background variant="dots" gap={12} size={1} />
        </ReactFlow>
      )}
      {/* Update button to save changes */}
      {currentChartId && (
        <button
          onClick={handleUpdateChart}
          style={{ marginTop: "15px", padding: "10px 15px" }}
        >
          Update Flowchart
        </button>
      )}
    </div>
  )
}

export default Flow
