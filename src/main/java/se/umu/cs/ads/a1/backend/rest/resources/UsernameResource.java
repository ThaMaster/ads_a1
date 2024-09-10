package se.umu.cs.ads.a1.backend.rest.resources;

import org.restlet.data.MediaType;
import org.restlet.ext.jackson.JacksonRepresentation;
import org.restlet.resource.Get;
import org.restlet.resource.ResourceException;
import org.restlet.resource.ServerResource;
import se.umu.cs.ads.a1.backend.InMemoryMessengerBackEnd;
import se.umu.cs.ads.a1.backend.rest.JsonUtil;
import se.umu.cs.ads.a1.backend.rest.RestBackend;
import se.umu.cs.ads.a1.types.Topic;
import se.umu.cs.ads.a1.types.Username;

public class UsernameResource extends ServerResource {
    private InMemoryMessengerBackEnd backend;

    @Override
    protected void doInit() throws ResourceException {
        super.doInit();
        backend = RestBackend.getBackend();
    }

    @Get
    public JacksonRepresentation<Username[]> handleGet() {
        String topicsJson = getQueryValue("topic");
        if(topicsJson != null)  {
            return listSubscribers(topicsJson);
        } else {
            return listAllUsers();
        }
    }

    public JacksonRepresentation<Username[]> listSubscribers(String topicJson) {
        Topic topic = JsonUtil.parseTopic(topicJson);
        JacksonRepresentation<Username[]> usernames = new JacksonRepresentation<>(backend.listSubscribers(topic));
        usernames.setMediaType(MediaType.APPLICATION_JSON);
        return usernames;
    }

    public JacksonRepresentation<Username[]> listAllUsers() {
        JacksonRepresentation<Username[]> usernames = new JacksonRepresentation<>(backend.listUsers());
        usernames.setMediaType(MediaType.APPLICATION_JSON);
        return usernames;
    }


}
