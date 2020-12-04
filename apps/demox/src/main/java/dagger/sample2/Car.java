package dagger.sample2;

import javax.inject.Inject;


public class Car {

    @Inject
    Engine engine;

    public Car(){
        DaggerCarComponent.builder().makeCarModule(new MakeCarModule()).build().inject(this);
    }

    public Engine getEngine() {
        return engine;
    }
}
