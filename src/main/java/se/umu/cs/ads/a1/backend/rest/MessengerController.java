package se.umu.cs.ads.a1.backend.rest;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import se.umu.cs.ads.a1.types.*;

import java.util.HashMap;

@RestController
public class MessengerController {
    private final HashMap<MessageId, Message> messageMap = new HashMap<>();
    private final HashMap<SubscriptionId, Subscription> subscriptionMap = new HashMap<>();
    //private final HashMap<Topic, InMemoryMessengerBackEnd.TopicBackEnd> topicMap = new HashMap();

    @PostMapping("/message")
    @ResponseStatus(HttpStatus.OK)
    public void store(@RequestBody Message message) {
        messageMap.put(message.getId(), message.copyWith(Timestamp.now()));
    }

    @PostMapping("/message/delete")
    @ResponseStatus(HttpStatus.OK)
    public void delete(@RequestBody MessageId mId) {
        messageMap.remove(mId);
    }

    @GetMapping("/message")
    @ResponseStatus(HttpStatus.OK)
    public Message retrieve(@RequestBody MessageId mId) {
        return messageMap.get(mId);
    }

}
