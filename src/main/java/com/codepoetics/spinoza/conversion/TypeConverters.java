package com.codepoetics.spinoza.conversion;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import com.codepoetics.spinoza.api.PathDescribingLens;
import com.codepoetics.spinoza.conversion.api.TypeConverter;
import com.codepoetics.spinoza.paths.api.PathDescriber;
import com.codepoetics.spinoza.utils.Reflection;

public final class TypeConverters {

    private TypeConverters() {
    }

    public static <O1, O2> String descriptionOf(TypeConverter<O1, O2> converter) {
        ParameterizedType tcType = Reflection.getParameterizedType(converter.getClass(), TypeConverter.class);
        Type[] actualTypeArguments = tcType.getActualTypeArguments();
        Type from = actualTypeArguments[0];
        Type to = actualTypeArguments[1];
        return Reflection.describeType(from) +" -> " + Reflection.describeType(to);
    }
    
    public static <O, O2, T> PathDescribingLens<O2, T> convertTarget(final PathDescribingLens<O, T> lens, final TypeConverter<O2, O> converter) {
        return new PathDescribingLens<O2, T>() {
            @Override
            public T get(O2 target) {
                return lens.get(converter.push(target));
            }

            @Override
            public O2 update(O2 target, T newValue) {
                return converter.pull(lens.update(converter.push(target), newValue));
            }

            @Override
            public void describeTo(PathDescriber describer) {
                describer.conversion(descriptionOf(converter)).pathDescribing(lens);
            }
            
        };
    }

}
