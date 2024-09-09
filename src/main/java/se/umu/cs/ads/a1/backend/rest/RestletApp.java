package se.umu.cs.ads.a1.backend.rest;

import org.restlet.Application;
import org.restlet.Restlet;
import org.restlet.routing.Router;
import se.umu.cs.ads.a1.backend.InMemoryMessengerBackEnd;
import se.umu.cs.ads.a1.backend.rest.resources.MessageIdsResource;
import se.umu.cs.ads.a1.backend.rest.resources.MessageResource;
import se.umu.cs.ads.a1.backend.rest.resources.MessagesResource;

public class RestletApp extends Application {

    @Override
    public Restlet createInboundRoot() {
        RestBackend.setBackend(new InMemoryMessengerBackEnd());

        Router router = new Router(getContext());

        router.attach("/message", MessageResource.class);
        router.attach("/message/{messageId}", MessageResource.class);

        router.attach("/messages", MessagesResource.class);
        router.attach("/messages/{ids}", MessagesResource.class);

        router.attach("/messageIds/username/{username}", MessageIdsResource.class);
        router.attach("/messageIds/topic/{topic}", MessageIdsResource.class);

        return router;
    }
}
