/**
 * For the TSP problem, pre-calculate the distance between each city and
 * store the values in a 2-D matrix.
 *
 * For dp, calculate dp[{k}, j] based on dp[{k - 1}, j} where k represents
 * the number of cities that visited from 0 to j.
 *
 * Use binary number to represent cities that have been visited, which are
 * marked as 1. The next round of the binary combinations are generated
 * from the previous round. Also use index maps to index each combination.
 */

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class TSP {

    private int SIZE = (int) (3 * Math.pow(10, 6));
    private int NUM_OF_CITY;
    private Map<Integer, Integer> prevIndexMap = new HashMap<>();
    private Map<Integer, Integer> curIndexMap = new HashMap<>();
    private int curIndexOfMap = 0;

    public double[][] readData() throws IOException {
        File file = new File("tsp.txt");
        BufferedReader br = new BufferedReader(new FileReader(file));
        String input;

        if((input = br.readLine()) != null) {
            NUM_OF_CITY = Integer.parseInt(input);
        }

        double[] x = new double[NUM_OF_CITY];
        double[] y = new double[NUM_OF_CITY];

        int index = 0;
        while((input = br.readLine()) != null) {
            String[] inputs = input.split(" ");

            x[index] = Double.parseDouble(inputs[0]);
            y[index] = Double.parseDouble(inputs[1]);
            index++;
        }

        double[][] distance = new double[NUM_OF_CITY][NUM_OF_CITY];

        for (int i = 0; i < NUM_OF_CITY; i++) {
            for (int j = i + 1; j < NUM_OF_CITY; j++) {
                distance[i][j] = Math.sqrt(Math.pow(x[i] - x[j], 2.0) + Math.pow(y[i] - y[j], 2.0));
                distance[j][i] = distance[i][j];
            }
        }
        return distance;
    }

    public int getPrevIndex(int key) {
        return prevIndexMap.get(key);
    }

    public int getCurIndex(int key) {
        if (!curIndexMap.containsKey(key)) {
            curIndexMap.put(key, curIndexOfMap);
            curIndexOfMap++;
        }
        return curIndexMap.get(key);
    }

    public double tsp(double[][] distance) {

        double[][] prevDp = new double[SIZE][NUM_OF_CITY - 1];
        double[][] curDp = new double[SIZE][NUM_OF_CITY - 1];
        Set<Integer> prevNum = new HashSet<>();
        Set<Integer> curNum = new HashSet<>();

        for (int i = 0; i < NUM_OF_CITY - 1; i++) {
            int key = 1 << i;
            int index = getCurIndex(key);
            curDp[index][i] = distance[0][i + 1];
            curNum.add(key);
        }

        for (int i = 2; i <= NUM_OF_CITY - 1; i++) {
            prevDp = curDp;
            prevNum = curNum;
            prevIndexMap = curIndexMap;
            curDp = new double[SIZE][NUM_OF_CITY - 1];
            curNum = new HashSet<>();
            curIndexMap = new HashMap<>();
            curIndexOfMap = 0;

            for (int key : prevNum) {

                for (int k = 0; k < NUM_OF_CITY - 1; k++) {
                    if ((key & (1 << k)) != 0) continue;
                    int curKey = key | (1 << k);
                    for (int m = 0; m < NUM_OF_CITY - 1; m++) {
                        if ((key & (1 << m)) == 0) continue;
                        int index = getPrevIndex(key);
                        int curIndex = getCurIndex(curKey);

                        if (curDp[curIndex][k] == 0 || prevDp[index][m] + distance[m + 1][k + 1] <  curDp[curIndex][k]) {
                            curDp[curIndex][k] = prevDp[index][m] + distance[m + 1][k + 1];
                        }
                    }
                    curNum.add(curKey);
                }
            }
        }

        int key = (1 << NUM_OF_CITY - 1) - 1;
        int index = getCurIndex(key);
        double minDist = Double.MAX_VALUE;
        for (int i = 0; i < NUM_OF_CITY - 1; i++) {
            minDist = Math.min(minDist, curDp[index][i] + distance[i + 1][0]);
        }
        return minDist;
    }

    public static void main(String[] args) throws Exception {
        TSP obj = new TSP();
        double[][] distance = obj.readData();
        double result = obj.tsp(distance);

        System.out.println(result);
    }
}

