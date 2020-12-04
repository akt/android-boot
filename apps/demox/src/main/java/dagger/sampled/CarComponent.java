package dagger.sampled;

import dagger.Component;

@CarScope
@Component(modules = {MakeCarModule4.class})
public interface CarComponent {
    void inject(Car car);
}
