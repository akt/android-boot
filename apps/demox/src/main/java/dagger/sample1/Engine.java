package dagger.sample1;

import javax.inject.Inject;

public class Engine {

    @Inject
    Engine(){

    }

    public void run(){
        System.out.println("go go go 1111");
    }
}
