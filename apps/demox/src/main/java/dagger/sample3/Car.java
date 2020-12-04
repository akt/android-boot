package dagger.sample3;

import javax.inject.Inject;


public class Car {

    @QualifierA @Inject Engine engineA;

    @QualifierB @Inject Engine engineB;

    public Engine getEngineA() {
        return engineA;
    }

    public Engine getEngineB() {
        return engineB;
    }

    public Car() {
        DaggerCarComponent.builder().makeCarModule3(new MakeCarModule3()).build().inject(this);
    }
}
