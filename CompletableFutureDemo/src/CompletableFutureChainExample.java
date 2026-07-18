import java.util.concurrent.CompletableFuture;

public class CompletableFutureChainExample {
    public static void main(String[] args) {
        System.out.println("[Main] Starting the order checkout workflow...");

        // Start the asynchronous pipeline
        CompletableFuture<Void> pipeline = CompletableFuture.supplyAsync(() -> {
            // STEP 1: Fetch Order (Returns a String)
            System.out.println("[" + Thread.currentThread().getName() + "] Step 1: Fetching order details...");
            sleep(1000); // Simulate network delay
            return "Order #88432";
        })
                .thenApply(order -> {
                    // STEP 2: Process Payment (Transforms String -> String)
                    // .thenApply() takes the result of the previous step and transforms it.
                    System.out.println(
                            "[" + Thread.currentThread().getName() + "] Step 2: Processing payment for " + order);
                    sleep(1000);
                    return order + " -> PAID";
                })
                .thenAccept(receipt -> {
                    // STEP 3: Send Email (Consumes the result, returns nothing)
                    // .thenAccept() takes the final result and consumes it (returns Void).
                    System.out.println("[" + Thread.currentThread().getName()
                            + "] Step 3: Sending email confirmation for: " + receipt);
                    sleep(500);
                });

        // The main thread is completely free while the background threads work!
        System.out.println("[Main] Main thread is non-blocked. Doing other work (ui rendering, input handling)...");
        for (int i = 0; i < 3; i++) {
            System.out.println("[Main] Free processing loop iteration " + i);
            sleep(400);
        }

        // Just for this demo: block main at the very end so the JVM doesn't exit before
        // background threads finish
        pipeline.join();
        System.out.println("[Main] All steps finished. Workflow complete.");
    }

    private static void sleep(long ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}