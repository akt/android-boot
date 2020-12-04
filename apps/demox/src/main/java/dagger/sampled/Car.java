package dagger.sampled;

import javax.inject.Inject;



public class Car {

    @Inject
    Engine engineA;

    @Inject
    Engine engineB;

    public Engine getEngineA() {
        return engineA;
    }

    public Engine getEngineB() {
        return engineB;
    }

    public Car() {
        DaggerCarComponent.builder().makeCarModule4(new MakeCarModule4()).build().inject(this);
    }
}
