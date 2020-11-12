package java0.homework;
import java.util.concurrent.Semaphore;

/**
 * 通过 Semaphore
 * result:103ms
 */
public class Question25 {
    public static void main(String[] args) throws InterruptedException {
        long start=System.currentTimeMillis();
        CalculateThread calculateThread = new CalculateThread();

        calculateThread.start();
        //主线程稍微等等，让子线程执行
        Thread.currentThread().join(100);
        //通过Semaphore阻塞式的获取结果
        System.out.println("异步计算结果为:" + calculateThread.getValue());
        System.out.println("使用时间："+ (System.currentTimeMillis()-start) + " ms");

    }
    static class CalculateThread extends Thread {
        private int value;
        private   Semaphore semaphore = new Semaphore(1);

        @Override
        public void run() {
            try {
                //Thread.currentThread().join(1);
                this.semaphore.acquire();
                this.value = Question25.sum();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                this.semaphore.release();
            }
        }

        public int getValue() {
            try {
                this.semaphore.acquire();
                return value;
            } catch (InterruptedException e) {
                e.printStackTrace();
                return 0;
            } finally {
                this.semaphore.release();
            }
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
