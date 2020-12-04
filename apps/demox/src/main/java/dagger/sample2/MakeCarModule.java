package dagger.sample2;

import dagger.Module;
import dagger.Provides;

@Module
public class MakeCarModule {

    public MakeCarModule(){}


    @Provides
    Engine provideEngine(){
        return new Engine("gear");
    }

}
