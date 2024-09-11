package se.umu.cs.ads.a1.backend;

import org.restlet.data.MediaType;
import org.restlet.ext.jackson.JacksonRepresentation;
import org.restlet.representation.Representation;
import org.restlet.resource.ClientResource;
import se.umu.cs.ads.a1.backend.rest.JsonUtil;
import se.umu.cs.ads.a1.backend.rest.RestletServer;
import se.umu.cs.ads.a1.interfaces.Messenger;
import se.umu.cs.ads.a1.types.Message;
import se.umu.cs.ads.a1.types.MessageId;
import se.umu.cs.ads.a1.types.Topic;
import se.umu.cs.ads.a1.types.Username;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class RestMessenger implements Messenger {

    private final ClientResource messageResource;
    private final ClientResource messagesResource;
    private final ClientResource messageIdTopicResource;
    private final ClientResource messageIdUsernameResource;
    private final ClientResource topicResource;
    private final ClientResource usernameResource;
    private final ClientResource subscriptionResource;

    public RestMessenger() {

        // Start the restlet server
        new RestletServer();

        // Initialize all the client resources
        messageResource = new ClientResource("http://localhost:8080/messenger/message");
        messagesResource = new ClientResource("http://localhost:8080/messenger/messages");
        messageIdTopicResource = new ClientResource("http://localhost:8080/messenger/messageIds/topic");
        messageIdUsernameResource = new ClientResource("http://localhost:8080/messenger/messageIds/username");
        topicResource = new ClientResource("http://localhost:8080/messenger/topics");
        usernameResource = new ClientResource("http://localhost:8080/messenger/usernames");
        subscriptionResource = new ClientResource("http://localhost:8080/messenger/subscription");

        // Turn of client and engine logging
        Logger.getLogger("org.restlet").setLevel(Level.OFF);
        Logger.getLogger("org.restlet.engine").setLevel(Level.OFF);
    }

    @Override
    public void store(Message message) {
        messageResource.getQuery().clear();
        // Parse the message to the jackson representation
        JacksonRepresentation<Message> msgRep = new JacksonRepresentation<>(message);
        msgRep.setMediaType(MediaType.APPLICATION_JSON);

        // Setup client and post the message
        messageResource.post(msgRep);
    }

    @Override
    public void store(Message[] messages) {
        messagesResource.getQuery().clear();
        // Parse the message to the jackson representation
        JacksonRepresentation<Message[]> msgRep = new JacksonRepresentation<>(messages);
        msgRep.setMediaType(MediaType.APPLICATION_JSON);

        // Setup client and post the message
        messagesResource.post(msgRep);
    }

    @Override
    public Message retrieve(MessageId message) {
        messageResource.getQuery().clear();
        try {
            JacksonRepresentation<MessageId> idRep = new JacksonRepresentation<>(message);
            messageResource.addQueryParameter("messageId", idRep.getText());

            JacksonRepresentation<Message> msgRep = messageResource.get(JacksonRepresentation.class);

            if (msgRep == null || !messageResource.getStatus().isSuccess()) {
                return null;
            }

            return JsonUtil.parseMessage(msgRep.getText());

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Message[] retrieve(MessageId[] message) {
        messagesResource.getQuery().clear();
        try {
            JacksonRepresentation<MessageId[]> idsRep = new JacksonRepresentation<>(message);
            messagesResource.addQueryParameter("messageIds", idsRep.getText());
            JacksonRepresentation<Message[]> msgRep = messagesResource.get(JacksonRepresentation.class);

            return JsonUtil.parseMessages(msgRep.getText());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void delete(MessageId message) {
        messageResource.getQuery().clear();
        try {
            JacksonRepresentation<MessageId> idRep = new JacksonRepresentation<>(message);
            messageResource.addQueryParameter("messageId", idRep.getText());
            messageResource.delete();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(MessageId[] messages) {
        messagesResource.getQuery().clear();
        try {
            JacksonRepresentation<MessageId[]> idsRep = new JacksonRepresentation<>(messages);
            messagesResource.addQueryParameter("messageIds", idsRep.getText());
            messagesResource.delete();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Topic[] subscribe(Username username, Topic topic) {
        subscriptionResource.getQuery().clear();
        try {
            JacksonRepresentation<Username> nameRep = new JacksonRepresentation<>(username);
            JacksonRepresentation<Topic> topRep = new JacksonRepresentation<>(topic);

            subscriptionResource.addQueryParameter("username", nameRep.getText());
            subscriptionResource.addQueryParameter("topic", topRep.getText());
            subscriptionResource.addQueryParameter("subscribe", "true");

            Representation response = subscriptionResource.put(null);

            JacksonRepresentation<Topic[]> recTopics = new JacksonRepresentation<>(response, Topic[].class);
            return JsonUtil.parseTopics(recTopics.getText());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Topic[] unsubscribe(Username username, Topic topic) {
        subscriptionResource.getQuery().clear();
        try {
            JacksonRepresentation<Username> nameRep = new JacksonRepresentation<>(username);
            JacksonRepresentation<Topic> topRep = new JacksonRepresentation<>(topic);

            subscriptionResource.addQueryParameter("username", nameRep.getText());
            subscriptionResource.addQueryParameter("topic", topRep.getText());
            subscriptionResource.addQueryParameter("subscribe", "false");

            Representation response = subscriptionResource.put(null);

            JacksonRepresentation<Topic[]> recTopics = new JacksonRepresentation<>(response, Topic[].class);
            return JsonUtil.parseTopics(recTopics.getText());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Username[] listUsers() {
        usernameResource.getQuery().clear();
        try {
            JacksonRepresentation<Username[]> msgRep = usernameResource.get(JacksonRepresentation.class);
            return JsonUtil.parseUsernames(msgRep.getText());

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Topic[] listTopics() {
        topicResource.getQuery().clear();
        try {
            JacksonRepresentation<Topic[]> msgRep = topicResource.get(JacksonRepresentation.class);
            return JsonUtil.parseTopics(msgRep.getText());

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Topic[] listTopics(Username username) {
        topicResource.getQuery().clear();
        try {
            JacksonRepresentation<Username> usernameRep = new JacksonRepresentation<>(username);
            topicResource.addQueryParameter("username", usernameRep.getText());

            JacksonRepresentation<Topic[]> msgRep = topicResource.get(JacksonRepresentation.class);
            return JsonUtil.parseTopics(msgRep.getText());

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Username[] listSubscribers(Topic topic) {
        usernameResource.getQuery().clear();
        try {
            JacksonRepresentation<Topic> topicRep = new JacksonRepresentation<>(topic);
            usernameResource.addQueryParameter("topic", topicRep.getText());

            JacksonRepresentation<Username[]> msgRep = usernameResource.get(JacksonRepresentation.class);
            return JsonUtil.parseUsernames(msgRep.getText());

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public MessageId[] listMessages(Username username) {
        messageIdUsernameResource.getQuery().clear();
        try {
            JacksonRepresentation<Username> usernameRep = new JacksonRepresentation<>(username);
            messageIdUsernameResource.addQueryParameter("username", usernameRep.getText());
            JacksonRepresentation<Message[]> msgRep = messageIdUsernameResource.get(JacksonRepresentation.class);

            if (!messageIdUsernameResource.getStatus().isSuccess()) {
                return null;
            }

            return JsonUtil.parseMessageIds(msgRep.getText());

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public MessageId[] listMessages(Topic topic) {
        messageIdTopicResource.getQuery().clear();
        try {
            JacksonRepresentation<Topic> topicRep = new JacksonRepresentation<>(topic);
            messageIdTopicResource.getReference().addQueryParameter("topic", topicRep.getText());

            JacksonRepresentation<Message[]> msgRep = messageIdTopicResource.get(JacksonRepresentation.class);

            if (!messageIdTopicResource.getStatus().isSuccess()) {
                return null;
            }

            return JsonUtil.parseMessageIds(msgRep.getText());

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


}
