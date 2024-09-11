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

public class GrpcMessenger implements Messenger {

    private ManagedChannel channel = null;
    private MessengerServiceGrpc.MessengerServiceBlockingStub stub;

    public GrpcMessenger() {
        startServerOnThread();

        // Initialize the channel and stub
        channel = ManagedChannelBuilder.forAddress("localhost", 8080)
                .usePlaintext()
                .maxInboundMessageSize(25 * 1024 * 1024) // 10 MB
                .build();
        stub = MessengerServiceGrpc.newBlockingStub(channel);
    }

    private void startServerOnThread() {
        new Thread(() -> {
            try {
                GrpcServer server = new GrpcServer();
                server.start();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
    }

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
