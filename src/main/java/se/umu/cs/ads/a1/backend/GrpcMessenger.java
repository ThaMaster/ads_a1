package se.umu.cs.ads.a1.backend;

import com.google.protobuf.Empty;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.StatusRuntimeException;
import org.apache.commons.lang3.tuple.Pair;
import proto.MessengerServiceGrpc;
import se.umu.cs.ads.a1.backend.grpc.GrpcServer;
import se.umu.cs.ads.a1.backend.grpc.GrpcUtil;
import se.umu.cs.ads.a1.interfaces.Messenger;
import se.umu.cs.ads.a1.types.Message;
import se.umu.cs.ads.a1.types.MessageId;
import se.umu.cs.ads.a1.types.Topic;
import se.umu.cs.ads.a1.types.Username;

import java.io.IOException;
import java.net.ServerSocket;

/**
 * The gRPC version of the Messenger interface. It handles
 * all the communication with Remote Procedure Calls, more
 * specifically the framework created by Google in 2016.
 */
public class GrpcMessenger implements Messenger {

    private ManagedChannel channel = null;
    private MessengerServiceGrpc.MessengerServiceBlockingStub stub;

    /**
     * Constructor of the GrpcMessenger, starts the server
     * locally on a different thread, creates a channel for
     * communication and creates a stub to communicate with.
     */
    public GrpcMessenger() {
        startServerOnThread();

        // Initialize the channel and stub
        channel = ManagedChannelBuilder.forAddress("127.0.0.1", 8080)
                .usePlaintext()
                .maxInboundMessageSize(25 * 1024 * 1024) // 10 MB
                .build();
        stub = MessengerServiceGrpc.newBlockingStub(channel);
    }

    /**
     * Starts the server on a separate thread, enabling
     * communication with this class.
     */
    private void startServerOnThread() {
        try {
            new Thread(() -> new GrpcServer().start()).start();
        } catch (Exception e) {
            System.out.println("BRUH");
        }
    }

    @Override
    public void store(Message message) {
        stub.storeMessage(GrpcUtil.javaToProto(message));
    }

    @Override
    public void store(Message[] messages) {
        stub.storeMessages(GrpcUtil.javaToProto(messages));
    }

    @Override
    public Message retrieve(MessageId message) {
        try {
            proto.Message response = stub.retrieveMessage(GrpcUtil.javaToProto(message));
            return GrpcUtil.protoToJava(response);
        } catch (StatusRuntimeException e) {
            return null;
        }
    }

    @Override
    public Message[] retrieve(MessageId[] message) {
        proto.Messages response = stub.retrieveMessages(GrpcUtil.javaToProto(message));
        return GrpcUtil.protoToJava(response);
    }

    @Override
    public void delete(MessageId message) {
        stub.deleteMessage(GrpcUtil.javaToProto(message));
    }

    @Override
    public void delete(MessageId[] messages) {
        stub.deleteMessages(GrpcUtil.javaToProto(messages));
    }

    @Override
    public Topic[] subscribe(Username username, Topic topic) {
        Pair<Username, Topic> subReq = Pair.of(username, topic);
        proto.Topics response = stub.subscribe(GrpcUtil.javaToProto(subReq));
        return GrpcUtil.protoToJava(response);
    }

    @Override
    public Topic[] unsubscribe(Username username, Topic topic) {
        Pair<Username, Topic> subReq = Pair.of(username, topic);
        proto.Topics response = stub.unsubscribe(GrpcUtil.javaToProto(subReq));
        return GrpcUtil.protoToJava(response);
    }

    @Override
    public Username[] listUsers() {
        proto.Usernames response = stub.listUsers(Empty.newBuilder().build());
        return GrpcUtil.protoToJava(response);
    }

    @Override
    public Topic[] listTopics() {
        proto.Topics response = stub.listTopics(Empty.newBuilder().build());
        return GrpcUtil.protoToJava(response);
    }

    @Override
    public Topic[] listTopics(Username username) {
        proto.Topics response = stub.listUserTopics(GrpcUtil.javaToProto(username));
        return GrpcUtil.protoToJava(response);
    }

    @Override
    public Username[] listSubscribers(Topic topic) {
        proto.Usernames response = stub.listSubscribers(GrpcUtil.javaToProto(topic));
        return GrpcUtil.protoToJava(response);
    }

    @Override
    public MessageId[] listMessages(Username username) {
        proto.MessageIds response = stub.listUserMessages(GrpcUtil.javaToProto(username));
        return GrpcUtil.protoToJava(response);
    }

    @Override
    public MessageId[] listMessages(Topic topic) {
            proto.MessageIds response = stub.listTopicMessages(GrpcUtil.javaToProto(topic));
            return GrpcUtil.protoToJava(response);
    }
}
