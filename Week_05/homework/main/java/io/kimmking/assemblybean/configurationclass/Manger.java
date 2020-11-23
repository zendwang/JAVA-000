package io.kimmking.assemblybean.configurationclass;


public class Manger {
    private User user;

    public Manger(User user) {
        this.user = user;
    }
    public void info() {
        System.out.println("user:" + user);
    }
}
