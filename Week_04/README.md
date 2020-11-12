#### 2. 思考有多少种方式，在 main 函数启动一个新线程，运行一个方法，拿到这个方法的返回值后，退出主线程？
1. 通过 volatile的特性 变量更新会通知其他线程 【Q2/Question20.java】
2. 通过 Thread.join 等待子线程终止 【Q2/Question21.java】
3. 通过实现 Callable接口【Q2/Question22.java】
4. 通过 Callable + 线程池【Q2/Question23.java】
5. 通过 CountDownLatch【Q2/Question24.java】
6. 通过 Semaphore【Q2/Question25.java】
7. 通过 synchronize + 对象锁【Q2/Question26.java】
8.  通过 Lock + Condition【Q2/Question27.java】

#### 4.把多线程和并发相关知识带你梳理一遍，画一个脑图，截图上传到 Github 上。
参见【Q4/image.png】
