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

    RestletServer server;

    public RestMessenger() {

        server = new RestletServer();

        // Turn of client and engine logging
        Logger.getLogger("org.restlet").setLevel(Level.OFF);
        Logger.getLogger("org.restlet.engine").setLevel(Level.OFF);
    }

    @Override
    public void store(Message message) {
        // Parse the message to the jackson representation
        JacksonRepresentation<Message> msgRep = new JacksonRepresentation<>(message);
        msgRep.setMediaType(MediaType.APPLICATION_JSON);

        // Setup client and post the message
        ClientResource client = new ClientResource("http://localhost:8080/messenger/message");
        client.post(msgRep);

        if (!client.getStatus().isSuccess()) {
            System.out.println("Rest: Failed to send message with id '" + message.getId().getValue() + "'. Status: " + client.getStatus());
        }
    }

    @Override
    public void store(Message[] messages) {
        // Parse the message to the jackson representation
        JacksonRepresentation<Message[]> msgRep = new JacksonRepresentation<>(messages);
        msgRep.setMediaType(MediaType.APPLICATION_JSON);

        // Setup client and post the message
        ClientResource client = new ClientResource("http://localhost:8080/messenger/messages");
        client.post(msgRep);
    }

    @Override
    public Message retrieve(MessageId message) {
        try {
            JacksonRepresentation<MessageId> idRep = new JacksonRepresentation<>(message);
            ClientResource client = new ClientResource("http://localhost:8080/messenger/message");
            client.addQueryParameter("messageId", idRep.getText());

            JacksonRepresentation<Message> msgRep = client.get(JacksonRepresentation.class);

            if (msgRep == null || !client.getStatus().isSuccess()) {
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
        try {
            JacksonRepresentation<MessageId[]> idsRep = new JacksonRepresentation<>(message);
            ClientResource client = new ClientResource("http://localhost:8080/messenger/messages");
            client.addQueryParameter("messageIds", idsRep.getText());

            JacksonRepresentation<Message[]> msgRep = client.get(JacksonRepresentation.class);

            return JsonUtil.parseMessages(msgRep.getText());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void delete(MessageId message) {
        try {
            JacksonRepresentation<MessageId> idRep = new JacksonRepresentation<>(message);
            ClientResource client = new ClientResource("http://localhost:8080/messenger/message");
            client.addQueryParameter("messageId", idRep.getText());
            client.delete();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(MessageId[] messages) {
        try {
            JacksonRepresentation<MessageId[]> idsRep = new JacksonRepresentation<>(messages);
            ClientResource client = new ClientResource("http://localhost:8080/messenger/messages");
            client.addQueryParameter("messageIds", idsRep.getText());
            client.delete();
            if (!client.getStatus().isSuccess()) {
                System.out.println("Could not delete");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Topic[] subscribe(Username username, Topic topic) {
        try {
            JacksonRepresentation<Username> nameRep = new JacksonRepresentation<>(username);
            JacksonRepresentation<Topic> topRep = new JacksonRepresentation<>(topic);

            ClientResource client = new ClientResource("http://localhost:8080/messenger/subscription");
            client.addQueryParameter("username", nameRep.getText());
            client.addQueryParameter("topic", topRep.getText());
            client.addQueryParameter("subscribe", "true");

            Representation response = client.put(null);

            JacksonRepresentation<Topic[]> recTopics = new JacksonRepresentation<>(response, Topic[].class);
            return JsonUtil.parseTopics(recTopics.getText());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Topic[] unsubscribe(Username username, Topic topic) {
        try {
            JacksonRepresentation<Username> nameRep = new JacksonRepresentation<>(username);
            JacksonRepresentation<Topic> topRep = new JacksonRepresentation<>(topic);

            ClientResource client = new ClientResource("http://localhost:8080/messenger/subscription");
            client.addQueryParameter("username", nameRep.getText());
            client.addQueryParameter("topic", topRep.getText());
            client.addQueryParameter("subscribe", "false");

            Representation response = client.put(null);

            JacksonRepresentation<Topic[]> recTopics = new JacksonRepresentation<>(response, Topic[].class);
            return JsonUtil.parseTopics(recTopics.getText());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Username[] listUsers() {
        try {
            ClientResource client = new ClientResource("http://localhost:8080/messenger/usernames");
            JacksonRepresentation<Username[]> msgRep = client.get(JacksonRepresentation.class);
            return JsonUtil.parseUsernames(msgRep.getText());

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Topic[] listTopics() {
        try {
            ClientResource client = new ClientResource("http://localhost:8080/messenger/topics");
            JacksonRepresentation<Topic[]> msgRep = client.get(JacksonRepresentation.class);
            return JsonUtil.parseTopics(msgRep.getText());

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Topic[] listTopics(Username username) {
        try {
            JacksonRepresentation<Username> usernameRep = new JacksonRepresentation<>(username);
            ClientResource client = new ClientResource("http://localhost:8080/messenger/topics");
            client.addQueryParameter("username", usernameRep.getText());

            JacksonRepresentation<Topic[]> msgRep = client.get(JacksonRepresentation.class);
            return JsonUtil.parseTopics(msgRep.getText());

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Username[] listSubscribers(Topic topic) {
        try {
            JacksonRepresentation<Topic> topicRep = new JacksonRepresentation<>(topic);
            ClientResource client = new ClientResource("http://localhost:8080/messenger/usernames");
            client.addQueryParameter("topic", topicRep.getText());

            JacksonRepresentation<Username[]> msgRep = client.get(JacksonRepresentation.class);
            return JsonUtil.parseUsernames(msgRep.getText());

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public MessageId[] listMessages(Username username) {
        try {
            JacksonRepresentation<Username> usernameRep = new JacksonRepresentation<>(username);
            ClientResource client = new ClientResource("http://localhost:8080/messenger/messageIds/username");
            client.getReference().addQueryParameter("username", usernameRep.getText());
            JacksonRepresentation<Message[]> msgRep = client.get(JacksonRepresentation.class);

            if (!client.getStatus().isSuccess()) {
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
        try {
            JacksonRepresentation<Topic> topicRep = new JacksonRepresentation<>(topic);
            ClientResource client = new ClientResource("http://localhost:8080/messenger/messageIds/topic");
            client.getReference().addQueryParameter("topic", topicRep.getText());

            JacksonRepresentation<Message[]> msgRep = client.get(JacksonRepresentation.class);

            if (!client.getStatus().isSuccess()) {
                return null;
            }

            return JsonUtil.parseMessageIds(msgRep.getText());

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


}
