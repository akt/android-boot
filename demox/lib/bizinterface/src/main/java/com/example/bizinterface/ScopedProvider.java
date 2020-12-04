package com.example.bizinterface;

public final class ScopedProvider<T extends IBizModule> implements IBizModuleProvider<T> {
    private static final Object UNINITIALIZED = new Object();

    private final IBizModuleProvider<T> factory;
    private volatile Object instance = UNINITIALIZED;

    private ScopedProvider(IBizModuleProvider<T> factory) {
        assert factory != null;
        this.factory = factory;
    }

    @SuppressWarnings("unchecked")
    @Override
    public T provideModule() {
        Object result = instance;
        if (result == UNINITIALIZED) {
            synchronized (this) {
                result = instance;
                if (result == UNINITIALIZED) {
                    instance = result = factory.provideModule();
                }
            }
        }
        return (T) result;
    }

    public static <T extends IBizModule> IBizModuleProvider<T> create(IBizModuleProvider<T> factory) {
        if (factory == null) {
            throw new NullPointerException();
        }
        return new ScopedProvider<>(factory);
    }
}
