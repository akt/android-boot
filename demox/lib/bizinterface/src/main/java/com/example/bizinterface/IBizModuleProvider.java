package com.example.bizinterface;

/**
 * Created by ak
 */
public interface IBizModuleProvider<T extends IBizModule> {

    T provideModule();
}
