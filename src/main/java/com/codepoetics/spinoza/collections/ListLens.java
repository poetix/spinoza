package com.codepoetics.spinoza.collections;

import java.util.Iterator;
import java.util.List;

import com.codepoetics.spinoza.api.PathDescribingLens;
import com.codepoetics.spinoza.paths.api.PathDescriber;
import com.google.common.collect.ImmutableList;

public class ListLens<T> implements PathDescribingLens<List<? extends T>, T> {

    public static <T> PathDescribingLens<List<? extends T>, T> atIndex(final int index) {
        return new ListLens<T>(index);
    }

    private int index;

    private ListLens(int index) {
        this.index = index;
    }

    @Override
    public T get(List<? extends T> target) {
        return target.get(index);
    }

    @Override
    public List<? extends T> update(List<? extends T> target, T newValue) {
        ImmutableList.Builder<T> builder = ImmutableList.builder();
        Iterator<? extends T> iterator = target.iterator();
        int i = 0;
        while(iterator.hasNext()) {
            if (i == index) {
                builder.add(newValue);
            } else {
                builder.add(iterator.next());
            }
            i += 1;
        }
        return builder.build();
    }

    @Override
    public void describeTo(PathDescriber describer) {
        describer.index(index);
    }
}
