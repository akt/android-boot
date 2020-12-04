package dagger.sample2;

import dagger.Component;

@Component(modules = {MakeCarModule.class})
public interface CarComponent {
    void inject(Car car);
}
