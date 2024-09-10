package se.umu.cs.ads.a1.backend.rest.resources;

import org.restlet.representation.Representation;
import org.restlet.resource.Get;
import org.restlet.resource.Post;
import org.restlet.resource.ResourceException;
import org.restlet.resource.ServerResource;
import se.umu.cs.ads.a1.backend.InMemoryMessengerBackEnd;
import se.umu.cs.ads.a1.backend.rest.RestBackend;

public class UserResource extends ServerResource {

    private InMemoryMessengerBackEnd backend;

    @Override
    protected void doInit() throws ResourceException {
        super.doInit();
        backend = RestBackend.getBackend();
    }

    @Post
    public void handlePost(Representation msgEntity) {
    }

    @Get
    public String handleGet() {
        // Process GET request and generate a response
        String response = "Hello, this is a response from the GET request!";
        return response;
    }
}
