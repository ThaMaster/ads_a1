package se.umu.cs.ads.a1.backend.rest.resources;

import org.restlet.data.Status;
import org.restlet.representation.Representation;
import org.restlet.representation.StringRepresentation;
import org.restlet.resource.Get;
import org.restlet.resource.ServerResource;
import se.umu.cs.ads.a1.backend.InMemoryMessengerBackEnd;
import se.umu.cs.ads.a1.backend.rest.JsonUtil;
import se.umu.cs.ads.a1.types.MessageId;
import se.umu.cs.ads.a1.types.Topic;
import se.umu.cs.ads.a1.types.Username;

public class MessageIdsResource extends ServerResource {

    @Get("json")
    public Representation handleGet() {
        InMemoryMessengerBackEnd backend = (InMemoryMessengerBackEnd) getContext().getAttributes().get("backend");
        String username = getQueryValue("username");
        String topic = getQueryValue("topic");

        if (username != null) {
            return getMessagesByUsername(username);
        } else if (topic != null) {
            return getMessagesByTopic(topic);
        } else {
            setStatus(Status.CLIENT_ERROR_BAD_REQUEST);
            return null;
        }
    }

    private Representation getMessagesByUsername(String username) {
        InMemoryMessengerBackEnd backend = (InMemoryMessengerBackEnd) getContext().getAttributes().get("backend");
        MessageId[] messages = backend.listMessages(new Username(username));
        setStatus(Status.SUCCESS_OK);
        return new StringRepresentation(JsonUtil.toJson(messages));
    }

    private Representation getMessagesByTopic(String topic) {
        InMemoryMessengerBackEnd backend = (InMemoryMessengerBackEnd) getContext().getAttributes().get("backend");
        MessageId[] messages = backend.listMessages(new Topic(topic));
        setStatus(Status.SUCCESS_OK);
        return new StringRepresentation(JsonUtil.toJson(messages));
    }
}
