package se.umu.cs.ads.a1.backend.grpc;

import com.google.protobuf.Empty;
import io.grpc.Server;
import io.grpc.ServerBuilder;
import io.grpc.Status;
import io.grpc.stub.StreamObserver;
import org.apache.commons.lang3.tuple.Pair;
import proto.*;
import se.umu.cs.ads.a1.backend.InMemoryMessengerBackEnd;
import se.umu.cs.ads.a1.types.Message;
import se.umu.cs.ads.a1.types.MessageId;
import se.umu.cs.ads.a1.types.Topic;
import se.umu.cs.ads.a1.types.Username;

import java.io.IOException;

public class GrpcServer {

    private InMemoryMessengerBackEnd backend;
    private Server server;

    public GrpcServer() {
        this.backend = new InMemoryMessengerBackEnd();
        this.server = ServerBuilder.forPort(8080)
                .maxInboundMessageSize(25 * 1024 * 1024) // Set to 25 MB
                .addService(new MessengerService())
                .build();
    }

    public void start() throws IOException, InterruptedException {
        server.start();
        Runtime.getRuntime().addShutdownHook(new Thread(this::stop));
    }

    private void stop() {
        if (server != null) {
            server.shutdown();
        }
    }

    private class MessengerService extends MessengerServiceGrpc.MessengerServiceImplBase {

        @Override
        public void storeMessage(proto.Message request, StreamObserver<Empty> responseObserver) {
            Message message = GrpcUtil.protoToJava(request);
            backend.store(message);

            responseObserver.onNext(Empty.newBuilder().build());
            responseObserver.onCompleted();
        }

        @Override
        public void storeMessages(Messages request, StreamObserver<Empty> responseObserver) {
            Message[] messages = GrpcUtil.protoToJava(request);
            backend.store(messages);

            responseObserver.onNext(Empty.newBuilder().build());
            responseObserver.onCompleted();
        }

        @Override
        public void retrieveMessage(proto.MessageId request, StreamObserver<proto.Message> responseObserver) {
            MessageId id = GrpcUtil.protoToJava(request);
            try {
                Message foundMessage = backend.retrieve(id);

                responseObserver.onNext(GrpcUtil.javaToProto(foundMessage));
                responseObserver.onCompleted();

            } catch (IllegalArgumentException e) {
                responseObserver.onError(Status.NOT_FOUND.asRuntimeException());
            }
        }

        @Override
        public void retrieveMessages(MessageIds request, StreamObserver<Messages> responseObserver) {
            MessageId[] ids = GrpcUtil.protoToJava(request);
            Message[] foundMessages = backend.retrieve(ids);

            if (foundMessages == null) {
                responseObserver.onError(Status.NOT_FOUND.asRuntimeException());
                return;
            }

            responseObserver.onNext(GrpcUtil.javaToProto(foundMessages));
            responseObserver.onCompleted();
        }

        @Override
        public void deleteMessage(proto.MessageId request, StreamObserver<Empty> responseObserver) {
            MessageId id = GrpcUtil.protoToJava(request);
            backend.delete(id);

            responseObserver.onNext(Empty.newBuilder().build());
            responseObserver.onCompleted();
        }

        @Override
        public void deleteMessages(MessageIds request, StreamObserver<Empty> responseObserver) {
            MessageId[] ids = GrpcUtil.protoToJava(request);
            backend.delete(ids);

            responseObserver.onNext(Empty.newBuilder().build());
            responseObserver.onCompleted();
        }

        @Override
        public void subscribe(Subscription request, StreamObserver<Topics> responseObserver) {
            Pair<Username, Topic> subPair = GrpcUtil.protoToJava(request);
            Topic[] topics = backend.subscribe(subPair.getLeft(), subPair.getRight());

            responseObserver.onNext(GrpcUtil.javaToProto(topics));
            responseObserver.onCompleted();
        }

        @Override
        public void unsubscribe(Subscription request, StreamObserver<Topics> responseObserver) {
            Pair<Username, Topic> subPair = GrpcUtil.protoToJava(request);
            Topic[] topics = backend.unsubscribe(subPair.getLeft(), subPair.getRight());

            responseObserver.onNext(GrpcUtil.javaToProto(topics));
            responseObserver.onCompleted();
        }

        @Override
        public void listUsers(Empty request, StreamObserver<Usernames> responseObserver) {
            Username[] usernames = backend.listUsers();

            responseObserver.onNext(GrpcUtil.javaToProto(usernames));
            responseObserver.onCompleted();
        }

        @Override
        public void listTopics(Empty request, StreamObserver<Topics> responseObserver) {
            Topic[] topics = backend.listTopics();

            responseObserver.onNext(GrpcUtil.javaToProto(topics));
            responseObserver.onCompleted();
        }

        @Override
        public void listUserTopics(proto.Username request, StreamObserver<Topics> responseObserver) {
            Username username = GrpcUtil.protoToJava(request);
            Topic[] userTopics = backend.listTopics(username);

            responseObserver.onNext(GrpcUtil.javaToProto(userTopics));
            responseObserver.onCompleted();
        }

        @Override
        public void listSubscribers(proto.Topic request, StreamObserver<Usernames> responseObserver) {
            Topic topic = GrpcUtil.protoToJava(request);
            Username[] subscribers = backend.listSubscribers(topic);

            responseObserver.onNext(GrpcUtil.javaToProto(subscribers));
            responseObserver.onCompleted();
        }

        @Override
        public void listUserMessages(proto.Username request, StreamObserver<MessageIds> responseObserver) {
            Username username = GrpcUtil.protoToJava(request);
            MessageId[] messageIds = backend.listMessages(username);

            responseObserver.onNext(GrpcUtil.javaToProto(messageIds));
            responseObserver.onCompleted();
        }

        @Override
        public void listTopicMessages(proto.Topic request, StreamObserver<MessageIds> responseObserver) {
            Topic topic = GrpcUtil.protoToJava(request);
            MessageId[] messageIds = backend.listMessages(topic);

            responseObserver.onNext(GrpcUtil.javaToProto(messageIds));
            responseObserver.onCompleted();
        }
    }
}
