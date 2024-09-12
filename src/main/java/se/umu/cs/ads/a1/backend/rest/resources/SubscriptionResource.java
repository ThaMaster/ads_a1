package se.umu.cs.ads.a1.backend.rest.resources;

import org.restlet.data.MediaType;
import org.restlet.data.Status;
import org.restlet.representation.Representation;
import org.restlet.representation.StringRepresentation;
import org.restlet.resource.Put;
import org.restlet.resource.ResourceException;
import org.restlet.resource.ServerResource;
import se.umu.cs.ads.a1.backend.InMemoryMessengerBackEnd;
import se.umu.cs.ads.a1.backend.rest.JsonUtil;
import se.umu.cs.ads.a1.backend.rest.RestBackend;
import se.umu.cs.ads.a1.types.Topic;
import se.umu.cs.ads.a1.types.Username;

public class SubscriptionResource extends ServerResource {
    private InMemoryMessengerBackEnd backend;

    @Override
    protected void doInit() throws ResourceException {
        super.doInit();
        backend = RestBackend.getBackend();
    }

    @Put
    public Representation handlePut() {
        String username = getQueryValue("username");
        String topic = getQueryValue("topic");
        String subscribe = getQueryValue("subscribe");

        if (username == null || topic == null || subscribe == null) {
            setStatus(Status.CLIENT_ERROR_BAD_REQUEST);
            return null;
        }

        if (subscribe.equals("true")) {
            return subscribe(username, topic);
        } else {
            return unsubscribe(username, topic);
        }
    }

    private Representation subscribe(String username, String topic) {
        Topic[] recTopics = backend.subscribe(new Username(username), new Topic(topic));
        return new StringRepresentation(JsonUtil.toJson(recTopics));
    }

    private Representation unsubscribe(String username, String topic) {
        Topic[] recTopics = backend.unsubscribe(new Username(username), new Topic(topic));
        return new StringRepresentation(JsonUtil.toJson(recTopics));
    }
}
