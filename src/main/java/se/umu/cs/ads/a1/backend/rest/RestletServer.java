package se.umu.cs.ads.a1.backend.rest;

import org.restlet.Component;
import org.restlet.data.Protocol;
import org.restlet.service.LogService;

public class RestletServer {

    private static Component component;

    public RestletServer() {
        // Create the HTTP server and listen on port 8080
        System.out.println("Rest: Starting server!");
        // Create a Restlet component
        component = new Component();

        // Create and configure the HTTP server on localhost
        component.getServers().add(Protocol.HTTP, "localhost", 8080);

        // Attach the application to the component under the "/api" path
        component.getDefaultHost().attach("/messenger", new RestletApp());

        // Disable server logging
        component.setLogService(new LogService(false));

        // Start the server
        start();

        // Add shutdown hook to close the server when closing this thread.
        Runtime.getRuntime().addShutdownHook(new Thread(this::stop));
    }

    public void start() {
        try {
            component.start();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void stop() {
        try {
            component.stop();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
