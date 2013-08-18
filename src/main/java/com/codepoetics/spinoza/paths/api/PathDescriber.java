package com.codepoetics.spinoza.paths.api;

public interface PathDescriber {

    PathDescriber text(String description);
    PathDescriber index(Object index);
    PathDescriber conversion(String descriptionOf);
    PathDescriber pathDescribing(PathDescribing pathDescribing);
    PathDescriber path(String string);

}
