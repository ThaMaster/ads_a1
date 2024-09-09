package se.umu.cs.ads.a1.backend.rest;

import se.umu.cs.ads.a1.backend.InMemoryMessengerBackEnd;

public class RestBackend {
    private static InMemoryMessengerBackEnd backend;

    public static void setBackend(InMemoryMessengerBackEnd backend) {
        RestBackend.backend = backend;
    }

    public static InMemoryMessengerBackEnd getBackend()  {
        return backend;
    }
}
