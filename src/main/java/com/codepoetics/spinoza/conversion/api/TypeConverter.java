package com.codepoetics.spinoza.conversion.api;

public interface TypeConverter<T1, T2> {

    T2 push(T1 in);
    T1 pull(T2 out);
    
}
