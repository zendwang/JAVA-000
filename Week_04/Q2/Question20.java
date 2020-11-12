package java0.homework;


/**
 * 通过 volatile的特性 变量更新会通知其他线程
 * result:130ms
 */
public class Question20 {
    //计算结果
    private static volatile int value;
    public static void main(String[] args) throws InterruptedException {
        long start=System.currentTimeMillis();
        Thread thread = new Thread(new CalculateTask());
        thread.start();

        while (value == 0);//自旋，可以设置当前线程sleep，但会增加上下文调度开销

        System.out.println("异步计算结果为:" + value);
        System.out.println("使用时间："+ (System.currentTimeMillis()-start) + " ms");

    }
    static class CalculateTask implements Runnable {
        @Override
        public void run() {
            Question20.value = Question20.sum();
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
