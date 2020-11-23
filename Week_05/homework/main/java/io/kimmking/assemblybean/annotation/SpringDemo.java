package io.kimmking.assemblybean.annotation;


import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * 基于注解的装配
 * @Autowired 按类型自动装配（byType）。
 * @Qualifier 按名称自动装配，不能单用，需要和@Autowired配合使用（byName）。
 * @Resource 是byName、byType方式的结合。
 */
public class SpringDemo {
    public static void main(String[] args) {
        ApplicationContext context = new ClassPathXmlApplicationContext("applicationContext02.xml");

        // @Autowire注解 不必写setter方法，也不必写构造器。在依赖的对象上添加@Autowired注解（当然也可以在setter方法上写），即按照类型自动装配依赖。相当于byType。
       Manger manger = context.getBean("manger", Manger.class);
       manger.info();
        //@Qualifier是byName方式的自动装配，需要用value指定依赖Bean的id/name，Spring容器根据这个value找到id/name为vBean，注入。
        //添加@Qualifier，@Qualifier不能单独用，还需要添加@Autowired。
        BusinessManger businessManger = context.getBean("businessManger", BusinessManger.class);
        businessManger.info();

        //先根据name找到Spring容器中name/id为student的Bean，注入。即优先以getName方式。
        //如果找不到name/id为指定值的Bean，或缺省name直接写@Resource，则以默认的getName方式
        //如果还是找不到依赖的Bean，则以byType方式注入。
        CustomerManger customerManger = context.getBean("customerManger", CustomerManger.class);
        customerManger.info();
    }
}
