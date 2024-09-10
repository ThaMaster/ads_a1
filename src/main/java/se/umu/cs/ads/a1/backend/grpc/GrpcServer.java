package se.umu.cs.ads.a1.backend.grpc;

import io.grpc.Server;
import io.grpc.ServerBuilder;
import io.grpc.stub.StreamObserver;
import proto.*;
import se.umu.cs.ads.a1.backend.InMemoryMessengerBackEnd;

import java.io.IOException;

public class GrpcServer {

    private InMemoryMessengerBackEnd backend;
    private Server server;

    public GrpcServer() {
        this.backend = new InMemoryMessengerBackEnd();
        this.server = ServerBuilder.forPort(8080)
                .addService(new MessengerService())
                .build();
    }

    public void start() throws IOException, InterruptedException {
        server.start();
        System.out.println("Server started, listening on " + 8080);
        Runtime.getRuntime().addShutdownHook(new Thread(this::stop));
    }

    private void stop() {
        if(server != null) {
            server.shutdown();
        }
    }

    private class MessengerService extends MessengerServiceGrpc.MessengerServiceImplBase {
        @Override
        public void storeMessage(Message request, StreamObserver<StoreMessageResponse> responseObserver) {
            super.storeMessage(request, responseObserver);
        }

        @Override
        public void storeMessages(Messages request, StreamObserver<StoreMessagesResponse> responseObserver) {
            super.storeMessages(request, responseObserver);
        }
    }
}
