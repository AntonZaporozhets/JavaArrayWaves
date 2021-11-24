import java.util.Scanner;
import java.util.concurrent.*;


class Mass implements Runnable{
    private final int index;
    private final int size;
    private final CountDownLatch latch;

    Mass(CountDownLatch latch, int index, int size) {
        this.latch = latch;
        this.index = index;
        this.size = size;
    }

    public void run() {
        Main.array[index] = Main.array[index]+Main.array[size-1-index];
//        System.out.printf("%s: %s %s\n", Thread.currentThread().getName(), index, size-1-index);
        latch.countDown();
    }
}

public class Main {
    public static long[] array;
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        System.out.print("Input a size of array: ");
        int size = in.nextInt();
        array = new long[size];
        for (int i = 0; i < array.length; i++) {
            array[i] = i;
            //(int) Math.round((Math.random()*200 - 100));
        }
        long checkRes = (array[0] + array[size-1]) * size / 2;


        final ExecutorService service = Executors.newCachedThreadPool();
        while (size > 1) {
            int numThreads = size/2;
            CountDownLatch latch = new CountDownLatch(numThreads);
            for (int i = 0; i < numThreads; i++) {
                service.submit(new Mass(latch, i, size));
            }
            if (size % 2 == 1) {
                size = size/2 + 1;
            } else {
                size /= 2;
            }
            try {
                latch.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
//            System.out.println("New wave");
//            for (long el : array) {
//                System.out.println(el);
//            }
        }
        service.shutdown();

        System.out.println("Result: " + array[0]);
        System.out.println("Check: " + checkRes);
    }
}