import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class KnapsackBig {

    private static int KNAPSACK_SIZE;
    private static int NUM_OF_ITEMS;
    private ArrayList<Integer> values;
    private ArrayList<Integer> weights;

    public void readData() throws IOException {

        File file = new File("knapsack_big.txt");
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

        int[] dp = new int[KNAPSACK_SIZE + 1];

        for (int i = 0; i <= KNAPSACK_SIZE; i++) {
            dp[i] = 0;
        }

        for (int i = 0; i < NUM_OF_ITEMS; i++) {
            int[] tmp = new int[KNAPSACK_SIZE + 1];

            for (int j = 1; j <= KNAPSACK_SIZE; j++) {
                if (weights.get(i) <= j) {
                    tmp[j] = Math.max(dp[j], dp[j - weights.get(i)] + values.get(i));
                } else {
                    tmp[j]= dp[j];
                }
            }
            dp = tmp;
        }

        return dp[KNAPSACK_SIZE];
    }

    public static void main(String[] args) throws Exception {
        KnapsackBig obj = new KnapsackBig();
        obj.readData();
        int result = obj.findMaxValue();

        System.out.println(result);
    }

}
