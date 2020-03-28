import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.ExecutionException;

public class IntegerWithMaxNumberOfDivisors {

	private static final int N = 100000;
	private static int maxNumOfDivisors;
	private static int integerWithMaxNumOfDivisors;

	private class ComputeDivisors implements Runnable {
		private int firstInt;
		private int lastInt;

		public ComputeDivisors(int firstInt, int lastInt) {
			this.firstInt = firstInt;
			this.lastInt = lastInt;
		}

		public void run() {
			// System.out.println("Thread " + Thread.currentThread().getName() + " computes numbers in the range of [" + firstInt + "," + lastInt + "]");
			int maxNumOfDivisors = 0;
			int integerWithMaxNumOfDivisors = 0;

			for(int i = firstInt; i <= lastInt; i++) {
				int divs = computeDivisors(i);
				if(divs > maxNumOfDivisors) {
					maxNumOfDivisors = divs;
					integerWithMaxNumOfDivisors = i;
				}
			}
			findIntWithMaxNumOfDivisors(maxNumOfDivisors, integerWithMaxNumOfDivisors);
		}

		// helper method to compute the number of dividors
		public int computeDivisors(int num) {
			int numOfDivisors = 0;
			for(int i = 1; i <= N; i++) {
				if(num % i == 0) {
					numOfDivisors++;
				}
			}
			return numOfDivisors;
		}
	}

	synchronized private static void findIntWithMaxNumOfDivisors(int localMaxNumOfDivisors, int localIntegerWithMaxNumOfDivisors) {
		if (localMaxNumOfDivisors > maxNumOfDivisors) {
			maxNumOfDivisors = localMaxNumOfDivisors;
			integerWithMaxNumOfDivisors = localIntegerWithMaxNumOfDivisors;
		}
	}

	private void divideWorkAmongThreads() throws InterruptedException, ExecutionException {
		
		int[] testThreads = {6, 12, 30, 72, 100};
		
		for(int i = 0; i < testThreads.length; i++) {
			int numberOfThreads = testThreads[i];
			System.out.println("Searching for an integer with the greatest number of divisors using " + numberOfThreads + " threads...");
			long startTime = System.currentTimeMillis();
			ExecutorService executor = Executors.newFixedThreadPool(numberOfThreads);
			ComputeDivisors[] threads = new ComputeDivisors[numberOfThreads];

			int numbersPerThread = N/numberOfThreads;
			int firstNumber = 1;
			int lastNumber = firstNumber + numbersPerThread - 1;

			for (int j = 0; j < numberOfThreads; j++) {
				if (j == numberOfThreads - 1) {
					lastNumber = N;
				}
				threads[j] = new ComputeDivisors(firstNumber, lastNumber);
				firstNumber = lastNumber + 1; 
				lastNumber = firstNumber + numbersPerThread - 1;
			}

			for (int j = 0; j < numberOfThreads; j++) {
				executor.execute(threads[j]);
			}
				
			executor.shutdown();
			executor.awaitTermination(1, TimeUnit.HOURS);

			long endTime = System.currentTimeMillis();
			System.out.println("Computation took "+((endTime - startTime)/1000.0) + " ms using " + numberOfThreads + " threads and naive method to search for divisors.");
			System.out.println("Integer with maximum number of divisors is " + integerWithMaxNumOfDivisors + " with " + maxNumOfDivisors);
			System.out.println();
		}
	}

	public static void countDivisorsUsingOneThread() {
		System.out.println("Searching for an integer with the greatest number of divisors using one thread...");
		int maxNumOfDivisors = 0;
		int integerWithMaxNumOfDivisors = 0;
		long startTime = System.currentTimeMillis();
		for(int i = 1; i <= N; i++) {
			int numOfDivisors = 0;
	        for (int j=1; j<=i; j++) 
	        { 	
	            if (i%j==0)
	                numOfDivisors++;
	        }
	        if(numOfDivisors > maxNumOfDivisors) {
	        	maxNumOfDivisors = numOfDivisors;
	        	integerWithMaxNumOfDivisors = i;
	        }
	    }

	    long endTime = System.currentTimeMillis();
		System.out.println("Computation took "+((endTime - startTime)/1000.0) + " ms using one thread and naive method to search for divisors.");
		System.out.println("Integer with maximum number of divisors is " + integerWithMaxNumOfDivisors + " with " + maxNumOfDivisors);
	}

	public static void main(String[] args)  {
		System.out.println("Searching for number of divisors between 1 and " + N);
		System.out.println();
		countDivisorsUsingOneThread();
		System.out.println();
		IntegerWithMaxNumberOfDivisors findInt = new IntegerWithMaxNumberOfDivisors();
		try {
			findInt.divideWorkAmongThreads();
		} catch(Exception e) {
			e.getMessage();
		}
		System.out.println();
	}
}





