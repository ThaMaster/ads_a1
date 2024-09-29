package se.umu.cs.ads.a1.backend.rest.resources;

import org.restlet.data.MediaType;
import org.restlet.data.Status;
import org.restlet.representation.Representation;
import org.restlet.representation.StringRepresentation;
import org.restlet.resource.Delete;
import org.restlet.resource.Get;
import org.restlet.resource.Post;
import org.restlet.resource.ServerResource;
import se.umu.cs.ads.a1.backend.InMemoryMessengerBackEnd;
import se.umu.cs.ads.a1.backend.rest.JsonUtil;
import se.umu.cs.ads.a1.types.Message;
import se.umu.cs.ads.a1.types.MessageId;

import java.io.IOException;

public class MessageResource extends ServerResource {

    @Post
    public void storeMessage(Representation msgEntity) {
        InMemoryMessengerBackEnd backend = (InMemoryMessengerBackEnd) getContext().getAttributes().get("backend");
        try {
            backend.store(JsonUtil.parseMessage(msgEntity.getText()));
            setStatus(Status.SUCCESS_ACCEPTED);
        } catch (IOException e) {
            e.printStackTrace();
            setStatus(Status.CLIENT_ERROR_BAD_REQUEST);
        }
    }

    @Get
    public Message getMessageById() {
        InMemoryMessengerBackEnd backend = (InMemoryMessengerBackEnd) getContext().getAttributes().get("backend");
        String id = getQueryValue("messageId");
        try {
            // Fetch
            Message msg = backend.retrieve(new MessageId(id));
            // Send
            setStatus(Status.SUCCESS_OK);
            return msg;
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    @Delete
    public void handleDelete() {
        InMemoryMessengerBackEnd backend = (InMemoryMessengerBackEnd) getContext().getAttributes().get("backend");
        String id = getQueryValue("messageId");
        if (id == null || id.isEmpty()) {
            setStatus(Status.CLIENT_ERROR_BAD_REQUEST, "No message id provided!");
        }
        backend.delete(new MessageId(id));
        setStatus(Status.SUCCESS_OK);
    }
}
