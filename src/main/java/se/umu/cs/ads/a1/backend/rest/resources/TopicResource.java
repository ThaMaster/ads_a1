package se.umu.cs.ads.a1.backend.rest.resources;

import org.restlet.data.MediaType;
import org.restlet.data.Status;
import org.restlet.representation.Representation;
import org.restlet.representation.StringRepresentation;
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
    public Representation handleGet() {
        String username = getQueryValue("username");
        if(username != null) {
            return getTopicsByUsername(username);
        } else {
            return getAllTopics();
        }
    }

    public Representation getTopicsByUsername(String username) {
        Topic[] topics = backend.listTopics(new Username(username));
        StringRepresentation topicRep = new StringRepresentation(JsonUtil.toJson(topics));
        topicRep.setMediaType(MediaType.APPLICATION_JSON);
        setStatus(Status.SUCCESS_OK);
        return topicRep;
    }

    public Representation getAllTopics() {
        Topic[] topics = backend.listTopics();
        StringRepresentation topicRep = new StringRepresentation(JsonUtil.toJson(topics));
        topicRep.setMediaType(MediaType.APPLICATION_JSON);
        setStatus(Status.SUCCESS_OK);
        return topicRep;
    }
}
