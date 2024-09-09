package se.umu.cs.ads.a1.clients;

import se.umu.cs.ads.a1.interfaces.Messenger;
import se.umu.cs.ads.a1.types.Message;
import se.umu.cs.ads.a1.types.Topic;
import se.umu.cs.ads.a1.types.Username;
import se.umu.cs.ads.a1.util.Util;

import java.util.ArrayList;

public class LogicTest
{
  private final Messenger messenger;

  //----------------------------------------------------------
  public LogicTest (Messenger messenger)
  {
    this.messenger = messenger;
  }

  public void runAllTests() {
      ArrayList<String> testResults = new ArrayList<>();
//      testResults.add(testStoreRetrieveDelete());
//      testResults.add(testStoreDeleteWithListCheck());
//      testResults.add(testStoreAndListByUsername());
      testResults.add(testStoreAndListByTopic());

      for(int i = 0; i < testResults.size(); i++) {
          System.out.println("[" + i + "] " + testResults.get(i));
      }
  }

  //----------------------------------------------------------
    public String testStoreRetrieveDelete() {
        System.out.println("\t Store, retrieve and delete...");

        Topic topic = new Topic("/test/logic");
        Username username = new Username("test-user");
        Message message = Util.constructRandomMessage(username, topic, 1024);

        messenger.store(message);

        Message msg1 = messenger.retrieve(message.getId());
        if(msg1 == null) {
            return "\t Store, retrieve and delete - FAIL";
        }

        messenger.delete(message.getId());

        Message msg2 = messenger.retrieve(message.getId());
        if(msg2 != null) {
            return "\t Store, retrieve and delete - FAIL";
        }

        return "\t Store, retrieve and delete - SUCCESS";
    }

    public String testStoreDeleteWithListCheck()
    {
        System.out.println("\t Store, delete and check with listing...");

        Topic topic = new Topic("/test/logic");
        Username username = new Username("test-user");
        Message message = Util.constructRandomMessage(username, topic, 1024);

        int nrMessagesBeforeStore = messenger.listMessages(username).length;
        messenger.store(message);
        int nrMessagesAfterStore = messenger.listMessages(username).length;
        if (nrMessagesAfterStore != (nrMessagesBeforeStore + 1)) {
            return "\t Store, delete and check with listing - FAIL";
        }

        messenger.delete(message.getId());
        int nrMessagesAfterDelete = messenger.listMessages(username).length;
        if (nrMessagesAfterDelete != (nrMessagesAfterStore - 1)) {
            return "\t Store, delete and check with listing - FAIL";
        }

        return "\t Store, delete and check with listing - SUCCESS";
    }

    public String testStoreAndListByUsername() {
        System.out.println("\t Store and list messages by username...");
        Username username0 = new Username("test-user-0");
        Username username1 = new Username("test-user-1");
        Username username2 = new Username("test-user-2");
        Topic topic0 = new Topic("/test/logic/0");
        Topic topic1 = new Topic("/test/logic/1");

        int nrMessagesBeforeStore = messenger.listMessages(username0).length;

        if(nrMessagesBeforeStore != 0) {
            return "\t Store and list messages by username - FAIL";
        }

        // Store some messages
        messenger.store(Util.constructRandomMessage(username0, topic0, 1024));
        messenger.store(Util.constructRandomMessage(username1, topic1, 1024));
        messenger.store(Util.constructRandomMessage(username2, topic0, 1024));
        messenger.store(Util.constructRandomMessage(username0, topic1, 1024));

        int nrMessagesAfterStore = messenger.listMessages(username0).length;

        if(nrMessagesAfterStore != 2) {
            return "\t Store and list messages by username - FAIL";
        }

        return "\t Store and list messages by username - SUCCESS";
    }

    public String testStoreAndListByTopic() {
        System.out.println("\t Store and list messages by username...");
        Username username0 = new Username("test-user-0");
        Username username1 = new Username("test-user-1");
        Username username2 = new Username("test-user-2");
        Topic topic0 = new Topic("/test/logic/0");
        Topic topic1 = new Topic("/test/logic/1");

        int nrMessagesBeforeStore = messenger.listMessages(topic0).length;

        if(nrMessagesBeforeStore != 0) {
            return "\t Store and list messages by topic - FAIL";
        }

        // Store some messages
        messenger.store(Util.constructRandomMessage(username0, topic0, 1024));
        messenger.store(Util.constructRandomMessage(username1, topic1, 1024));
        messenger.store(Util.constructRandomMessage(username2, topic0, 1024));
        messenger.store(Util.constructRandomMessage(username0, topic1, 1024));

        int nrMessagesAfterStore = messenger.listMessages(topic0).length;

        if(nrMessagesAfterStore != 2) {
            return "\t Store and list messages by topic - FAIL";
        }

        return "\t Store and list messages by topic - SUCCESS";
    }
}
