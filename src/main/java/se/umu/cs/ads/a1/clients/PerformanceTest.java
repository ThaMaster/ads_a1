package se.umu.cs.ads.a1.clients;

import se.umu.cs.ads.a1.interfaces.Messenger;
import se.umu.cs.ads.a1.types.*;
import se.umu.cs.ads.a1.util.Util;

public class PerformanceTest {
    private final Messenger messenger;

    //----------------------------------------------------------
    public PerformanceTest(Messenger messenger) {
        this.messenger = messenger;
    }

    //----------------------------------------------------------
    public String testMessageRetrieval(Username username, int nrMessages, int payloadSize) {
        String testResults = "Message Retrieval Test (" + nrMessages + " messages):\n";
        Topic topic = new Topic("/test/performance");
        messenger.delete(messenger.listMessages(topic));
        Content content = Content.EMPTY;
        Data data = Util.constructRandomData(payloadSize);
        Message[] messages = Message.construct(username, topic, content, data, nrMessages);
        messenger.store(messages);

        MessageId[] messageIds = messenger.listMessages(topic);
        if (messageIds.length != nrMessages)
            throw new IllegalStateException("testMessageRetrieval(): setup failure");

        long t1 = System.currentTimeMillis();
        for (MessageId message : messageIds)
            messenger.retrieve(message);

        long t2 = System.currentTimeMillis();

        testResults += "\t" + (t2 - t1) + " ms (sequential)\n";

        long t3 = System.currentTimeMillis();
        messenger.retrieve(messageIds);
        long t4 = System.currentTimeMillis();

        testResults += "\t" + (t4 - t3) + " ms (batch)\n";

        messenger.delete(messenger.listMessages(topic));
        if (messenger.listMessages(topic).length != 0)
            throw new IllegalStateException("testMessageRetrieval(): cleanup failure");

        return testResults;
    }


}
