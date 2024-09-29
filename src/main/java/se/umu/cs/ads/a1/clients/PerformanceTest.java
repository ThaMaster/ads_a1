package se.umu.cs.ads.a1.clients;

import se.umu.cs.ads.a1.interfaces.Messenger;
import se.umu.cs.ads.a1.types.*;
import se.umu.cs.ads.a1.util.Util;

public class PerformanceTest {
    private final Messenger messenger;
    private final int RUNS = 15;

    private String seqTestResults = "";
    private String batTestResults = "";

    private Topic topic;
    private MessageId[] messageIds;

    private final int[] nrMessages = {1000, 2000, 4000, 8000, 16000, 3200, 6400, 12800};
    private final int[] payloadSize = {128, 256, 512, 1024, 2048, 4096, 8192, 16384};

    private final int FIXED_NR_MESSAGES = 200;
    private final int FIXED_PAYLOAD_SIZE = 512;


    //----------------------------------------------------------
    public PerformanceTest(Messenger messenger) {
        this.messenger = messenger;
    }

    public void testRetrievalFixedMessages() {
        for (int size : payloadSize) {
            long[] batchTimes = new long[RUNS];
            long[] sequentialTimes = new long[RUNS];
            System.out.println("m=" + FIXED_NR_MESSAGES + " s=" + size);
            for (int i = 0; i < RUNS; i++) {
                setup(FIXED_NR_MESSAGES, size);
                sequentialTimes[i] = testMessageRetrievalSequential();
                batchTimes[i] = testMessageRetrievalBatch();
                tearDown();
            }
            addResults(sequentialTimes, batchTimes);
        }
    }

    //----------------------------------------------------------
    public void testRetrievalFixedPayload() {
        warmup(10000, 4096);
        for (int messages : nrMessages) {
            long[] batchTimes = new long[RUNS];
            long[] sequentialTimes = new long[RUNS];
            System.out.println("m=" + messages + " s=" + FIXED_PAYLOAD_SIZE);
            for (int i = 0; i < RUNS; i++) {
                setup(messages, FIXED_PAYLOAD_SIZE);
                sequentialTimes[i] = testMessageRetrievalSequential();
                batchTimes[i] = testMessageRetrievalBatch();
                tearDown();
            }
            addResults(sequentialTimes, batchTimes);
        }
    }

    public void setup(int nrMessages, int payloadSize) {
        // Prepares the messages
        Username username = new Username("testusername");
        this.topic = new Topic("/test/performance/retrieval");

        Content content = Content.EMPTY;
        Data data = Util.constructRandomData(payloadSize);
        Message[] messages = Message.construct(username, topic, content, data, nrMessages);
        messenger.store(messages);

        messageIds = messenger.listMessages(topic);
        if (messageIds.length != nrMessages) {
            throw new IllegalStateException("testMessageRetrieval(): setup failure");
        }
    }

    public void tearDown() {
        messenger.delete(messenger.listMessages(topic));
        if (messenger.listMessages(topic).length != 0) {
            throw new IllegalStateException("testMessageRetrieval(): cleanup failure");
        }
    }

    public long testMessageRetrievalSequential() {
        long start = System.currentTimeMillis();
        for (MessageId messageId : messageIds) {
            messenger.retrieve(messageId);
        }
        long end = System.currentTimeMillis();

        return (end - start);
    }

    public long testMessageRetrievalBatch() {
        long start = System.currentTimeMillis();
        messenger.retrieve(messageIds);
        long end = System.currentTimeMillis();

        return (end - start);
    }

    public void warmup(int nrMessages, int payloadSize) {
        // Warm-up phase
        setup(nrMessages, payloadSize); // Use fixed warm-up configuration
        testMessageRetrievalSequential();     // You can use either batch or sequential
        testMessageRetrievalBatch();     // You can use either batch or sequential
        tearDown(); // Reset after warm-up
    }

    public void addResults(long[] sequentialTimes, long[] batchTimes) {
        for (int i = 0; i < batchTimes.length; i++) {
            batTestResults += String.valueOf(batchTimes[i]);
            if (i != batchTimes.length - 1) {
                batTestResults += ", ";
            } else {
                batTestResults += "\n";
            }
        }

        for (int i = 0; i < sequentialTimes.length; i++) {
            seqTestResults += String.valueOf(sequentialTimes[i]);
            if (i != sequentialTimes.length - 1) {
                seqTestResults += ", ";
            } else {
                seqTestResults += "\n";
            }
        }
    }

    public void printResults() {
        System.out.println("\tSequential times (ms)");
        System.out.println(seqTestResults);
        System.out.println("\tBatch times (ms)");
        System.out.println(batTestResults);
        seqTestResults = "";
        batTestResults = "";
    }
}
