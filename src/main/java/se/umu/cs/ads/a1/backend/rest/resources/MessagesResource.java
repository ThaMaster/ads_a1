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

public class MessagesResource extends ServerResource {

    @Post("json")
    public void batchStoreMessages(Representation msgEntity) {
        InMemoryMessengerBackEnd backend = (InMemoryMessengerBackEnd) getContext().getAttributes().get("backend");
        try {
            if (msgEntity.getMediaType().equals(MediaType.APPLICATION_JSON)) {
                Message[] msgList = JsonUtil.parseMessages(msgEntity.getText());
                backend.store(msgList);
                setStatus(Status.SUCCESS_OK);
            } else {
                setStatus(Status.CLIENT_ERROR_UNSUPPORTED_MEDIA_TYPE);
            }
        } catch (IOException e) {
            e.printStackTrace();
            setStatus(Status.CLIENT_ERROR_BAD_REQUEST);
        }
    }

    @Get("json")
    public Representation handleGet() {
        InMemoryMessengerBackEnd backend = (InMemoryMessengerBackEnd) getContext().getAttributes().get("backend");
        try {
            String qValue = getQueryValue("messageIds");
            MessageId[] ids = JsonUtil.parseMessageIds(qValue);

            Message[] messages = backend.retrieve(ids);

            setStatus(Status.SUCCESS_OK);
            return new StringRepresentation(JsonUtil.toJson(messages));
        } catch (IllegalArgumentException e) {
            return new StringRepresentation("");
        }
    }

    @Delete
    public void handleDelete() {
        InMemoryMessengerBackEnd backend = (InMemoryMessengerBackEnd) getContext().getAttributes().get("backend");
        String qValue = getQueryValue("messageIds");
        MessageId[] ids = JsonUtil.parseMessageIds(qValue);

        backend.delete(ids);
        setStatus(Status.SUCCESS_OK);
    }
}
