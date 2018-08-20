/**
 * Use Kruskal's MST algorithm for clustering. Implement Union Find
 * data structure for combining nodes.
 */

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

public class Clustering {

    private static class Edge implements Comparable<Edge> {
        int vertex1;
        int vertex2;
        int weight;

        public Edge(int vertex1, int vertex2, int weight) {
            this.vertex1 = vertex1;
            this.vertex2 = vertex2;
            this.weight = weight;
        }

        @Override
        public int compareTo(Edge that) {
            return this.weight - that.weight;
        }
    }

    /**
     * Use code QuickFindUF.java from Princeton University.
     * https://algs4.cs.princeton.edu/15uf/QuickFindUF.java.html
     */
    private static class UnionFind {
        private int[] id;
        private int count;

        public UnionFind(int n) {
            count = n;
            id = new int[n];
            for (int i = 0; i < n; i++) {
                id[i] = i;
            }
        }

        public int getCount() {
            return count;
        }

        public void validate(int p) {
            int n = id.length;
            if (p < 0 || p >= n) {
                throw new IllegalArgumentException("index " + p + " is not between 0 and " + (n-1));
            }
        }

        public int find(int p) {
            validate(p);
            return id[p];
        }

        public boolean connected(int p, int q) {
            validate(p);
            validate(q);
            return id[p] == id[q];
        }

        public void union(int p, int q) {
            validate(p);
            validate(q);
            int pID = id[p];
            int qID = id[q];

            if (pID == qID) return;

            for (int i = 0; i < id.length; i++) {
                if (id[i] == pID) id[i] = qID;
            }
            count--;
        }
    }

    private static final int K = 4;
    private static UnionFind uf = null;

    public ArrayList<Edge> readData() throws IOException {

        File file = new File("clustering1.txt");
        BufferedReader br = new BufferedReader(new FileReader(file));
        String input;
        ArrayList<Edge> edgeList = new ArrayList<>();

        if ((input = br.readLine()) != null) {
            int n = Integer.parseInt(input);
            uf = new UnionFind(n);
        }

        while ((input = br.readLine()) != null) {
            String[] inputs = input.split(" ");
            int v1 = Integer.parseInt(inputs[0]);
            int v2 = Integer.parseInt(inputs[1]);
            int w = Integer.parseInt(inputs[2]);
            edgeList.add(new Edge(v1 - 1, v2 - 1, w));
        }

        return edgeList;
    }

    private int clustering(ArrayList<Edge> edgeList) {
        Collections.sort(edgeList);

        int index = 0;

        while (uf.getCount() > K) {
            Edge edge = edgeList.get(index++);
            if (!uf.connected(edge.vertex1, edge.vertex2)) {
                uf.union(edge.vertex1, edge.vertex2);
            }
        }

        // The next edge that in two clusters is the minimum spacing
        for (int i = index; i < edgeList.size(); i++) {
            Edge edge = edgeList.get(index++);
            if (!uf.connected(edge.vertex1, edge.vertex2)) {
                return edge.weight;
            }
        }

        return 0;
    }

    public static void main (String[] args) throws Exception {
        Clustering obj = new Clustering();
        ArrayList<Edge> edgeList = obj.readData();
        int result = obj.clustering(edgeList);

        System.out.println(result);
    }
}
