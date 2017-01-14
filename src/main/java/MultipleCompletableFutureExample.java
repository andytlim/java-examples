import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * This example shows how to execute multiple completable futures and wait for completion.
 * @author Andy
 *
 */
public class MultipleCompletableFutureExample {

	public static void main(String[] args) throws InterruptedException, ExecutionException {
		
		// Create a fixed thread pool instead of using the default ForkJoinPool.commonPool()
		ExecutorService newFixedThreadPool = Executors.newFixedThreadPool(5);
		
		// Create a list to store the completable futures
		List<CompletableFuture<Void>> futureList = new LinkedList<>();
		
		// Iterate through the completable futures
		for (int i = 0; i < 10; i++) {
			final int test = i;
			CompletableFuture<Void> cf = CompletableFuture.runAsync(() -> {
				System.out.println(Thread.currentThread() + ": " + test);
				try {
					Thread.sleep(test * 1000);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}, newFixedThreadPool);	
			futureList.add(cf);
		}
		
		// +1 style points for using allOf() to join all the completable futures together
		CompletableFuture<Void> allDoneFuture =
		        CompletableFuture.allOf(futureList.toArray(new CompletableFuture[futureList.size()]));
		allDoneFuture.thenRun(() -> { 
			newFixedThreadPool.shutdown(); 
			System.out.println(Thread.currentThread() + ": " + "Done!");
		});
		
//      Make a blocking get() call to wait for all the futures to complete before shutting down thread pool		
//		for (CompletableFuture cf : futureList) {
//			cf.get();
//		}
//		newFixedThreadPool.shutdown();
//		System.out.println("Done!");
	}
}
