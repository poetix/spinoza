package com.codepoetics.spinoza.conversion;

import com.codepoetics.spinoza.conversion.api.TypeConverter;

public final class StringConversion {

    private StringConversion() { }
    
    public static final TypeConverter<String, Character[]> toCharArray = new TypeConverter<String, Character[]>() {

        @Override
        public Character[] push(String in) {
            char[] chars = in.toCharArray();
            Character[] characters = new Character[chars.length];
            for (int i = 0; i < chars.length; i++) {
                characters[i] = chars[i];
            }
            return characters;
        }

        @Override
        public String pull(Character[] out) {
            char[] chars = new char[out.length];
            for (int i = 0; i < out.length; i++) {
                chars[i] = out[i];
            }
            return String.copyValueOf(chars);
        }
    };
    
}
