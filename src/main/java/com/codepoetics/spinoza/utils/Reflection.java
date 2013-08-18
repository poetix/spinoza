package com.codepoetics.spinoza.utils;

import java.lang.reflect.GenericArrayType;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import com.google.common.base.Joiner;

public class Reflection {

    public static ParameterizedType getParameterizedType(Class<?> klass,
            Class<?> type) {
        Type[] genericInterfaces = klass.getGenericInterfaces();
        for (Type genericInterface : genericInterfaces) {
            if (genericInterface instanceof ParameterizedType) {
                ParameterizedType pType = (ParameterizedType) genericInterface;
                Class<?> rawType = (Class<?>) pType.getRawType();
                if (rawType.equals(type)) {
                    return pType;
                }
            }
        }
        return null;
    }

    public static String describeType(Type type) {
        if (type instanceof Class) {
            Class<?> klass = (Class<?>) type;
            return klass.getSimpleName();
        }

        if (type instanceof GenericArrayType) {
            GenericArrayType gat = (GenericArrayType) type;
            return describeType(gat.getGenericComponentType()) + "[]";
        }

        if (type instanceof ParameterizedType) {
            ParameterizedType pType = (ParameterizedType) type;
            Type[] typeArguments = pType.getActualTypeArguments();
            String[] descriptions = new String[typeArguments.length];
            for (int i = 0; i < typeArguments.length; i++) {
                descriptions[i] = describeType(typeArguments[i]);
            }
            return describeType(pType.getRawType()) + "<"
                    + Joiner.on(",").join(descriptions) + ">";
        }

        return type.toString();
    }
}
