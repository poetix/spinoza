package com.codepoetics.spinoza.api;

public interface ComposableLens<O, T> extends PathDescribingLens<O, T> {
    <T2> ComposableLens<O, T2> with(Lens<T, T2> other);
    <T2> ComposableLens<O, T2> with(PathDescribingLens<T, T2> other);
}
