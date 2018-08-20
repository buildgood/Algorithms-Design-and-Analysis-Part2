/**
 * For each node, union it with node that has the same bits or has
 * the Hamming distance of 1 or 2.
 *
 * The masks are generated for getting bits with 1 or 2 Hamming
 * distance of the input bits.
 */

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class ClusteringBig {

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

    private static int numOfNodes;
    private static int numOfBits;
    private static UnionFind uf = null;
    private static ArrayList<String> mask = null;

    public HashMap<String, ArrayList<Integer>> readData() throws IOException {
        File file = new File("clustering_big.txt");
        BufferedReader br = new BufferedReader(new FileReader(file));
        String input;

        if ((input = br.readLine()) != null) {
            numOfNodes = Integer.parseInt(input.split(" ")[0]);
            numOfBits = Integer.parseInt(input.split(" ")[1]);
            uf = new UnionFind(numOfNodes);
        }

        HashMap<String, ArrayList<Integer>> map = new HashMap<>();
        int index = 0;

        while ((input = br.readLine()) != null) {
            String key = input.replaceAll("\\s+", "");
            if (!map.containsKey(key)) {
                map.put(key, new ArrayList<>());
            }
            map.get(key).add(index++);
        }
        return map;
    }

    private void generateMask() {
        mask = new ArrayList<>();
        String[] temp = new String[numOfBits];
        for (int i = 0; i < numOfBits; i++) {
            temp[i] = "0";
        }
        for (int i = 0; i < numOfBits; i++) {
            temp[i] = "1";
            mask.add(String.join("", temp));
            for (int j = i + 1; j < numOfBits; j++) {
                temp[j] = "1";
                mask.add(String.join("", temp));
                temp[j] = "0";
            }
            temp[i] = "0";
        }
    }

    private ArrayList<String> getCloseKey(String key) {
        ArrayList<String> closeKeys = new ArrayList<>();

        for (String maskKey: mask) {
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < numOfBits; i++) {
                if (key.charAt(i) == maskKey.charAt(i)) {
                    sb.append("0");
                } else {
                    sb.append("1");
                }
            }
            closeKeys.add(sb.toString());
        }
        return closeKeys;
    }

    public int clustering(HashMap<String, ArrayList<Integer>> map) {
        generateMask();

        for (String key: map.keySet()) {
            ArrayList<Integer> nodes = map.get(key);
            int node = nodes.get(0);
            for (int i = 1; i < nodes.size(); i++) {
                uf.union(node, nodes.get(i));
            }
            ArrayList<String> closeKeys = getCloseKey(key);
            for (String closeKey: closeKeys) {
                ArrayList<Integer> closeNodes = map.get(closeKey);
                if (closeNodes != null) {
                    for (int i = 0; i < closeNodes.size(); i++) {
                        uf.union(node, closeNodes.get(i));
                    }
                }
            }
        }

        return uf.getCount();
    }

    public static void main(String[] args) throws Exception {
        ClusteringBig obj = new ClusteringBig();
        HashMap<String, ArrayList<Integer>> map = obj.readData();
        int result = obj.clustering(map);

        System.out.println(result);
    }
}
