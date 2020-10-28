### Week02 作业题目（周四）
####  4.（必做） 根据上述自己对于 1 和 2 的演示，写一段对于不同 GC 的总结
串行GC
java -XX:+UseSerialGC -Xms512m -Xmx512m -XX:+PrintGCDetails -XX:+PrintGCDateStamps GCLogAnalysis
年轻代使用拷贝-复制，老年代使用 标记-清理-整理
GC时间长　单线程GC 暂停时间长
内存越小，GC次数越多
Young GC:eden区满 -> 清空，存s区，部分对象晋升到老年代
Full GC: 新生代老年代清理过期

并行GC
java -XX:+UseParallelGC -Xms512m -Xmx512m -XX:+PrintGCDetails -XX:+PrintGCDateStamps GCLogAnalysis
不设置堆初始大小时 第一次YGC 堆内存比较小,第一次YGC提前
YGC  清理Young区,对象晋升到老年代
FGC  清理Young区 清理Old区不活跃对象
年轻代使用拷贝-复制，老年代使用标记-清除-整理
GC处理时，暂停业务处理，所有线程处理GC垃圾回收。平常运行时，所以线程都去处理业务。因此，吞吐量比较高。
内存越小，GC次数越多

CMS GC
java -XX:+UseConcMarkSweepGC -Xms512m -Xmx512m -Xloggc:cmsgc.512m.log -XX:+PrintGCDetails -XX:+PrintGCDateStamps GCLogAnalysis
java -XX:+UseConcMarkSweepGC -Xms4g -Xmx4g -Xloggc:cmsgc.4g.log -XX:+PrintGCDetails -XX:+PrintGCDateStamps GCLogAnalysis
年轻代使用拷贝-复制，老年代使用标记-清除
CMS默认GC线程数是1/4，并且老年代只清除，无整理。所以当GC发生时，吞吐量不如并行 GC
CMS GC 6个阶段
初始化标记 - 暂停GC
并行标记
并行预清理
最终标记 - 暂停GC
并行清理
并行重置
因为无整理，并且CMS GC 6阶段 暂停时间短，所以延迟比较低
内存越小，GC次数越多

G1 GC
java -XX:+UseG1GC -Xms512m -Xmx512m -XX:+PrintGCDetails -XX:+PrintGCDateStamps GCLogAnalysis
java -XX:+UseG1GC -Xms512m -Xmx512m -XX:+PrintGC -XX:+PrintGCDateStamps GCLogAnalysis

不分代,使用 region(2048) 存储数据，分为：
Eden区 （标记-复制 算法）
存活区
老年区 (标记-复制-整理 算法)
GC 3个阶段
(G1 Evacuation Pause) (young)
类似 CMS GC 的 并发标记
initial-mark
concurrent-root-region-scan
concurrent-mark
remark
concurrent-cleanup
(G1 Evacuation Pause) (mix)
使用 -XX:MaxGCPauseMills 参数可以控制 GC暂停时间

适用场景
ParNewGC + CMS 适用于 低延迟
ParallelGC + ParllelGC Old 适用于 高吞吐量
G1 适用于 内存大于4G，并且GC时间可控制
### Week02 作业题目（周六）
#### 1. 运行课上的例子，以及 Netty 的例子，分析相关现象。
##### Demo HttpServer01 单线程（同步 阻塞IO）
sb -u http://localhost:8801 -c 40 -N 30
Result: 28.2(request/second)[见图images/单线程.png]
> 测试指定http://localhost:8801 30秒内40个并发


#####  Demo HttpServer02 多线程 （同步 阻塞IO）
每来一个请求创建一个线程，
缺点，线程创建需要耗费时间，服务器线程资源也是有限的
sb -u http://localhost:8802 -c 40 -N 30
Result: 275.5(request/second)[见图images/多线程.png]

#####  Demo HttpServer03 线程池 （同步 阻塞IO）
sb -u http://localhost:8803 -c 40 -N 30
Result: 670.9(request/second) [见图images/固定线程池.png]

#####  Demo NettyServer (异步非阻塞IO) 通过多路复用IO技术实现的
sb -u http://localhost:8808/test -c 40 -N 30
Result: 1216.4(request/second)[见图images/netty.png]

**Selector 多路复用器**
多路复用的核心就是通过Selector来轮询注册在其上的Channel，当发现某个或者多个Channel处于就绪状态后，从阻塞状态返回就绪的Channel的选择键集合，进行IO操作。
由于多路复用器是NIO实现非阻塞IO的关键，它又是主要通过Selector实现

#### 2. 写一段代码，使用 HttpClient 或 OkHttp 访问 http://localhost:8801 ，代码提交到 Github。
> 可直接参阅 com.geekbang.camp.HttpClientDemo.java