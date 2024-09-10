package se.umu.cs.ads.a1.backend.rest.resources;

import org.restlet.data.MediaType;
import org.restlet.data.Status;
import org.restlet.ext.jackson.JacksonRepresentation;
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
    public JacksonRepresentation<Topic[]> handlePut() {
        String usernameJson = getQueryValue("username");
        String topicJson = getQueryValue("topic");
        String subscribe = getQueryValue("subscribe");

        if (usernameJson == null || topicJson == null || subscribe == null) {
            setStatus(Status.CLIENT_ERROR_BAD_REQUEST);
            return null;
        }

        if (subscribe.equals("true")) {
            return subscribe(usernameJson, topicJson);
        } else {
            return unsubscribe(usernameJson, topicJson);
        }
    }

    private JacksonRepresentation<Topic[]> subscribe(String usernameJson, String topicJson) {
        Username username = new Username(JsonUtil.getValueFromJson(usernameJson));
        Topic topic = JsonUtil.parseTopic(topicJson);
        JacksonRepresentation<Topic[]> recTopics = new JacksonRepresentation<>(backend.subscribe(username, topic));
        recTopics.setMediaType(MediaType.APPLICATION_JSON);
        return recTopics;
    }

    private JacksonRepresentation<Topic[]> unsubscribe(String usernameJson, String topicJson) {
        Username username = new Username(JsonUtil.getValueFromJson(usernameJson));
        Topic topic = JsonUtil.parseTopic(topicJson);
        JacksonRepresentation<Topic[]> recTopics = new JacksonRepresentation<>(backend.unsubscribe(username, topic));
        recTopics.setMediaType(MediaType.APPLICATION_JSON);
        return recTopics;
    }
}
