package com.codepoetics.spinoza.paths;

import com.codepoetics.spinoza.paths.api.PathDescriber;
import com.codepoetics.spinoza.paths.api.PathDescribing;


public class StringDescribing implements PathDescribing {

    public static PathDescribing with(String description) {
        return new StringDescribing(description);
    }

    private final String description;
    
    private StringDescribing(String description) {
        this.description = description;
    }

    @Override
    public void describeTo(PathDescriber describer) {
        describer.text(description);
    }
}
