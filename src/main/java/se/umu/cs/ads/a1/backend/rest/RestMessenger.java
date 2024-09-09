package se.umu.cs.ads.a1.backend.rest;

import org.restlet.data.MediaType;
import org.restlet.ext.jackson.JacksonRepresentation;
import org.restlet.resource.ClientResource;
import se.umu.cs.ads.a1.backend.JsonUtil;
import se.umu.cs.ads.a1.interfaces.Messenger;
import se.umu.cs.ads.a1.types.Message;
import se.umu.cs.ads.a1.types.MessageId;
import se.umu.cs.ads.a1.types.Topic;
import se.umu.cs.ads.a1.types.Username;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class RestMessenger implements Messenger {

    RestletServer server;

    public RestMessenger() {
        server = new RestletServer();
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
        JacksonRepresentation<List<Message>> msgRep = new JacksonRepresentation<>(Arrays.stream(messages).toList());
        msgRep.setMediaType(MediaType.APPLICATION_JSON);

        // Setup client and post the message
        ClientResource client = new ClientResource("http://localhost:8080/messenger/messages");
        client.post(msgRep);
    }

    @Override
    public Message retrieve(MessageId message) {
        try {
            ClientResource client = new ClientResource("http://localhost:8080/messenger/message/" + message.getValue());

            JacksonRepresentation<Message> msgRep = client.get(JacksonRepresentation.class);

            if(msgRep == null || !client.getStatus().isSuccess()) {
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
        // TODO: Implement this function
        return new Message[0];
    }

    @Override
    public void delete(MessageId message) {
        ClientResource client = new ClientResource("http://localhost:8080/messenger/message/" + message.getValue());
        client.delete();

        if (!client.getStatus().isSuccess()) {
            System.out.println("Rest: error while deleting message with id '" + message.getValue() + "'. Status: " + client.getStatus());
        }
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
        try {
            ClientResource client = new ClientResource("http://localhost:8080/messenger/messageIds/username/" + username.getValue());
            JacksonRepresentation<Message[]> msgRep = client.get(JacksonRepresentation.class);

            if(!client.getStatus().isSuccess()) {
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
            // TODO: Maybe just use object mapper and write it as json object?
            String uri = "http://localhost:8080/messenger/messageIds/topic/" + topicRep.getText();
            ClientResource client = new ClientResource(uri);
            JacksonRepresentation<Message[]> msgRep = client.get(JacksonRepresentation.class);

            if(!client.getStatus().isSuccess()) {
                return null;
            }

            return JsonUtil.parseMessageIds(msgRep.getText());

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


}
