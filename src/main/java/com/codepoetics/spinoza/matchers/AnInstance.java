package com.codepoetics.spinoza.matchers;

import java.util.List;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.hamcrest.TypeSafeDiagnosingMatcher;

import com.codepoetics.spinoza.api.Lens;
import com.codepoetics.spinoza.api.PathDescribingLens;
import com.codepoetics.spinoza.paths.PathDescription;
import com.google.common.collect.Lists;

public class AnInstance<O> extends TypeSafeDiagnosingMatcher<O> {

    public static class LensBinder<O, T> {
        
        private final AnInstance<O> target;
        private final PathDescribingLens<O, T> lens;

        private LensBinder(AnInstance<O> target, PathDescribingLens<O, T> lens) {
            this.target = target;
            this.lens = lens;
            
        }
        
        public AnInstance<O> matching(Matcher<T> matcher) {
            target.add(lens, matcher);
            return target;
        }
        
        public AnInstance<O> equalTo(T value) {
            return matching(Matchers.equalTo(value));
        }
    }

    public static <O> AnInstance<O> of(Class<O> targetClass) {
        return new AnInstance<O>(targetClass);
    }

    private final Class<O> targetClass;
    private List<LensMatcher<O, ?>> matchers = Lists.newArrayList();
    
    private AnInstance(Class<O> targetClass) {
        this.targetClass = targetClass;
    }

    public <T> LensBinder<O, T> withProperty(Lens<O, T> lens) {
        return withProperty(PathDescription.ensureDescribing(lens));
    }
    
    public <T> LensBinder<O, T> withProperty(PathDescribingLens<O, T> lens) {
        return new LensBinder<O, T>(this, lens);
    }
    
    private static class LensMatcher<O, T> extends TypeSafeDiagnosingMatcher<O> {
        private final PathDescribingLens<O, T> lens;
        private final Matcher<T> matcher;
        
        public LensMatcher(PathDescribingLens<O, T> lens, Matcher<T> matcher) {
            this.lens = lens;
            this.matcher = matcher;
        }

        @Override
        public void describeTo(Description description) {
            description.appendText("a value at ").appendText(PathDescription.of(lens)).appendText(" matching ");
            matcher.describeTo(description);
        }

        @Override
        protected boolean matchesSafely(O item, Description description) {
            T value = lens.get(item);
            if (!matcher.matches(value)) {
                description.appendText("value at ").appendText(PathDescription.of(lens)).appendText(" ");
                matcher.describeMismatch(value, description);
                return false;
            }
            return true;
        }
    }
    
    public <T> AnInstance<O> add(PathDescribingLens<O, T> lens, Matcher<T> matcher) {
        matchers.add(new LensMatcher<O, T>(lens, matcher));
        return this;
    }
    
    @Override
    public void describeTo(Description description) {
        description.appendText(targetClass.getSimpleName()).appendText(" with ");
        boolean first = true;
        for (LensMatcher<O, ?> matcher : matchers) {
            if (first) { first = false; } else { description.appendText(" and "); }
            matcher.describeTo(description);
        }
    }

    @Override
    protected boolean matchesSafely(O item, Description description) {
        boolean result = true;
        boolean first = true;
        for (LensMatcher<O, ?> matcher : matchers) {
            if (!matcher.matches(item)) {
                if (first) { first = false; } else { description.appendText(" and "); }
                matcher.describeMismatch(item, description);
                result = false;
            }
        }
        return result;
    }

}
