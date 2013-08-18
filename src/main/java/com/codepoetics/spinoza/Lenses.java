package com.codepoetics.spinoza;

import com.codepoetics.spinoza.api.Lens;
import com.codepoetics.spinoza.api.PathDescribingLens;
import com.codepoetics.spinoza.paths.PathDescription;
import com.google.common.base.Function;

public final class Lenses {

    private Lenses() { }
    
    public static <T> PathDescribingLens<T, T> identity() {
        return PathDescription.describing(new Lens<T, T>() {
            @Override
            public T get(T target) {
                return target;
            }

            @Override
            public T update(T target, T newValue) {
                return newValue;
            }
            
        }).withDescription("{id}");
    }

    public static class UpdateBinder<O, T> {
    
        private O target;
        private Lens<O, T> lens;

        public UpdateBinder(O target, Lens<O, T> lens) {
            this.target = target;
            this.lens = lens;
        }
        
        public O with(Function<T, T> updater) {
            return lens.update(target, updater.apply(lens.get(target)));
        }

    }
    
    public static class LensBinder<O> {
        private final O target;
        
        public LensBinder(O target) {
            this.target = target;
        }
        
        public <T> UpdateBinder<O, T> using(Lens<O, T> lens) {
            return new UpdateBinder<O, T>(target, lens);
        }
    }
    
    public static <O> LensBinder<O> update(O target) {
        return new LensBinder<O>(target);
    }
}
