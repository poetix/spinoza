package com.codepoetics.spinoza;

import com.codepoetics.spinoza.api.ComposableLens;
import com.codepoetics.spinoza.api.Lens;
import com.codepoetics.spinoza.api.PathDescribingLens;
import com.codepoetics.spinoza.paths.PathDescription;
import com.codepoetics.spinoza.paths.api.PathDescriber;

public final class Compose<O, T> {

    private Compose() {
    }

    public static <O, T> ComposableLens<O, T> theLens(Lens<O, T> lens) {
        final PathDescribingLens<O, T> describingLens = PathDescription
                .ensureDescribing(lens);
        return theLens(describingLens);
    }

    public static <O, T> ComposableLens<O, T> theLens(
            final PathDescribingLens<O, T> describingLens) {
        return new ComposableLens<O, T>() {
            @Override
            public T get(O target) {
                return describingLens.get(target);
            }

            @Override
            public O update(O target, T newValue) {
                return describingLens.update(target, newValue);
            }

            @Override
            public void describeTo(PathDescriber describer) {
                describingLens.describeTo(describer);
            }

            @Override
            public <T2> ComposableLens<O, T2> with(Lens<T, T2> other) {
                return with(PathDescription.ensureDescribing(other));
            }

            @Override
            public <T2> ComposableLens<O, T2> with(
                    PathDescribingLens<T, T2> other) {
                return new ComposedLens<O, T, T2>(describingLens, other);
            }
        };
    }

    private static class ComposedLens<O, T, T2> implements
            ComposableLens<O, T2> {

        private PathDescribingLens<O, T> first;
        private PathDescribingLens<T, T2> second;

        private ComposedLens(PathDescribingLens<O, T> first,
                PathDescribingLens<T, T2> second) {
            this.first = first;
            this.second = second;
        }

        @Override
        public T2 get(O target) {
            return second.get(first.get(target));
        }

        @Override
        public O update(O target, T2 newValue) {
            return first.update(target,
                    second.update(first.get(target), newValue));
        }

        @Override
        public void describeTo(PathDescriber describer) {
            first.describeTo(describer);
            second.describeTo(describer);
        }

        @Override
        public <T3> ComposableLens<O, T3> with(Lens<T2, T3> third) {
            return with(PathDescription.ensureDescribing(third));
        }

        @Override
        public <T3> ComposableLens<O, T3> with(PathDescribingLens<T2, T3> third) {
            return new ComposedLens<O, T2, T3>(this, third);
        }
    }
}
