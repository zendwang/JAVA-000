package io.kimmking.assemblybean.configurationclass;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * 基于Java配置类的状态
 */
public class SpringDemo {
    public static void main(String[] args) {
        ApplicationContext context = new AnnotationConfigApplicationContext(Config.class);

        Manger manger = context.getBean("manger", Manger.class);
        manger.info();
    }
}
