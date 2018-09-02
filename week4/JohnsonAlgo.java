/**
 * Implement Johnson's algorithm to solve all pairs
 * shortest path problems.
 */

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.PriorityQueue;

public class JohnsonAlgo {

    /**
     * The Edge class represents a weighted edge in
     * a directed graph.
     */
    private static class Edge {
        private int tail;
        private int head;
        private int weight;

        public Edge(int tail, int head, int weight) {
            this.tail = tail;
            this.head = head;
            this.weight = weight;
        }
    }

    /**
     * The Graph class represents a directed graph.
     */
    private static class Graph {
        private int numOfNodes;
        private int numOfEdges;
        private Edge[] edges;
        private int[][] adj;
        private int[] distance;

        public Graph(int numOfNodes, int numOfEdges, Edge[] edges) {
            this.numOfNodes = numOfNodes;
            this.numOfEdges = numOfEdges;
            this.edges = edges;
            this.adj = new int[numOfNodes + 1][numOfNodes + 1];
            this.distance = new int[numOfNodes + 1];

            for (int[] array : adj) {
                Arrays.fill(array, -1);
            }

            for (int i = 1; i < distance.length; i++) {
                distance[i] = Integer.MAX_VALUE;
            }
        }
    }

    /**
     * The Node class represents a node with label
     * and distance from source.
     */
    private static class Node {
        private int vertex;
        private int distance;

        public Node(int vertex) {
            this.vertex = vertex;
            this.distance = Integer.MAX_VALUE;
        }

        public void setDistance(int distance) {
            this.distance = distance;
        }
    }

    public Graph readData(String filename) throws IOException {
        File file = new File(filename);
        BufferedReader br = new BufferedReader(new FileReader(file));
        String input;
        int numOfNodes = 0;
        int numOfEdges = 0;

        if ((input = br.readLine()) != null) {
            numOfNodes = Integer.parseInt(input.split(" ")[0]);
            numOfEdges = Integer.parseInt(input.split(" ")[1]);
        }

        Edge[] edges = new Edge[numOfEdges + numOfNodes];

        for (int i = 0; i < numOfNodes; i++) {
            edges[i] = new Edge(0, i + 1, 0);
        }

        int index = numOfNodes;
        while ((input = br.readLine()) != null) {
            String[] inputs = input.split(" ");

            int tail = Integer.parseInt(inputs[0]);
            int head = Integer.parseInt(inputs[1]);
            int weight = Integer.parseInt(inputs[2]);

            edges[index++] = new Edge(tail, head, weight);
        }

        Graph graph = new Graph(numOfNodes, numOfEdges, edges);
        return graph;
    }

    public boolean bellmanFord(Graph graph) {
        int numOfNodes = graph.numOfNodes + 1;

        for (int i = 0; i < numOfNodes - 1; i++) {
            for (Edge edge : graph.edges) {
                int sum = edge.weight + graph.distance[edge.tail];
                if (sum < graph.distance[edge.head]) {
                    graph.distance[edge.head] = sum;
                }
            }
        }

        for (Edge edge: graph.edges) {
            int sum = edge.weight + graph.distance[edge.tail];
            if (sum < graph.distance[edge.head]) {
                return false;
            }
        }

        return true;
    }

    public void reweighting(Graph graph) {
        int[] distance = graph.distance;

        for (Edge edge : graph.edges) {
            int tail = edge.tail;
            int head = edge.head;
            graph.adj[tail][head] = edge.weight + distance[tail] - distance[head];
        }
    }

    public int dijkstra(Graph graph) {

        int numOfNodes = graph.numOfNodes;
        int[] distance = graph.distance;
        int[][] adj = graph.adj;
        int minPath = Integer.MAX_VALUE;

        for (int i = 1; i <= numOfNodes; i++) {

            Node[] nodes = new Node[numOfNodes + 1];

            PriorityQueue<Node> pq = new PriorityQueue<>((a, b) -> (a.distance - b.distance));

            for (int v = 1; v <= numOfNodes; v++) {
                nodes[v] = new Node(v);
                if (v == i) {
                    nodes[v].setDistance(0);
                }
                pq.offer(nodes[v]);
            }

            while (pq.size() > 0) {
                Node node = pq.poll();

                for (int j = 1; j <= numOfNodes; j++) {
                    if (node.vertex == j) continue;
                    if (adj[node.vertex][j] >= 0) {
                        int sum = adj[node.vertex][j] + nodes[node.vertex].distance;
                        if (sum < nodes[j].distance) {
                            pq.remove(nodes[j]);
                            nodes[j].setDistance(sum);
                            pq.offer(nodes[j]);
                        }
                    }
                }
            }

            for (int k = 1; k <= numOfNodes; k++) {
                if (i == k) continue;
                minPath = Math.min(minPath, nodes[k].distance - distance[i] + distance[k]);
            }
        }

        return minPath;
    }

    public static void run(JohnsonAlgo obj, String filename) throws Exception {
        System.out.println("Find shortest path for " + filename + ":");
        Graph graph = obj.readData(filename);
        boolean noNegativeCycle = obj.bellmanFord(graph);
        if (noNegativeCycle) {
            obj.reweighting(graph);
            int result = obj.dijkstra(graph);
            System.out.println("The shortest path is " + result);
        } else {
            System.out.println("The graph contains negative cycles");
        }
    }


    public static void main(String[] args) throws Exception {
        JohnsonAlgo obj = new JohnsonAlgo();
        run(obj, "g1.txt");
        run(obj, "g2.txt");
        run(obj, "g3.txt");
    }
}
