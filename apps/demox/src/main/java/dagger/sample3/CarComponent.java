package dagger.sample3;

import dagger.Component;

@Component(modules = {MakeCarModule3.class})
public interface CarComponent {
    void inject(Car car);
}
