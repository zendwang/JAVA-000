#### Week05 作业题目（周四）
##### 2.（必做）写代码实现 Spring Bean 的装配，方式越多越好（XML、Annotation 都可以）, 提交到 Github。
1. 基于XML的Bean装配（示例代码 io.kimmking.assemblybean.xml.SpringDemo）
 在xml bean节点上根据属性值autowire=no|byName|byType|constructor|default，还可以在beans节点设置全局默认属性default-autowire
2. 基于注解的Bean装配（示例代码 io.kimmking.assemblybean.annotation.SpringDemo）
 @Autowired 按类型自动装配（byType）。
 @Qualifier 按名称自动装配，不能单用，需要和@Autowired配合使用（byName）。
 @Resource 是byName、byType方式的结合。
3. 基于配置类的Bean装配（示例代码 io.kimmking.assemblybean.configurationclass.SpringDemo）
通过@Configuration注解标识配置类
####  Week05 作业题目（周六）：
##### 4.（必做）给前面课程提供的 Student/Klass/School 实现自动配置和 Starter。
参见spring-boot-starter-demo,该starter通过配置，来实现创建Student Bean、Klass Bean、School Bean
```
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Springboot02Application.class)
public class StarterDemoTest {
    @Autowired
    ApplicationContext context;
    @Test
    public  void testDemoStarter() {
        School school = context.getBean("school", School.class);
        System.out.println(school.getStudent100());
        System.out.println(school.getClass1().getStudents());
    }
}
```
其中Springboot02Application.java为启动类
```
@SpringBootApplication(scanBasePackages = {"io.kimmking.demo.others"})
public class Springboot02Application {
	public static void main(String[] args) {
		SpringApplication.run(Springboot02Application.class, args);
	}
}
```
在application.yml中配置
```
demo:
  student:
    id: 20000
    name: test
```
感觉starter这块主要是按格式，要配置启动类，创建需要的对象，会涉及怎么获取外部配置，怎么不跟外部的Bean冲突。
需要用到这些注解@EnableConfigurationProperties、@ConditionalOnProperty、ConditionalOnMissingBean等

##### 6.（必做）研究一下 JDBC 接口和数据库连接池，掌握它们的设计和用法：
参见io.kimmking.jdbc.CrudBean
可通过配置org.springframework.jdbc.datasource.DriverManagerDataSource  创建DataSource Bean
来实现普通JDBC　CRUD
也可以通过HikariPC连接池 创建 com.zaxxer.hikari.HikariDataSource 创建DataSource Bean 
来实现基于连接池的　CRUD