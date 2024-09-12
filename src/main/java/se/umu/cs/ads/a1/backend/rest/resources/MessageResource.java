package se.umu.cs.ads.a1.backend.rest.resources;

import org.restlet.data.MediaType;
import org.restlet.data.Status;
import org.restlet.representation.Representation;
import org.restlet.representation.StringRepresentation;
import org.restlet.resource.*;
import se.umu.cs.ads.a1.backend.InMemoryMessengerBackEnd;
import se.umu.cs.ads.a1.backend.rest.JsonUtil;
import se.umu.cs.ads.a1.backend.rest.RestBackend;
import se.umu.cs.ads.a1.types.Message;
import se.umu.cs.ads.a1.types.MessageId;

import java.io.IOException;

public class MessageResource extends ServerResource {

    private InMemoryMessengerBackEnd backend;

    @Override
    protected void doInit() throws ResourceException {
        super.doInit();
        backend = RestBackend.getBackend();
    }

    @Post("json")
    public void storeMessage(Representation msgEntity) {
        try {
            if (msgEntity.getMediaType().equals(MediaType.APPLICATION_JSON)) {

                backend.store(JsonUtil.parseMessage(msgEntity.getText()));

                setStatus(Status.SUCCESS_ACCEPTED);
            } else {
                setStatus(Status.CLIENT_ERROR_UNSUPPORTED_MEDIA_TYPE);
            }
        } catch (IOException e) {
            e.printStackTrace();
            setStatus(Status.CLIENT_ERROR_BAD_REQUEST);
        }
    }

    @Get("json")
    public Representation getMessageById() {
        String id = getQueryValue("messageId");
        try {
            // Fetch
            Message msg = backend.retrieve(new MessageId(id));

            // Send
            setStatus(Status.SUCCESS_OK);
            return new StringRepresentation(JsonUtil.toJson(msg));
        } catch (IllegalArgumentException e) {
            return new StringRepresentation("");
        }
    }

    @Delete
    public void handleDelete() {
        String id = getQueryValue("messageId");
        if (id == null || id.isEmpty()) {
            setStatus(Status.CLIENT_ERROR_BAD_REQUEST, "No message id provided!");
        }
        backend.delete(new MessageId(id));
        setStatus(Status.SUCCESS_OK);
    }
}
