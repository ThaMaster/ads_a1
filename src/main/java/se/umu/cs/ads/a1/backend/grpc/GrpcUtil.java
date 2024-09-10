package se.umu.cs.ads.a1.backend.grpc;

import proto.Topics;
import se.umu.cs.ads.a1.types.*;

public class GrpcUtil {


    public static proto.MessageId javaToProto(MessageId javaId) {
        return proto.MessageId.newBuilder().setValue(javaId.getValue()).build();
    }

    public static proto.Timestamp javaToProto(Timestamp javaTimestamp) {
        return proto.Timestamp.newBuilder().setValue(javaTimestamp.getValue()).build();
    }

    public static proto.Username javaToProto(Username javaUsername) {
        return proto.Username.newBuilder().setValue(javaUsername.getValue()).build();
    }

    public static proto.Topic javaToProto(Topic javaTopic) {
        return proto.Topic.newBuilder()
                .setValue(javaTopic.getValue())
                .setWildcard(javaTopic.getWildcard()).build();
    }

    public static proto.Topics javaToProto(Topic[] javaTopics) {
        Topics.Builder builder = proto.Topics.newBuilder();
        for(Topic javaTopic : javaTopics) {
            builder.addTopics(javaToProto(javaTopic));
        }
        return builder.build();
    }

    public static proto.Content javaToProto(Content javaContent) {
        return proto.Content.newBuilder().setValue(javaContent.getValue()).build();
    }

}
