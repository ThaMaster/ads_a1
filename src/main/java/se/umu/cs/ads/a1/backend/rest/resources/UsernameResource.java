package se.umu.cs.ads.a1.backend.rest.resources;

import org.restlet.data.MediaType;
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

public class UsernameResource extends ServerResource {
    private InMemoryMessengerBackEnd backend;

    @Override
    protected void doInit() throws ResourceException {
        super.doInit();
        backend = RestBackend.getBackend();
    }

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
        Username[] usernames = backend.listSubscribers(new Topic(topic));
        StringRepresentation usrRep = new StringRepresentation(JsonUtil.toJson(usernames));
        usrRep.setMediaType(MediaType.APPLICATION_JSON);
        return usrRep;
    }

    public Representation listAllUsers() {
        Username[] usernames = backend.listUsers();
        StringRepresentation usrRep = new StringRepresentation(JsonUtil.toJson(usernames));
        usrRep.setMediaType(MediaType.APPLICATION_JSON);
        return usrRep;
    }


}
