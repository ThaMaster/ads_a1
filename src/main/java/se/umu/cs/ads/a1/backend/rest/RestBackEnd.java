package se.umu.cs.ads.a1.backend.rest;

import se.umu.cs.ads.a1.backend.InMemoryMessengerBackEnd;
import se.umu.cs.ads.a1.interfaces.Messenger;
import se.umu.cs.ads.a1.types.*;

import java.util.HashMap;

public class RestBackEnd implements Messenger {

    @Override
    public void store(Message message) {
    }

    @Override
    public void store(Message[] messages) {

    }

    @Override
    public Message retrieve(MessageId message) {
        return null;
    }

    @Override
    public Message[] retrieve(MessageId[] message) {
        return new Message[0];
    }

    @Override
    public void delete(MessageId message) {

    }

    @Override
    public void delete(MessageId[] messages) {

    }

    @Override
    public Topic[] subscribe(Username username, Topic topic) {
        return new Topic[0];
    }

    @Override
    public Topic[] unsubscribe(Username username, Topic topic) {
        return new Topic[0];
    }

    @Override
    public Username[] listUsers() {
        return new Username[0];
    }

    @Override
    public Topic[] listTopics() {
        return new Topic[0];
    }

    @Override
    public Topic[] listTopics(Username username) {
        return new Topic[0];
    }

    @Override
    public Username[] listSubscribers(Topic topic) {
        return new Username[0];
    }

    @Override
    public MessageId[] listMessages(Username username) {
        return new MessageId[0];
    }

    @Override
    public MessageId[] listMessages(Topic topic) {
        return new MessageId[0];
    }
}
