package io.kimmking.assemblybean.xml;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
public class Manger {
    private User user;
    public Manger() {}
    public Manger(User user) {
        System.out.println("**constructor autowire**");
        this.user = user;
    }
}
