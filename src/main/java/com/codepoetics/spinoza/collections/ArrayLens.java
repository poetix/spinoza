package com.codepoetics.spinoza.collections;

import java.util.Arrays;

import com.codepoetics.spinoza.api.PathDescribingLens;
import com.codepoetics.spinoza.paths.api.PathDescriber;

public class ArrayLens<T> implements PathDescribingLens<T[], T> {

    public static <T> PathDescribingLens<T[], T> atIndex(final int index) {
        return new ArrayLens<T>(index);
    }

    private int index;

    private ArrayLens(int index) {
        this.index = index;
    }

    @Override
    public T get(T[] target) {
        return target[index];
    }

    @Override
    public T[] update(T[] target, T newValue) {
        T[] copy = Arrays.copyOf(target, target.length);
        copy[index] = newValue;
        return copy;
    }

    @Override
    public void describeTo(PathDescriber describer) {
        describer.index(index);
    }

}
