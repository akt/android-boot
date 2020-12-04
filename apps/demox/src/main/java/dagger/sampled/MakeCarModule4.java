package dagger.sampled;

import dagger.Module;
import dagger.Provides;

@Module
public class MakeCarModule4 {

    public MakeCarModule4(){}


    @Provides
    @CarScope
    public Engine provideEngine() {
        return new Engine("gear");
    }


}
