// src/components/FlowchartForm.js

import React, { useState } from "react"
import axios from "axios"

const FlowchartForm = ({
  onFlowchartCreated,
  onFlowchartUpdated,
  currentFlowchart,
}) => {
  const [nodes, setNodes] = useState(
    currentFlowchart ? currentFlowchart.nodes.join(", ") : ""
  )
  const [edges, setEdges] = useState(
    currentFlowchart ? currentFlowchart.edges.join(", ") : ""
  )
  const [errorMessage, setErrorMessage] = useState("")

  const handleSubmit = async (e) => {
    e.preventDefault()
    const flowchartData = {
      nodes: nodes.split(",").map((node) => node.trim()),
      edges: edges.split(",").map((edge) => edge.trim()),
    }

    try {
      if (currentFlowchart) {
        await axios.put(
          `http://localhost:8080/api/flowcharts/${currentFlowchart.id}`,
          flowchartData
        )
        onFlowchartUpdated({ ...currentFlowchart, ...flowchartData })
      } else {
        const response = await axios.post(
          "http://localhost:8080/api/flowcharts",
          flowchartData
        )
        onFlowchartCreated(response.data)
      }
      setNodes("")
      setEdges("")
      setErrorMessage("")
    } catch (error) {
      console.error("Error saving flowchart:", error)
      setErrorMessage(
        "Failed to save flowchart. Please check your input and try again."
      )
    }
  }

  return (
    <form onSubmit={handleSubmit} style={{ marginBottom: "20px" }}>
      <div>
        <label htmlFor="nodes">Nodes (comma-separated):</label>
        <input
          type="text"
          id="nodes"
          value={nodes}
          onChange={(e) => setNodes(e.target.value)}
          placeholder="e.g., Start, Process"
          required
          style={{ width: "100%", padding: "8px", marginTop: "5px" }}
        />
        <small>Enter node names separated by commas.</small>
      </div>
      <div style={{ marginTop: "15px" }}>
        <label htmlFor="edges">
          Edges (comma-separated, e.g., Start->Process):
        </label>
        <input
          type="text"
          id="edges"
          value={edges}
          onChange={(e) => setEdges(e.target.value)}
          placeholder="e.g., Start->Process"
          required
          style={{ width: "100%", padding: "8px", marginTop: "5px" }}
        />
        <small>
          Enter edges in the format "NodeA->NodeB" separated by commas.
        </small>
      </div>
      {errorMessage && <p style={{ color: "red" }}>{errorMessage}</p>}
      <button type="submit" style={{ marginTop: "15px", padding: "10px 15px" }}>
        {currentFlowchart ? "Update Flowchart" : "Create Flowchart"}
      </button>
    </form>
  )
}

export default FlowchartForm
