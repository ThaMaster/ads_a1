package se.umu.cs.ads.a1.backend.rest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import se.umu.cs.ads.a1.types.*;

public class JsonUtil {

    private static ObjectMapper mapper = new ObjectMapper();

    public static Message parseMessage(String jsonString) {
        try {
            JsonNode rootNode = mapper.readTree(jsonString);

            return getMessageFromNode(rootNode);

        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static Message getMessageFromNode(JsonNode rootNode) {
        String messageId = rootNode.get("id").get("value").asText();
        long timestampValue = rootNode.get("timestamp").get("value").longValue();
        String usernameValue = rootNode.get("username").get("value").toString();
        String topicValue = rootNode.get("topic").get("value").toString();
        String contentValue = rootNode.get("content").get("value").toString();
        byte[] data = rootNode.get("data").get("value").toString().getBytes();

        return new Message(
                new MessageId(cleanupJsonValue(messageId)),
                new Timestamp(timestampValue),
                new Username(cleanupJsonValue(usernameValue)),
                new Topic(cleanupJsonValue(topicValue)),
                new Content(cleanupJsonValue(contentValue)),
                new Data(data));
    }

    public static String getValueFromJson(String jsonString) {
        try {
            JsonNode rootNode = mapper.readTree(jsonString);
            return cleanupJsonValue(rootNode.get("value").toString());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static MessageId[] parseMessageIds(String jsonString) {
        try {
            JsonNode arrayNode = mapper.readTree(jsonString);
            // Check if the json object is of array type
            if (arrayNode.isArray()) {
                MessageId[] messageIds = new MessageId[arrayNode.size()];
                // Iterate over each value and add it to the list
                for (int i = 0; i < arrayNode.size(); i++) {
                    JsonNode objectNode = arrayNode.get(i);
                    messageIds[i] = new MessageId(objectNode.get("value").asText());
                }
                return messageIds;
            }

        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return new MessageId[0];
    }

    public static Message[] parseMessages(String jsonString) {
        try {
            JsonNode arrayNode = mapper.readTree(jsonString);
            // Check if the json object is of array type
            if (arrayNode.isArray()) {
                Message[] messages = new Message[arrayNode.size()];
                // Iterate over each value and add it to the list
                for (int i = 0; i < arrayNode.size(); i++) {
                    JsonNode objectNode = arrayNode.get(i);
                    messages[i] = getMessageFromNode(objectNode);
                }
                return messages;
            }

        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return new Message[0];
    }

    public static Username[] parseUsernames(String jsonString) {
        try {
            JsonNode arrayNode = mapper.readTree(jsonString);
            // Check if the json object is of array type
            if (arrayNode.isArray()) {
                Username[] usernames = new Username[arrayNode.size()];
                // Iterate over each value and add it to the list
                for (int i = 0; i < arrayNode.size(); i++) {
                    JsonNode objectNode = arrayNode.get(i);
                    usernames[i] = new Username(objectNode.get("value").asText());
                }
                return usernames;
            }

        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return new Username[0];
    }

    public static Topic[] parseTopics(String jsonString) {
        try {
            JsonNode arrayNode = mapper.readTree(jsonString);
            // Check if the json object is of array type
            if (arrayNode.isArray()) {
                Topic[] topics = new Topic[arrayNode.size()];
                // Iterate over each value and add it to the list
                for (int i = 0; i < arrayNode.size(); i++) {
                    JsonNode objectNode = arrayNode.get(i);
                    topics[i] = getTopicFromNode(objectNode);
                }
                return topics;
            }

        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return new Topic[0];
    }

    private static Topic getTopicFromNode(JsonNode node) {
        if(node.get("wildcard").booleanValue()) {
            return new Topic(node.get("value").asText() + "*");
        } else {
            return new Topic(node.get("value").asText());
        }
    }

    public  static Topic parseTopic(String jsonString) {
        try {
            JsonNode rootNode = mapper.readTree(jsonString);
            return getTopicFromNode(rootNode);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static String cleanupJsonValue(String input) {
        String jsonValue = input;
        if (jsonValue == null) {
            return null;
        }

        if (jsonValue.charAt(0) == '\"') {
            jsonValue = jsonValue.replace("\"", "");
        }

        return jsonValue;
    }

    public static void printJson(String jsonString) {
        try {
            JsonNode rootNode = mapper.readTree(jsonString);
            System.out.println(rootNode.toPrettyString());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    public static String getJsonObject(Object o) {
        try {
            return mapper.writeValueAsString(o);
        } catch (JsonProcessingException e) {
            return null;
        }
    }
}
