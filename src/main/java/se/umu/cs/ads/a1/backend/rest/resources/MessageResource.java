package se.umu.cs.ads.a1.backend.rest.resources;

import org.restlet.resource.Get;
import org.restlet.resource.Post;
import org.restlet.resource.ServerResource;
import se.umu.cs.ads.a1.backend.InMemoryMessengerBackEnd;
import se.umu.cs.ads.a1.types.Message;
import se.umu.cs.ads.a1.types.MessageId;

public class MessageResource extends ServerResource {

    private InMemoryMessengerBackEnd backend;

    @Post
    public void handlePost(Message msg) {
        backend.store(msg);
    }

    @Get
    public Message handleGet(MessageId msgId) {
        return backend.retrieve(msgId);
    }
}
