package dagger.sampled;

import javax.inject.Inject;

public class Engine {

    private String name;

    @Inject
    Engine(String name) {
        System.out.println("create engine");
        this.name = name;
    }

    public void run(){
        System.out.println(name + "go go go");
        System.out.println(hashCode());
    }
}
