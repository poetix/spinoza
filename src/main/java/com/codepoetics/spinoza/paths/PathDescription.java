package com.codepoetics.spinoza.paths;

import com.codepoetics.spinoza.api.Lens;
import com.codepoetics.spinoza.api.PathDescribingLens;
import com.codepoetics.spinoza.paths.api.PathDescriber;
import com.codepoetics.spinoza.paths.api.PathDescribing;

public final class PathDescription {

    private PathDescription() { }
    
    public static <O, T> String of(Lens<O, T> lens) {
        PathDescribingLens<O, T> lensPD = PathDescription.ensureDescribing(lens);
        return ofDescribing(lensPD);
    }
    
    public static String ofDescribing(PathDescribing describing) {
        SimplePathDescriber describer = new SimplePathDescriber();
        describing.describeTo(describer);
        return describer.toString();
    }
    
    public static class DescriptionBinder<O, T> {
        private final class LensWithPathDescriber implements PathDescribingLens<O, T> {
            private final PathDescribing pathDescribing;

            private LensWithPathDescriber(PathDescribing pathDescribing) {
                this.pathDescribing = pathDescribing;
            }

            @Override
            public T get(O target) {
                return lens.get(target);
            }

            @Override
            public O update(O target, T newValue) {
                return lens.update(target, newValue);
            }

            @Override
            public void describeTo(PathDescriber describer) {
                pathDescribing.describeTo(describer);
            }
        }

        private Lens<O, T> lens;

        private DescriptionBinder(Lens<O, T> lens) {
            this.lens = lens;
        }
        
        public PathDescribingLens<O, T> with(final PathDescribing pathDescribing) {
            return new LensWithPathDescriber(pathDescribing);
        }
        
        public PathDescribingLens<O, T> withDescription(String description) {
            return new LensWithPathDescriber(StringDescribing.with(description));
        }
        
        public PathDescribingLens<O, T> withClassName(String classname) {
            return withDescription("<" + classname + ">");
        }
    }
    
    public static <O, T> DescriptionBinder<O, T> describing(Lens<O, T> lens) {
        return new DescriptionBinder<O, T>(lens);
    }
    
    public static <O, T> PathDescribingLens<O, T> ensureDescribing(Lens<O, T> lens) {
        if (lens instanceof PathDescribingLens) {
            return (PathDescribingLens<O, T>) lens;
        }
        return describing(lens).withClassName(lens.getClass().getSimpleName());
    }
}
