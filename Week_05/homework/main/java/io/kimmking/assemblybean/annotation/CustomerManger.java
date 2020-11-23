package io.kimmking.assemblybean.annotation;

import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import javax.annotation.Resource;

@Data
public class CustomerManger {
    @Resource(name="user3")
    private User user;

    public void info() {
        System.out.println("CustomerManger Resource");
        System.out.println("user:" + user);
    }
}
