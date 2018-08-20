import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.*;

public class Prim {
	
	public static void main(String[] args) throws Exception{
		
		File file = new File("edges.txt");
		BufferedReader br = new BufferedReader(new FileReader(file));
		String[] num = br.readLine().split(" ");
		int numberOfNodes = Integer.parseInt(num[0]);
		int numberOfEdges = Integer.parseInt(num[1]);
		int[][] cost = new int[numberOfNodes + 1][numberOfNodes + 1];
		HashMap<Integer, ArrayList<Integer>> node = new HashMap<Integer, ArrayList<Integer>>();
		
		for(int i = 0; i <= numberOfNodes; i++)
			for(int j = 0; j <= numberOfNodes; j++) {
			cost[i][j] = Integer.MAX_VALUE;
		}
		
		String tmp;
		int start, end;
		while((tmp = br.readLine()) != null){
			String[] input = tmp.split(" ");
			start = Integer.parseInt(input[0]);
			end = Integer.parseInt(input[1]);
			
			if(node.containsKey(start)) {
				ArrayList<Integer> list = node.get(start);
				list.add(end);
				node.put(start, list);
			}else {
				ArrayList<Integer> list = new ArrayList<Integer>();
				list.add(end);
				node.put(start, list);
			}
			
			if(node.containsKey(end)) {
				ArrayList<Integer> list = node.get(end);
				list.add(start);
				node.put(end, list);
			}else {
				ArrayList<Integer> list = new ArrayList<Integer>();
				list.add(start);
				node.put(end, list);
			}
			
			cost[start][end] = Integer.parseInt(input[2]);
			cost[end][start] = Integer.parseInt(input[2]);
		}
		br.close();
		
		//compute the mst
		ArrayList<Integer> explored = new ArrayList<Integer>();
		explored.add(1);
		long mst = 0;
		while(explored.size() < numberOfNodes) {
			int minEdge = Integer.MAX_VALUE;
			int exploreNode = Integer.MAX_VALUE;
			
			for(Integer e : explored) {
				List<Integer> endNode = node.get(e);
				for(Integer s : endNode) {
					if(!explored.contains(s)) {
						if(cost[e][s] < minEdge) {
							minEdge = cost[e][s];
							exploreNode = s;
						}
						
					}
				}
				
			}
			mst += minEdge;
			explored.add(exploreNode);
			
		}
		
		System.out.println("MST is:" + mst);
	}
}
