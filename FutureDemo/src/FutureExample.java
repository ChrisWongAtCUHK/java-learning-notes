import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class FutureExample {
    public static void main(String[] args) {
        // 1. Create a thread pool
        ExecutorService executor = Executors.newSingleThreadExecutor();

        // 2. Define a task that takes some time (e.g., fetching data from an API)
        Callable<String> databaseTask = () -> {
            System.out.println(" Background task started... fetching user data.");
            Thread.sleep(2000); // Simulate a 2-second delay
            return "User Profile Data: John Doe";
        };

        System.out.println("[Main Thread] Submitting the task to the executor...");

        // 3. Submit the task. This returns IMMEDIATELY with a Future object.
        Future<String> futureResult = executor.submit(databaseTask);

        System.out.println("[Main Thread] Task submitted! I can do other things now...");

        // 4. Do some independent work on the main thread while the background task runs
        for (int i = 1; i <= 3; i++) {
            System.out.println("[Main Thread] Doing heavy calculation step " + i);
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
            }
        }

        System.out.println("[Main Thread] Done with independent work. Now I need the background data.");

        try {
            // 5. Get the result.
            // CRITICAL: .get() is a BLOCKING call. If the background thread isn't done,
            // the main thread will pause and wait here until it is.
            String result = futureResult.get();
            System.out.println("[Main Thread] SUCCESS! Received result: " + result);
        } catch (Exception e) {
            System.out.println("[Main Thread] Something went wrong: " + e.getMessage());
        } finally {
            // Always shut down your executor pools when finished
            executor.shutdown();
        }
    }
}