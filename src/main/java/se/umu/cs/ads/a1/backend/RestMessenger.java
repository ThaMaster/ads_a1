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
    private final ClientResource subscriptionResource;
    private final ClientResource usernamesResource;
    private final ClientResource topicsResource;
    private final ClientResource messageIdsUsernameResource;
    private final ClientResource messageIdsTopicResource;

    public RestMessenger() {

        new RestletServer();

        messageResource = new ClientResource("http://localhost:8080/messenger/message");
        messagesResource = new ClientResource("http://localhost:8080/messenger/messages");
        subscriptionResource = new ClientResource("http://localhost:8080/messenger/subscription");
        usernamesResource = new ClientResource("http://localhost:8080/messenger/usernames");
        topicsResource = new ClientResource("http://localhost:8080/messenger/topics");
        messageIdsUsernameResource = new ClientResource("http://localhost:8080/messenger/messageIds/username");
        messageIdsTopicResource = new ClientResource("http://localhost:8080/messenger/messageIds/topic");

        // Turn of client and engine logging
        Logger.getLogger("org.restlet").setLevel(Level.OFF);
        Logger.getLogger("org.restlet.engine").setLevel(Level.OFF);
    }

    @Override
    public void store(Message message) {
        messageResource.getReference().setQuery(null);
        // Parse the message to the jackson representation
        JacksonRepresentation<Message> msgRep = new JacksonRepresentation<>(message);
        msgRep.setMediaType(MediaType.APPLICATION_JSON);

        // Setup client and post the message
        messageResource.post(msgRep);
    }

    @Override
    public void store(Message[] messages) {
        messagesResource.getReference().setQuery(null);
        // Parse the message to the jackson representation
        JacksonRepresentation<Message[]> msgRep = new JacksonRepresentation<>(messages);
        msgRep.setMediaType(MediaType.APPLICATION_JSON);

        // Setup client and post the message
        messagesResource.post(msgRep);
    }

    @Override
    public Message retrieve(MessageId message) {
        messageResource.getReference().setQuery(null);
        messageResource.addQueryParameter("messageId", message.getValue());

        // This takes a VEEERY long time..
        String msgRep = messageResource.get(String.class);

        if (msgRep == null || !messageResource.getStatus().isSuccess()) {
            return null;
        }

        Message msg = JsonUtil.parseMessage(msgRep);
        return msg;
    }

    @Override
    public Message[] retrieve(MessageId[] message) {
        messagesResource.getReference().setQuery(null);
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
        messageResource.getReference().setQuery(null);
        messageResource.addQueryParameter("messageId", message.getValue());
        messageResource.delete();
    }

    @Override
    public void delete(MessageId[] messages) {
        messagesResource.getReference().setQuery(null);
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
        subscriptionResource.getReference().setQuery(null);
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
        subscriptionResource.getReference().setQuery(null);
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
        usernamesResource.getReference().setQuery(null);
        try {
            JacksonRepresentation<Username[]> msgRep = usernamesResource.get(JacksonRepresentation.class);
            return JsonUtil.parseUsernames(msgRep.getText());

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Topic[] listTopics() {
        topicsResource.getReference().setQuery(null);
        try {
            JacksonRepresentation<Topic[]> msgRep = topicsResource.get(JacksonRepresentation.class);
            return JsonUtil.parseTopics(msgRep.getText());

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Topic[] listTopics(Username username) {
        topicsResource.getReference().setQuery(null);
        try {
            JacksonRepresentation<Username> usernameRep = new JacksonRepresentation<>(username);
            topicsResource.addQueryParameter("username", usernameRep.getText());

            JacksonRepresentation<Topic[]> msgRep = topicsResource.get(JacksonRepresentation.class);
            return JsonUtil.parseTopics(msgRep.getText());

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Username[] listSubscribers(Topic topic) {
        usernamesResource.getReference().setQuery(null);
        try {
            JacksonRepresentation<Topic> topicRep = new JacksonRepresentation<>(topic);
            usernamesResource.addQueryParameter("topic", topicRep.getText());

            JacksonRepresentation<Username[]> msgRep = usernamesResource.get(JacksonRepresentation.class);
            return JsonUtil.parseUsernames(msgRep.getText());

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public MessageId[] listMessages(Username username) {
        messageIdsUsernameResource.getReference().setQuery(null);
        try {
            JacksonRepresentation<Username> usernameRep = new JacksonRepresentation<>(username);
            messageIdsUsernameResource.getReference().addQueryParameter("username", usernameRep.getText());
            JacksonRepresentation<Message[]> msgRep = messageIdsUsernameResource.get(JacksonRepresentation.class);

            if (!messageIdsUsernameResource.getStatus().isSuccess()) {
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
        messageIdsTopicResource.getReference().setQuery(null);
        try {
            JacksonRepresentation<Topic> topicRep = new JacksonRepresentation<>(topic);
            messageIdsTopicResource.addQueryParameter("topic", topicRep.getText());

            JacksonRepresentation<Message[]> msgRep = messageIdsTopicResource.get(JacksonRepresentation.class);

            if (!messageIdsTopicResource.getStatus().isSuccess()) {
                return null;
            }

            return JsonUtil.parseMessageIds(msgRep.getText());

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


}