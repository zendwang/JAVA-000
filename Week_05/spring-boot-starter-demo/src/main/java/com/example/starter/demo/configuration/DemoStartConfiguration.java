package com.example.starter.demo.configuration;

import com.example.starter.demo.entity.*;
import com.example.starter.demo.properties.StudentProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

/**
 * 自动装配类
 */
@Slf4j
@RequiredArgsConstructor
@Configuration
@EnableConfigurationProperties(StudentProperties.class)
public class DemoStartConfiguration {

    @Bean("student100")
    @ConditionalOnProperty(prefix = "demo.student",name="name",matchIfMissing = false)
    public Student student(StudentProperties studentProperties) {
        return  new Student(studentProperties.getId(),studentProperties.getName());
    }
    @Bean
    @ConditionalOnMissingBean(Klass.class)
    public Klass klass() {
        List<Student> students = new ArrayList<>(3);
        students.add( new Student(10,"Lucy"));
        students.add( new Student(11,"LiLy"));
        students.add( new Student(12,"Hanmeimei"));

        Klass klass = new Klass();
        klass.setStudents(students);

        return klass;
    }


    @Bean
    @ConditionalOnMissingBean(School.class)
    public School school(Student student100,Klass class1) {
        School school = new School();
        school.setStudent100(student100);
        school.setClass1(class1);
        return school;
    }
}
//- school:
//    student:
//        id:
//        name:
//    class1:
//        students:
//            - id:
//            name:
//            - id:
//            name:
