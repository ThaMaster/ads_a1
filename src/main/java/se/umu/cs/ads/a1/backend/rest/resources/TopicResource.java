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
import se.umu.cs.ads.a1.types.Topic;
import se.umu.cs.ads.a1.types.Username;

public class TopicResource extends ServerResource {
    private InMemoryMessengerBackEnd backend;

    @Override
    protected void doInit() throws ResourceException {
        super.doInit();
        backend = RestBackend.getBackend();
    }

    @Get
    public JacksonRepresentation<Topic[]> handleGet() {
        String usernameJson = getQueryValue("username");
        if(usernameJson != null) {
            return getTopicsByUsername(usernameJson);
        } else {
            return getAllTopics();
        }
    }

    public JacksonRepresentation<Topic[]> getTopicsByUsername(String usernameJson) {
        Username username = new Username(JsonUtil.getValueFromJson(usernameJson));
        JacksonRepresentation<Topic[]> topics = new JacksonRepresentation<>(backend.listTopics(username));
        topics.setMediaType(MediaType.APPLICATION_JSON);
        setStatus(Status.SUCCESS_OK);
        return topics;
    }

    public JacksonRepresentation<Topic[]> getAllTopics() {
        JacksonRepresentation<Topic[]> topics = new JacksonRepresentation<>(backend.listTopics());
        topics.setMediaType(MediaType.APPLICATION_JSON);
        setStatus(Status.SUCCESS_OK);
        return topics;
    }
}
