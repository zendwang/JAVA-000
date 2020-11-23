package io.kimmking.assemblybean.configurationclass;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Config {
    @Value("张三") String name;

    @Bean(name = "user")
    public User user() {
        User user  = new User(name);
        return user;
    }
    @Bean("manger")
    public Manger manger(User user) {
        return new Manger(user);
    }
}
