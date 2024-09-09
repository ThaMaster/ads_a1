package se.umu.cs.ads.a1.backend.rest.resources;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.restlet.data.MediaType;
import org.restlet.data.Status;
import org.restlet.ext.jackson.JacksonRepresentation;
import org.restlet.resource.Get;
import org.restlet.resource.ResourceException;
import org.restlet.resource.ServerResource;
import se.umu.cs.ads.a1.backend.InMemoryMessengerBackEnd;
import se.umu.cs.ads.a1.backend.rest.RestBackend;
import se.umu.cs.ads.a1.types.MessageId;
import se.umu.cs.ads.a1.types.Topic;
import se.umu.cs.ads.a1.types.Username;

import java.io.IOException;

public class MessageIdsResource extends ServerResource {
    private InMemoryMessengerBackEnd backend;

    @Override
    protected void doInit() throws ResourceException {
        super.doInit();
        backend = RestBackend.getBackend();
    }

    @Get
    public JacksonRepresentation<MessageId[]> handleGet() {
        String username = (String) getRequestAttributes().get("username");
        String topic = (String) getRequestAttributes().get("topic");

        if(username != null) {
            return getMessagesByUsername(username);
        } else if(topic != null) {
            return getMessagesByTopic(topic);
        } else {
            setStatus(Status.CLIENT_ERROR_BAD_REQUEST);
            return null;
        }
    }

    private JacksonRepresentation<MessageId[]> getMessagesByUsername(String username) {
        MessageId[] messages = backend.listMessages(new Username(username));

        JacksonRepresentation<MessageId[]> msgRep = new JacksonRepresentation<>(messages);
        msgRep.setMediaType(MediaType.APPLICATION_JSON);
        return msgRep;
    }

    private JacksonRepresentation<MessageId[]> getMessagesByTopic(String topic) {
        MessageId[] messages = backend.listMessages(new Topic(topic));

        JacksonRepresentation<MessageId[]> msgRep = new JacksonRepresentation<>(messages);
        msgRep.setMediaType(MediaType.APPLICATION_JSON);
        return msgRep;
    }
}
