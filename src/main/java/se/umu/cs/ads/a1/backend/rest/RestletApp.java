package se.umu.cs.ads.a1.backend.rest;

import org.restlet.Application;
import org.restlet.Restlet;
import org.restlet.routing.Router;
import se.umu.cs.ads.a1.backend.rest.resources.MessageResource;

public class RestletApp extends Application {

    @Override
    public Restlet createInboundRoot() {
        Router router = new Router(getContext());

        router.attach("/message", MessageResource.class);
        return router;
    }
}
