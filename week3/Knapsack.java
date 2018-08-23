import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class Knapsack {

    private static int KNAPSACK_SIZE;
    private static int NUM_OF_ITEMS;
    private ArrayList<Integer> values;
    private ArrayList<Integer> weights;

    public void readData() throws IOException {

        File file = new File("knapsack1.txt");
        BufferedReader br = new BufferedReader(new FileReader(file));
        String input;

        if ((input = br.readLine()) != null) {
            KNAPSACK_SIZE = Integer.parseInt(input.split(" ")[0]);
            NUM_OF_ITEMS = Integer.parseInt(input.split(" ")[1]);
        }

        values = new ArrayList<>();
        weights = new ArrayList<>();

        while ((input = br.readLine()) != null) {
            int value = Integer.parseInt(input.split(" ")[0]);
            int weight = Integer.parseInt(input.split(" ")[1]);
            values.add(value);
            weights.add(weight);
        }
    }

    public int findMaxValue() {

        int[][] dp = new int[NUM_OF_ITEMS + 1][KNAPSACK_SIZE + 1];

        for (int i = 0; i <= NUM_OF_ITEMS; i++) {
            dp[i][0] = 0;
        }

        for (int j = 0; j <= KNAPSACK_SIZE; j++) {
            dp[0][j] = 0;
        }

        for (int i = 1; i <= NUM_OF_ITEMS; i++) {
            for (int j = 1; j <= KNAPSACK_SIZE; j++) {

                if (weights.get(i - 1) <= j) {
                    dp[i][j] = Math.max(dp[i - 1][j], dp[i - 1][j - weights.get(i - 1)] + values.get(i - 1));
                } else {
                    dp[i][j] = dp[i - 1][j];
                }
            }
        }

        return dp[NUM_OF_ITEMS][KNAPSACK_SIZE];
    }

    public static void main(String[] args) throws Exception {
        Knapsack obj = new Knapsack();
        obj.readData();
        int result = obj.findMaxValue();

        System.out.println(result);
    }
}
