package se.umu.cs.ads.a1.clients;

import se.umu.cs.ads.a1.interfaces.Messenger;
import se.umu.cs.ads.a1.types.Message;
import se.umu.cs.ads.a1.types.MessageId;
import se.umu.cs.ads.a1.types.Topic;
import se.umu.cs.ads.a1.types.Username;
import se.umu.cs.ads.a1.util.Util;

import java.util.ArrayList;

public class LogicTest {
    private final Messenger messenger;

    private int currentTest = 0;

    //----------------------------------------------------------
    public LogicTest(Messenger messenger) {
        this.messenger = messenger;
    }

    public void runAllTests() {
        ArrayList<String> testResults = new ArrayList<>();
        testResults.add(testStoreRetrieveDelete());
        testResults.add(testCheckWithListing());
        testResults.add(testListByUsername());
        testResults.add(testListByTopic());
        testResults.add(testBatchStore());
        testResults.add(testBatchRetrieve());
        testResults.add(testBatchDelete());
        testResults.add(testListTopics());
        testResults.add(testListUsernames());
        testResults.add(testSubscribeAndUnsubscribe());
        testResults.add(testListSubscribers());
        testResults.add(testListTopicsByUsername());
        testResults.add(testWildcardTopic());
        System.out.println("\nTest Results:");

        for (int i = 0; i < testResults.size(); i++) {
            System.out.println("[" + i + "] " + testResults.get(i));
        }
    }

    //----------------------------------------------------------
    public String testStoreRetrieveDelete() {
        printTestStart();

        Topic topic = new Topic("/test/logic");
        Username username = new Username("test-user");
        Message message = Util.constructRandomMessage(username, topic, 1024);

        messenger.store(message);

        Message msg1 = messenger.retrieve(message.getId());
        if (msg1 == null) {
            return testFailed("Message not empty on before insert");
        }

        messenger.delete(message.getId());

        Message msg2 = messenger.retrieve(message.getId());
        if (msg2 != null) {
            testFailed("Message empty after insert");
        }

        return testSuccess();
    }

    public String testCheckWithListing() {
        printTestStart();

        Topic topic = new Topic("/test/logic");
        Username username = new Username("test-user");
        Message message = Util.constructRandomMessage(username, topic, 1024);

        int nrMessagesBeforeStore = messenger.listMessages(username).length;
        messenger.store(message);
        int nrMessagesAfterStore = messenger.listMessages(username).length;
        if (nrMessagesAfterStore != (nrMessagesBeforeStore + 1)) {
            return testFailed(
                    "Incorrect number of messages",
                    String.valueOf(nrMessagesAfterStore),
                    String.valueOf((nrMessagesBeforeStore + 1)));
        }

        messenger.delete(message.getId());
        int nrMessagesAfterDelete = messenger.listMessages(username).length;
        if (nrMessagesAfterDelete != (nrMessagesAfterStore - 1)) {
            return testFailed(
                    "Incorrect number of messages",
                    String.valueOf(nrMessagesAfterDelete),
                    String.valueOf((nrMessagesAfterStore - 1)));
        }

        return testSuccess();
    }

    public String testListByUsername() {
        printTestStart();

        Username username0 = new Username("test-user-0");
        Username username1 = new Username("test-user-1");
        Username username2 = new Username("test-user-2");
        Topic topic0 = new Topic("/test/logic/0");
        Topic topic1 = new Topic("/test/logic/1");

        ArrayList<Message> messages = new ArrayList<>();
        messages.add(Util.constructRandomMessage(username0, topic0, 1024));
        messages.add(Util.constructRandomMessage(username1, topic1, 1024));
        messages.add(Util.constructRandomMessage(username2, topic0, 1024));
        messages.add(Util.constructRandomMessage(username0, topic1, 1024));

        int nrMessagesBeforeStore = messenger.listMessages(username0).length;

        if (nrMessagesBeforeStore != 0) {
            return testFailed("Incorrect number of messages", String.valueOf(nrMessagesBeforeStore), "0");
        }

        // Store some messages
        for (Message m : messages)
            messenger.store(m);

        int nrMessagesAfterStore = messenger.listMessages(username0).length;

        if (nrMessagesAfterStore != 2) {
            return testFailed("Incorrect number of messages", String.valueOf(nrMessagesAfterStore), "2");
        }

        for (Message m : messages) {
            messenger.delete(m.getId());
        }

        return testSuccess();
    }

    public String testListByTopic() {
        printTestStart();

        Username username0 = new Username("test-user-0");
        Username username1 = new Username("test-user-1");
        Username username2 = new Username("test-user-2");
        Topic topic0 = new Topic("/test/logic/0");
        Topic topic1 = new Topic("/test/logic/1");

        ArrayList<Message> messages = new ArrayList<>();
        messages.add(Util.constructRandomMessage(username0, topic0, 1024));
        messages.add(Util.constructRandomMessage(username1, topic1, 1024));
        messages.add(Util.constructRandomMessage(username2, topic0, 1024));
        messages.add(Util.constructRandomMessage(username0, topic1, 1024));

        int nrMessagesBeforeStore = messenger.listMessages(topic0).length;

        if (nrMessagesBeforeStore != 0) {
            return testFailed("Incorrect number of messages", String.valueOf(nrMessagesBeforeStore), "0");
        }

        // Store some messages
        for (Message m : messages)
            messenger.store(m);

        int nrMessagesAfterStore = messenger.listMessages(topic0).length;

        if (nrMessagesAfterStore != 2) {
            return testFailed("Incorrect number of messages", String.valueOf(nrMessagesAfterStore), "2");
        }

        for (Message m : messages) {
            messenger.delete(m.getId());
        }

        return testSuccess();
    }

    public String testBatchStore() {
        printTestStart();
        Username username = new Username("test-user");
        Topic topic = new Topic("/test/logic");
        Message[] messages = {
                Util.constructRandomMessage(username, topic, 1024),
                Util.constructRandomMessage(username, topic, 1024),
                Util.constructRandomMessage(username, topic, 1024),
                Util.constructRandomMessage(username, topic, 1024)
        };

        int nrMessagesBeforeStore = messenger.listMessages(username).length;

        if (nrMessagesBeforeStore != 0) {
            return testFailed("Incorrect number of messages", String.valueOf(nrMessagesBeforeStore), "0");
        }

        messenger.store(messages);

        int nrMessagesAfterStore = messenger.listMessages(username).length;

        if (nrMessagesAfterStore != 4) {
            return testFailed("Incorrect number of messages", String.valueOf(nrMessagesAfterStore), "4");
        }

        for (Message m : messages) {
            messenger.delete(m.getId());
        }

        return testSuccess();
    }

    public String testBatchRetrieve() {
        printTestStart();

        Username username = new Username("test-user");
        Topic topic = new Topic("/test/logic");
        Message[] messages = {
                Util.constructRandomMessage(username, topic, 1024),
                Util.constructRandomMessage(username, topic, 1024),
                Util.constructRandomMessage(username, topic, 1024)
        };

        messenger.store(messages);

        MessageId[] ids = {messages[0].getId(), messages[1].getId(), messages[2].getId()};
        Message[] retrievedMessages = messenger.retrieve(ids);

        if (retrievedMessages.length != 3) {
            return testFailed("Incorrect number of messages", String.valueOf(retrievedMessages.length), "3");
        }

        for (Message m : messages) {
            messenger.delete(m.getId());
        }

        return testSuccess();
    }

    public String testBatchDelete() {
        printTestStart();
        Username username = new Username("test-user");
        Topic topic = new Topic("/test/logic");
        Message[] messages = {
                Util.constructRandomMessage(username, topic, 1024),
                Util.constructRandomMessage(username, topic, 1024),
                Util.constructRandomMessage(username, topic, 1024),
                Util.constructRandomMessage(username, topic, 1024)
        };

        messenger.store(messages);

        int nrMessagesAfterStore = messenger.listMessages(username).length;

        if (nrMessagesAfterStore != 4) {
            return testFailed("Incorrect number of messages", String.valueOf(nrMessagesAfterStore), "4");
        }

        MessageId[] ids = {messages[0].getId(), messages[1].getId(), messages[2].getId(), messages[3].getId()};
        messenger.delete(ids);

        int nrMessagesAfterDelete = messenger.listMessages(username).length;

        if (nrMessagesAfterDelete != 0) {
            return testFailed("Incorrect number of messages", String.valueOf(nrMessagesAfterDelete), "0");
        }

        return testSuccess();
    }

    public String testListTopics() {
        printTestStart();

        Topic topic0 = new Topic("/test/logic/topicTest0");
        Topic topic1 = new Topic("/test/logic/topicTest1");
        Topic topic2 = new Topic("/test/logic/topicTest2");
        Username username0 = new Username("test-user");

        Message[] messages = {
                Util.constructRandomMessage(username0, topic0, 1024),
                Util.constructRandomMessage(username0, topic1, 1024),
                Util.constructRandomMessage(username0, topic2, 1024),
                Util.constructRandomMessage(username0, topic0, 1024)
        };

        int nrTopicsBefore = messenger.listTopics().length;

        messenger.store(messages);

        int nrTopicsAfter = messenger.listTopics().length;

        if (nrTopicsAfter != (nrTopicsBefore + 3)) {
            return testFailed("Incorrect number of topics", String.valueOf(nrTopicsAfter), String.valueOf(nrTopicsBefore + 3));
        }

        MessageId[] ids = {messages[0].getId(), messages[1].getId(), messages[2].getId(), messages[3].getId()};
        messenger.delete(ids);

        return testSuccess();
    }

    public String testListUsernames() {
        printTestStart();

        Topic topic = new Topic("/test/logic");
        Username username0 = new Username("test-user-0");
        Username username1 = new Username("test-user-1");
        Username username2 = new Username("test-user-2");

        Message[] messages = {
                Util.constructRandomMessage(username0, topic, 1024),
                Util.constructRandomMessage(username0, topic, 1024),
                Util.constructRandomMessage(username1, topic, 1024),
                Util.constructRandomMessage(username2, topic, 1024)
        };

        messenger.store(messages);

        Username[] retrievedUsernames = messenger.listUsers();

        if (retrievedUsernames.length != 3) {
            return testFailed("Incorrect number of usernames", String.valueOf(retrievedUsernames.length), "3");
        }

        MessageId[] ids = {messages[0].getId(), messages[1].getId(), messages[2].getId(), messages[3].getId()};
        messenger.delete(ids);

        return testSuccess();
    }

    public String testSubscribeAndUnsubscribe() {
        printTestStart();

        Username username0 = new Username("test-user-subtest-0");
        Username username1 = new Username("test-user-subtest-1");
        Topic topic0 = new Topic("/test/logic/subtest/0");
        Topic topic1 = new Topic("/test/logic/subtest/1");
        Message[] messages = {
                Util.constructRandomMessage(username0, topic0, 1024),
                Util.constructRandomMessage(username1, topic0, 1024),
                Util.constructRandomMessage(username0, topic1, 1024),
                Util.constructRandomMessage(username1, topic1, 1024)
        };

        messenger.store(messages);

        Topic[] subTopics1 = messenger.subscribe(username0, topic0);

        if (subTopics1.length != 1) {
            return testFailed("Incorrect amount of subscribed topics", String.valueOf(subTopics1.length), "1");
        } else if (!subTopics1[0].getValue().equals(topic0.getValue())) {
            return testFailed("Incorrect topic", subTopics1[0].getValue(), topic0.getValue());
        }

        Topic[] subTopics2 = messenger.subscribe(username0, topic1);

        if (subTopics2.length != 1) {
            return testFailed("Incorrect amount of subscribed topics", String.valueOf(subTopics2.length), "1");
        } else if (!subTopics2[0].getValue().equals(topic1.getValue())) {
            return testFailed("Incorrect topic", subTopics2[0].getValue(), topic1.getValue());
        }

        Topic[] subTopicsAfterDelete = messenger.unsubscribe(username0, topic0);

        if (subTopicsAfterDelete.length != 1) {
            return testFailed("Incorrect amount of subscribed topics", String.valueOf(subTopicsAfterDelete.length), "1");
        } else if (!subTopicsAfterDelete[0].getValue().equals(topic0.getValue())) {
            return testFailed("Incorrect topic", subTopicsAfterDelete[0].getValue(), topic0.getValue());
        }

        for (Message m : messages) {
            messenger.delete(m.getId());
        }

        return testSuccess();
    }

    public String testListSubscribers() {
        printTestStart();

        Username username0 = new Username("test-user-listsub-0");
        Username username1 = new Username("test-user-listsub-1");
        Username username2 = new Username("test-user-listsub-2");
        Topic topic0 = new Topic("/test/logic/listsub/0");
        Topic topic1 = new Topic("/test/logic/listsub/1");

        Message[] messages = {
                Util.constructRandomMessage(username0, topic0, 1024),
                Util.constructRandomMessage(username1, topic0, 1024),
                Util.constructRandomMessage(username0, topic1, 1024),
                Util.constructRandomMessage(username2, topic1, 1024)
        };
        messenger.store(messages);

        messenger.subscribe(username1, topic0);
        Username[] subs = messenger.listSubscribers(topic0);
        if (subs.length != 1) {
            return testFailed("Incorrect number of subscribers", String.valueOf(subs.length), "1");
        }

        messenger.subscribe(username2, topic0);

        subs = messenger.listSubscribers(topic0);
        if (subs.length != 2) {
            return testFailed("Incorrect number of subscribers", String.valueOf(subs.length), "2");
        }

        messenger.unsubscribe(username2, topic0);
        subs = messenger.listSubscribers(topic0);

        if (subs.length != 1) {
            return testFailed("Incorrect number of subscribers", String.valueOf(subs.length), "1");
        }

        return testSuccess();
    }

    public String testListTopicsByUsername() {
        printTestStart();

        Username username0 = new Username("test-user-topiclist-0");
        Username username1 = new Username("test-user-topiclist-1");
        Topic topic0 = new Topic("/test/logic/topiclist/0");
        Topic topic1 = new Topic("/test/logic/topiclist/1");
        Topic topic2 = new Topic("/test/logic/topiclist/2");

        Message[] messages = {
                Util.constructRandomMessage(username0, topic0, 1024),
                Util.constructRandomMessage(username1, topic0, 1024),
                Util.constructRandomMessage(username0, topic1, 1024),
                Util.constructRandomMessage(username1, topic2, 1024)
        };

        messenger.store(messages);

        messenger.subscribe(username1, topic0);
        Topic[] topics = messenger.listTopics(username1);
        if (topics.length != 1) {
            return testFailed("Incorrect number of topics", String.valueOf(topics.length), "1");
        } else if (!topics[0].getValue().equals(topic0.getValue())) {
            return testFailed("Incorrect topic", topics[0].getValue(), topic0.getValue());
        }

        messenger.subscribe(username1, topic1);

        topics = messenger.listTopics(username1);
        if (topics.length != 2) {
            return testFailed("Incorrect number of topics", String.valueOf(topics.length), "2");
        }

        messenger.unsubscribe(username1, topic0);
        topics = messenger.listTopics(username1);

        if (topics.length != 1) {
            return testFailed("Incorrect number of topics", String.valueOf(topics.length), "1");
        }

        return testSuccess();
    }

    public String testWildcardTopic() {
        printTestStart();

        Username username0 = new Username("test-user-wildcard-0");
        Username username1 = new Username("test-user-wildcard-1");
        Topic topic0 = new Topic("/test/wildcard*");
        Topic topic1 = new Topic("/test/wildcard/this/included");
        Topic topic2 = new Topic("/test/wildcard/and/this");

        Message[] messages = {
                Util.constructRandomMessage(username0, topic0, 1024),
                Util.constructRandomMessage(username0, topic1, 1024),
                Util.constructRandomMessage(username0, topic2, 1024)
        };

        messenger.store(messages);

        Topic[] topics = messenger.subscribe(username1, topic0);

        if (topics.length != 3) {
            return testFailed("Incorrect number of topics", String.valueOf(topics.length), "3");
        }

        return testSuccess();
    }

    //----------------------------------------------------------
    private void printTestStart() {
        StackTraceElement element = Thread.currentThread().getStackTrace()[2];
        System.out.println("[" + currentTest + "]\t" + element.getMethodName() + "...");
        currentTest++;
    }

    private String testSuccess() {
        StackTraceElement element = Thread.currentThread().getStackTrace()[2];
        return "\t " + element.getMethodName() + " - SUCCESS";
    }

    private String testFailed(String message) {
        StackTraceElement element = Thread.currentThread().getStackTrace()[2];
        return "\t " + element.getMethodName() + " - FAIL, line '" + element.getLineNumber() + "': " + message;
    }

    private String testFailed(String message, String actual, String expected) {
        StackTraceElement element = Thread.currentThread().getStackTrace()[2];
        return "\t " + element.getMethodName() + " - FAIL, line '" + element.getLineNumber() + "': " + message + ", Expected: " + expected + " Actual: " + actual;
    }
}
