package se.umu.cs.ads.a1.backend.grpc;

import com.google.protobuf.ByteString;
import org.apache.commons.lang3.tuple.Pair;
import proto.Topics;
import se.umu.cs.ads.a1.types.*;

/**
 * Utility class for parsing data between the generated
 * proto data types and the java data types. Will be
 * used at the endpoints to send and retrieve data.
 */
public class GrpcUtil {

    /**
     * Converts the java MessageId class to the proto version.
     *
     * @param javaId the java class of MessageId
     * @return the proto version of the MessageId
     */
    public static proto.MessageId javaToProto(MessageId javaId) {
        return proto.MessageId.newBuilder().setValue(javaId.getValue()).build();
    }

    /**
     * Converts the proto MessageId type to the java class.
     *
     * @param protoId the proto version of MessageId
     * @return the java version of MessageId
     */
    public static MessageId protoToJava(proto.MessageId protoId) {
        return new MessageId(protoId.getValue());
    }

    /**
     * Converts an array of the java MessageId class to the proto version.
     *
     * @param javaIds the array of the java class of MessageId
     * @return the proto version of the array
     */
    public static proto.MessageIds javaToProto(MessageId[] javaIds) {
        proto.MessageIds.Builder builder = proto.MessageIds.newBuilder();
        for (MessageId javaTopic : javaIds) {
            builder.addMessageIds(javaToProto(javaTopic));
        }
        return builder.build();
    }

    /**
     * Converts an array of the proto version of MessageId to the java class.
     *
     * @param protoIds the array of the proto version of MessageID
     * @return the java class version of the array
     */
    public static MessageId[] protoToJava(proto.MessageIds protoIds) {
        MessageId[] ids = new MessageId[protoIds.getMessageIdsCount()];
        for (int i = 0; i < ids.length; i++) {
            ids[i] = protoToJava(protoIds.getMessageIds(i));
        }
        return ids;
    }

    /**
     * Converts the java class of Timestamp to the proto version.
     *
     * @param javaTimestamp the java class of Timestamp
     * @return the proto version of Timestamp
     */
    public static proto.Timestamp javaToProto(Timestamp javaTimestamp) {
        return proto.Timestamp.newBuilder().setValue(javaTimestamp.getValue()).build();
    }

    /**
     * Converts the proto version of Timestamp to the java class.
     *
     * @param protoTimestamp the proto version of Timestamp
     * @return the java class of Timestamp
     */
    public static Timestamp protoToJava(proto.Timestamp protoTimestamp) {
        return new Timestamp(protoTimestamp.getValue());
    }

    /**
     * Converts the java class of Username to the proto version.
     *
     * @param javaUsername the java class of Username
     * @return the proto version of Username
     */
    public static proto.Username javaToProto(Username javaUsername) {
        return proto.Username.newBuilder().setValue(javaUsername.getValue()).build();
    }

    /**
     * Converts the proto version of Username to the java class.
     *
     * @param protoUsername the proto version of Username
     * @return the java class of Username
     */
    public static Username protoToJava(proto.Username protoUsername) {
        return new Username(protoUsername.getValue());
    }

    /**
     * Converts an array of the java Username class to the proto version.
     *
     * @param javaUsernames the array of the java class of Username
     * @return the converted proto version of the array
     */
    public static proto.Usernames javaToProto(Username[] javaUsernames) {
        proto.Usernames.Builder builder = proto.Usernames.newBuilder();
        for (Username username : javaUsernames) {
            builder.addUsernames(javaToProto(username));
        }
        return builder.build();
    }

    /**
     * Converts an array of the proto version of Username to the java class.
     *
     * @param protoUsernames the array of the proto version of Username
     * @return the java class version of the array
     */
    public static Username[] protoToJava(proto.Usernames protoUsernames) {
        Username[] usernames = new Username[protoUsernames.getUsernamesCount()];
        for (int i = 0; i < usernames.length; i++) {
            usernames[i] = protoToJava(protoUsernames.getUsernames(i));
        }
        return usernames;
    }

    public static proto.Topic javaToProto(Topic javaTopic) {
        return proto.Topic.newBuilder()
                .setValue(javaTopic.getValue())
                .setWildcard(javaTopic.getWildcard()).build();
    }

    public static Topic protoToJava(proto.Topic protoTopic) {
        if (protoTopic.getWildcard()) {
            return new Topic(protoTopic.getValue() + "*");
        } else {
            return new Topic(protoTopic.getValue());
        }
    }

    public static proto.Topics javaToProto(Topic[] javaTopics) {
        Topics.Builder builder = proto.Topics.newBuilder();
        for (Topic javaTopic : javaTopics) {
            builder.addTopics(javaToProto(javaTopic));
        }
        return builder.build();
    }

    public static Topic[] protoToJava(proto.Topics protoTopics) {
        Topic[] topics = new Topic[protoTopics.getTopicsCount()];
        for (int i = 0; i < topics.length; i++) {
            topics[i] = protoToJava(protoTopics.getTopics(i));
        }
        return topics;
    }

    public static proto.Content javaToProto(Content javaContent) {
        return proto.Content.newBuilder().setValue(javaContent.getValue()).build();
    }

    public static Content protoToJava(proto.Content protoContent) {
        return new Content(protoContent.getValue());
    }

    public static proto.Data javaToProto(Data javaData) {
        return proto.Data.newBuilder().setData(ByteString.copyFrom(javaData.getValue())).build();
    }

    public static Data protoToJava(proto.Data protoData) {
        return new Data(protoData.getData().toByteArray());
    }

    public static proto.Subscription javaToProto(Pair<Username, Topic> javaSub) {
        return proto.Subscription.newBuilder()
                .setUsername(javaToProto(javaSub.getLeft()))
                .setTopic(javaToProto(javaSub.getRight()))
                .build();
    }

    public static Pair<Username, Topic> protoToJava(proto.Subscription protoSub) {
        return Pair.of(protoToJava(protoSub.getUsername()), protoToJava(protoSub.getTopic()));
    }

    public static proto.Message javaToProto(Message javaMessage) {
        return proto.Message.newBuilder()
                .setId(javaToProto(javaMessage.getId()))
                .setTimestamp(javaToProto(javaMessage.getTimestamp()))
                .setUsername(javaToProto(javaMessage.getUsername()))
                .setTopic(javaToProto(javaMessage.getTopic()))
                .setContent(javaToProto(javaMessage.getContent()))
                .setData(javaToProto(javaMessage.getData()))
                .build();
    }

    public static Message protoToJava(proto.Message protoMessage) {
        return new Message(
                protoToJava(protoMessage.getId()),
                protoToJava(protoMessage.getTimestamp()),
                protoToJava(protoMessage.getUsername()),
                protoToJava(protoMessage.getTopic()),
                protoToJava(protoMessage.getContent()),
                protoToJava(protoMessage.getData())
        );
    }

    public static proto.Messages javaToProto(Message[] javaMessages) {
        proto.Messages.Builder builder = proto.Messages.newBuilder();
        for (Message message : javaMessages) {
            builder.addMessages(javaToProto(message));
        }
        return builder.build();
    }

    public static Message[] protoToJava(proto.Messages protoMessages) {
        Message[] messages = new Message[protoMessages.getMessagesCount()];
        for (int i = 0; i < messages.length; i++) {
            messages[i] = protoToJava(protoMessages.getMessages(i));
        }
        return messages;
    }
}
