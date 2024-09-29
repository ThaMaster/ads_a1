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

            // Full performance run
            if (Util.containsFlag(args, "-perfFixedMessages")) {
                PerformanceTest test = new PerformanceTest(messenger);
                test.testRetrievalFixedMessages();
                test.printResults();
                System.out.println("Done\n");
            }

            if (Util.containsFlag(args, "-perfFixedPayload")) {
                PerformanceTest test = new PerformanceTest(messenger);
                test.testRetrievalFixedPayload();
                test.printResults();
                System.out.println("Done\n");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        System.exit(0);
    }
}
