package se.umu.cs.ads.a1.clients;

import se.umu.cs.ads.a1.interfaces.Messenger;
import se.umu.cs.ads.a1.types.Message;
import se.umu.cs.ads.a1.types.Topic;
import se.umu.cs.ads.a1.types.Username;
import se.umu.cs.ads.a1.util.Util;

import java.util.ArrayList;

public class LogicTest {
    private final Messenger messenger;

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
            return testFailed("Message not empty on first check");
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
            return testFailed("Number of messages are not exactly one more");
        }

        messenger.delete(message.getId());
        int nrMessagesAfterDelete = messenger.listMessages(username).length;
        if (nrMessagesAfterDelete != (nrMessagesAfterStore - 1)) {
            return testFailed("Number of messages are not decremented");
        }

        return testSuccess();
    }

    public String testListByUsername() {
        printTestStart();

        ArrayList<Message> messages = new ArrayList<>();
        messages.add(Util.constructRandomMessage(
                new Username("test-user-0"), new Topic("/test/logic/0"), 1024));
        messages.add(Util.constructRandomMessage(
                new Username("test-user-1"), new Topic("/test/logic/1"), 1024));
        messages.add(Util.constructRandomMessage(
                new Username("test-user-2"), new Topic("/test/logic/0"), 1024));
        messages.add(Util.constructRandomMessage(
                new Username("test-user-0"), new Topic("/test/logic/1"), 1024));

        int nrMessagesBeforeStore = messenger.listMessages(messages.get(0).getUsername()).length;

        if (nrMessagesBeforeStore != 0) {
            return testFailed("Number of messages are not zero on startup");
        }

        // Store some messages
        messenger.store(messages.get(0));
        messenger.store(messages.get(1));
        messenger.store(messages.get(2));
        messenger.store(messages.get(3));

        int nrMessagesAfterStore = messenger.listMessages(messages.get(0).getUsername()).length;

        if (nrMessagesAfterStore != 2) {
            return testFailed("Number of messages does not increase upon adding");
        }

        for (Message m : messages) {
            messenger.delete(m.getId());
        }

        int nrMessagesAfterDelete = messenger.listMessages(messages.get(0).getUsername()).length;

        if (nrMessagesAfterDelete != 0) {
            return testFailed("All messages could not be deleted");
        }

        return testSuccess();
    }

    public String testListByTopic() {
        printTestStart();

        ArrayList<Message> messages = new ArrayList<>();
        messages.add(Util.constructRandomMessage(
                new Username("test-user-0"), new Topic("/test/logic/0"), 1024));
        messages.add(Util.constructRandomMessage(
                new Username("test-user-1"), new Topic("/test/logic/1"), 1024));
        messages.add(Util.constructRandomMessage(
                new Username("test-user-2"), new Topic("/test/logic/0"), 1024));
        messages.add(Util.constructRandomMessage(
                new Username("test-user-0"), new Topic("/test/logic/1"), 1024));

        int nrMessagesBeforeStore = messenger.listMessages(messages.get(0).getTopic()).length;

        if (nrMessagesBeforeStore != 0) {
            return testFailed("Number of messages are not zero on startup");
        }

        // Store some messages
        messenger.store(messages.get(0));
        messenger.store(messages.get(1));
        messenger.store(messages.get(2));
        messenger.store(messages.get(3));

        int nrMessagesAfterStore = messenger.listMessages(messages.get(0).getTopic()).length;

        if (nrMessagesAfterStore != 2) {
            return testFailed("Number of messages does not increase upon adding");
        }

        for (Message m : messages) {
            messenger.delete(m.getId());
        }

        int nrMessagesAfterDelete = messenger.listMessages(messages.get(0).getUsername()).length;

        if (nrMessagesAfterDelete != 0) {
            return testFailed("All messages could not be deleted");
        }

        return testSuccess();
    }

    //----------------------------------------------------------
    private void printTestStart() {
        StackTraceElement element = Thread.currentThread().getStackTrace()[2];
        System.out.println("\t " + element.getMethodName() + "...");
    }

    private String testSuccess() {
        StackTraceElement element = Thread.currentThread().getStackTrace()[2];
        return "\t " + element.getMethodName() + " - SUCCESS";
    }

    private String testFailed(String message) {
        StackTraceElement element = Thread.currentThread().getStackTrace()[2];
        return "\t " + element.getMethodName() + " - FAIL, line '" + element.getLineNumber() + "': " + message;
    }
}
