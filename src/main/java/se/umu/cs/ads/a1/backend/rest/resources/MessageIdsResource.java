package se.umu.cs.ads.a1.backend.rest.resources;

import org.restlet.data.MediaType;
import org.restlet.data.Status;
import org.restlet.ext.jackson.JacksonRepresentation;
import org.restlet.resource.Get;
import org.restlet.resource.ResourceException;
import org.restlet.resource.ServerResource;
import se.umu.cs.ads.a1.backend.InMemoryMessengerBackEnd;
import se.umu.cs.ads.a1.backend.rest.JsonUtil;
import se.umu.cs.ads.a1.backend.rest.RestBackend;
import se.umu.cs.ads.a1.types.MessageId;
import se.umu.cs.ads.a1.types.Topic;
import se.umu.cs.ads.a1.types.Username;

public class MessageIdsResource extends ServerResource {
    private InMemoryMessengerBackEnd backend;

    @Override
    protected void doInit() throws ResourceException {
        super.doInit();
        backend = RestBackend.getBackend();
    }

    @Get
    public JacksonRepresentation<MessageId[]> handleGet() {
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

    private JacksonRepresentation<MessageId[]> getMessagesByUsername(String usernameJson) {
        Username username = new Username(JsonUtil.getValueFromJson(usernameJson));
        MessageId[] messages = backend.listMessages(username);

        JacksonRepresentation<MessageId[]> msgRep = new JacksonRepresentation<>(messages);
        msgRep.setMediaType(MediaType.APPLICATION_JSON);
        setStatus(Status.SUCCESS_OK);
        return msgRep;
    }

    private JacksonRepresentation<MessageId[]> getMessagesByTopic(String topicJson) {
        Topic topic = new Topic(JsonUtil.getValueFromJson(topicJson));
        MessageId[] messages = backend.listMessages(topic);

        JacksonRepresentation<MessageId[]> msgRep = new JacksonRepresentation<>(messages);
        msgRep.setMediaType(MediaType.APPLICATION_JSON);
        setStatus(Status.SUCCESS_OK);
        return msgRep;
    }
}
