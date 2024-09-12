package se.umu.cs.ads.a1.backend.rest;

import org.restlet.Application;
import org.restlet.Context;
import org.restlet.Restlet;
import org.restlet.routing.Router;
import se.umu.cs.ads.a1.backend.InMemoryMessengerBackEnd;
import se.umu.cs.ads.a1.backend.rest.resources.*;

public class RestletApp extends Application {

    @Override
    public Restlet createInboundRoot() {
        Context context = getContext();
        context.getAttributes().put("backend", new InMemoryMessengerBackEnd());

        Router router = new Router(context);

        router.attach("/message", MessageResource.class);
        router.attach("/message/{messageId}", MessageResource.class);
        router.attach("/messages", MessagesResource.class);

        router.attach("/messageIds/username", MessageIdsResource.class);
        router.attach("/messageIds/topic", MessageIdsResource.class);

        router.attach("/topics", TopicResource.class);
        router.attach("/usernames", UsernameResource.class);

        router.attach("/subscription", SubscriptionResource.class);

        return router;
    }
}
