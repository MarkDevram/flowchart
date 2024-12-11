// src/components/DagreLayout.js

import { useEffect } from "react"
import dagre from "dagre"
import { useReactFlow } from "reactflow"

const DagreLayout = ({ nodes, edges }) => {
  const { setNodes } = useReactFlow()

  useEffect(() => {
    if (nodes.length > 0) {
      const g = new dagre.graphlib.Graph()
      g.setGraph({ rankdir: "TB" })
      g.setDefaultEdgeLabel(() => ({}))

      nodes.forEach((node) => {
        g.setNode(node.id, { width: 100, height: 50 })
      })

      edges.forEach((edge) => {
        g.setEdge(edge.source, edge.target)
      })

      dagre.layout(g)

      const layoutedNodes = nodes.map((node) => {
        const nodeWithPosition = g.node(node.id)
        return {
          ...node,
          position: {
            x: nodeWithPosition.x - 50,
            y: nodeWithPosition.y - 25,
          },
        }
      })

      setNodes(layoutedNodes)
    }
  }, [nodes, edges, setNodes])

  return null
}

export default DagreLayout
