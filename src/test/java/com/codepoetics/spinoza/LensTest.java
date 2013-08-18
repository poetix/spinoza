package com.codepoetics.spinoza;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

import org.junit.Test;

import com.codepoetics.spinoza.api.Lens;
import com.codepoetics.spinoza.api.PathDescribingLens;
import com.codepoetics.spinoza.matchers.AnInstance;
import com.codepoetics.spinoza.paths.PathDescription;
import com.codepoetics.spinoza.paths.api.PathDescriber;
import com.codepoetics.spinoza.utils.StringLenses;
import com.google.common.base.Function;

public class LensTest {

    private static class Message {
        public final String message;
        public final int importance;
        public Message(String message, int importance) {
            this.message = message;
            this.importance = importance;
        }

    }
    
    private static class MessageLens implements PathDescribingLens<Message, String> {

        @Override
        public String get(Message target) {
            return target.message;
        }

        @Override
        public Message update(Message target, String newValue) {
            return new Message(newValue, target.importance);
        }

        @Override
        public void describeTo(PathDescriber describer) {
            describer.path("message");
        }
        
    }

    private static class ImportanceLens implements PathDescribingLens<Message, Integer> {

        @Override
        public Integer get(Message target) {
            return target.importance;
        }

        @Override
        public Message update(Message target, Integer newValue) {
            return new Message(target.message, newValue);
        }

        @Override
        public void describeTo(PathDescriber describer) {
            describer.path("importance");
        }
        
    }
    
    private static final Lens<Message, String> messageLens = new MessageLens();
    private static final Lens<Message, Integer> importanceLens = new ImportanceLens();
    private static final Message message = new Message("hello world",3);
    
    private static final Function<Integer, Integer> increment = new Function<Integer, Integer>() {
        @Override
        public Integer apply(Integer arg0) {
            return arg0 + 1;
        }
    };
    
    @Test public void
    get_retrieves_the_lensed_property_of_the_target() {
        assertThat(message, aMessage()
                .withProperty(messageLens).equalTo("hello world")
                .withProperty(importanceLens).equalTo(3));
    }
    
    @Test public void
    update_retrieves_a_clone_of_the_target_with_the_lensed_property_updated() {
        Message moreImportantMessage = importanceLens.update(message, 4);

        assertThat(moreImportantMessage, aMessage()
                .withProperty(messageLens).equalTo("hello world")
                .withProperty(importanceLens).equalTo(4));
    }
    
    @Test public void
    update_leaves_source_object_unmodified() {
        importanceLens.update(message, 4);

        assertThat(message, aMessage().withProperty(importanceLens).equalTo(3));
    }
    
    @Test public void
    update_with_updater_applies_updater_to_lensed_property() {
        Message moreImportantMessage = Lenses.update(message).using(importanceLens).with(increment);

        assertThat(moreImportantMessage, aMessage().withProperty(importanceLens).matching(equalTo(4)));
    }
    
    @Test public void
    lenses_compose_with_other_lenses() {
        Lens<Message, Character> firstCharOfMessage = Compose.theLens(messageLens).with(StringLenses.characterAt(0));
        
        Message updatedMessage = firstCharOfMessage.update(message,'j');
        
        assertThat(PathDescription.of(firstCharOfMessage), equalTo("/message{String -> Character[]}[0]"));
        assertThat(updatedMessage, aMessage()
                .withProperty(firstCharOfMessage).equalTo('j')
                .withProperty(messageLens).equalTo("jello world"));
    }
    
    private AnInstance<Message> aMessage() {
        return AnInstance.of(Message.class);
    }
}