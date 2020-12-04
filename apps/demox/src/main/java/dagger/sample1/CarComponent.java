package dagger.sample1;

import dagger.Component;

@Component
public interface CarComponent {
    void inject(Car car);
}
