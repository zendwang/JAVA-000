package io.kimmking.jdbc;


import com.zaxxer.hikari.HikariDataSource;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.List;
import java.util.Map;

public class SpringDemo {
    public static void main(String[] args) {
        ApplicationContext context = new ClassPathXmlApplicationContext("applicationContext03.xml");

        String selectSql = "select * from user";
        String insertSql = "insert into `user`(name,sex,age) values('zs',0,15)";
        String deleteSql = "delete from `user` where name='zs'";
        String updateSql = "update `user` set name='zendwang' where id=1";

//        JdbcUtil.select(selectSql);
//        JdbcUtil.insert(insertSql);
//        JdbcUtil.delete(deleteSql);
//        JdbcUtil.update(updateSql);

        CrudBean crudBean = context.getBean("crudBean", CrudBean.class);

        boolean result = crudBean.insert(insertSql);
        if(result) {
            System.out.println("sql:" + insertSql + ", insert successfully." );
        } else {
            System.out.println("sql:" + insertSql + ", insert failed." );
        }
        result = crudBean.delete(deleteSql);
        if(result) {
            System.out.println("sql:" + deleteSql + ", delete successfully." );
        } else {
            System.out.println("sql:" + deleteSql + ", delete failed." );
        }
        result = crudBean.update(updateSql);
        if(result) {
            System.out.println("sql:" + updateSql + ", update successfully." );
        } else {
            System.out.println("sql:" + updateSql + ", update failed." );
        }

        List<Map<String, Object>> list = crudBean.select(selectSql);
        list.forEach(e -> {
            System.out.println("----------------------------------------");
            for (Map.Entry<String,Object> entry : e.entrySet()) {
                System.out.println("key:" + entry.getKey() + " value:" + entry.getValue());
            }
        });
    }
}
