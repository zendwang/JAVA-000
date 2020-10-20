学习笔记

##### 本机使用 G1 GC 启动一个程序，仿照课上案例分析一下 JVM 情况。
 > 见 Q2-1
 - 查看JVM默认的垃圾回收器
 ```
 java -XX:+PrintCommandLineFlags -version
 ```
  -  用G1收集器 启动程序
 ```
 java -jar -XX:+UseG1GC -XX:MaxGCPauseMillis=50  demo-springboot-1.0-SNAPSHOT.jar
 
 // java -jar -XX:+UseParallelGC -XX:+UseParallelOldGC demo-springboot-1.0-SNAPSHOT.jar
 // java -jar -XX:+UseConcMarkSweepGC x demo-springboot-1.0-SNAPSHOT.jar
 
 ```
 - 查看JVM当前堆情况
 ```
 jmap -heap [pid]
 
 Attaching to process ID 3528, please wait...
 Debugger attached successfully.
 Server compiler detected.
 JVM version is 25.202-b08
 
 using thread-local object allocation.
 Garbage-First (G1) GC with 4 thread(s)
 #并发线程4个
 
 Heap Configuration:
    MinHeapFreeRatio         = 40
    MaxHeapFreeRatio         = 70
    MaxHeapSize              = 2122317824 (2024.0MB) #最大堆内存
    NewSize                  = 1363144 (1.2999954223632812MB)
    MaxNewSize               = 1272971264 (1214.0MB) #Young区容量最大内存值 0.6 * 2024
    OldSize                  = 5452592 (5.1999969482421875MB)
    NewRatio                 = 2 #Young区(eden+survior)和old区的比例  Young区占整个堆的1/3
    SurvivorRatio            = 8 #Surivior和eden比例  Surivior占整个young区1/10 
    MetaspaceSize            = 21807104 (20.796875MB)
    CompressedClassSpaceSize = 1073741824 (1024.0MB)
    MaxMetaspaceSize         = 17592186044415 MB
    G1HeapRegionSize         = 1048576 (1.0MB) #块大小
 
 Heap Usage:
 G1 Heap:
    regions  = 2024 #分块数量
    capacity = 2122317824 (2024.0MB)
    used     = 33030136 (31.49999237060547MB)
    free     = 2089287688 (1992.5000076293945MB)
    1.5563237337255666% used
 G1 Young Generation:
 Eden Space:
    regions  = 9 #Eden当前分配9个块
    capacity = 78643200 (75.0MB) #Eden最多分配75个块
    used     = 9437184 (9.0MB) #Eden当前使用9个块
    free     = 69206016 (66.0MB)
    12.0% used
 Survivor Space:
    regions  = 5
    capacity = 5242880 (5.0MB)
    used     = 5242880 (5.0MB)
    free     = 0 (0.0MB)
    100.0% used
 G1 Old Generation:
    regions  = 18
    capacity = 50331648 (48.0MB)
    used     = 17301496 (16.49999237060547MB)
    free     = 33030152 (31.50000762939453MB)
    34.37498410542806% used
 
 15734 interned Strings occupying 2084960 bytes.
 
 ```

我自己在win10 上看到的是
MaxHeapSize=2024MB G1 Heap regions  = 2024
在CentOS上看到的是 MaxHeapSize=1968MB G1 Heap regions  = 1968
究其原因是系统JVM默认环境变量InitialHeapSize初始值所致
该值 为os内存的1/4



