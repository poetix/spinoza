package com.codepoetics.spinoza;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

import java.util.Map;

import org.hamcrest.Matchers;
import org.junit.Test;

import com.codepoetics.spinoza.api.Lens;
import com.codepoetics.spinoza.collections.MapLens;
import com.codepoetics.spinoza.paths.PathDescription;
import com.google.common.collect.ImmutableMap;

public class MapLensTest {

    private final Map<String, String> map = ImmutableMap.<String, String>builder()
            .put("a", "apple")
            .put("b", "banana")
            .put("c", "cucumber")
            .build();
    
    private final Lens<Map<? extends String, ? extends String>, String> aLens = MapLens.atKey("a");
    
    @Test public void
    lens_retrieves_a_member_of_a_map() {
        assertThat(aLens.get(map), equalTo("apple"));
    }
    
    @Test public void
    lens_updates_a_map() {
        assertThat(aLens.update(map, "artichoke"), Matchers.hasEntry("a", "artichoke"));
    }
    
    @Test public void
    lens_describes_its_own_path() {
        assertThat(PathDescription.of(aLens), equalTo("[a]"));
    }
}
