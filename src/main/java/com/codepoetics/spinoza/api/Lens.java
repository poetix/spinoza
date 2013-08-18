package com.codepoetics.spinoza.api;

public interface Lens<O, T> {
    T get(O target);
    O update(O target, T newValue);
}