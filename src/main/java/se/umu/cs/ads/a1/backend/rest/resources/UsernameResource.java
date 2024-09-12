package se.umu.cs.ads.a1.backend.rest.resources;

import org.restlet.data.MediaType;
import org.restlet.representation.Representation;
import org.restlet.representation.StringRepresentation;
import org.restlet.resource.Get;
import org.restlet.resource.ServerResource;
import se.umu.cs.ads.a1.backend.InMemoryMessengerBackEnd;
import se.umu.cs.ads.a1.backend.rest.JsonUtil;
import se.umu.cs.ads.a1.types.Topic;
import se.umu.cs.ads.a1.types.Username;

public class UsernameResource extends ServerResource {

    @Get("json")
    public Representation handleGet() {
        String topic = getQueryValue("topic");
        if (topic != null) {
            return listSubscribers(topic);
        } else {
            return listAllUsers();
        }
    }

    public Representation listSubscribers(String topic) {
        InMemoryMessengerBackEnd backend = (InMemoryMessengerBackEnd) getContext().getAttributes().get("backend");
        Username[] usernames = backend.listSubscribers(new Topic(topic));
        StringRepresentation usrRep = new StringRepresentation(JsonUtil.toJson(usernames));
        usrRep.setMediaType(MediaType.APPLICATION_JSON);
        return usrRep;
    }

    public Representation listAllUsers() {
        InMemoryMessengerBackEnd backend = (InMemoryMessengerBackEnd) getContext().getAttributes().get("backend");
        Username[] usernames = backend.listUsers();
        StringRepresentation usrRep = new StringRepresentation(JsonUtil.toJson(usernames));
        usrRep.setMediaType(MediaType.APPLICATION_JSON);
        return usrRep;
    }


}
