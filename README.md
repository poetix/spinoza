spinoza
=======

Lenses for Java

```java

    private static final Lens<Message, String> messageLens = new MessageLens();
    private static final Lens<Message, Integer> importanceLens = new ImportanceLens();
    private static final Message message = new Message("hello world",3);
    
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
```
