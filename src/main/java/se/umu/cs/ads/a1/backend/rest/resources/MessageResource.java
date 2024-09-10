package se.umu.cs.ads.a1.backend.rest.resources;

import org.restlet.data.MediaType;
import org.restlet.data.Status;
import org.restlet.ext.jackson.JacksonRepresentation;
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

    @Post
    public void storeMessage(JacksonRepresentation<Message> msgEntity) {
        try {
            if (msgEntity.getMediaType().equals(MediaType.APPLICATION_JSON)) {
                Message msg = JsonUtil.parseMessage(msgEntity.getText());

                backend.store(msg);

                setStatus(Status.SUCCESS_ACCEPTED);
            } else {
                setStatus(Status.CLIENT_ERROR_UNSUPPORTED_MEDIA_TYPE);
            }
        } catch (IOException e) {
            e.printStackTrace();
            setStatus(Status.CLIENT_ERROR_BAD_REQUEST);
        }
    }

    @Get
    public JacksonRepresentation<Message> getMessageById() {
        // Get the message id to retrieve
        String qValue = getQueryValue("messageId");
        String id = JsonUtil.getValueFromJson(qValue);
        if (id == null || id.isEmpty()) {
            setStatus(Status.CLIENT_ERROR_BAD_REQUEST, "No message id provided!");
            return null;
        }

        Message msg;

        try {
            // Fetch
            msg = backend.retrieve(new MessageId(id));
        } catch (IllegalArgumentException e) {
            return null;
        }

        // Serialize
        JacksonRepresentation<Message> msgRep = new JacksonRepresentation<>(msg);
        msgRep.setMediaType(MediaType.APPLICATION_JSON);

        // Send
        setStatus(Status.SUCCESS_OK);
        return msgRep;
    }

    @Delete
    public void handleDelete() {
        String qValue = getQueryValue("messageId");
        String id = JsonUtil.getValueFromJson(qValue);
        if (id == null || id.isEmpty()) {
            setStatus(Status.CLIENT_ERROR_BAD_REQUEST, "No message id provided!");
        }
        backend.delete(new MessageId(id));
        setStatus(Status.SUCCESS_OK);
    }
}
