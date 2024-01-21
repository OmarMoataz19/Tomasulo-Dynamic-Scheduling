import java.util.Scanner;

public class Main {
	public static void main(String[] args) {
		System.out.println("Enter Add/Sub Latency");
        Scanner input = new Scanner(System.in);
		int AddLatency = input.nextInt();
        System.out.println("Enter Mul Latency");
        int MulLatency = input.nextInt();
         System.out.println("Enter Div Latency");
         int DivLatency =input.nextInt();
         System.out.println("Enter Load Latency");
         int LoadLatency = input.nextInt();
         System.out.println("Enter Store Latency");
         int StoreLatency = input.nextInt();
		 
		Processor processor = new Processor("test1.txt", AddLatency, MulLatency, DivLatency, LoadLatency, StoreLatency);
		processor.run();
		processor.print();
	}

}
