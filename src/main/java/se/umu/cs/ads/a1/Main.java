package se.umu.cs.ads.a1;

import se.umu.cs.ads.a1.backend.InMemoryMessengerBackEnd;
import se.umu.cs.ads.a1.clients.LogicTest;
import se.umu.cs.ads.a1.clients.PerformanceTest;
import se.umu.cs.ads.a1.interfaces.Messenger;
import se.umu.cs.ads.a1.types.Topic;
import se.umu.cs.ads.a1.types.Username;
import se.umu.cs.ads.a1.util.Util;

import java.util.ArrayList;

public class Main
{
  //----------------------------------------------------------
  //----------------------------------------------------------
  private static Messenger loadMessenger (String fqn)
  {
    try {

      Class<?> _class = Class.forName(fqn);
      return (Messenger)_class.getDeclaredConstructor().newInstance();

    } catch (Exception e) {

      e.printStackTrace();
      throw new IllegalStateException("unable to instantiate messenger class '" + fqn + "'");

    }
  }


  //----------------------------------------------------------
  //----------------------------------------------------------
  public static void main (String[] args)
  {
    try {
      // defaults to example messenger implementation
      final String[] arguments = Util.filterFlags(args);
      final String fqn;
      if(arguments.length > 0) {
        fqn = "se.umu.cs.ads.a1.backend." +  arguments[0];
      } else {
        fqn = InMemoryMessengerBackEnd.class.getCanonicalName();
      }

      // dynamic class loading for messenger instantiation
      Messenger messenger = loadMessenger(fqn);

      // test data
      Username username = new Username("testusername");

      // example utility function
      if (Util.containsFlag(args,"-topics"))
      {
        Topic[] topics = messenger.listTopics(username);
        System.out.println(topics.length + " topics");
        for (Topic t : topics)
          System.out.println("  " + t);
      }

      // example logic test
      if (Util.containsFlag(args,"-logic"))
      {
        LogicTest test = new LogicTest(messenger);
        System.out.println("Testing logic:");
        test.runAllTests();
      }

      // example performance test
      if (Util.containsFlag(args,"-perf")) {
        PerformanceTest test = new PerformanceTest(messenger);

        System.out.println("testing retrieval performance...");
        System.out.println(test.testMessageRetrieval(username,10,128));
        System.out.println(test.testMessageRetrieval(username,100,128));
        System.out.println(test.testMessageRetrieval(username,1000,128));
        System.out.println(test.testMessageRetrieval(username,10000,128));
        System.out.println(test.testMessageRetrieval(username,100000,128));
      }
    } catch (Exception e) {
      e.printStackTrace();
    }

    System.exit(0);
  }
}
