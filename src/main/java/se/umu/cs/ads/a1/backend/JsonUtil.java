package se.umu.cs.ads.a1.backend;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;
import se.umu.cs.ads.a1.types.*;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public class JsonUtil {

    public static Message parseMessage(String jsonString) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode rootNode = mapper.readTree(jsonString);

            return getMessageFromNode(rootNode);

        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static Message getMessageFromNode(JsonNode rootNode) {
        long timestampValue = rootNode.get("timestamp").get("value").longValue();
        String usernameValue = rootNode.get("username").get("value").toString();
        String topicValue = rootNode.get("topic").get("value").toString();
        String contentValue = rootNode.get("content").get("value").toString();
        byte[] data = rootNode.get("data").get("value").toString().getBytes();

        return new Message(
                getMessageIdFromNode(rootNode),
                new Timestamp(timestampValue),
                new Username(cleanupJsonValue(usernameValue)),
                new Topic(cleanupJsonValue(topicValue)),
                new Content(cleanupJsonValue(contentValue)),
                new Data(data));
    }

    private static MessageId getMessageIdFromNode(JsonNode rootNode) {
        return new MessageId(cleanupJsonValue(rootNode.get("id").get("value").asText()));
    }

    public static MessageId[] parseMessageIds(String jsonString) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode arrayNode = mapper.readTree(jsonString);
            // Check if the json object is of array type
            if (arrayNode.isArray()) {
                MessageId[] messageIds = new MessageId[arrayNode.size()];
                // Iterate over each value and add it to the list
                for (int i = 0; i < arrayNode.size(); i++) {
                    JsonNode objectNode = arrayNode.get(i);
                    String value = objectNode.get("value").asText();
                    messageIds[i] = new MessageId(value);
                }
                return messageIds;
            }

        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return new MessageId[0];
    }

    public static Message[] parseMessages(String jsonString) {
        // TODO: Implement this function
        return null;
    }

    public static void printJson(String jsonString) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode rootNode = mapper.readTree(jsonString);
            System.out.println(rootNode.toPrettyString());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    private static String cleanupJsonValue(String input) {
        String jsonValue = input;
        if(jsonValue == null) {
            return null;
        }

        if(jsonValue.charAt(0) == '\"') {
            jsonValue = jsonValue.replace("\"", "");
        }

        return jsonValue;
    }
}
