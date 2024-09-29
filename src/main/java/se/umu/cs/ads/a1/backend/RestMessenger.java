package se.umu.cs.ads.a1.backend;

import org.restlet.Client;
import org.restlet.Request;
import org.restlet.Response;
import org.restlet.data.MediaType;
import org.restlet.data.Method;
import org.restlet.data.Protocol;
import org.restlet.representation.Representation;
import org.restlet.representation.StringRepresentation;
import org.restlet.resource.ClientResource;
import se.umu.cs.ads.a1.backend.rest.JsonUtil;
import se.umu.cs.ads.a1.backend.rest.RestletServer;
import se.umu.cs.ads.a1.interfaces.Messenger;
import se.umu.cs.ads.a1.types.*;
import se.umu.cs.ads.a1.util.Util;

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

    private final Client client;

    public RestMessenger() {

        new RestletServer();
        this.client = new Client(Protocol.HTTP);
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
        StringRepresentation msgRep = new StringRepresentation(JsonUtil.toJson(message));
        msgRep.setMediaType(MediaType.APPLICATION_JSON);

        // Setup client and post the message
        messageResource.post(msgRep);
    }

    @Override
    public void store(Message[] messages) {
        messagesResource.getReference().setQuery(null);
        // Serialize the message
        StringRepresentation msgRep = new StringRepresentation(JsonUtil.toJson(messages));
        msgRep.setMediaType(MediaType.APPLICATION_JSON);

        // Setup client and post the message
        messagesResource.post(msgRep);
    }

    @Override
    public Message retrieve(MessageId message) {
        messageResource.getReference().setQuery(null);
        messageResource.setQueryValue("messageId", message.getValue());

        try {
            Representation msgRep = messageResource.get();
            String jsonText = msgRep.getText();
            if (jsonText == null) {
                return null;
            }
            return JsonUtil.parseMessage(jsonText);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public Message[] retrieve(MessageId[] message) {
        messagesResource.getReference().setQuery(null);
        try {
            StringRepresentation idsRep = new StringRepresentation(JsonUtil.toJson(message));
            messagesResource.addQueryParameter("messageIds", idsRep.getText());

            Representation msgRep = messagesResource.get();

            String jsonText = msgRep.getText();
            if (jsonText == null) {
                return null;
            }

            return JsonUtil.parseMessages(jsonText);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
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
        StringRepresentation idsRep = new StringRepresentation(JsonUtil.toJson(messages));
        messagesResource.addQueryParameter("messageIds", idsRep.getText());
        messagesResource.delete();

    }

    @Override
    public Topic[] subscribe(Username username, Topic topic) {
        subscriptionResource.getReference().setQuery(null);
        try {
            subscriptionResource.addQueryParameter("username", username.getValue());
            String topicValue = topic.getWildcard() ? topic.getValue() + "*" : topic.getValue();
            subscriptionResource.addQueryParameter("topic", topicValue);
            subscriptionResource.addQueryParameter("subscribe", "true");

            Representation response = subscriptionResource.put(null);
            return JsonUtil.parseTopics(response.getText());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Topic[] unsubscribe(Username username, Topic topic) {
        subscriptionResource.getReference().setQuery(null);
        try {
            subscriptionResource.addQueryParameter("username", username.getValue());
            String topicValue = topic.getWildcard() ? topic.getValue() + "*" : topic.getValue();
            subscriptionResource.addQueryParameter("topic", topicValue);
            subscriptionResource.addQueryParameter("subscribe", "false");

            Representation response = subscriptionResource.put(null);

            return JsonUtil.parseTopics(response.getText());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Username[] listUsers() {
        usernamesResource.getReference().setQuery(null);
        try {
            Representation msgRep = usernamesResource.get();

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
            Representation msgRep = topicsResource.get();
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
            topicsResource.addQueryParameter("username", username.getValue());

            Representation msgRep = topicsResource.get();
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
            String topicValue = topic.getWildcard() ? topic.getValue() + "*" : topic.getValue();
            usernamesResource.addQueryParameter("topic", topicValue);

            Representation msgRep = usernamesResource.get();
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
            messageIdsUsernameResource.getReference().addQueryParameter("username", username.getValue());
            Representation msgRep = messageIdsUsernameResource.get();

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
            String topicValue = topic.getWildcard() ? topic.getValue() + "*" : topic.getValue();
            messageIdsTopicResource.addQueryParameter("topic", topicValue);

            Representation msgRep = messageIdsTopicResource.get();

            return JsonUtil.parseMessageIds(msgRep.getText());

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


}