package se.umu.cs.ads.a1.backend.rest;

import org.restlet.Server;
import org.restlet.data.Protocol;

public class RestletServer {

    private static Server server;

    public RestletServer() {
        // Create the HTTP server and listen on port 8182
        server = new Server(Protocol.HTTP, 8080);
        server.setNext(new RestletApp());
        start();
    }

    public void start() {
        try {
            server.start();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void stop() {
        try {
            server.stop();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
