package se.umu.cs.ads.a1.backend.grpc;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import se.umu.cs.ads.a1.interfaces.Messenger;
import se.umu.cs.ads.a1.types.Message;
import se.umu.cs.ads.a1.types.MessageId;
import se.umu.cs.ads.a1.types.Topic;
import se.umu.cs.ads.a1.types.Username;

import java.io.IOException;

public class GrpcMessenger implements Messenger {

    private GrpcServer server;

    private ManagedChannel channel = null;

    public GrpcMessenger() {
        startServerOnThread();

        // Initialize the channel and stub
        channel = ManagedChannelBuilder.forAddress("localhost", 8080)
                .usePlaintext()
                .build();
    }

    private void startServerOnThread() {
        new Thread(() -> {
            try {
                GrpcServer server = new GrpcServer();
                server.start();

            } catch (IOException e) {
                System.out.println("IO EXCEPTOIN");
            } catch (InterruptedException e) {
                System.out.println("InterruptedException");
            }
        }).start();
    }

    public void store(Message message) {

//        HelloRequest request = HelloRequest.newBuilder().setName("Chrille").build();
//        GreeterGrpc.GreeterBlockingStub blockingStub = GreeterGrpc.newBlockingStub(channel);
//        HelloReply reply = blockingStub.sayHello(request);
//        System.out.println("[User] " + reply);
    }

    @Override
    public void store(Message[] messages) {
        // TODO: Implement this function
    }

    @Override
    public Message retrieve(MessageId message) {
        // TODO: Implement this function
        return null;
    }

    @Override
    public Message[] retrieve(MessageId[] message) {
        // TODO: Implement this function
        return new Message[0];
    }

    @Override
    public void delete(MessageId message) {
        // TODO: Implement this function
    }

    @Override
    public void delete(MessageId[] messages) {
        // TODO: Implement this function
    }

    @Override
    public Topic[] subscribe(Username username, Topic topic) {
        // TODO: Implement this function
        return new Topic[0];
    }

    @Override
    public Topic[] unsubscribe(Username username, Topic topic) {
        // TODO: Implement this function
        return new Topic[0];
    }

    @Override
    public Username[] listUsers() {
        // TODO: Implement this function
        return new Username[0];
    }

    @Override
    public Topic[] listTopics() {
        // TODO: Implement this function
        return new Topic[0];
    }

    @Override
    public Topic[] listTopics(Username username) {
        // TODO: Implement this function
        return new Topic[0];
    }

    @Override
    public Username[] listSubscribers(Topic topic) {
        // TODO: Implement this function
        return new Username[0];
    }

    @Override
    public MessageId[] listMessages(Username username) {
        // TODO: Implement this function
        return new MessageId[0];
    }

    @Override
    public MessageId[] listMessages(Topic topic) {
        // TODO: Implement this function
        return new MessageId[0];
    }
}
