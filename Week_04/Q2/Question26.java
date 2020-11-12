package java0.homework;

/**
 * 通过 synchronize + 对象锁
 * result:103ms
 */
public class Question26 {
    public static void main(String[] args) throws InterruptedException {
        long start=System.currentTimeMillis();

        CalculateThread calculateThread = new CalculateThread();
        calculateThread.start();
        //主线程稍微等等，让子线程执行
        Thread.currentThread().join(100);
        //阻塞式获取结果
        System.out.println("异步计算结果为:" + calculateThread.getValue());
        System.out.println("使用时间："+ (System.currentTimeMillis()-start) + " ms");

    }
    static class CalculateThread extends Thread {

        private byte[] lock = new byte[1];
        private int value;
        @Override
        public void run() {
            synchronized (lock) {
                this.value = Question26.sum();
            }
        }

        public int getValue() {
            synchronized (lock) {
                return value;
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
