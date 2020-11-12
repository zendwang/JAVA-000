package java0.homework;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 通过 Lock + Condition
 * result:104ms
 */
public class Question27 {
    public static void main(String[] args) throws InterruptedException {
        long start=System.currentTimeMillis();

        CalculateThread calculateThread = new CalculateThread();
        calculateThread.start();
        //阻塞式获取结果
        System.out.println("异步计算结果为:" + calculateThread.getValue());
        System.out.println("使用时间："+ (System.currentTimeMillis()-start) + " ms");

    }
    static class CalculateThread extends Thread {

        final Lock lock = new ReentrantLock();
        final Condition overCondition  = lock.newCondition();
        private int value;
        @Override
        public void run() {
            lock.lock();
            try {
                this.value = Question27.sum();
                overCondition.signal();
            } finally {
                lock.unlock();
            }
        }

        public int getValue() {
            lock.lock();
            try {
                //尝试获取锁，自旋等待锁释放，等待时间稍微长点等待线程run方法先执行
                while (this.value == 0){
                    overCondition.await();
                }
                return value;
            } catch (InterruptedException e) {
                e.printStackTrace();
                return 0;
            } finally {
                lock.unlock();
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
