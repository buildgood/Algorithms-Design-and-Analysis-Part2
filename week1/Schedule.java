import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.*;
import java.lang.*;

public class Schedule {
	
	public static void main(String[] args) throws Exception{
		File file = new File("jobs.txt");
		BufferedReader br = new BufferedReader(new FileReader(file));
		int numberOfJobs = Integer.parseInt(br.readLine());
		List<int[]> list = new ArrayList<int[]>();
		String tmp;
		while((tmp = br.readLine()) != null){
			String[] input = tmp.split(" ");
			int[] job = new int[2];
			job[0] = Integer.parseInt(input[0]);
			job[1] = Integer.parseInt(input[1]);
			list.add(job);
		}
		br.close();
		
		//sort by weight - length
		Collections.sort(list, new Comparator<int[]>() {
			@Override
			public int compare(int[] o1, int[] o2) {
				if((o1[0] - o1[1]) == (o2[0] - o2[1])) {
					return o2[0] - o1[0];
				}else {
					return (o2[0] - o2[1]) - (o1[0] - o1[1]);
				}
			}
		});
		
		long sum = 0;
		long time = 0;
		for(int i = 0; i < numberOfJobs; i++) {
			time += list.get(i)[1];
			sum += list.get(i)[0] * time;
		}
		
		System.out.println("weight-length:" + sum);
		
		//sort by weight/length
		Collections.sort(list, new Comparator<int[]>() {
			@Override
			public int compare(int[] o1, int[] o2) {
				if((o1[0] / (1.0 * o1[1])) >= (o2[0] / (1.0 * o2[1])))
					return -1;
				else
					return 1;
			}
		});
		
		sum = 0;
		time = 0;
		for(int i = 0; i < numberOfJobs; i++) {
			time += list.get(i)[1];
			sum += list.get(i)[0] * time;
		}
		
		System.out.println("weight/length:" + sum);
	}
}
