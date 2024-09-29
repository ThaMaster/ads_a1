package se.umu.cs.ads.a1;

import se.umu.cs.ads.a1.backend.InMemoryMessengerBackEnd;
import se.umu.cs.ads.a1.clients.LogicTest;
import se.umu.cs.ads.a1.clients.PerformanceTest;
import se.umu.cs.ads.a1.interfaces.Messenger;
import se.umu.cs.ads.a1.types.Username;
import se.umu.cs.ads.a1.util.Util;

public class Main {
    //----------------------------------------------------------
    //----------------------------------------------------------
    private static Messenger loadMessenger(String fqn) {
        try {

            Class<?> _class = Class.forName(fqn);
            return (Messenger) _class.getDeclaredConstructor().newInstance();

        } catch (Exception e) {

            e.printStackTrace();
            throw new IllegalStateException("unable to instantiate messenger class '" + fqn + "'");

        }
    }


    //----------------------------------------------------------
    //----------------------------------------------------------
    public static void main(String[] args) {
        try {
            // defaults to example messenger implementation
            final String[] arguments = Util.filterFlags(args);
            final String fqn;
            if (arguments.length > 0) {
                fqn = "se.umu.cs.ads.a1.backend." + arguments[0];
            } else {
                fqn = InMemoryMessengerBackEnd.class.getCanonicalName();
            }

            // dynamic class loading for messenger instantiation
            Messenger messenger = loadMessenger(fqn);


            // Test the logic
            if (Util.containsFlag(args, "-logic")) {
                LogicTest test = new LogicTest(messenger);
                System.out.println("Testing logic:");
                test.runAllTests();
            }

            Username username = new Username("testusername");

            // Full performance run
            if (Util.containsFlag(args, "-perfAll")) {
                System.out.println("Testing throughput...");
                PerformanceTest test = new PerformanceTest(messenger);
                test.testMessageThroughput(username, 1, 512);
                test.testMessageThroughput(username, 2, 512);
                test.testMessageThroughput(username, 4, 512);
                test.testMessageThroughput(username, 8, 512);
                test.printResults();
                System.out.println("Done");

                test = new PerformanceTest(messenger);
                System.out.println("Testing retrieval latency (Fixed Messages)...");
                test.testMessageRetrieval(username, 200, 128);
                test.testMessageRetrieval(username, 200, 256);
                test.testMessageRetrieval(username, 200, 512);
                test.testMessageRetrieval(username, 200, 1024);
                test.testMessageRetrieval(username, 200, 2048);
                test.testMessageRetrieval(username, 200, 4096);
                test.testMessageRetrieval(username, 200, 8192);
                test.testMessageRetrieval(username, 200, 16384);
                test.printResults();
                System.out.println("Done\n");

                test = new PerformanceTest(messenger);
                System.out.println("Testing retrieval latency (Fixed Payload)...");
                test.testMessageRetrieval(username, 100, 512);
                test.testMessageRetrieval(username, 200, 512);
                test.testMessageRetrieval(username, 400, 512);
                test.testMessageRetrieval(username, 800, 512);
                test.testMessageRetrieval(username, 1600, 512);
                test.testMessageRetrieval(username, 3200, 512);
                test.testMessageRetrieval(username, 6400, 512);
                test.testMessageRetrieval(username, 12800, 512);
                test.printResults();
                System.out.println("Done\n");
            }

            // Test the throughput with heavy payload
            if (Util.containsFlag(args, "-throughput")) {
                System.out.println("Testing throughput...");
                PerformanceTest test = new PerformanceTest(messenger);
                test.testMessageThroughput(username, 1, 512);
                test.testMessageThroughput(username, 2, 512);
                test.testMessageThroughput(username, 4, 512);
                test.testMessageThroughput(username, 8, 512);
                test.printResults();
                System.out.println("Done");
            }

            // Test the latency of retrieval with light payload
            if (Util.containsFlag(args, "-latencyFixedMessages")) {
                PerformanceTest test = new PerformanceTest(messenger);
                System.out.println("Testing retrieval latency (Fixed Messages)...");
                test.testMessageRetrieval(username, 200, 128);
                test.testMessageRetrieval(username, 200, 256);
                test.testMessageRetrieval(username, 200, 512);
                test.testMessageRetrieval(username, 200, 1024);
                test.testMessageRetrieval(username, 200, 2048);
                test.testMessageRetrieval(username, 200, 4096);
                test.testMessageRetrieval(username, 200, 8192);
                test.testMessageRetrieval(username, 200, 16384);
                test.printResults();
                System.out.println("Done\n");
            }

            // Test the latency of retrieval with light payload
            if (Util.containsFlag(args, "-latencyFixedPayload")) {
                PerformanceTest test = new PerformanceTest(messenger);
                System.out.println("Testing retrieval latency (Fixed Payload)...");
                test.testMessageRetrieval(username, 100, 512);
                test.testMessageRetrieval(username, 200, 512);
                test.testMessageRetrieval(username, 400, 512);
                test.testMessageRetrieval(username, 800, 512);
                test.testMessageRetrieval(username, 1600, 512);
                test.testMessageRetrieval(username, 3200, 512);
                test.testMessageRetrieval(username, 6400, 512);
                test.testMessageRetrieval(username, 12800, 512);
                test.printResults();
                System.out.println("Done\n");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        System.exit(0);
    }
}
