package dagger.sample3;

import javax.inject.Inject;

public class Engine {

    private String name;

    @Inject
    Engine(String name) {
        this.name = name;
    }

    public void run(){
        System.out.println(name + "go go go");
    }
}
