package java0.homework;


/**
 * 通过 Thread.join 等待子线程终止
 * result:130ms
 */
public class Question21 {
    public static void main(String[] args) throws InterruptedException {
        long start=System.currentTimeMillis();

        CalculateThread calculateThread = new CalculateThread();
        calculateThread.start();
        //线程没有执行完之前，会一直阻塞在join方法处
        calculateThread.join();

        System.out.println("异步计算结果为:" + calculateThread.getValue());
        System.out.println("使用时间："+ (System.currentTimeMillis()-start) + " ms");

    }
    static class CalculateThread extends Thread {
        private int value;
        @Override
        public void run() {
            this.value = Question21.sum();
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
