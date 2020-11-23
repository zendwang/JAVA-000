package io.kimmking.assemblybean.xml;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * 基于XML的装配
 * */
public class SpringDemo {
    public static void main(String[] args) {
        ApplicationContext context = new ClassPathXmlApplicationContext("applicationContext01.xml");
        //no属性值 这是autowire采用的默认值，它不采用自动装配。当前设置默认为byType
        System.out.println("================不采用自动装配================");
        Manger manger2 = (Manger) context.getBean("manger2");
        System.out.println(manger2.toString());

        //byName属性值 autowire按Bean名称自动装配
        System.out.println("================按Bean名称自动装配User================");
        Manger manger = (Manger) context.getBean("manger");
        System.out.println("name: " + manger.getUser().getName());
        System.out.println("age: " + manger.getUser().getAge());
        System.out.println("sex: " + manger.getUser().getSex());

        //byType属性值 autowire按Bean类型自动装配
        System.out.println("================按Bean类型自动装配User================");
        BusinessManger businessManger = context.getBean("businessManger",BusinessManger.class);
        System.out.println("name: " + businessManger.getUs().getName());
        System.out.println("age: " + businessManger.getUs().getAge());
        System.out.println("sex: " + businessManger.getUs().getSex());

        //constructor属性值 通过构造方法的参数类型自动装配。
        System.out.println("================构造方法的参数类型自动装配================");
        Manger manger3 = context.getBean("manger3",Manger.class);
        System.out.println("name: " + manger3.getUser().getName());
        System.out.println("age: " + manger3.getUser().getAge());
        System.out.println("sex: " + manger3.getUser().getSex());

        //default属性值 全局全自动。当前设置默认为byType
        System.out.println("================全局全自动================");
        BusinessManger businessManger2 = context.getBean("businessManger2",BusinessManger.class);
        System.out.println("name: " + businessManger2.getUs().getName());
        System.out.println("age: " + businessManger2.getUs().getAge());
        System.out.println("sex: " + businessManger2.getUs().getSex());
    }
}
