package se.umu.cs.ads.a1.clients;

import se.umu.cs.ads.a1.interfaces.Messenger;
import se.umu.cs.ads.a1.types.*;
import se.umu.cs.ads.a1.util.Util;

import java.util.Arrays;

public class PerformanceTest {
    private final Messenger messenger;
    private final int RUNS = 15;

    //----------------------------------------------------------
    public PerformanceTest(Messenger messenger) {
        this.messenger = messenger;
    }

    //----------------------------------------------------------
    public void testMessageRetrieval(Username username, int nrMessages, int payloadSize) {
        System.out.println("m=" + nrMessages + ", s=" + payloadSize);
        Topic topic = new Topic("/test/performance/retrieval");
        messenger.delete(messenger.listMessages(topic));
        Data data = Util.constructRandomData(payloadSize);

        Message[] messages = new Message[nrMessages];
        for(int i = 0; i < nrMessages; i++) {
            messages[i] = Util.constructFixedSizeMessage(username, topic, payloadSize);
        }
        messenger.store(messages);

        MessageId[] messageIds = messenger.listMessages(topic);
        if (messageIds.length != nrMessages)
            throw new IllegalStateException("testMessageRetrieval(): setup failure");

        long[] seqResults = new long[RUNS];
        long t1;
        long t2;
        for (int i = 0; i < RUNS; i++) {
            t1 = System.currentTimeMillis();
            for (Message message : messages)
                messenger.store(message);
            t2 = System.currentTimeMillis();
            seqResults[i] = (t2 - t1);
        }

        messenger.delete(messenger.listMessages(topic));

        long[] batchResults = new long[RUNS];
        for (int i = 0; i < RUNS; i++) {
            t1 = System.currentTimeMillis();
            messenger.store(messages);
            t2 = System.currentTimeMillis();
            batchResults[i] += (t2 - t1);
        }

        System.out.println("\tSequential times (ms): " + Arrays.toString(seqResults));
        System.out.println("\tBatch times (ms): " + Arrays.toString(batchResults));

        messenger.delete(messenger.listMessages(topic));
        if (messenger.listMessages(topic).length != 0)
            throw new IllegalStateException("testMessageRetrieval(): cleanup failure");
    }

    public void testMessageThroughput(Username username, long durationSeconds, int payloadSize) {
        System.out.println("t=" + durationSeconds + "(s), s=" + payloadSize);
        Topic topic = new Topic("/test/performance/throughput");
        messenger.delete(messenger.listMessages(topic));
        Message message = Util.constructFixedSizeMessage(username, topic, payloadSize);
        messenger.store(message);
        MessageId id = message.getId();

        long endTime;
        int[] requestResults = new int[RUNS];
        int requestCount;
        for (int i = 0; i < RUNS; i++) {
            endTime = System.currentTimeMillis() + (durationSeconds * 1000);
            requestCount = 0;
            while (true) {
                long requestStartTime = System.currentTimeMillis();
                long remainingTime = endTime - requestStartTime;

                messenger.retrieve(id);

                long requestEndTime = System.currentTimeMillis();
                long requestDuration = requestEndTime - requestStartTime;

                if (requestDuration > remainingTime) {
                    break;
                }
                requestCount++;
            }
            messenger.delete(messenger.listMessages(topic));
            requestResults[i] = requestCount;
        }

        System.out.println("\tNumber of requests: " + Arrays.toString(requestResults));

        messenger.delete(messenger.listMessages(topic));
        if (messenger.listMessages(topic).length != 0)
            throw new IllegalStateException("testMessageRetrieval(): cleanup failure");
    }
}
