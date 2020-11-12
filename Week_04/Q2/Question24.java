package java0.homework;
import java.util.concurrent.CountDownLatch;

/**
 * 通过 CountDownLatch
 * result:103ms
 */
public class Question24 {
    public static void main(String[] args) throws InterruptedException {
        long start=System.currentTimeMillis();

        CountDownLatch latch = new CountDownLatch(1);
        CalculateThread calculateThread = new CalculateThread(latch);
        calculateThread.start();

        latch.await();

        System.out.println("异步计算结果为:" + calculateThread.getValue());
        System.out.println("使用时间："+ (System.currentTimeMillis()-start) + " ms");

    }
    static class CalculateThread extends Thread {
        private int value;
        private CountDownLatch latch;
        public CalculateThread(CountDownLatch latch) {
            this.latch = latch;
        }
        @Override
        public void run() {
            this.value = Question24.sum();
            this.latch.countDown();
        }

        public int getValue() {
            return value;
        }
    }
    private static int sum() {
        return fibo(36);
    }

    private static int fibo(int a) {
        if ( a < 2)
            return 1;
        return fibo(a-1) + fibo(a-2);
    }
}
