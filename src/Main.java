import java.util.Scanner;
import java.util.List;
import java.util.ArrayList;
import java.util.concurrent.*;


class Mass implements Runnable{
    private final int index;
    private final int size;
    CyclicBarrier cyclbar;

    Mass(CyclicBarrier c, int index, int size) {
        this.index = index;
        this.size = size;
        cyclbar = c;
    }

    public void run() {
        Main.array[index] = Main.array[index]+Main.array[size-1-index];
//        System.out.printf("%s: %s %s\n", Thread.currentThread().getName(), index, size-1-index);
        try {
            cyclbar.await();
        } catch (BrokenBarrierException | InterruptedException exc) {
            System.out.println(exc);
        }
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

        List<Mass> threads;
        CyclicBarrier cb;
        while (size > 1) {
            int numThreads = size / 2;
            threads = new ArrayList<>();
            cb = new CyclicBarrier(numThreads);
            for (int i = 0; i < numThreads; i++) {
                threads.add(new Mass(cb, i, size));
            }
            for (Mass thr : threads) {
                new Thread(thr).start();
            }
            if (size % 2 == 1) {
                size = size/2 + 1;
            } else {
                size /= 2;
            }
//            System.out.println("New wave");
//            for (long el : array) {
//                System.out.println(el);
//            }
        }

        System.out.println("Result: " + array[0]);
        System.out.println("Check: " + checkRes);
    }
}