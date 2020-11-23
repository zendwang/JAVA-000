package io.kimmking.assemblybean.annotation;

import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

@Data
public class BusinessManger {
    @Autowired
    @Qualifier(value = "us")
    private User us;

    public void info() {
        System.out.println("BusinessManger Qualifier");
        System.out.println("user:" + us);
    }
}
