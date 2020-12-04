package dagger.sample3;

import dagger.Module;
import dagger.Provides;

@Module
public class MakeCarModule3 {

    public MakeCarModule3(){}


    @Provides
    @QualifierA
    public Engine provideEngineA() {
        return new Engine("A");
    }

    @Provides
    @QualifierB
    public Engine provideEngineB() {
        return new Engine("B");
    }

}
