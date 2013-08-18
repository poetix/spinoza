package com.codepoetics.spinoza.utils;

import static com.codepoetics.spinoza.conversion.StringConversion.toCharArray;

import com.codepoetics.spinoza.api.Lens;
import com.codepoetics.spinoza.collections.ArrayLens;
import com.codepoetics.spinoza.conversion.TypeConverters;


public class StringLenses {

    public static Lens<String, Character> characterAt(final int index) {
        return TypeConverters.convertTarget(ArrayLens.<Character>atIndex(index), toCharArray);
    }
}
