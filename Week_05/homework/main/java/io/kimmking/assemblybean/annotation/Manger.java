package io.kimmking.assemblybean.annotation;

import org.springframework.beans.factory.annotation.Autowired;



public class Manger {
    @Autowired
    private User user;

    public void info() {
        System.out.println("Manger Autowired");
        System.out.println("user:" + user);
    }
}
