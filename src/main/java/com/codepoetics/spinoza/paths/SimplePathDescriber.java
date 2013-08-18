package com.codepoetics.spinoza.paths;

import com.codepoetics.spinoza.paths.api.PathDescriber;
import com.codepoetics.spinoza.paths.api.PathDescribing;

public class SimplePathDescriber implements PathDescriber {

    private final StringBuilder builder = new StringBuilder();
    
    @Override
    public PathDescriber text(String description) {
        builder.append(description);
        return this;
    }

    @Override
    public PathDescriber index(Object index) {
        builder.append("[").append(index).append("]");
        return this;
    }

    @Override
    public PathDescriber conversion(String conversionDescription) {
        builder.append("{").append(conversionDescription).append("}");
        return this;
    }

    @Override
    public PathDescriber pathDescribing(PathDescribing pathDescribing) {
        pathDescribing.describeTo(this);
        return this;
    }
    
    @Override
    public PathDescriber path(String path) {
        builder.append("/").append(path);
        return this;
    }
    
    @Override
    public String toString() {
        return builder.toString();
    }

}
